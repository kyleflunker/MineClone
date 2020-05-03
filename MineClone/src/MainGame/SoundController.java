package MainGame;

import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SoundController {
	
	public static Boolean walkPlayed = false;
	
	//this HashMap holds all the sounds that are currently playing in the game
	private static HashMap<String, Clip> currentSounds = new HashMap<String, Clip>();
	
	//play a sound once
	public static void playSound(String filepath, String soundName) {
		try {	
			Clip clip = AudioSystem.getClip();					
	        clip.open(AudioSystem.getAudioInputStream(new File(filepath)));
	        
	        //add the sound clip we've created to currentSounds
	        currentSounds.put(soundName, clip);	
	        
	        //if the sound ends, remove it from currentSounds
	        clip.addLineListener(new LineListener() {
	            public void update(LineEvent event) {
	              if (event.getType() == LineEvent.Type.STOP) {
	                event.getLine().close();
	                currentSounds.remove(soundName);
	              }
	            }
	        });
	        
	        clip.start();	
	        
		} catch (Exception e) {
			System.out.println("Error playing sound: " + soundName);
		}
	}
	
	//play a sound looped continuously
	public static void playLoopedSound(String filepath, String soundName) {
		try {	
			Clip clip = AudioSystem.getClip();					
	        clip.open(AudioSystem.getAudioInputStream(new File(filepath)));
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
	        clip.start();	
	        //add the sound clip we've created to currentSounds
	        currentSounds.put(soundName, clip);	
		} catch (Exception e) {
			System.out.println("Error playing sound: " + soundName);
		}
		if(soundName == "Walk") {
			walkPlayed = true;
		}
	}
	
	//stop a sound that's playing
	public static void stopSound(String soundName) {
		if (soundName == "Walk") {
			walkPlayed = false;
		}
		//reference the HashMap to get the correct sound and stop it
		currentSounds.get(soundName).stop();	
		currentSounds.remove(soundName);
	}
	
	public static Boolean soundPlaying(String soundName) {
		if(walkPlayed == false) {
			return true;
		} else {
			return false;
		}
	}

}
