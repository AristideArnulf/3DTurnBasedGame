/*
 * This file is made by group 5 of the course 2IOE0
 */
package renderEngine.objHandler;

/**
 * Object of such type stores vertices, textureCoords, normals and indices.
 *
 * @author Leonardo
 */
public class ModelData {
    
    // Arrays to store OBJ data
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    
    // Float to store the furthespoint
    private float furthestPoint;

    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
            float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
    }

    // Returns the vertices array
    public float[] getVertices() {
        return vertices;
    }
    
    // Returns the texture coordinates array
    public float[] getTextureCoords() {
        return textureCoords;
    }
    
    // Returns the normals array
    public float[] getNormals() {
        return normals;
    }
    
    // Returns the indices array
    public int[] getIndices() {
        return indices;
    }

    // Returns the furthest point from the model it represents
    public float getFurthestPoint() {
        return furthestPoint;
    }

}
