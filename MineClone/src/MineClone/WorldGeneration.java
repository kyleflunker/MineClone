package MineClone;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.util.vector.Vector3f;
import Entities.Camera;
import Blocks.Chunk;
import Tools.Noise;
import Tools.Vec3i;

import java.util.Random;
import Blocks.TreeSpawner;
import Blocks.*;

public class WorldGeneration {	
	
	public static HashMap<String, Chunk> generatedChunks = new HashMap<String, Chunk>();
	private static ArrayList<Chunk> renderedChunks = new ArrayList<Chunk>();
	public static List<Vec3i> needGenerate = new ArrayList<>();
	private static int initialGeneration = -50;
	public static boolean firstRun = true;
	static TreeSpawner treeSpawner =  new TreeSpawner();
	

	public static void chunkController() {
		//these position values will give you the chunk startX, startY, and startZ coordinates of the chunk the player is currently in
		int posX = (int) Math.floor(Camera.getPosition().x / 10) * 10;
		int posZ = (int) Math.floor(Camera.getPosition().z / 10) * 10;
		int posY = (int) Math.floor(Camera.getPosition().y / 10) * 10;

		int renderDistance = 100;
		
		long millis = System.currentTimeMillis();		
		renderedChunks.forEach(c -> c.tickUnload());
		renderedChunks.removeIf(c -> c.unloaded);
		
		//if player is in a new chunk
		if(Camera.isPlayerInNewChunk()) {
		   long unloadFrame = System.currentTimeMillis();
		   
		   for (Chunk c : renderedChunks) c.unloadFrame = unloadFrame;
		   
			for (Chunk c : renderedChunks) {
				if (
						c.position.x < posX - renderDistance || c.position.x > posX + renderDistance ||
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
		   
		   for(int i = posX - renderDistance - initialGeneration; i <= posX + renderDistance + initialGeneration; i += 10) {
			   for(int j = posZ - renderDistance - initialGeneration; j <= posZ + renderDistance + initialGeneration; j += 10) {
				   for(int k = posY - renderDistance - initialGeneration; k <= posY + renderDistance + initialGeneration; k += 10) {
					   
					   if(generatedChunks.containsKey(createChunkID(i, k, j))) {
	
							Chunk asd = generatedChunks.get(createChunkID(i, k, j));
							asd.unloading = false;
							asd.unloaded = false;
						    renderedChunks.add(asd);  
						    
					   } else {
	
						if (firstRun) {
							   createNewChunk(i, k, j); 
						} else {
							   needGenerate.add(new Vec3i(i, k, j));
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
			System.out.printf("generation time: %f\n", ((System.currentTimeMillis() - millis) / (double)1000));
			firstRun = false;
		}
		
		Vec3i bestP = null;
		Chunk best = null;
		float bestD = 999999999f;
		
		for (int qwerqwe = 0; qwerqwe < 2; ++qwerqwe) {
			bestD = 99999999999f;
			bestP = null;
			List<Vec3i> forcedGen = new ArrayList<>();
			for (Vec3i c : needGenerate) {
				float lenSq = Vector3f.sub(c.pos, Camera.position, null).lengthSquared();
				if (lenSq < 900) {
					//System.out.println("FORCED CHUNK GEN");
					createNewChunk(c.x,c.y,c.z).needsPopIn = false;
					forcedGen.add(c);
				} else if (lenSq < bestD) {
				bestP = c;
					bestD = lenSq;
				}
			}
			
			needGenerate.removeAll(forcedGen);
			
			if (bestP != null) {
				//System.out.println("frame chunk gen");
				needGenerate.remove(bestP);
				createNewChunk(bestP.x, bestP.y, bestP.z);
			} else break;
		}
		
		for (int qwerqwe = 0; qwerqwe < 2; ++qwerqwe) {
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
				//System.out.println("frame chunk render");
				best.chooseRenderedBlocks();
			} else break;
		
		}
	
		initialGeneration = 0;
		   
	}	
		   
		
	
	
	public static Chunk createNewChunk(int xPos, int yPos, int zPos) {

		//these are the baselines for tree spawn rates
		int treecheckmax = 80;
		int treecheckmin = 0;
		int treecheck = 0;
		
		//2 spawns sand
		int biomemax = 2;
		//1 spawns grass
		int biomemin = 1;
		Random num2 = new Random();
		int biomecheck = num2.nextInt((biomemax - biomemin) + 1) + biomemin;
		
		//these are the baselines for cacti spawn rates
		int cacticheckmax = 80;
		int cacticheckmin = 0;
		int cacticheck = 0;
		
		Chunk blockChunk = new Chunk(xPos, yPos, zPos);
		blockChunk.needsPopIn = true;
		
		
		
		
		
		for(int i = xPos; i < xPos + 10; i++) {
			for(int j = zPos; j < zPos + 10; j++) {				
					float zVal = MainGame.noiseGenerator.generateHeight(i, j);		
				for(float k = yPos; k < yPos + 10; k++) {		
					   
					if(k == zVal && biomecheck == 1) {
						// if top layer, add a grass block (block.type = 0)
						blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k , j), 0));
						//determines if a tree spawns
						Random num = new Random();
						treecheck = num.nextInt((treecheckmax - treecheckmin) + 1) + treecheckmin;
					} 
					//spawns the tree
					if(k == zVal && treecheck == 35 && i < 8 ) {
						treeSpawner.TreeSpawnerMethod(i, k, j);
					}
					//checks if the biome is sand or not
					if(k == zVal && biomecheck == 2) {
						blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k, j), 3));
						Random num3 = new Random();
						cacticheck = num3.nextInt((cacticheckmax - cacticheckmin) +1) + cacticheckmin;
					}
					//spawns in the cacti
					if(k == zVal && cacticheck == 50) {
						CactiSpawner b = new CactiSpawner(blockChunk, i, k, j, zVal);
					}
				else if (k < zVal) {
						 //if k is below the top layer, add a stone block (block.type = 1)
						blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k , j), 1));
					}
				
				}
			}
		}
		
		generatedChunks.put(blockChunk.getChunkID(), blockChunk);
		blockChunk.unloading = false;
		blockChunk.unloaded = false;
		renderedChunks.add(blockChunk);	
		//since a new chunk is being created, we must re-render its adjacent chunks (if they exist)
		setAdjacentChunksNeedRender(blockChunk);
		return blockChunk;
		
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



