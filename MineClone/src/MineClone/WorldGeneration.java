package MineClone;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.util.vector.Vector3f;

import BlockObjects.CactiSpawner;
import BlockObjects.OakTreeSpawner;
import BlockObjects.BirchTreeSpawner;
import Entities.Camera;
import Tools.Noise;
import Tools.Vec3i;
import java.util.Stack;

import java.util.Random;

import Blocks.*;

public class WorldGeneration {	
	
	public static HashMap<String, Chunk> generatedChunks = new HashMap<String, Chunk>();
	private static ArrayList<Chunk> renderedChunks = new ArrayList<Chunk>();
	public static List<Vec3i> needGenerate = new ArrayList<>();
	private static int initialGeneration = -50;
	public static int renderDistance = 100;
	public static boolean firstRun = true;
	private static OakTreeSpawner oakTreeSpawner =  new OakTreeSpawner();
	private static CactiSpawner cactiSpawner = new CactiSpawner();
	private static BirchTreeSpawner birchTreeSpawner = new BirchTreeSpawner();
	public static boolean woods = true;
	static Stack<Integer> stack = new Stack<Integer>();
	

	public static void chunkController() {
		//these position values will give you the chunk startX, startY, and startZ coordinates of the chunk the player is currently in
		int posX = (int) Math.floor(Camera.getPosition().x / 10) * 10;
		int posZ = (int) Math.floor(Camera.getPosition().z / 10) * 10;
		int posY = (int) Math.floor(Camera.getPosition().y / 10) * 10;
		long millis = System.currentTimeMillis();		
		
		renderedChunks.forEach(c -> c.tickUnload());
		renderedChunks.removeIf(c -> c.unloaded);
		
		
		//if player is in a new chunk
		if(Camera.isPlayerInNewChunk()) {
		   long unloadFrame = System.currentTimeMillis();
		   
		   for (Chunk c : renderedChunks) c.unloadFrame = unloadFrame;
		   
		   //if a rendered chunk is beyond render distance (relative to player position), unload it
		   for (Chunk c : renderedChunks) {
				if (c.position.x < posX - renderDistance || c.position.x > posX + renderDistance ||
					c.position.y < posY - renderDistance || c.position.y > posY + renderDistance ||
					c.position.z < posZ - renderDistance || c.position.z > posZ + renderDistance) {
					c.unloadChunk();
				}
			}
			
			renderedChunks.removeIf(c -> (!(
						c.position.x < posX - renderDistance || c.position.x > posX + renderDistance ||
						c.position.y < posY - renderDistance || c.position.y > posY + renderDistance ||
						c.position.z < posZ - renderDistance || c.position.z > posZ + renderDistance
			)));
			
		   needGenerate.clear();
		   
		   // in each xyz direction, add or create a chunk to renderedChunks all the way up to renderDistance
		   for(int x = posX - renderDistance - initialGeneration; x <= posX + renderDistance + initialGeneration; x += 10) {
			   for(int z = posZ - renderDistance - initialGeneration; z <= posZ + renderDistance + initialGeneration; z += 10) {
				   for(int y = posY - renderDistance - initialGeneration; y <= posY + renderDistance + initialGeneration; y += 10) {
					   
					   if(generatedChunks.containsKey(createChunkID(x, y, z))) {
	
							Chunk genChunk = generatedChunks.get(createChunkID(x, y, z));
							genChunk.unloading = false;
							genChunk.unloaded = false;
						    renderedChunks.add(genChunk);  
						    
					   } else {	
							if (firstRun) {
								createNewChunk(x, y, z);
								
							} else {
								needGenerate.add(new Vec3i(x, y, z));
							}
					   }	
				   }			   
			   }
		   }
	
		for (Chunk c : renderedChunks) {
			if (c.unloadFrame != unloadFrame) {
				c.needsPopIn = true; }
		   }
		}
		
		
		//the rest will run even if player isn't in new chunk		
		if (firstRun) {
			for (Chunk c : generatedChunks.values()) {
				if (c.needsRender) {
					c.chooseRenderedBlocks();
					c.needsPopIn = false;
				}
			}
			System.out.printf("Generation Time: %f\n", ((System.currentTimeMillis() - millis) / (double)1000));
			firstRun = false;
		}
		
		
		Vec3i bestP = null;
		Chunk best = null;
		float bestD = 999999999f;
		
		for (int count = 0; count < 2; count++) {
			bestD = 99999999999f;
			bestP = null;
			List<Vec3i> forcedGen = new ArrayList<>();
			for (Vec3i c : needGenerate) {
				float lenSq = Vector3f.sub(c.pos, Camera.position, null).lengthSquared();
				if (lenSq < 900) {					
					createNewChunk(c.x,c.y,c.z).needsPopIn = false;
					forcedGen.add(c);
				} else if (lenSq < bestD) {
					bestP = c;
					bestD = lenSq;
				}
			}
			
			needGenerate.removeAll(forcedGen);
			
			if (bestP != null) {
				needGenerate.remove(bestP);
				createNewChunk(bestP.x, bestP.y, bestP.z);
			} else break;
		}
		
		for (int count = 0; count < 2; count++) {
			bestD = 99999999999f;
			best = null;
			
			for (Chunk c : renderedChunks) {
				if (c.needsRender) {
					float lenSq = Vector3f.sub(c.position, Camera.position, null).lengthSquared();
					if (lenSq < 400) {
						c.chooseRenderedBlocks();
						c.needsPopIn = false;
						//System.out.println("FORCED CHUNK RENDER");
					} else if (lenSq < bestD) {
						best = c;
						bestD = lenSq;
					}
				}
			}
			
			if (best != null) {
				best.chooseRenderedBlocks();
			} else break;
		
		}
	
		initialGeneration = 0;
	}	
	
		   
		
	
	
