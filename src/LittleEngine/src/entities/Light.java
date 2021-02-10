/*
 * This file is made by group 5 of the course 2IOE0
 */
package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * This class represents a light
 * @author Leonardo
 */
public class Light {
    
    private Vector3f position;
    private Vector3f colour;
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    // Default constructor for an infinite light
    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.colour = colour;
    }
    
    // Constructor for a point light
    public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
        this.position = position;
        this.colour = colour;
        this.attenuation = attenuation;
    }
       
    // Move the light in the world
    public void increasePosistion(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }
    

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public Vector3f getAttenuation() {
        return this.attenuation;
    }    
}
