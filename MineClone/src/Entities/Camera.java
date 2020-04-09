package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Blocks.Chunk;
import MineClone.*;

public class Camera {
	
	public static Vector3f position;
	public static Vector3f normal;
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
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			moveAt *= 5;
		}
		if(Mouse.isGrabbed()) {
			rotX -= Math.toRadians(Mouse.getDY() * turn_speed);
			rotY += Math.toRadians(Mouse.getDX() * turn_speed);	
		}

		rotY += Math.PI / 2;
		normal = new Vector3f(
			(float)(Math.cos(rotY) * Math.cos(rotX)),
			(float)(Math.sin(rotX)),
			(float)(Math.sin(rotY) * Math.cos(rotX))
			);
		rotY -= Math.PI / 2;
		float dx = moveAt * normal.x;
		float dy = moveAt * normal.y;
		float dz = moveAt * normal.z;
		
		position.x += dx;
		position.y += dy;
		position.z += dz;
		
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
