/*
 * This file is made by group 5 of the course 2IOE0
 */
package entities.cameras;

import entities.Entity;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Default rotating camera, rotates around object (entity)
 * @author Leonardo
 */
public class DefaultCamera extends Camera {
    
    // Camera settings
    private float distanceFromEntity = 20;
    private float angleAroundEntity = 0;

    // Default position
    Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;
    
    private Entity entity;

    public DefaultCamera(Entity centerEntity) {
        this.entity = centerEntity;  
        this.pitch = 10;
    }

    public DefaultCamera(float pitch, float yaw, float roll, Entity entity) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.entity = entity;
    }
    
    
    
    
    // Move method
    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundEntity();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (entity.getRotY() + angleAroundEntity);
        yaw%=360;
    }
    
    public void invertPitch(){
        this.pitch = -pitch;
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
   
    private void calculateCameraPosition(float horizontalDistance, 
            float verticalDistance) {
        float theta = entity.getRotY() + angleAroundEntity;
        float offsetX = 
                (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = 
                (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = entity.getPosition().x - offsetX;
        position.z = entity.getPosition().z - offsetZ;
        position.y = entity.getPosition().y + verticalDistance + 4;
        
    }
    
    private float calculateHorizontalDistance() {
        return (float) (distanceFromEntity * Math.cos(Math.toRadians(pitch + 4)));
    }
    
    private float calculateVerticalDistance() {
        return (float) (distanceFromEntity * Math.sin(Math.toRadians(pitch + 4)));
    }
    
    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.02f; 
        distanceFromEntity -= zoomLevel;
        
        // Check if distance is not too close
        if (distanceFromEntity < 3) {
            distanceFromEntity = 3;
        }
        
        // Check if distance is not too far
        if (distanceFromEntity > 20) {
            distanceFromEntity = 20;
        }
    }
    
    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.4f;
            pitch -= pitchChange;
            
            // Prevents flipping of the camera
            if (pitch < 0) {
                pitch = 0;
            } else if (pitch > 90) {
                pitch = 90;
            }
        }
    }
    
    private void calculateAngleAroundEntity() {
        if (Mouse.isButtonDown(1)) {
            float angleChange = Mouse.getDX() * 0.4f;
            angleAroundEntity -= angleChange;
        }
    }    
}
