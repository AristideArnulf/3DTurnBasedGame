/*
 * This file is made by group 5 of the course 2IOE0
 */
package renderEngine;

import models.RawModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import renderEngine.objHandler.ModelData;

/**
 * Deals with loading 3d models into memory by storing positional data in a VAO
 * @author Leonardo
 */
public class Loader {
    
    // List containing all VAO id's
    private List<Integer> vaos = new ArrayList<Integer>();
    
    // List containing all VBO id's
    private List<Integer> vbos = new ArrayList<Integer>();
    
    // List containing all textures
    private List<Integer> textures = new ArrayList<Integer>();
    
    // Load rawModel
    public RawModel loadToVAO(float[] positions, float[] textureCoords, 
            float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }
    
    // Load RawModel from ModelData
    public RawModel loadToVAO(ModelData data) {
        // Extract data from modeldata
        int[] indices = data.getIndices();
        float[] positions = data.getVertices();
        float[] textureCoords  = data.getTextureCoords();
        float[] normals = data.getNormals();
        
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }
    
    // For GUI usage
   public RawModel loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 2, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / 2);
   }
    
    // Loads a texture into OpenGL
    public int loadTexture(String fileName) {
        Texture texture = null;
        try {
            texture = TextureLoader
                    .getTexture(
                            "PNG", 
                            new FileInputStream("res/" + fileName + ".png")
                    );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loader.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }
    
    // Once the game is closed, delete all vao's and vbo's
    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }
    
    // Returns the id of the created VAO
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        vaos.add(vaoID);
        return vaoID;
    }
       
    // Store the data into one of the attribute lists of the VAO
    private void storeDataInAttributeList(
            int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, 
                GL11.GL_FLOAT, false,0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
            
    // Unbinds the VAO
    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }
    
    // Load of index buffer (smart way to prevent coordinate duplicates)
    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, 
                buffer, GL15.GL_STATIC_DRAW);
    }
    
    // Stores indices into VBO
    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    
    // Convert float array data into float buffer
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
