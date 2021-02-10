package mainGame;

import ai.Algo;
import ai.MiniMax;
import engineTester.*;
import audio.AudioMaster;
import audio.Source;

import models.RawModel;
import models.TexturedModel;

import textures.ModelTexture;

import guis.GuiRenderer;
import guis.GuiTexture;

import entities.Entity;
import entities.Light;
import entities.cameras.Camera;
import entities.cameras.DefaultCamera;
import entities.cameras.FreeCamera;

import terrain.Terrain;

import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import renderEngine.Loader;
import renderEngine.objHandler.OBJFileLoader;
import renderEngine.objHandler.ModelData;

import toolbox.Globals;
import toolbox.Globals.State;

import logic.board.Board;
import logic.board.Move;
import logic.player.Player;
import logic.units.Unit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import toolbox.Globals.CameraState;

/**
 * Main game loop, consists of playing board
 *
 * @author All
 */
public class LogicTestGameLoop {

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
    Entity[][] playersEntity;
    List<Entity> tilesEntity;

    // Entity list for the lights
    List<Entity> lightsEntity;

    // Game Logic
    LogicLoop logic;

    // Flag for mouse button presses
    boolean mouseflag = true;

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

    // Setup the game logic
    private void setupLogic() {
        logic = new LogicLoop();
    }

    // Setup the camera
    private void setupCamera() {
        camera = new DefaultCamera(10, 0, 0, centerPieceEntity);
    }
    
