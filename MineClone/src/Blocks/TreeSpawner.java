package Blocks;

import Blocks.Chunk;
import MineClone.WorldGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import Blocks.OakTreeBlock;
import Blocks.OakLeafBlock; 
import Models.*; import Textures.*; import Entities.*; import Blocks.*;


public class TreeSpawner {	
	
	public void TreeSpawnerMethod(Chunk blockChunk, int i, float k, int j) {
		
		int x = 1;
		while(x <= 7) {
			
			checkForCorrectChunk(new Vector3f(i, k+x, j), 4);
			
		if(x == 4 || x == 5) {
			for(int r=1; r<=2;) {	
				checkForCorrectChunk(new Vector3f(i+r, k+x, j-r), 5);
				checkForCorrectChunk(new Vector3f(i-r, k+x, j-r), 5);
				checkForCorrectChunk(new Vector3f(i+r, k+x, j+r), 5);
				checkForCorrectChunk(new Vector3f(i-r, k+x, j+r), 5);
				checkForCorrectChunk(new Vector3f(i+r, k+x, j), 5);
				checkForCorrectChunk(new Vector3f(i-r, k+x, j), 5);
				checkForCorrectChunk(new Vector3f(i, k+x, j-r), 5);
				checkForCorrectChunk(new Vector3f(i, k+x, j+r), 5);
			r++;
			if(r == 2) {
				checkForCorrectChunk(new Vector3f(i+2, k+x, j-1), 5);
				checkForCorrectChunk(new Vector3f(i-2, k+x, j-1), 5);
				checkForCorrectChunk(new Vector3f(i+2, k+x, j+1), 5);
				checkForCorrectChunk(new Vector3f(i-1, k+x, j+2), 5);
				checkForCorrectChunk(new Vector3f(i+1, k+x, j+2), 5);
				checkForCorrectChunk(new Vector3f(i-1, k+x, j-2), 5);
				checkForCorrectChunk(new Vector3f(i-1, k+x, j+1), 5);
				checkForCorrectChunk(new Vector3f(i+1, k+x, j-1), 5);
				checkForCorrectChunk(new Vector3f(i+1, k+x, j-2), 5);
				checkForCorrectChunk(new Vector3f(i-2, k+x, j+1), 5);
				
			}
			}
		
		}
		if(x == 6) {
			for(int r=1; r<= 1; r++) {
				checkForCorrectChunk(new Vector3f(i+r, k+x, j-r), 5);
				checkForCorrectChunk(new Vector3f(i-r, k+x, j-r), 5);
				checkForCorrectChunk(new Vector3f(i+r, k+x, j+r), 5);
				checkForCorrectChunk(new Vector3f(i-r, k+x, j+r), 5);
				checkForCorrectChunk(new Vector3f(i+r, k+x, j), 5);
				checkForCorrectChunk(new Vector3f(i-r, k+x, j), 5);
				checkForCorrectChunk(new Vector3f(i, k+x, j-r), 5);
				checkForCorrectChunk(new Vector3f(i, k+x, j+r), 5);
			}
		}
		if(x == 7) {
			checkForCorrectChunk(new Vector3f(i, k+x, j), 5);
		}
		x++;
		}
		
		
		
	}
	
	public void checkForCorrectChunk(Vector3f position, int blockType) {
		int chunkPosX = (int) Math.floor(position.x / 10) * 10;
		int chunkPosZ = (int) Math.floor(position.z / 10) * 10;
		int chunkPosY = (int) Math.floor(position.y / 10) * 10;
		if(WorldGeneration.getGeneratedChunks().containsKey(WorldGeneration.createChunkID(chunkPosX, chunkPosY, chunkPosZ))) {
			Chunk chunk = WorldGeneration.getGeneratedChunks().get(WorldGeneration.createChunkID(chunkPosX, chunkPosY, chunkPosZ));
			addBlockToCorrectChunk(position, blockType, chunk);     
		   } else {
			   WorldGeneration.createNewChunk(chunkPosX, chunkPosY, chunkPosZ);
			   Chunk chunk = WorldGeneration.getGeneratedChunks().get(WorldGeneration.createChunkID(chunkPosX, chunkPosY, chunkPosZ));
			   addBlockToCorrectChunk(position, blockType, chunk);  
		   }	
	}
	
	public void addBlockToCorrectChunk(Vector3f position, int type, Chunk chunk) {
		chunk.addToChunkBlocks(new Block(position, type));
	}
	
}


