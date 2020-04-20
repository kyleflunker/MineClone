package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Blocks.Chunk;
import MineClone.*;
import Tools.Noise;

public class Camera {
	
	public static Vector3f position = new Vector3f(0, 0, 0);
	public static Vector3f normal = new Vector3f(1, 0, 0);
	public static Vector3f frontVec = new Vector3f(1, 0, 0);
	public static Vector3f rightVec = new Vector3f(1, 0, 0);
	public static Vector3f headVec = new Vector3f(1, 0, 0);
	public static Vector3f upVec = new Vector3f(0, 1, 0);
	public static Vector3f moveVec = new Vector3f(1, 0, 0);
	static Vector3f chunkPosition = new Vector3f(0, 0, 0);
	static boolean playerInNewChunk = true;
	float rotX;
	float rotY;
	float rotZ;
	float speed = 0.1f;
	float turn_speed = .1f;
	float moveAt = 0;
	private static float targetY = 0;
	
	public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
		this.position = position;
		targetY = position.y;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}
	
	public void move() {
		float horizMove = 0;
		float vertMove = 0;
		
		//handle front/back movement
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveAt = -speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveAt = speed;
		} else {
			moveAt = 0;
		}
		
		//handle left/right movement
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			horizMove = -speed;
		} 
		else if (!Keyboard.isKeyDown(Keyboard.KEY_D) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
			horizMove = speed;
		}
		
		//handle up/down movement
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			vertMove = speed;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			vertMove = -speed;
		}
		
		//if mouse is attached to the game, modify rotation values
		if(Mouse.isGrabbed()) {
			rotX -= Math.toRadians(Mouse.getDY() * turn_speed);
			rotY += Math.toRadians(Mouse.getDX() * turn_speed);	
		}

		//simple, simple collision system.  set the player's y position 3 above the noiseGenerator's output		
		targetY = MainGame.noiseGenerator.generateHeight((int) position.x, (int) position.z) + 3;
		position.y += (targetY - position.y) / 5.0f;				
 

 		rotY += Math.PI / 2;
 		normal = new Vector3f(
 			(float)(Math.cos(rotY) * Math.cos(rotX)),
 			(float)(Math.sin(rotX)),
 			(float)(Math.sin(rotY) * Math.cos(rotX))
 			);
		frontVec.set(normal);
		Vector3f.cross(normal, upVec, rightVec);
		Vector3f.cross(normal, rightVec, headVec);

 		rotY -= Math.PI / 2;

		frontVec.scale(moveAt);
		rightVec.scale(horizMove);
		headVec.scale(vertMove);

		Vector3f.add(frontVec, rightVec, moveVec);
		Vector3f.add(moveVec, headVec, moveVec);
		
		if (moveVec.lengthSquared() != 0) {
			moveVec.normalise();
			moveVec.scale(speed);
			
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				moveVec.scale(5);
			}
			Vector3f.add(position, moveVec, position);
		}
		
		testIfPlayerIsInNewChunk();
		
	}

	public static Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return (float)Math.toDegrees(rotX);
	}

	public float getRotY() {
		return (float)Math.toDegrees(rotY);
	}

	public float getRotZ() {
		return (float)Math.toDegrees(rotZ);
	}
	
	//sets playerInNewChunk to true if player has entered a new chunk
	public void testIfPlayerIsInNewChunk() {
		int posX = (int) Math.floor(Camera.getPosition().x / 10) * 10;
		int posZ = (int) Math.floor(Camera.getPosition().z / 10) * 10;
		int posY = (int) Math.floor(Camera.getPosition().y / 10) * 10;
		if(posX != chunkPosition.x || posZ != chunkPosition.z || posY != chunkPosition.y) {
			playerInNewChunk = true;
			
			chunkPosition = new Vector3f(posX, posY, posZ);			
		}
	}
	
	public static boolean isPlayerInNewChunk() {
		if(playerInNewChunk) {
		   playerInNewChunk = false;	
		   return true;
		} else {
		   return false;
		}
		
	}
	

}
