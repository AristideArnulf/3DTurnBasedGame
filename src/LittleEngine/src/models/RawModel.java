/*
 * This file is made by group 5 of the course 2IOE0
 */
package models;

/**
 * Datastructure for a raw model
 * @author Leonardo
 */
public class RawModel {
    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }    
}