    private void setupFreeCam () {
        Vector3f position = camera.getPosition();
        float yaw = camera.getYaw();
        float pitch = camera.getPitch();
        float roll = camera.getRoll();
        camera = new FreeCamera(position, yaw, roll, pitch);
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
            terrain.getTexture().setReflectivity(2f);
            terrain.getTexture().setShineDamper(200f);
        }
    }

    // Setup all the players
    private void setupPlayers() {
        playersEntity = new Entity[2][logic.p1.getUnits().size()];
        float playerheight = 2f;
        float scale = 0.15f;
        float scaleGnome = 0.13f;
        int j = 0;
        for (Player p : new Player[]{logic.p1, logic.p2}) {
            int i = 0;
            for (Unit u : p.getUnits()) {
                String fig = u.getUnitClass().toLowerCase();

                // Set index
                u.setIndex(i);

                // REPLACE BY 'fig' WHEN MODELS AVAILABLE:
                String path = "objects/game/models/".concat(fig + "/" + fig);

                // Load the OBJ model
                ModelData playersData = OBJFileLoader.loadOBJ(path);
                // Set it to rawmodel
                RawModel playerModel = loader.loadToVAO(playersData);

                // Load the texture
                ModelTexture playerTexture
                        = new ModelTexture(loader.loadTexture(path.concat(p.getName())));

                // Set lightning params
                playerTexture.setReflectivity(.5f);
                playerTexture.setShineDamper(100f);

                // Combines model with texture
                TexturedModel p1ayerTextured = new TexturedModel(playerModel,
                        playerTexture);

                // Set the position according to logic
                Vector3f position = logic.b.actualLocationToRenderLocation(
                        u.getPoint());
                position.y = playerheight;

                // Initializes the pieces
                playersEntity[j][i] = new Entity(p1ayerTextured, position, 0, 0, 0, 1);

                // Set scale and rotate if necessary
                float s = fig.equalsIgnoreCase("gnome") ? scaleGnome : scale;
                playersEntity[j][i].setScale(s);
                if (p.getSide() == -1) {
                    playersEntity[j][i].setRotY(180f);
                }
                i++;
            }
            j++;
        }
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
            int buffer
                    = AudioMaster.loadSound(new File("res/audio/LittleKing.wav"));
            source = new Source();
            source.play(buffer);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BoardGameLoop.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    // Reads the player input and do things accordingly
    private void readInput() {
        // Read mouse click input
        if (Mouse.isButtonDown(0) && globals.getState() == State.GAME
                && mouseflag) {

            mouseflag = false;
            Board b = logic.getBoard();
            // Create temp lists to evaluate closest clicked object
            List<Entity> temp = new ArrayList<Entity>();
            List<Float> dist = new ArrayList<Float>();
            Unit unit = null;
            Point mbPoint = null;

            // Calculate world coordinates from mouse coordinates:
            Vector3f world = renderer.mouseToWorld(
                    new Vector2f(Mouse.getX(), Mouse.getY()), camera);

            // Check for intersection with board
            Vector3f boardpos = renderer.intersectBoard(camera, world);

            // Check intersection of mouse ray for each piece
            for (Entity[] e : playersEntity) {
                for (Entity x : e) {
                    if (renderer.intersectPlayer(camera, world, x)) {
                        temp.add(x);
                        dist.add(renderer.intersection);
                    }
                }
            }

            // Set chosen point and unit
            if (!temp.isEmpty()) {
                mbPoint = b.renderLocationToActualLocation(temp.get(dist.
                        indexOf(Collections.min(dist))).getPosition());
                unit = b.getUnitAtLocation(mbPoint.x, mbPoint.y);
            } else if (boardpos.y >= 0f) {
                mbPoint = b.renderLocationToActualLocationBoard(boardpos);
                unit = null;
            }

            if (mbPoint != null) {
                if (logic.getChosenUnit() != null) {
                    Move m = new Move(mbPoint, logic.getChosenUnit());

                    if (logic.getChosenUnit().allowedMoves().contains(m)) {
                        logic.makeMove(m);
                        logic.setChosenUnit(null);
                    } else if (logic.getChosenUnit() == unit) {
                        logic.setChosenUnit(null);
                    } else if (unit != null && unit.getPlayer() == logic.getPlaying()) {
                        if (!unit.allowedMoves().isEmpty()) {
                            logic.setChosenUnit(unit);
                        }
                    }
                } else if (unit != null && unit.getPlayer() == logic.getPlaying()) {
                    if (!unit.allowedMoves().isEmpty()) {
                        logic.setChosenUnit(unit);
                    }
                } else {
                    logic.setChosenUnit(null);
                }
            }
        } else if (!Mouse.isButtonDown(0)) {
            mouseflag = true;
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    // Toggle axis
                    case Keyboard.KEY_T:
                        globals.flipAxisVisible();
                        break;

                    // Change the lightning mode
                    case Keyboard.KEY_L:
                        globals.flipRenderWithLightning();
                        break;

                    // Change rendering mode
                    case Keyboard.KEY_R:
                        if (globals.getState() == State.INTRO) {
                            break;
                        }
                        globals.flipRenderingMethod();
                        break;
                    
                    // Switch to freecam
                    case Keyboard.KEY_C:
                        if (globals.getCameraState() == CameraState.DEFAULT) {
                            globals.setCameraState(CameraState.FREE);
                            setupFreeCam();
                        } else {
                            globals.setCameraState(CameraState.DEFAULT);
                            setupCamera();
                        }
                        break;
                    
                    // End turn
                    case Keyboard.KEY_I:
                        Algo mm = new MiniMax(2);
                        Move aiMove = mm.execute(logic);
                        logic.makeMove(aiMove);
                        break;

                    //Test key:
                    case Keyboard.KEY_Z:
                        break;

                    // Start game
                    case Keyboard.KEY_SPACE:
                        if (globals.getState() == State.INTRO) {
                            globals.setState(State.GAME);
                        }
                        break;

                    // Exit game
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

    // Animate a piece move
    private void animatePlayers() {
        int j = 0;
        float speed = 0.03f;
        for (Player p : new Player[]{logic.p1, logic.p2}) {
            for (Unit u : p.getUnits()) {
                Vector3f pos;
                // not at desired position
                if (Math.abs(u.getxRender() - playersEntity[j][u.getIndex()].getPosition().x) > 0.01 || Math.abs(u.getyRender() - playersEntity[j][u.getIndex()].getPosition().z) > 0.01) {
                    System.out.println("pE: " + playersEntity[j][u.getIndex()].getPosition());
                    System.out.println("renderer: " + u.getxRender() + ", " + u.getyRender());
                    pos = new Vector3f(playersEntity[j][u.getIndex()].getPosition().x - speed * (playersEntity[j][u.getIndex()].getPosition().x - u.getxRender()),
                        playersEntity[j][u.getIndex()].getPosition().y,
                        playersEntity[j][u.getIndex()].getPosition().z - speed * (playersEntity[j][u.getIndex()].getPosition().z - u.getyRender()));
                } else { // at desired position
                    pos = new Vector3f(u.getxRender(),
                        playersEntity[j][u.getIndex()].getPosition().y,
                        u.getyRender());
                }
                
                playersEntity[j][u.getIndex()].setPosition(pos);
            }
            for (Unit u : p.getTakenUnits()) {
                Vector3f pos = new Vector3f(0f, -100f, 0f);
                playersEntity[j][u.getIndex()].setPosition(pos);
            }
            j++;
        }
    }
    
    // Render the tiles to show possible moves
    private void renderPossibleMoves() {
        tilesEntity = new ArrayList<Entity>();
        float TILESCALE = 0.5f;
        float TILEHEIGHT = 2.05f;
        if (logic.getChosenUnit() != null) {
            for (Move move : logic.chosenUnit.allowedMoves()) {
                // Load the OBJ model
                ModelData tileData
                        = OBJFileLoader.loadOBJ("objects/game/models/moveplane/moveplane");

                // Set it to rawmodel
                RawModel tileModel = loader.loadToVAO(tileData);

                // Load the texture
                ModelTexture tileTexture
                        = new ModelTexture(loader.
                                loadTexture("objects/game/models/moveplane/moveplane"));

                // Combines model with texture
                TexturedModel tileModelTextured
                        = new TexturedModel(tileModel, tileTexture);

                // Set the position
                // Place at 0, 0, 0 in world coordinates
                Vector3f position = logic.b.actualLocationToRenderLocation(move.getDestLoc());
                position.y = TILEHEIGHT;

                // Initializes the axis
                tilesEntity.add(new Entity(tileModelTextured, position, 0, 0, 0, 1));
                tilesEntity.get(tilesEntity.size()-1).setScale(TILESCALE);
            }
        } else {
            if (tilesEntity != null) {
                if (!tilesEntity.isEmpty()) {
                    for (Entity e : tilesEntity) {
                        renderer.removeEntity(e);
                    }
                }
            }
        }
    }


    public void mainLoop() {
        // Setup
        setup();
        setupLogic();
        setupIntroGui();
        setupGameGui();
        setupBoard();
        setupCenterPiece();
        setupAxis();
        setupCamera();
        setupLights();
        setupPlayers();
//        setupTiles();
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

                    // Enable game logic
                    logic.inGame = true;
                    

                    // Read player input
                    readInput();

                    // Call animation function
                    animatePlayers();

                    // Render possible move tiles
                    renderPossibleMoves();

                    // Allows the camera to move
                    camera.move();

                    // Add simple entities to simple render queue
                    renderer.processSimpleEntity(centerPieceEntity);

                    // Renders the scene
                    renderer.render(lights, camera);
                    System.out.println(camera.getPosition());
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

                        // Render the shaded objects
                        for (Terrain terrain : terrains) {
                            renderer.processTerrain(terrain);
                        }
                        for (Entity light : lightsEntity) {
                            renderer.processEntity(light);
                        }
                        for (Entity tile : tilesEntity) {
                            renderer.removeSimpleEntity(tile);
                        }
                        for (Entity tile : tilesEntity) {
                            renderer.processEntity(tile);
                        }
                        renderer.processEntity(boardEntity);

                        for (Entity[] e : playersEntity) {
                            for (Entity x : e) {
                                renderer.removeSimpleEntity(x);
                            }
                        }
                        renderer.processEntity(boardEntity);
                        for (Entity[] e : playersEntity) {
                            for (Entity x : e) {
                                renderer.processEntity(x);
                            }
                        }

                    } else {
                        for (Terrain terrain : terrains) {
                            renderer.removeTerrain(terrain);
                        }
                        for (Entity light : lightsEntity) {
                            renderer.removeEntity(light);
                        }
                        renderer.removeEntity(boardEntity);

                        for (Terrain terrain : terrains) {
                            renderer.processSimpleTerrain(terrain);
                        }
                        for (Entity light : lightsEntity) {
                            renderer.processSimpleEntity(light);
                        }
                        renderer.processSimpleEntity(boardEntity);

                        for (Entity[] e : playersEntity) {
                            for (Entity x : e) {
                                renderer.removeEntity(x);
                            }
                        }
                        renderer.processSimpleEntity(boardEntity);
                        for (Entity[] e : playersEntity) {
                            for (Entity x : e) {
                                renderer.processSimpleEntity(x);
                            }
                        }
                        for (Entity tile : tilesEntity) {
                            renderer.removeEntity(tile);
                        }
                        for (Entity tile : tilesEntity) {
                            renderer.processSimpleEntity(tile);
                        }

                    }

                    // Do not render GUI when we only render edges
                    if (!globals.isRenderLinesOnly()) {
                        guiRenderer.render(gameGuis);
                    }

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
        (new LogicTestGameLoop()).mainLoop();
    }
}
