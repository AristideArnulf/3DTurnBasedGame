/*
 * This file is made by group 5 of the course 2IOE0
 */
package renderEngine;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS_CAP = 120;

    private static final String TITLE = "Little King";

    private static long lastFrameTime;
    private static float delta;
   
    public static void createDisplay() {

        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            try {
                Display.setIcon(new ByteBuffer[] {
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/objects/game/guis/icon.png")), false, false, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/objects/game/guis/icon.png")), false, false, null)
                });
            } catch (IOException ex) {
                Logger.getLogger(DisplayManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle(TITLE);
            
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }
    
    public static int getHeight() {
        return HEIGHT;
    }
    
    public static int getWidth() {
        return WIDTH;
    }

    public static void closeDisplay() {
        Display.destroy();
    }
    
    private static long getCurrentTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }
}
