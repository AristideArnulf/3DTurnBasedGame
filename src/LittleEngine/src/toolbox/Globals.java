/*
 * This file is made by group 5 of the course 2IOE0
 */
package toolbox;

/**
 * Stores all the game globals
 * @author Leonardo
 */
public class Globals {    
    
    // Class instance, used in singleton pattern
    private static Globals instance;
    
    // Settings changed by user
    
    // Boolean that returns true if user wants lightning 
    private boolean renderWithLightning;
    
    // Boolean that returns true if user only wants to see lines
    private boolean renderLinesOnly = false;
    
    // Boolean that returns true if user wants to show the axis frame
    private boolean axisVisible;
    
    // State the game is currently in
    private State state = State.INTRO;
    
    // Camera state we start in
    private CameraState cameraState = CameraState.DEFAULT;
    
    // All the states the game has
    public enum State {
        INTRO, GAME;
    }
    
    // All camera states
    public enum CameraState {
        DEFAULT, FREE;
    }

    // Private constructor so that other classes cant create this class in this
    // way
    private Globals() {
        this.renderWithLightning = true;
    }
    
    // Creates or returns current Globals instance
    public static Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }
    
    // Lightning in scene
    public boolean isRenderWithLightning() {
        return renderWithLightning;
    }

    public void setRenderWithLightning(boolean renderWithLightning) {
        this.renderWithLightning = renderWithLightning;
    }
    
    public void flipRenderWithLightning() {
        this.renderWithLightning = !this.renderWithLightning;
    }

    public boolean isRenderLinesOnly() {
        return renderLinesOnly;
    }

    public void setRenderLinesOnly(boolean renderLinesOnly) {
        this.renderLinesOnly = renderLinesOnly;
    }
    
    public void flipRenderingMethod() {
        this.renderLinesOnly = !this.renderLinesOnly;
    }
    
    // View the axis frame inside scene
    public boolean isAxisVisible() {
        return axisVisible;
    }

    public void setAxisVisible(boolean axisVisible) {
        this.axisVisible = axisVisible;
    }
    
    public void flipAxisVisible() {
        this.axisVisible = !this.axisVisible;
    }
    
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public CameraState getCameraState() {
        return cameraState;
    }

    public void setCameraState(CameraState cameraState) {
        this.cameraState = cameraState;
    }
    
    public void flipState() {
        State currentState = this.getState();
        if (currentState == State.INTRO) {
            this.state = State.GAME;
        } else if (currentState == State.GAME) {
            this.state = State.INTRO;
        }
    }
    
    
    
    
}
