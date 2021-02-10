/*
 * This file is made by group 5 of the course 2IOE0
 */
package entities;

import entities.Entity;
import java.util.List;
import java.util.Map;
import entities.StaticShader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shaders.SimpleShader;
import textures.ModelTexture;
import toolbox.Maths;

/**
 * Render model from vao
 * @author Leonardo
 */
public class SimpleEntityRenderer {

    private SimpleShader shader;

    public SimpleEntityRenderer(SimpleShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        // Start and stop shaders
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    } 
    
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity: batch) {
                prepareInstance(entity);
                // Renders objects using triangles as quads
                GL11.glDrawElements(
                        GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
        
    }
    
    // This method prepares a textured model
    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        
        // Enable VAO lists and bind Vertex array
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        
        // Load specular variables
        ModelTexture texture = model.getTexture();
        
        // Tell OpenGL to use a certain texture, put it in a texture bank
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 
                model.getTexture().getTextureID());
    }
    
    // This method unbinds a textured model
    private void unbindTexturedModel() {
        // Disable VAO lists
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        
        GL30.glBindVertexArray(0);
    }
    
    // This method prepares an instance
    private void prepareInstance(Entity entity) {
        // Loadup the position, rotation and scale to the vertex shader
        Matrix4f transformationMatrix = 
                Maths.createTransformationMatrix(
                        entity.getPosition(), 
                        entity.getRotX(), 
                        entity.getRotY(), 
                        entity.getRotZ(), 
                        entity.getScale()
                );
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
