package engineTester;

import ai.Algo;
import ai.MiniMax;
import entities.cameras.Camera;
import entities.Entity;
import entities.cameras.FreeCamera;
import entities.Light;
import guis.GuiRenderer;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import logic.board.Move;
import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.MasterRenderer;
import renderEngine.objHandler.OBJLoader;
import entities.EntityRenderer;
import entities.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;
import toolbox.Globals;

/**
 *
 * @author Leonardo
 */
public class WaterTestLoop {
    Globals globals = Globals.getInstance();
    
    
    public void game() {
        globals.setState(Globals.State.GAME);
        // Create the display
        DisplayManager.createDisplay();
        
        // Create instance of a loader
        Loader loader = new Loader();
        
        // Create castle
        RawModel model = OBJLoader.loadObjModel("objects/castle/castle", loader);
        
        TexturedModel staticModel = new TexturedModel(
                model, new ModelTexture(loader.loadTexture("objects/castle/castle"))
        );
        ModelTexture texture = staticModel.getTexture();
        texture.setShineDamper(10f);
        texture.setReflectivity(0.5f);

        
        Entity entity = 
                new Entity(staticModel, new Vector3f(3.5f, -1, -4.5f), 0, 0, 0, 1);

        // Create tree
        RawModel treeModel = OBJLoader.loadObjModel("objects/tree/tree", loader);
        TexturedModel staticTreeModel = new TexturedModel(
                treeModel, new ModelTexture(loader.loadTexture("objects/tree/tree"))
        );
        ModelTexture treeTexture = staticTreeModel.getTexture();
        texture.setShineDamper(10f);
        texture.setReflectivity(0.2f);
        List<Entity> allTrees = new ArrayList<Entity>();        
        Random random = new Random();
        for (int i = 0; i< 100; i++) {
            float x = 1 + random.nextFloat() * 8;
            float y = -2;
            float z = -9 + random.nextFloat() * 9;
            allTrees.add(new Entity(staticTreeModel, new Vector3f(x, y, z), 
                    0, random.nextFloat() * 180, 0f, 1f));
        }

        
        // Create terrain chunk
        Terrain terrain = new Terrain(0, -1f, loader, 
                new ModelTexture(loader.loadTexture("objects/terrain/water")));
        
        // Middle cube
        RawModel cube = OBJLoader.loadObjModel("objects/tools/axis/axis", loader);
        TexturedModel cubeModel = new TexturedModel(cube, 
                new ModelTexture(loader.loadTexture("objects/tools/axis/axis")));
        
        Entity cubeEntity = 
                new Entity(cubeModel, new Vector3f(0, -2, .4f), 0, 0, 0, 1);
        
        List<Light> lights = new ArrayList<Light>();
        Light light = new Light(new Vector3f(1,1000,700), new Vector3f(1,1,1));
        lights.add(light);
        
        Camera camera = new FreeCamera(new Vector3f(4.5f, 4, 4.5f));
        

        MasterRenderer renderer = new MasterRenderer();
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        while (!Display.isCloseRequested()) {
            camera.move();
            readInput();
            if (globals.isRenderWithLightning()) {
                renderer.processTerrain(terrain);
                renderer.render(lights, camera);
            } else {
                renderer.processSimpleTerrain(terrain);
                renderer.render(lights, camera);
            }
            DisplayManager.updateDisplay();
            
        }
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
    public static void main(String[] args) {
        new WaterTestLoop().game();
    }

    private void readInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    // Change the lightning mode
                    case Keyboard.KEY_L:
                        globals.flipRenderWithLightning();
                        break;

                    // Change rendering mode
                    case Keyboard.KEY_R:
                        if (globals.getState() == Globals.State.INTRO) {
                            break;
                        }
                        globals.flipRenderingMethod();
                        break;  

                    // Exit game
                    case Keyboard.KEY_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        }
    }
    

}
