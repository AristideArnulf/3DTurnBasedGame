/*
 * This file is made by group 5 of the course 2IOE0
 */
package audio;

import org.lwjgl.openal.AL10;

/**
 * Sound sources, objects that can play sound effects
 * @author Leonardo
 */
public class Source {
    
    private int sourceId;

    // Constructor
    public Source() {
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_GAIN, .4f);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
        AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING,  AL10.AL_TRUE);
    }
    
    public void play(int buffer) {
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        AL10.alSourcePlay(sourceId);
    }
    
    // Delete source once we are done with it
    public void delete() {
        AL10.alDeleteSources(sourceId);
    }
    
}
