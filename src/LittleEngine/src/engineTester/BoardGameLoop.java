package engineTester;

import audio.AudioMaster;
import audio.Source;
import models.RawModel;
import models.TexturedModel;

import textures.ModelTexture;

import entities.Entity;
import entities.Light;
import entities.cameras.Camera;
import entities.cameras.DefaultCamera;
import guis.GuiRenderer;
import guis.GuiTexture;
import java.io.File;
import java.io.FileNotFoundException;

import terrain.Terrain;

import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import renderEngine.Loader;
import renderEngine.objHandler.OBJLoader;

import toolbox.Globals;
import toolbox.Globals.State;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.objHandler.OBJFileLoader;
import renderEngine.objHandler.ModelData;

/**
 * Main game loop, consists of playing board
 *
 * @author Leonardo
 */
public class BoardGameLoop {

    // Game components
    Loader loader;

    MasterRenderer renderer;

    GuiRenderer guiRenderer;

    Globals globals;

    State state;

    // The camera
    Camera camera;

    // Gui lists
    List<GuiTexture> introGuis;
    List<GuiTexture> gameGuis;

    // The scene lightsources
    List<Light> lights;

    // The scene terrain
    List<Terrain> terrains;

    // Entities
    Entity boardEntity;
    Entity centerPieceEntity;
    Entity axisEntity;
    Entity playersEntity;

    // Entity list for the lights
    List<Entity> lightsEntity;

    // Audio
    Source source;

    // Animate 
    float dx;

    // Setup of display and engine components
    private void setup() {
        // Create display
        DisplayManager.createDisplay();

        // Initialize the VAO loader
        loader = new Loader();

        // Setup renderer
        renderer = new MasterRenderer();

        // Setup GUI renderer
        guiRenderer = new GuiRenderer(loader);

        // Setup globals
        globals = globals.getInstance();

        // Get the game state
        state = globals.getState();
    }

    // Setup the camera
    private void setupCamera() {
        camera = new DefaultCamera(centerPieceEntity);
    }

    // Setup for the intro gui
    private void setupIntroGui() {
        // Create the list that stores the elements
        introGuis = new ArrayList<GuiTexture>();

        // Create the gui objects
        GuiTexture logo
                = new GuiTexture(loader.loadTexture("objects/game/guis/logo"),
                        new Vector2f(0f, 0f), new Vector2f(.75f, .75f));
        GuiTexture text
                = new GuiTexture(loader.loadTexture("objects/game/guis/playtext"),
                        new Vector2f(0f, -0.8f), new Vector2f(.25f, .25f));

        // Add the objects to the list
        introGuis.add(logo);
        introGuis.add(text);

        // Initialize the animate float
        dx = 0;
    }

    // Setup for the game gui
    private void setupGameGui() {
        gameGuis = new ArrayList<GuiTexture>();
        GuiTexture objective
                = new GuiTexture(loader.loadTexture("objects/game/guis/objective"),
                        new Vector2f(-.7f, 0.45f), new Vector2f(0.5f, 0.5f));
        GuiTexture helptext
                = new GuiTexture(loader.loadTexture("objects/game/guis/helptext"),
                        new Vector2f(0f, -0.8f), new Vector2f(0.25f, 0.25f));
        GuiTexture options
                = new GuiTexture(loader.loadTexture("objects/game/guis/options"),
                        new Vector2f(0.8f, 0.7f), new Vector2f(0.25f, 0.25f));
        gameGuis.add(objective);
        gameGuis.add(options);
        gameGuis.add(helptext);
    }

    // Setup all the lights
    private void setupLights() {
        // Initiate lights list
        lights = new ArrayList<Light>();

        // Create sun light
        Light sunOne
                = new Light(new Vector3f(0, 5, 100), new Vector3f(.9f, .9f, .9f));

        Vector3f spotlightColour = new Vector3f(.8f, .65f, 0.3f);
        Vector3f spotlightAttenuation = new Vector3f(1, 0.0007f, 0.00003f);

        // Create spotlights
        Light lightOne
                = new Light(new Vector3f(3.8f, 4f, 3.8f), spotlightColour,
                        spotlightAttenuation);
        Light lightTwo
                = new Light(new Vector3f(3.8f, 4f, -3.8f), spotlightColour,
                        spotlightAttenuation);
        Light lightThree
                = new Light(new Vector3f(-3.8f, 4f, 3.8f), spotlightColour,
                        spotlightAttenuation);
        Light lightFour
                = new Light(new Vector3f(-3.8f, 4f, -3.8f), spotlightColour,
                        spotlightAttenuation);

        // Add them to the array list
        lights.add(sunOne);

        lights.add(lightOne);
        lights.add(lightTwo);
        lights.add(lightThree);
        lights.add(lightFour);

    }

