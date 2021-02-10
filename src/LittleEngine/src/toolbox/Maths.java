/*
 * This file is made by group 5 of the course 2IOE0
 */
package toolbox;

import entities.cameras.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Class that holds all maths concepts needed for our games
 * @author Leonardo
 */
public class Maths {
    
    // Returns a transformation matrix for 2D gui usage
    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x / ( (float) 1280 / 720), scale.y, 1f), matrix, matrix);
		return matrix;
	}
    
    // Returns a transformation matrix
    public static Matrix4f createTransformationMatrix(
            Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate(
                (float) Math.toRadians(rx), 
                new Vector3f(1, 0, 0), 
                matrix, matrix);
        Matrix4f.rotate(
                (float) Math.toRadians(ry), 
                new Vector3f(0, 1, 0), 
                matrix, matrix);
        Matrix4f.rotate(
                (float) Math.toRadians(rz), 
                new Vector3f(0, 0, 1), 
                matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }
    
    // Returns a view matrix based on the camera
     public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), 
                new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()),
                new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()),
                new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = 
                new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
}
