/*
 * This file is made by group 5 of the course 2IOE0
 */
package entities.cameras;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * This is the abstract camera class, used to define the view matrix
 * @author Leonardo
 */
public abstract class Camera {
    
    Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 10;
    private float yaw;
    private float roll;

    public Camera() {}
    
    // Move method
    public void move() {}

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }       
}
