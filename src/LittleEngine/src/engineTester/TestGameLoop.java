package engineTester;

import entities.cameras.Camera;
import entities.Entity;
import entities.cameras.FreeCamera;
import entities.Light;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.MasterRenderer;
import renderEngine.objHandler.OBJLoader;
import textures.ModelTexture;

/**
 *
 * @author Leonardo
 */
public class TestGameLoop {
    public static void main(String[] args) {
        // Create the display
        DisplayManager.createDisplay();
        
        // Create instance of a loader
        Loader loader = new Loader();
        
        RawModel model = OBJLoader.loadObjModel("objects/cube/cube", loader);
        TexturedModel cubeModel = new TexturedModel(model, 
                new ModelTexture(loader.loadTexture("objects/cube/cube")));
        ModelTexture texture = cubeModel.getTexture();
        texture.setReflectivity(1);
        texture.setShineDamper(10f);
        
        List<Light> lights = new ArrayList<Light>();
        Light light = new Light(new Vector3f(0, 1, 1), new Vector3f(1, 1, 1));
        lights.add(light);
        
        Camera camera = new FreeCamera(new Vector3f(0,5,0));
        
        List<Entity> allCubes = new ArrayList<Entity>();
        Random random = new Random();
        
        for (int i = 0; i< 70000; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            allCubes.add(new Entity(cubeModel, new Vector3f(x, y, z), 
                    random.nextFloat() * 180, random.nextFloat() * 180, 0f, 1f));
        }
        for (Entity cube : allCubes) {
            cube.setScale(random.nextFloat() * 2 - 1);
        }
        
        MasterRenderer renderer = new MasterRenderer();
        while (!Display.isCloseRequested()) {
            camera.move();
            for (Entity cube : allCubes) {
                renderer.processEntity(cube);
            }
            for (Light li : lights) {
                li.increasePosistion(.5f, 0, 0);
            }
            renderer.render(lights, camera);
            DisplayManager.updateDisplay();
        }
        
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
