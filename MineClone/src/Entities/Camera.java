package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Blocks.Chunk;

public class Camera {
	
	static Vector3f position;
	static Vector3f chunkPosition = new Vector3f(0, 0, 0);
	static boolean playerInNewChunk = true;
	float rotX;
	float rotY;
	float rotZ;
	float speed = 0.1f;
	float turn_speed = .1f;
	float moveAt = 0;
	
	public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}
	
	public void move() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveAt = -speed;
		}else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveAt = speed;
		} else {
			moveAt = 0;
		}
		
		if(Mouse.isGrabbed()) {
		rotX += Mouse.getDY() * turn_speed;
		rotY += Mouse.getDX() * turn_speed;	
		}
		
		float dx = (float) -(moveAt * Math.sin(Math.toRadians(rotY)));
		float dy = (float) (moveAt * Math.sin(Math.toRadians(rotX)));
		float dz = (float) (moveAt * Math.cos(Math.toRadians(rotY)));
		
		position.x += dx;
		position.y += dy;
		position.z += dz;
		
		testIfPlayerIsInNewChunk();
		
		//System.out.println(getPosition().toString());
		
		
	}

	public static Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}
	
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
		   System.out.println("Player is in new chunk.");	
		   playerInNewChunk = false;	
		   return true;
		} else {
		   return false;
		}
		
	}
	

}
