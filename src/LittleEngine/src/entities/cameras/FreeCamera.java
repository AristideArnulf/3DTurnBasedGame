/*
 * This file is made by group 5 of the course 2IOE0
 */
package entities.cameras;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Extends Abstract Camera class. Free camera using WSAD
 * @author Leonardo
 */
public class FreeCamera extends Camera {
    
    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;

    public FreeCamera(Vector3f startingPosition) {
        this.position = startingPosition;
        this.pitch = 10;
    }

    public FreeCamera(Vector3f position, float yaw, float roll, float pitch) {
        this.position = position;
        this.yaw = yaw;
        this.roll = roll;
        this.pitch = pitch;
    }
        
    public void move() {
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= 0.05f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.05f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.05f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.05f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += 0.05f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            position.y -= 0.05f;
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
            yaw += .05f;
           
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            yaw -= .05f;
       
        }
        
        if(Keyboard.isKeyDown(Keyboard.KEY_W) 
                && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.z -= 1.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)
                && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.x += 1.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)
                && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.x -= 1.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)
                && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.z += 1.2f;
        }
    } 
    
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
