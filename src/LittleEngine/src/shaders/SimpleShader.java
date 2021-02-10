/*
 * This file is made by group 5 of the course 2IOE0
 */
package shaders;

import entities.cameras.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * Simple shading, no lightning
 * @author Leonardo
 */
public class SimpleShader extends ShaderProgram {

    private static final String VERTEX_FILE = 
            "res/shaders/simpleShader/simpleVertexShader.txt";
    private static final String FRAGMENT_FILE = 
            "res/shaders/simpleShader/simpleFragmentShader.txt";

    // Matrices
    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;   

    public SimpleShader() {
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
    }

    // Loads the transformation matrix into the shader uniform
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    // Loads the projection matrix into the shader uniform
    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(locationProjectionMatrix, projection);
    }

    // Loads the view matrix into the shader uniform
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix, viewMatrix);
    }
}
