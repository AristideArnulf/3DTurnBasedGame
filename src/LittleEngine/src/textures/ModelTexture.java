/*
 * This file is made by group 5 of the course 2IOE0
 */
package textures;

/**
 * Datastructure for a texture
 * @author Leonardo
 */
public class ModelTexture {
    
    private int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public int getTextureID() {
        return this.textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
