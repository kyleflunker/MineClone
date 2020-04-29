package BlockObjects;

import org.lwjgl.util.vector.Vector3f;
import Blocks.*;
import MainGame.WorldGeneration;


public class OakTreeSpawner {
	//2d block array positions are relative to the spawn positions that are passed in the spawn method
	int[][] leafBlocks = new int[][] {
		{1, 4, 1}, {1, 4, -1}, {1, 4, 0}, {-1, 4, -1}, {-1, 4, 1}, {-1, 4, 0}, {0, 4, 1}, {0, 4, -1},
		{2, 4, 2}, {2, 4, -2}, {2, 4, 0}, {-2, 4, -2}, {-2, 4, 2}, {-2, 4, 0}, {0, 4, 2}, {0, 4, -2},
		{2, 4, 1}, {2, 4, -1}, {-2, 4, 1}, {-2, 4, -1}, {1, 4, -2}, {-1, 4, -2}, {-1, 4, 2}, {1, 4, 2}, {1, 4, -2},
		{1, 5, 1}, {1, 5, -1}, {1, 5, 0}, {-1, 5, -1}, {-1, 5, 1}, {-1, 5, 0}, {0, 5, 1}, {0, 5, -1},
		{2, 5, 2}, {2, 5, -2}, {2, 5, 0}, {-2, 5, -2}, {-2, 5, 2}, {-2, 5, 0}, {0, 5, 2}, {0, 5, -2},
		{2, 5, 1}, {2, 5, -1}, {-2, 5, 1}, {-2, 5, -1}, {1, 5, -2}, {-1, 5, -2}, {-1, 5, 2}, {1, 5, 2}, {1, 5, -2},
		{1, 6, 1}, {1, 6, -1}, {1, 6, 0}, {-1, 6, -1}, {-1, 6, 1}, {-1, 6, 0}, {0, 6, 1}, {0, 6, -1},
		{0, 7, 0}
	};
	int[][] oakTreeBlocks = new int[][] {
		{0, 1, 0}, {0, 2, 0}, {0, 3, 0}, {0, 4, 0}, {0, 5, 0}, {0, 6, 0}
	};
	
	public void spawnOakTree(int spawnPosX, float spawnPosY, int spawnPosZ) {
		//positions for x,y,z values in block arrays
		int x = 0; 
		int y = 1;
		int z = 2;
		
		for(int[] leaf : leafBlocks) {
			checkForCorrectChunk(new Vector3f(spawnPosX + leaf[x], spawnPosY + leaf[y], spawnPosZ + leaf[z]), 5);
		}
		for(int[] oakTree : oakTreeBlocks) {
			checkForCorrectChunk(new Vector3f(spawnPosX + oakTree[x], spawnPosY + oakTree[y], spawnPosZ + oakTree[z]), 4);
		}
		
	}
	
	//decides if a new chunk needs to be created or not (based on Vector3f argument)
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
	
	//add the block to the correct chunk (passed in from checkForCorrectChunk)
	public void addBlockToCorrectChunk(Vector3f position, int type, Chunk chunk) {
		chunk.addToChunkBlocks(new Block(position, type));
		chunk.needsRender = true;
	}
	
}


