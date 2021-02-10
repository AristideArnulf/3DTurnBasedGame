package engineTester;

import entities.cameras.Camera;
import entities.Entity;
import entities.cameras.FreeCamera;
import entities.Light;
import entities.cameras.DefaultCamera;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.MasterRenderer;
import renderEngine.objHandler.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;

/**
 *
 * @author Leonardo
 */
public class GridGameLoop {

    public static void main(String[] args) {

        boolean cameraType = true;
        // Create the display
        DisplayManager.createDisplay();

        // Create instance of a loader
        Loader loader = new Loader();

        // Create terrain chunk
        Terrain terrain = new Terrain(-.5f, -1.5f, loader,
                new ModelTexture(loader.loadTexture("objects/terrain/grass")));
        terrain.getTexture().setReflectivity(0);
        terrain.getTexture().setShineDamper(1);

        // Create island
        RawModel model = OBJLoader.loadObjModel("objects/terrain/island", loader);
        TexturedModel staticModel = new TexturedModel(
                model, new ModelTexture(loader.loadTexture("objects/terrain/island"))
        );
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10f);
        texture.setReflectivity(0);
        Entity entity
                = new Entity(staticModel, new Vector3f(-4.5f, -29.02f, -4.5f), 0, 0, 0, 1);

        // Middle axis
        RawModel axis = OBJLoader.loadObjModel("objects/tools/axis/axis", loader);
        TexturedModel axisModel = new TexturedModel(axis,
                new ModelTexture(loader.loadTexture("objects/tools/axis/axis")));
        Entity axisEntity
                = new Entity(axisModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
        
        // Middle axis
        RawModel centerPiece = 
                OBJLoader.loadObjModel("objects/tools/center/gamecenter", loader);
        TexturedModel centerPieceModel = new TexturedModel(centerPiece,
                new ModelTexture(loader.loadTexture("objects/tools/center/gamecenter")));
        Entity centerPieceEntity
                = new Entity(centerPieceModel, new Vector3f(0, -4, -9f), 0, 0, 0, 1);

        entity.setScale(10);
        
        List<Light> lights = new ArrayList<Light>();
        Light light = new Light(new Vector3f(0, 3, -15f), new Vector3f(1, 1, 1));
        lights.add(light);
        
        Camera camera = new DefaultCamera(centerPieceEntity);
        //Camera camera = new FreeCamera(new Vector3f(1, 5, 0));
        Camera camera2 = new FreeCamera(new Vector3f(100, 5, 0));

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            if (cameraType) {
                camera.move();
                renderer.processTerrain(terrain);
                renderer.processEntity(entity);
                renderer.processSimpleEntity(axisEntity);
                renderer.processSimpleEntity(centerPieceEntity);
                renderer.render(lights, camera);
            }
            if (!cameraType) {
                camera2.move();
                renderer.processTerrain(terrain);
                renderer.processEntity(entity);
                renderer.processSimpleEntity(axisEntity);
                renderer.processSimpleEntity(centerPieceEntity);
                renderer.render(lights, camera2);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                cameraType = !cameraType;
            }
            DisplayManager.updateDisplay();

        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
