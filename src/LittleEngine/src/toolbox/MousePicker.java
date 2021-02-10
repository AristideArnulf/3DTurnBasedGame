/*
 * This file is made by group 5 of the course 2IOE0
 */
package toolbox;

import entities.cameras.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Convert 2d clip space coordinates to 3d world space coordinates
 * @author Leonardo
 */
public class MousePicker {
    
    private Vector3f currentRay;
    
    private Matrix4f projectionMatrix;
    
    private Matrix4f viewMatrix;
    
    private Camera camera;
    
    public MousePicker(Camera camera, Matrix4f pojectionMatrixIn) {
        this.camera = camera;
        this.projectionMatrix = pojectionMatrixIn;
        this.viewMatrix = Maths.createViewMatrix(camera);
    }
    
    public Vector3f getCurrentRay() {
        return currentRay;
    }
    
    public void update() {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
    }
    
    private Vector3f calculateMouseRay() {
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizeDeviceCoords = getNormalizeDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords =
                new Vector4f(normalizeDeviceCoords.x,
                        normalizeDeviceCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }
    
    // Converts coordinate in eye space to coordinate in world space
    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedViewMatrix = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedViewMatrix, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }
    
    // Convers coordinate in clip space to coordinate in eye space
    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjectionMatrix = 
                Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = 
                Matrix4f.transform(invertedProjectionMatrix, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }
    
    // Converts 2d mouse point in viewport space to 2d point in normalized 
    // device space
    private Vector2f getNormalizeDeviceCoords(float mouseX, float mouseY) {
        float x = (2f * mouseX) / Display.getWidth() - 1f;
        float y = (2f * mouseY) / Display.getHeight() - 1f;
        return new Vector2f(x, y);
    }
    
}
