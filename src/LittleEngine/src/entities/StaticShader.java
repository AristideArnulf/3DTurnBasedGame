/*
 * This file is made by group 5 of the course 2IOE0
 */
package entities;

import entities.cameras.Camera;
import entities.Light;
import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * Static shading 
 * @author Leonardo
 */
public class StaticShader extends ShaderProgram {
    
    private static final int MAX_LIGHTS = 5;

    private static final String VERTEX_FILE = "res/shaders/staticShader/vertexShader.txt";
    private static final String FRAGMENT_FILE = "res/shaders/staticShader/fragmentShader.txt";

    // Matrices
    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;

    // Lighting
    private int locationLightPosition[];
    private int locationLightColour[];
    private int locationAttenuation[];
    
    // Specular lighting
    private int locationShineDamper;
    private int locationReflectivity;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        // VAO at index 0 are the vertices position, bind that witht the shader
        // position variable
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        // Get matrix locations
        locationTransformationMatrix
                = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");

        // Get light locations
        locationLightPosition = new int[MAX_LIGHTS];
        locationLightColour = new int[MAX_LIGHTS];
        locationAttenuation = new int[MAX_LIGHTS];
        
        for (int i = 0; i < MAX_LIGHTS; i++) {
            locationLightPosition[i] = 
                    super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColour[i] = 
                    super.getUniformLocation("lightColour[" + i + "]");
            locationAttenuation[i] = 
                    super.getUniformLocation("attenuation[" + i + "]");
        }
        
        // Get specular light locations
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
    }

    // Loads the transformation matrix into the shader uniform
    @Override
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    // Loads the projection matrix into the shader uniform
    @Override
    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(locationProjectionMatrix, projection);
    }

    // Loads the view matrix into the shader uniform
    @Override
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix, viewMatrix);
    }

    // Loads the lights position and colour into the uniforms
    @Override
    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if ( i < lights.size()) {
                // Load in all the lights to the shader array
                super.loadVector(locationLightPosition[i], 
                        lights.get(i).getPosition());
                super.loadVector(locationLightColour[i], 
                        lights.get(i).getColour());
                super.loadVector(locationAttenuation[i], 
                        lights.get(i).getAttenuation());
            } else {
                // Load in some dummy information
                super.loadVector(locationLightPosition[i], 
                        new Vector3f(0, 0, 0));
                super.loadVector(locationLightColour[i], 
                        new Vector3f(0, 0, 0));
                super.loadVector(locationAttenuation[i], 
                        new Vector3f(1, 0, 0));
            }
        }
    }
    
    // Loads specular variables into te uniforms
    @Override
    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(locationShineDamper, damper);
        super.loadFloat(locationReflectivity, reflectivity);
    }

}