	public static Chunk createNewChunk(int xPos, int yPos, int zPos) {					
		
		Random random = new Random();
		//these are the baselines for tree spawn rates
		int treecheckmax = 80;
		int treecheckmin = 0;
		int treecheck = 0;
		int counter = 0;
		
		int deepforest;
		deepforest = random.nextInt(100);
		//these are the baselines for cacti spawn rates
		int cacticheckmax = 80;
		int cacticheckmin = 0;
		int cacticheck = 0;
		
		
		Chunk newChunk = new Chunk(xPos, yPos, zPos);		
		
		//we don't want to create a chunk if it's already been created
		if (generatedChunks.containsKey(newChunk.getChunkID())) {
			return generatedChunks.get(newChunk.getChunkID()); 
	    }
		
		// if the chunk truly hasn't been created yet, then add it to generatedChunks
		generatedChunks.put(newChunk.getChunkID(), newChunk);
		newChunk.needsPopIn = true;
		newChunk.unloading = false;
		newChunk.unloaded = false;
		newChunk.needsRender = true;
		
		
		//populate the chunk with blocks
		for(int x = xPos; x < xPos + 10; x++) {
			for(int z = zPos; z < zPos + 10; z++) {	
				float noiseHeightValue = MainGame.noiseGenerator.generateHeight(x, z);
				for(float y = yPos; y < yPos + 10; y++) {	
					
					Random random2 = new Random();
					
					// if on the top layer of the chunk and the biome is grassland
					if(y == noiseHeightValue && woods == true) {
						// add a grass block
						newChunk.addToChunkBlocks(new Block(new Vector3f(x, y , z), 0));
						//determines if a tree spawns						
						treecheck = random2.nextInt(50);
						if(deepforest >= 90) {
							if(y == noiseHeightValue && treecheck == 15 || y == noiseHeightValue && treecheck == 20) {
								oakTreeSpawner.spawnOakTree(x, y, z);	
							}
							if(y == noiseHeightValue && treecheck == 30) {
								birchTreeSpawner.spawnBirchTree(x, y, z);
							}
						}
						if(deepforest > 30 && deepforest < 90) {
							if(y == noiseHeightValue && treecheck == 30) {
								oakTreeSpawner.spawnOakTree(x, y, z);	
							}
							if(y == noiseHeightValue && treecheck == 40) {
								birchTreeSpawner.spawnBirchTree(x, y, z);
							}
						}
				} 
					

					// if on the top layer of the chunk and the biome is desert
					if(y == noiseHeightValue && woods == false) {
						newChunk.addToChunkBlocks(new Block(new Vector3f(x, y, z), 3));
		
						cacticheck = random.nextInt((cacticheckmax - cacticheckmin) +1) + cacticheckmin;
						
						
						if(y == noiseHeightValue && cacticheck == 50) {
							cactiSpawner.spawnCacti(x, y, z);
							cacticheck = random.nextInt(50);
							if(cacticheck== 49) {
								woods = true;
							}
						}
					}
					
					// if below the top layer of the world, spawn stone
					if (y < noiseHeightValue) {
						newChunk.addToChunkBlocks(new Block(new Vector3f(x, y , z), 1));
					}
				
				}
			}
			stack.push(counter);
			if(stack.size() == 20000) {
				woods = false;
			}
			if(stack.size() >= 30000) {
				woods = true;
				stack.clear();
			}
		}
			
		renderedChunks.add(newChunk);	
		//since a new chunk is being created, we must re-render its adjacent chunks (if they exist)
		setAdjacentChunksNeedRender(newChunk);
		return newChunk;
	}
	
