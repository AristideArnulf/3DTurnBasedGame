/*
 * This file is made by group 5 of the course 2IOE0
 */
package renderEngine;


import entities.EntityRenderer;
import entities.SimpleEntityRenderer;
import terrain.SimpleTerrainRenderer;
import terrain.TerrainRenderer;
import entities.cameras.Camera;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

import entities.StaticShader;
import terrain.TerrainShader;
import entities.Entity;
import entities.Light;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaders.ShaderProgram;
import shaders.SimpleShader;
import terrain.SimpleTerrainShader;
import terrain.Terrain;
import toolbox.Globals;
import toolbox.Maths;

/**
 * Handles all of the rendering code
 * @author Leonardo
 */
public class MasterRenderer {
    
    private static final float FOV = 100;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    
    public float intersection;
    
    private Matrix4f projectionMatrix;    
    
    private Globals globals = Globals.getInstance();
    
    // Renders entities with lightning
    private ShaderProgram shader = new StaticShader();
    private EntityRenderer entityRenderer;
    
    // Renders entities without lightning
    private SimpleShader simpleShader = new SimpleShader();
    private SimpleEntityRenderer simpleEntityRenderer;
    
    // Terrain shader
    private TerrainShader terrainShader = new TerrainShader();
    private TerrainRenderer terrainRenderer;
    
    // Simple terrain shader
    private SimpleTerrainShader simpleTerrainShader = new SimpleTerrainShader();
    private SimpleTerrainRenderer simpleTerrainRenderer;
        
    // Has a load of textured model keys
    // Each of them will be mapped to a list of the entities that use 
    // that textured model
    private Map<TexturedModel, List<Entity>> entities = 
            new HashMap<TexturedModel, List<Entity>>();
    
    private Map<TexturedModel, List<Entity>> simpleEntities = 
            new HashMap<TexturedModel, List<Entity>>();
    
    // List to hold all the terrains
    private List<Terrain> terrains = new ArrayList<Terrain>();
    
    // List to hold all the simple terrains
    private List<Terrain> simpleTerrains = new ArrayList<Terrain>();
    
    
    
    public MasterRenderer() {
        
        // Used to cull the back facing polygons
        // See 2IV60 slides hidden-surf.pdf
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        
        // Create the projection matrix
        createProjectionMatrix();
        
        // Create an entity entityRenderer
        entityRenderer = 
                new EntityRenderer(shader, projectionMatrix);
        
        // Create a simpleEntityRenderer
        simpleEntityRenderer = 
                new SimpleEntityRenderer(simpleShader, projectionMatrix);
        
        // Create an terrain entityRenderer
        terrainRenderer = 
                new TerrainRenderer(terrainShader, projectionMatrix);
        
        // Simple terrain renderer
        simpleTerrainRenderer = 
                new SimpleTerrainRenderer(simpleTerrainShader, projectionMatrix); 
    }
    
    public void render(List<Light> lights, Camera camera) {
        
        prepare();
        
        
        
        
        // Entities rendering
        shader.start();
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        shader.stop();
        
        // Simple entity rendering without lightning
        simpleShader.start();
        simpleShader.loadViewMatrix(camera);
        simpleEntityRenderer.render(simpleEntities);
        simpleShader.stop();
        
        // Terrain Rendering
        terrainShader.start();
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        
        // Simple Terrain Rendering
        simpleTerrainShader.start();
        simpleTerrainShader.loadLights(lights);
        simpleTerrainShader.loadViewMatrix(camera);
        simpleTerrainRenderer.render(simpleTerrains);
        simpleTerrainShader.stop();

        // Clear it each frame
        entities.clear();
        terrains.clear();
        simpleEntities.clear();
        simpleTerrains.clear();
    }
    
