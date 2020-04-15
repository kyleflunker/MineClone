package MineClone;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
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
import Blocks.TreeSpawner;
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
		
		System.out.println("Seed: " + MainGame.getSeed());
		Noise height = new Noise(100, 14, 20, MainGame.getSeed());
		Chunk blockChunk = new Chunk(xPos, yPos, zPos);
		
		//since a new chunk is being created, we must re-render its adjacent chunks (if they exist)
		setAdjacentChunksNeedRender(blockChunk);
		
		generatedChunks.put(blockChunk.getChunkID(), blockChunk);
		renderedChunks.add(blockChunk);	
		TreeSpawner x =  new TreeSpawner();
		for(int i = xPos; i < xPos + 10; i++) {
			for(int j = zPos; j < zPos + 10; j++) {				
					float zVal = height.generateHeight(i, j);		
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
						x.TreeSpawnerMethod(blockChunk, i, k, j);
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

