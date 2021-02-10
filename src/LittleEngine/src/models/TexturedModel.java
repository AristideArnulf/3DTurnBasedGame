/*
 * This file is made by group 5 of the course 2IOE0
 */
package models;

import textures.ModelTexture;

/**
 * Basically combining RawModel with a texture
 * @author Leonardo
 */
public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture texture;

    public TexturedModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }
    
    
    
    
}
