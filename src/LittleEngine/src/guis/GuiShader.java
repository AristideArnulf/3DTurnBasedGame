/*
 * This file is made by group 5 of the course 2IOE0
 */
package guis;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;

/**
 *
 * @author Leonardo
 */
public class GuiShader extends ShaderProgram {
    
   private static final String VERTEX_FILE = 
            "res/shaders/guiShader/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = 
            "res/shaders/guiShader/guiFragmentShader.txt";

    // Matrices
    private int locationTransformationMatrix;
    
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
    
    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix =
                super.getUniformLocation("transformationMatrix");
    }

    // Binds the attributes
    @Override
    protected void bindAttributes() {
       super.bindAttribute(0, "position");
    }
    
    // Loads the transformation matrix into the shader uniform
    @Override
    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

}
