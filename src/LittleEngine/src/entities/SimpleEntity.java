/*
 * This file is made by group 5 of the course 2IOE0
 */
package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Datastructure for an instance of a textured model without lightning
 * @author Leonardo
 */
public class SimpleEntity {
    
    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    public SimpleEntity(TexturedModel model, Vector3f position, float rotX, 
            float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }
    
    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
