package MineClone;

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
import Blocks.TreeBlock;
import Blocks.LeafBlock;
import java.util.Random;

public class WorldGeneration {	
	
	static HashMap<String, Chunk> generatedChunks = new HashMap<String, Chunk>();
	private static ArrayList<Chunk> renderedChunks = new ArrayList<Chunk>();
	static Loader loader;
	
	public static void setLoader(Loader loader1) {
		loader = loader1;
	}
	
	private static int initialGeneration = 10;
	
	public static void chunkController() {
		Vector3f positionVector = Camera.getPosition();
		int posX = (int) Math.floor(Camera.getPosition().x / 10) * 10;
		int posZ = (int) Math.floor(Camera.getPosition().z / 10) * 10;
		int posY = (int) Math.floor(Camera.getPosition().y / 10) * 10;
		
		int renderDistance = 10;
		
		if(Camera.isPlayerInNewChunk()) {
		   renderedChunks.clear();
		   for(int i = posX - renderDistance - initialGeneration; i <= posX + renderDistance + initialGeneration; i += 10) {
			   for(int j = posZ - renderDistance - initialGeneration; j <= posZ + renderDistance + initialGeneration; j += 10) {
				   for(int k = posY - renderDistance - initialGeneration; k <= posY + renderDistance + initialGeneration; k += 10) {
					   
					   if(generatedChunks.containsKey(createChunkID(i, k, j))) {
						   renderedChunks.add(generatedChunks.get(createChunkID(i, k, j)));  
					   } else {
						   createNewChunk(i, k, j); 
					   }				  
				   
				   }			   
			   }
		   }
		}
		
		initialGeneration = 0;
		   
	}	
	
	
	public static void createNewChunk(int xPos, int yPos, int zPos) {
		System.out.println("Creating new chunk at: " + xPos + " " + yPos + " " + zPos);
		
		int biomespawnermax = 70;
		int biomespawnermin = 1;
		Random num = new Random();
		int biomecheck = num.nextInt((biomespawnermax - biomespawnermin) + 1) + biomespawnermin;
		
		if(biomecheck >= 60) {
			System.out.println("Forest Spawned");
		}
		
		int treecheckmax = 30;
		int treecheckmin = 1;
		Random num2 = new Random();
		int treecheck = num2.nextInt((treecheckmax - treecheckmin) + 1) + treecheckmin;
		
		
		Noise height = new Noise(100, 14, 20, 100);
		Chunk blockChunk = new Chunk(xPos, yPos, zPos);
		generatedChunks.put(blockChunk.getChunkID(), blockChunk);
		renderedChunks.add(blockChunk);	
		
		for(int i = xPos; i < xPos + 10; i++) {
			for(int j = zPos; j < zPos + 10; j++) {				
				for(float k = yPos; k < yPos + 10; k++) {				
					float zVal = height.generateHeight(i, j);
					   
					if(k == zVal) {
					   new GrassBlock(loader, blockChunk, new Vector3f(i, k , j));	
					} else if (k < zVal) {
					   new StoneBlock(loader, blockChunk, new Vector3f(i, k , j));
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
		blockChunk.chooseRenderedBlocks();
		
	}
	
	
	public static String createChunkID(int xCoord, int yCoord, int zCoord) {		
		return (xCoord + "-" + yCoord + "-" + zCoord);
	}

	public static HashMap<String, Chunk> getGeneratedChunks() {
		return generatedChunks;
	}
	
	public static ArrayList<Chunk> getRenderedChunks() {
		return renderedChunks;
	}

	 
}

