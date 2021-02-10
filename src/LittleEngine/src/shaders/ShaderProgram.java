/*
 * This file is made by group 5 of the course 2IOE0
 */
package shaders;

import entities.Light;
import entities.cameras.Camera;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.List;
import org.lwjgl.BufferUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Links shaders to game
 * @author Leonardo
 */
public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
    
    // Buffer that is used to load matrix into uniform
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    
    public ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }
    
    // Returns all the locations of uniform variables.
    protected abstract void getAllUniformLocations();
    
    // Gets location of a uniform variable in the shader code
    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    // Start program
    public void start() {
        GL20.glUseProgram(programID);
    }
    
    // Stop program
    public void stop() {
        GL20.glUseProgram(0);
    }

    // Detach and delete shaders (good for memory)
    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }
    
    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }
    
    // Load float into uniform
    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }
    
    // Load vector into uniform
    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }
    
    // Load boolean into uniform
    protected void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }
    
    // Load matrix into uniform
    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    // Loads vertex and fragment shaders
    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
           
            BufferedReader reader = 
                    new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, 
                GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
    
    // Loads a vector into shader uniform
    void loadVector(int locationReflectivity, float reflectivity) {}
    
    // Loads the transformation matrix into the shader uniform
    public void loadTransformationMatrix(Matrix4f matrix) {}

    // Loads the projection matrix into the shader uniform
    public void loadProjectionMatrix(Matrix4f projection) {}

    // Loads the view matrix into the shader uniform
    public void loadViewMatrix(Camera camera) {}

    // Loads the light position and colour into the uniforms
    public void loadLights(List<Light> lights) {}
    
    // Loads specular variables into te uniforms
    public void loadShineVariables(float damper, float reflectivity) {}

}