    // Setup all the light objects
    private void setupLightObjects() {
        // Initialize entity list
        lightsEntity = new ArrayList<Entity>();

        // Load the OBJ model
        ModelData lightObjectData
                = OBJFileLoader.loadOBJ("objects/game/models/lights/light");

        // Set it to rawmodel
        RawModel lightModel = loader.loadToVAO(lightObjectData);

        // Load the texture
        ModelTexture lightTexture
                = new ModelTexture(loader.loadTexture("objects/game/models/lights/light"));

        // Set lightning params
        lightTexture.setReflectivity(10f);
        lightTexture.setShineDamper(100f);

        // Combines model with texture
        TexturedModel lightTextured
                = new TexturedModel(lightModel, lightTexture);

        lightsEntity.add(new Entity(lightTextured,
                new Vector3f(3.5f, 1.8f, 3.5f), 0, 0, 0, 1));
        lightsEntity.add(new Entity(lightTextured,
                new Vector3f(3.5f, 1.8f, -3.5f), 0, 0, 0, 1));
        lightsEntity.add(new Entity(lightTextured,
                new Vector3f(-3.5f, 1.8f, 3.5f), 0, 0, 0, 1));
        lightsEntity.add(new Entity(lightTextured,
                new Vector3f(-3.5f, 1.8f, -3.5f), 0, 0, 0, 1));
    }

    // Sets the board up with model and texture
    private void setupBoard() {
        // Load the OBJ model
        ModelData boardData
                = OBJFileLoader.loadOBJ("objects/game/models/board/board");

        // Set it to rawmodel
        RawModel boardModel = loader.loadToVAO(boardData);

        // Load the texture
        ModelTexture boardTexture
                = new ModelTexture(loader.loadTexture("objects/game/models/board/board"));

        // Set lightning params
        boardTexture.setReflectivity(.1f);
        boardTexture.setShineDamper(10f);

        // Combines model with texture
        TexturedModel boardModelTextured
                = new TexturedModel(boardModel, boardTexture);

        // Set the position
        // Board is 9x9
        Vector3f position = new Vector3f(0, 2, 0);

        // Initializes the board
        boardEntity = new Entity(boardModelTextured, position, 0, 0, 0, 1);

    }

    // Setup the terrain
    private void setupTerrain() {
        terrains = new ArrayList<Terrain>();
        Terrain terrainBody = new Terrain(-0.5f, -0.5f, loader,
                new ModelTexture(loader.loadTexture("objects/game/water")));
        terrains.add(terrainBody);

        for (Terrain terrain : terrains) {
            terrain.getTexture().setReflectivity(1f);
            terrain.getTexture().setShineDamper(50f);
        }
    }

    // Setup all the players
    private void setupPlayers() {
        // Load the OBJ model
        ModelData playersData
                = OBJFileLoader.loadOBJ("objects/board/players");

        // Set it to rawmodel
        RawModel playerModel = loader.loadToVAO(playersData);

        // Load the texture
        ModelTexture playerTexture
                = new ModelTexture(loader.loadTexture("objects/board/players"));

        // Set lightning params
        playerTexture.setReflectivity(1f);
        playerTexture.setShineDamper(10f);

        // Combines model with texture
        TexturedModel playerTextured
                = new TexturedModel(playerModel, playerTexture);

        // Set the position
        // Place in center of 9x9 board
        Vector3f position = new Vector3f(.15f, 1.95f, .2f);

        // Initializes the centerpiece
        playersEntity
                = new Entity(playerTextured, position, 0, 0, 0, 1);
    }

    // Sets up the centerpiece
    private void setupCenterPiece() {
        // Load the OBJ model
        ModelData centerPieceData
                = OBJFileLoader.loadOBJ("objects/tools/center/gamecenter");

        // Set it to rawmodel
        RawModel centerPieceModel = loader.loadToVAO(centerPieceData);

        // Load the texture
        ModelTexture centerPieceTexture
                = new ModelTexture(loader.loadTexture("objects/tools/center/gamecenter"));

        // Combines model with texture
        TexturedModel centerPieceModelTextured
                = new TexturedModel(centerPieceModel, centerPieceTexture);

        // Set the position
        // Place in center of 9x9 board
        Vector3f position = new Vector3f(0, -1, 0);

        // Initializes the centerpiece
        centerPieceEntity
                = new Entity(centerPieceModelTextured, position, 0, 0, 0, 1);
    }

    // Setup the axis frame
    private void setupAxis() {
        // Load the OBJ model
        ModelData axisData
                = OBJFileLoader.loadOBJ("objects/tools/axis/axis");

        // Set it to rawmodel
        RawModel axisModel = loader.loadToVAO(axisData);

        // Load the texture
        ModelTexture axisTexture
                = new ModelTexture(loader.loadTexture("objects/tools/axis/axis"));

        // Combines model with texture
        TexturedModel axisModelTextured
                = new TexturedModel(axisModel, axisTexture);

        // Set the position
        // Place at 0, 0, 0 in world coordinates
        Vector3f position = new Vector3f(0, 0, 0.43f);

        // Initializes the axis
        axisEntity = new Entity(axisModelTextured, position, 0, 0, 0, 1);
    }

