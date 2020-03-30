package MineClone;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Blocks.DirtBlock;
import Blocks.GrassBlock;
import Blocks.StoneBlock;
import Entities.Camera;
import Blocks.Chunk;
import RenderEngine.Loader;
import Tools.Noise;

public class WorldGeneration {
	
	private static ArrayList<Chunk> generatedChunks = new ArrayList<Chunk>();
	private static ArrayList<Chunk> renderedChunks = new ArrayList<Chunk>();
	static Loader loader;
	
	public static void setLoader(Loader loader1) {
		loader = loader1;
	}
	
	public static void chunkController() {
		Vector3f positionVector = Camera.getPosition();
		int posX = (int) Math.floor(Camera.getPosition().x / 10) * 10;
		int posZ = (int) Math.floor(Camera.getPosition().z / 10) * 10;		
		int renderDistance = 20;
		
		if(Camera.isPlayerInNewChunk()) {
		   renderedChunks.clear();
		   for(int i = posX; i <= posX + renderDistance; i += 10) {
			   for(int j = posZ; j <= posZ + renderDistance; j += 10) {
				   boolean doesChunkExist = false;
				   Chunk renderChunk = null;
				   for(Chunk genChunks : generatedChunks) {
					   if(i == genChunks.getxStartCoord() && j == genChunks.getzStartCoord()) {
						   doesChunkExist = true;
						   renderChunk = genChunks;
					   }
				   }
			   
				   if(doesChunkExist) {					
					   renderedChunks.add(renderChunk);	
				   } else {
					   createNewChunk(i, j);
				   }
			   
			   }
		   }
		}
		   
	}	
	
	
	public static void createNewChunk(int xPos, int zPos) {
		System.out.println("Creating new chunk at-" + xPos + " " + zPos);
		
		Noise height = new Noise(100, 14, 20, 100);
		Chunk blockChunk = new Chunk(xPos, 0, zPos);
		generatedChunks.add(blockChunk);
		renderedChunks.add(blockChunk);	
		
		for(int i = xPos; i < xPos + 10; i++) {
			for(int j = zPos; j < zPos + 10; j++) {				
				float zVal = height.generateHeight(i, j);
				new GrassBlock(loader, blockChunk, new Vector3f(i, zVal , j));
				
				for(float k = zVal - 1; k > -14; k--) {
					   new StoneBlock(loader, blockChunk, new Vector3f(i, k , j));	
					}
				
			}
		}
		blockChunk.chooseRenderedBlocks();
		
	}
	
	
		
	

	public static ArrayList<Chunk> getGeneratedChunks() {
		return generatedChunks;
	}
	
	public static ArrayList<Chunk> getRenderedChunks() {
		return renderedChunks;
	}

	


}
