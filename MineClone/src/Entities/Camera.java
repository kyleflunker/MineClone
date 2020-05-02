package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import Entities.PlayerHand;
import Blocks.Block;
import Blocks.Chunk;
import MainGame.*;

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
	private long lastTimePlacedOrDestroyed = 0;
	
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
		targetY = RunGame.noiseGenerator.generateHeight((int) position.x, (int) position.z) + 3;
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
 		
 		//handle block pacing and destroying
 		if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			Vector3f oldRayVec = new Vector3f(position);
			Vector3f rayVec = new Vector3f(position);
			Vector3f incVec = (Vector3f) new Vector3f(frontVec).scale(-1/100f);
			for (int i = 0; i < 700; i++) {
				rayVec = Vector3f.add(incVec, rayVec, null);
				int x = (int) rayVec.getX();
				int y = (int) rayVec.getY();
				int z = (int) rayVec.getZ();
				int ox = (int) oldRayVec.getX();
				int oy = (int) oldRayVec.getY();
				int oz = (int) oldRayVec.getZ();
				if (x != ox || y != oy || z != oz) {
					if (WorldGeneration.isBlockSolid(x, y, z)) {
						//if enough time has passed since the user has placed or destroyed a block, allow them to place or destroy a block
						if(System.currentTimeMillis() - lastTimePlacedOrDestroyed > 400) {							
							if (Mouse.isButtonDown(0)) {
								Chunk c = WorldGeneration.getGeneratedChunks().get(WorldGeneration.createChunkID(
										(int) Math.floor(x / 10f) * 10,
										(int) Math.floor(y / 10f) * 10,
										(int) Math.floor(z / 10f) * 10));
								c.getChunkBlocks()[c.determineArrayPosition(x, y, z)] = null;
								c.chooseRenderedBlocks();
								WorldGeneration.setAdjacentChunksNeedRender(c);
							} else {
								if(!PlayerHand.isHand()) {
									Chunk c = WorldGeneration.getGeneratedChunks().get(WorldGeneration.createChunkID(
											(int) Math.floor(ox / 10f) * 10,
											(int) Math.floor(oy / 10f) * 10,
											(int) Math.floor(oz / 10f) * 10));
									c.addToChunkBlocks(new Block(new Vector3f(ox, oy, oz), PlayerHand.getSelectedBlock()));
									c.chooseRenderedBlocks();
									WorldGeneration.setAdjacentChunksNeedRender(c);
								}
							}							
							lastTimePlacedOrDestroyed = System.currentTimeMillis();
							break;
						}
					}
				}
				oldRayVec = rayVec;
			}
			 
			
		}
 		

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
