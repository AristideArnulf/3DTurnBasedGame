/*
 * This file is made by group 5 of the course 2IOE0
 */
package renderEngine.objHandler;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a vertex in an OBJ file
 * @author Leonardo
 */
public class Vertex {
	
	private static final int NO_INDEX = -1;
	
	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;
	
	public Vertex(int index,Vector3f position){
		this.index = index;
		this.position = position;
		this.length = position.length();
	}
	
	public int getIndex(){
		return index;
	}
	
	public float getLength(){
		return length;
	}
	
	public boolean isSet(){
		return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
	}
	
        public Vector3f getPosition() {
		return position;
	}
        
	public boolean hasSameTextureAndNormal(int textureIndexOther,
                int normalIndexOther){
		return textureIndexOther == textureIndex && 
                        normalIndexOther == normalIndex;
	}
	
	public void setTextureIndex(int textureIndex){
		this.textureIndex = textureIndex;
	}
	
	public void setNormalIndex(int normalIndex){
		this.normalIndex = normalIndex;
	}
        
        public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	

}