    // Called once every frame, prepares OpenGL
    public void prepare() {
        // Test to see if we should only render lines
        if (globals.isRenderLinesOnly() == true) {
            GL11.glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
        } else {
            GL11.glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
        }
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(255, 255, 255, 1);
        
        // Enable opacity
        GL11.glEnable(GL11.GL_BLEND);
        
        // Set blending function
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Clear the display buffer to the colour defined in glClearColor
        // Clear the depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    // Creates a projection matrix using the constants above
    // See slides from 2IV60 computer graphics what those constants mean
    // And how to setup such a matrix
    private void createProjectionMatrix() {
        float aspectRatio = 
                (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = 
                (float) ((1f / Math.tan(
                        Math.toRadians(FOV / 2f))) * aspectRatio
                );
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    // Converts mouse click (screen) coordinates to 3D World
    public Vector3f mouseToWorld(Vector2f vec, Camera camera) {
        
        // (according to lwjgl tutorial)
        Vector3f normMouse = new Vector3f();
        
        normMouse.x = (2f * vec.x) / Display.getWidth() - 1f;
        normMouse.y = (2f * vec.y) / Display.getHeight() - 1f;
        normMouse.z = 1f;
        
        Vector4f ray_clip = new Vector4f(normMouse.x, normMouse.y, -1.0f, 1.0f);
        Vector4f ray_eye = Matrix4f.transform(Matrix4f.invert(projectionMatrix,
                null), ray_clip, null);
        
        Vector4f eye_ray = new Vector4f(ray_eye.x, ray_eye.y, -1f, 0f);
        Matrix4f invViewMatrix = Matrix4f.invert(
                Maths.createViewMatrix(camera), null);
        Vector4f ray_wor = Matrix4f.transform(invViewMatrix, eye_ray, null);
        Vector3f mouse_ray = new Vector3f(ray_wor.x, ray_wor.y, ray_wor.z);
        mouse_ray.normalise(null);
        return mouse_ray;
    }
    
    // Checks for a ray intersection with an AABB around an entity origin
    public boolean intersectPlayer(Camera camera, Vector3f ray, Entity ent) {
        intersection = 0f;
        
        // Set AABB box size for player entities (ENTITY SIZES --> ACCURACY!)
        float BOXRAD_X = 1.5f * ent.getScale();
        float BOXRAD_Y = 8f * ent.getScale();
        float BOXRAD_Z = 1.5f * ent.getScale();
        
        Vector3f lb = new Vector3f();
        Vector3f rt = new Vector3f();
        Vector3f dirvec = new Vector3f();
        
        // Set left bottom AABB point (lb)
        lb.x = ent.getPosition().x - BOXRAD_X;
        lb.y = ent.getPosition().y;
        lb.z = ent.getPosition().z - BOXRAD_Z;
        
        // Set right top AABB point (rb)
        rt.x = ent.getPosition().x + BOXRAD_X;
        rt.y = ent.getPosition().y + BOXRAD_Y;
        rt.z = ent.getPosition().z + BOXRAD_Z;
        
        // Set inverse of direction vector
        dirvec.x = 1f / ray.x;
        dirvec.y = 1f / ray.y;
        dirvec.z = 1f / ray.z;
        
        // Calculate AABB tracing points
        float t1 = (lb.x - camera.getPosition().x) * dirvec.x;
        float t2 = (rt.x - camera.getPosition().x) * dirvec.x;
        float t3 = (lb.y - camera.getPosition().y) * dirvec.y;
        float t4 = (rt.y - camera.getPosition().y) * dirvec.y;
        float t5 = (lb.z - camera.getPosition().z) * dirvec.z;
        float t6 = (rt.z - camera.getPosition().z) * dirvec.z;
        
        // Calculate tmin/tmax
        float tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
        float tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));
        float t;

        // if tmax < 0, ray (line) is intersecting AABB, but AABB is behind
        if (tmax < 0) {
            t = tmax;
            return false;
        }

        // if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax) {
            t = tmax;
            return false;
        }
        
        // Intersection found
        intersection = tmin;
        return true;
    }
    
    // Checks for a ray intersection with an AABB around an entity origin
    public Vector3f intersectBoard(Camera camera, Vector3f ray) {
        
        float BOARD = 3.17f;
        float HEIGHT = 2f;
        Vector3f S1 = new Vector3f(-BOARD, HEIGHT, -BOARD);
        Vector3f S2 = new Vector3f(-BOARD, HEIGHT, BOARD);
        Vector3f S3 = new Vector3f(BOARD, HEIGHT, -BOARD);

        Vector3f R1 = camera.getPosition();
        Vector3f R2 = ray;

        Vector3f dS21 = Vector3f.sub(S1,S2,null);
        Vector3f dS31 = Vector3f.sub(S1,S3,null);
        Vector3f n = Vector3f.cross(dS21, dS31, null);

        float ndotdR = Vector3f.dot(n, R2);

        float t = Vector3f.dot(n.negate(null), Vector3f.sub(R1,S1,null)) / ndotdR; 
        Vector3f M = Vector3f.add(R1, (Vector3f) R2.scale(t), null);
        M.x = M.x;
        if (M.x <= BOARD && M.x >= -BOARD &&
                M.z <= BOARD && M.z >= -BOARD){
            return M;
        }
        else
            return new Vector3f(0f,-100f,0f);
    }
    
    // Adds terrain to terrain list
    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }
    
    // Removes terrain
    public void removeTerrain(Terrain terrain) {
        terrains.remove(terrain);
    }
    
    // Adds terrain to terrain list for simple rendering
    public void processSimpleTerrain(Terrain terrain) {
        simpleTerrains.add(terrain);
    }
    
    // Removes simple terrain
    public void removeSimpleTerrain(Terrain terrain) {
        simpleTerrains.remove(terrain);
    }
    
    // Loads entities into the mapping
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }

    }
    
    // Loads simple entities into the mapping
    public void processSimpleEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = simpleEntities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            simpleEntities.put(entityModel, newBatch);
        }        
    }
    
    // Removes entity from mapping
    public void removeEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        simpleEntities.remove(entityModel);
    }
    
    // Removes simple entity from mapping
    public void removeSimpleEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        simpleEntities.remove(entityModel);
    }
    
    // Cleanup for when the game is closed
    public void cleanUp() {
        shader.cleanUp();
        simpleShader.cleanUp();
        terrainShader.cleanUp();
    }    
}