    // Setup OpenAL sounds
    private void setupAudio() {
        AudioMaster.init();
        AudioMaster.setListenerData();
        try {
            int buffer = 
                    AudioMaster.loadSound(new File("res/audio/LittleKing.wav"));
            source = new Source();
            source.play(buffer);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BoardGameLoop.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    // Reads the player input and do things accordingly
    private void readInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_T:
                        globals.flipAxisVisible();
                        break;

                    case Keyboard.KEY_L:
                        globals.flipRenderWithLightning();
                        break;

                    case Keyboard.KEY_SPACE:
                        if (globals.getState() == State.INTRO) {
                            globals.setState(State.GAME);
                        }
                        break;

                    case Keyboard.KEY_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
        }
    }

    // Animate a gui element to go from the left to the right
    private void animateIntroMove(GuiTexture guiElement) {
        GuiTexture introText = introGuis.get(1);
        guiElement.increasePosition(0.005f, 0);
        if (guiElement.getPosition().x >= 1.1) {
            guiElement.setXPosition(-1.27f);
        }
    }

    // Animate a gui object to scale up and scale down
    private void animateIntroScale(GuiTexture guiElement, float dx) {
        GuiTexture introText = introGuis.get(1);
        guiElement.changeScale(0.001f * (float) Math.sin(dx), 0.001f * (float) Math.sin(dx));
    }

    public void mainLoop() {
        // Setup
        setup();
        setupIntroGui();
        setupGameGui();
        setupBoard();
        setupCenterPiece();
        setupAxis();
        setupCamera();
        setupLights();
        setupPlayers();
        setupTerrain();
        setupLightObjects();

        // Start music
        setupAudio();

        // Main game loop, updated every frame
        while (!Display.isCloseRequested()) {
            switch (globals.getState()) {
                case INTRO:
                    // Read player input
                    readInput();

                    // Renders the scene
                    renderer.render(lights, camera);

                    // Renderer menu gui
                    guiRenderer.render(introGuis);

                    // Animate the text underneath the logo
                    GuiTexture introText = introGuis.get(1);

                    dx += 0.1;
                    dx %= 2 * Math.PI;
                    animateIntroScale(introText, dx);
                    //animateIntroMove(introText);

                    break;

                case GAME:
                    // Read player input
                    readInput();

                    // Allows the camera to move
                    camera.move();

                    // Add simple entities to simple render queue
                    renderer.processSimpleEntity(centerPieceEntity);

                    // Renders the scene
                    renderer.render(lights, camera);

                    // Render axis
                    if (globals.isAxisVisible()) {
                        renderer.processSimpleEntity(axisEntity);
                    } else {
                        renderer.removeSimpleEntity(axisEntity);
                    }

                    // Check if lightning is enabled and render accordingly
                    if (globals.isRenderWithLightning()) {
                        // Remove the simple objects
                        for (Terrain terrain : terrains) {
                            renderer.removeSimpleTerrain(terrain);
                        }
                        for (Entity light : lightsEntity) {
                            renderer.removeSimpleEntity(light);
                        }
                        renderer.removeSimpleEntity(boardEntity);
                        renderer.removeSimpleEntity(playersEntity);

                        // Render the shaded objects
                        for (Terrain terrain : terrains) {
                            renderer.processTerrain(terrain);
                        }
                        for (Entity light : lightsEntity) {
                            renderer.processEntity(light);
                        }
                        renderer.processEntity(boardEntity);
                        renderer.processEntity(playersEntity);

                    } else {
                        for (Terrain terrain : terrains) {
                            renderer.removeTerrain(terrain);
                        }
                        for (Entity light : lightsEntity) {
                            renderer.removeEntity(light);
                        }
                        renderer.removeEntity(boardEntity);
                        renderer.removeEntity(playersEntity);

                        for (Terrain terrain : terrains) {
                            renderer.processSimpleTerrain(terrain);
                        }
                        for (Entity light : lightsEntity) {
                            renderer.processSimpleEntity(light);
                        }
                        renderer.processSimpleEntity(boardEntity);
                        renderer.processSimpleEntity(playersEntity);

                    }

                    guiRenderer.render(gameGuis);

                    break;
            }

            // Update the display every frame
            DisplayManager.updateDisplay();
        }

        // When close is requested
        renderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();

        // Clean up for the audio
        source.delete();
        AudioMaster.cleanUp();

        // Close the display
        DisplayManager.closeDisplay();
    }

    public static void main(String[] args) {
        (new BoardGameLoop()).mainLoop();
    }
}
