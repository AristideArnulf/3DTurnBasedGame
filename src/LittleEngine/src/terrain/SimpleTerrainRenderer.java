/*
 * This file is made by group 5 of the course 2IOE0
 */
package terrain;


import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import terrain.Terrain;
import textures.ModelTexture;
import models.RawModel;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

/**
 * This class handles rendering the terrain
 * @author Leonardo
 */
public class SimpleTerrainRenderer {
    
    private SimpleTerrainShader shader;

    // Store the time
    private float tAnim = 0;
    
    public SimpleTerrainRenderer(SimpleTerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    // Main rendering method
    public void render(List<Terrain> terrains) {        
        tAnim = 1;
        shader.loadGameTime(tAnim);
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            // Renders terrain using triangles as quads
                GL11.glDrawElements(
                        GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
                
            unbindTexturedModel();
        }
    }
    
    // This method prepares the terrain model
    private void prepareTerrain(Terrain terrain) {
        
        
        RawModel rawModel = terrain.getModel();
        
        // Enable VAO lists and bind Vertex array
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        
        // Load specular variables
        ModelTexture texture = terrain.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), 
                texture.getReflectivity());
        
        // Tell OpenGL to use a certain texture, put it in a texture bank
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    }
    
    // This method unbinds a textured model
    private void unbindTexturedModel() {
        // Disable VAO lists
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        
        GL30.glBindVertexArray(0);
    }
    
    // This method prepares the terrain
    private void loadModelMatrix(Terrain terrain) {
        // Loadup the position
        Matrix4f transformationMatrix = 
                Maths.createTransformationMatrix(
                        new Vector3f(terrain.getX(), 0, terrain.getZ()), 
                        0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
