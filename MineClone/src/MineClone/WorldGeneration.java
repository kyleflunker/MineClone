package MineClone;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import Blocks.DirtBlock;
import Blocks.GrassBlock;
import Blocks.StoneBlock;
import Entities.Camera;
import Blocks.Chunk;
import RenderEngine.Loader;
import Tools.Noise;
import Blocks.OakTreeBlock;
import Blocks.OakLeafBlock;
import java.util.Random;
import Models.*; import Textures.*; import Entities.*; import Blocks.*;

public class WorldGeneration {	
	
	public static HashMap<String, Chunk> generatedChunks = new HashMap<String, Chunk>();
	private static ArrayList<Chunk> renderedChunks = new ArrayList<Chunk>();

	public static void chunkController() {
		//these position values will give you the chunk startX, startY, and startZ coordinates of the chunk the player is currently in
		int posX = (int) Math.floor(Camera.getPosition().x / 10) * 10;
		int posZ = (int) Math.floor(Camera.getPosition().z / 10) * 10;
		int posY = (int) Math.floor(Camera.getPosition().y / 10) * 10;

		int renderDistance = 30;
		
		//if the player is in a new chunk (since the last movement), render chunks relative to the chunk they're currently in
		if(Camera.isPlayerInNewChunk()) {
		   renderedChunks.clear();
		   for(int i = posX - renderDistance; i <= posX + renderDistance; i += 10) {
			   for(int j = posZ - renderDistance; j <= posZ + renderDistance; j += 10) {
				   for(int k = posY - renderDistance; k <= posY + renderDistance; k += 10) {
					   
					   if(generatedChunks.containsKey(createChunkID(i, k, j))) {
						   renderedChunks.add(generatedChunks.get(createChunkID(i, k, j)));  
					   } else {
						   createNewChunk(i, k, j); 
					   }				  
				   
				   }			   
			   }
		   }
		//for the chunks that should rendered, check to see if they need to re-choose renderedBlocks
		for (Chunk chunk : generatedChunks.values()) {
			if (chunk.needsRender)
				chunk.chooseRenderedBlocks();
		}
		}
		   
	}	
	
	
	public static void createNewChunk(int xPos, int yPos, int zPos) {
		
		int biomespawnermax = 70;
		int biomespawnermin = 1;
		Random num = new Random();
		int biomecheck = num.nextInt((biomespawnermax - biomespawnermin) + 1) + biomespawnermin;
		
		if(biomecheck >= 60) {
			//System.out.println("Forest Spawned");
		}
		
		int treecheckmax = 30;
		int treecheckmin = 1;
		Random num2 = new Random();
		int treecheck = num2.nextInt((treecheckmax - treecheckmin) + 1) + treecheckmin;
		
		
		Noise height = new Noise(100, 14, 20, 32423);
		Chunk blockChunk = new Chunk(xPos, yPos, zPos);
		
		//since a new chunk is being created, we must re-render its adjacent chunks (if they exist)
		setAdjacentChunksNeedRender(blockChunk);
		
		generatedChunks.put(blockChunk.getChunkID(), blockChunk);
		renderedChunks.add(blockChunk);	
		

		for(int i = xPos; i < xPos + 10; i++) {
			for(int j = zPos; j < zPos + 10; j++) {				
					float zVal = height.generateHeight(i, j);		
				for(float k = yPos; k < yPos + 10; k++) {		
					   
					if(k == zVal) {
						// if top layer, add a grass block (block.type = 0)
						blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k , j), 0));	
					} else if (k < zVal) {
						// if k is below the top layer, add a stone block (block.type = 1)
						blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k , j), 1));
					}
				   
				//increments a counter to spawn trees
				treecheck++;
				//determines if a forest biome spawns
				if(biomecheck >= 60) {
					treecheck = 0;
					treecheck+=15;
				}
				
				/*
					if(treecheck == 30) {
						//determines the height of the tree based on the random num generator above
						float blockcounter = zVal+7;
						for(float x = zVal; x <= blockcounter; x++) {
							//creates the tree itself
							new TreeBlock(loader, blockChunk, new Vector3f(i, x, j));
							if(x == zVal+4 || x == zVal+5) {
								for(int r = 0; r <= 2; r++) {
								//Unfortunately each of these calls must be here in order to properly construct the leaves, but the method to not render the leaves that are hidden is still in effect
								new LeafBlock(loader, blockChunk, new Vector3f(i-r, x, j));
								new LeafBlock(loader, blockChunk, new Vector3f(i+r, x, j));
								new LeafBlock(loader, blockChunk, new Vector3f(i-r, x, j-r));
								new LeafBlock(loader, blockChunk, new Vector3f(i+r, x, j-r));
								new LeafBlock(loader, blockChunk, new Vector3f(i+r, x, j+r));
								new LeafBlock(loader, blockChunk, new Vector3f(i, x, j-r));
								new LeafBlock(loader, blockChunk, new Vector3f(i, x, j+r));
								new LeafBlock(loader, blockChunk, new Vector3f(i-r, x, j+r));
								if(r == 2) {
								new LeafBlock(loader, blockChunk, new Vector3f(i + (2*1/r), x, j + r));
								new LeafBlock(loader, blockChunk, new Vector3f(i + (2*1/r), x, j - r));
								new LeafBlock(loader, blockChunk, new Vector3f(i - (2*1/r), x, j + r));
								new LeafBlock(loader, blockChunk, new Vector3f(i - (2*1/r), x, j - r));
								new LeafBlock(loader, blockChunk, new Vector3f(i + r, x, j + (2*1/r)));
								new LeafBlock(loader, blockChunk, new Vector3f(i + r, x, j - (2*1/r)));
								new LeafBlock(loader, blockChunk, new Vector3f(i - r, x, j + (2*1/r)));
								new LeafBlock(loader, blockChunk, new Vector3f(i - r, x, j - (2*1/r)));
									}
								}
							}
							//changes how many leaves spawn based on the height of the tree
							if(x == zVal+6) {
								for(int r = 0; r <= 1; r++) {
									new LeafBlock(loader, blockChunk, new Vector3f(i-r, x, j));
									new LeafBlock(loader, blockChunk, new Vector3f(i+r, x, j));
									new LeafBlock(loader, blockChunk, new Vector3f(i-r, x, j-r));
									new LeafBlock(loader, blockChunk, new Vector3f(i+r, x, j-r));
									new LeafBlock(loader, blockChunk, new Vector3f(i+r, x, j+r));
									new LeafBlock(loader, blockChunk, new Vector3f(i, x, j-r));
									new LeafBlock(loader, blockChunk, new Vector3f(i, x, j+r));
									new LeafBlock(loader, blockChunk, new Vector3f(i-r, x, j+r));
								}
							}
							if(x == zVal+7) {
								for(int r = 0; r <= 1; r++) {
									new LeafBlock(loader, blockChunk, new Vector3f(i+r, x, j));
									new LeafBlock(loader, blockChunk, new Vector3f(i, x, j-r));
									new LeafBlock(loader, blockChunk, new Vector3f(i, x, j+r));
									new LeafBlock(loader, blockChunk, new Vector3f(i-r, x, j));
									new LeafBlock(loader, blockChunk, new Vector3f(i, x+1, j));
								}
							}
						} 
					}
					*/
				}
			}
		}
		
		
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