	// this is used to create a chunkID which is used for the key when checking for a chunk in the HashMap
	public static String createChunkID(int xCoord, int yCoord, int zCoord) {		
		return (xCoord + "-" + yCoord + "-" + zCoord);
	}
	
	
	// this is used to allow for adjacent chunks (to a chunk that has just been created or edited) to re-asses which blocks need to be rendered.
	public static void setAdjacentChunksNeedRender(Chunk chunk) {
		int xPos = chunk.getxStartCoord();
		int yPos = chunk.getyStartCoord();
		int zPos = chunk.getzStartCoord();
		//if an adjacent chunk exists, set it to re-render
		if (generatedChunks.containsKey(createChunkID(xPos+10, yPos   , zPos   ))) generatedChunks.get(createChunkID(xPos+10, yPos   , zPos   )).needsRender = true;
		if (generatedChunks.containsKey(createChunkID(xPos-10, yPos   , zPos   ))) generatedChunks.get(createChunkID(xPos-10, yPos   , zPos   )).needsRender = true;
		if (generatedChunks.containsKey(createChunkID(xPos   , yPos+10, zPos   ))) generatedChunks.get(createChunkID(xPos   , yPos+10, zPos   )).needsRender = true;
		if (generatedChunks.containsKey(createChunkID(xPos   , yPos-10, zPos   ))) generatedChunks.get(createChunkID(xPos   , yPos-10, zPos   )).needsRender = true;
		if (generatedChunks.containsKey(createChunkID(xPos   , yPos   , zPos+10))) generatedChunks.get(createChunkID(xPos   , yPos   , zPos+10)).needsRender = true;
		if (generatedChunks.containsKey(createChunkID(xPos   , yPos   , zPos-10))) generatedChunks.get(createChunkID(xPos   , yPos   , zPos-10)).needsRender = true;
	}
	
	
	// this tests to see if there exists a block at a specified point
	public static boolean isBlockSolid(int x1, int y1, int z1) {
		int x = (int) Math.floor(x1 / (float)10) * 10;
		int z = (int) Math.floor(z1 / (float)10) * 10;
		int y = (int) Math.floor(y1 / (float)10) * 10;
		String key = createChunkID(x,y,z);
		// if the chunk exists, check to see if there exists a block at the specified point
		if (generatedChunks.containsKey(key)) {
			Chunk chunk = generatedChunks.get(key);
			return chunk.getChunkBlocks()[chunk.determineArrayPosition(x1, y1, z1)] != null;
		} else {
		// else if the chunk doesn't exist, assume that it is solid
			return true;
		}
	}

	public static HashMap<String, Chunk> getGeneratedChunks() {
		return generatedChunks;
	}
	
	public static ArrayList<Chunk> getRenderedChunks() {
		return renderedChunks;
	}

	 
}



