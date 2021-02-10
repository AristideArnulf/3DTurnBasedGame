/*
 * This file is made by group 5 of the course 2IOE0
 */
package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * Represents a GUI texture
 * @author Leonardo
 */
public class GuiTexture {
    
    private int texture;
    
    // Center of the quad
    private Vector2f position;
    
    // Size of the quad, scale.x * scale.y
    private Vector2f scale;

    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }
    
    public void setXPosition(float x) {
        this.position.x = x;
    }
    
    public void setYPosition(float y) {
        this.position.y = y;
    }
    
    public void increasePosition(float dx, float dy) {
        this.position.x += dx;
        this.position.y += dy;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void changeScale(float dx, float dy) {
        this.scale.x += dx;
        this.scale.y += dy;
    } 
}
