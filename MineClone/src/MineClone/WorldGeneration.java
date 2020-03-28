package MineClone;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Blocks.DirtBlock;
import Blocks.GrassBlock;
import Blocks.StoneBlock;
import Blocks.Chunk;
import RenderEngine.Loader;
import Tools.Noise;

public class WorldGeneration {
	
	private static ArrayList<Chunk> generatedChunks = new ArrayList<Chunk>();
		
	public static void generateWorld(Loader loader, int worldSize, int worldDepth) {
		
		Noise height = new Noise(worldSize, worldDepth, 20, 100);
		
		for(int i = 0; i < worldSize; i++) {
			for(int j = 0; j < worldSize; j++) {
				
				float zVal = height.generateHeight(i, j);
				
				boolean doesChunkExist = false;
				Chunk blockChunk = null;	
				
				for(Chunk genChunks : generatedChunks) {
					if((i >= genChunks.getxStartCoord() && i < genChunks.getxEndCoord()) && (j >= genChunks.getyStartCoord() && j < genChunks.getyEndCoord())) {
						doesChunkExist = true;
						blockChunk = genChunks;
					}
				}				
					
				if(!doesChunkExist) {					
					blockChunk = new Chunk(i, j, 0);
					generatedChunks.add(blockChunk);
					new StoneBlock(loader, blockChunk, new Vector3f(i, zVal , j));
				} else {
					new GrassBlock(loader, blockChunk, new Vector3f(i, zVal , j));
				}
					
				}
				        
				//new GrassBlock(loader, new Vector3f(i, zVal , j));
				
				//for(float k = zVal - 1; k > 0; k--) {
				   //new StoneBlock(loader, new Vector3f(i, k , j));	
				//}
						
					
				
			}
			
		}
		
		
	

	public static ArrayList<Chunk> getGeneratedChunks() {
		return generatedChunks;
	}

	


}
