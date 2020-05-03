package BlockObjects;


import org.lwjgl.util.vector.Vector3f;
import Blocks.*;
import MainGame.WorldGeneration;

public class JungleTreeSpawner{
	
	int[][] jungleLeafBlocks = new int[][] {
		/*{1, 7, 1}, {1, 7, -1}, {1, 7, 0}, {-1, 7, -1}, {-1, 7, 1}, {-1, 7, 0}, {0, 7, 1}, {0, 7, -1},
		{2, 7, 2}, {2, 7, -2}, {2, 7, 0}, {-2, 7, -2}, {-2, 7, 2}, {-2, 7, 0}, {0, 7, 2}, {0, 7, -2},
		{2, 7, 1}, {2, 7, -1}, {-2, 7, 1}, {-2, 7, -1}, {1, 7, -2}, {-1, 7, -2}, {-1, 7, 2}, {1, 7, 2}, {1, 7, -2},
		{3, 7, 1}, {3, 7, -1}, {3, 7, 0}, {-1, 7, -3}, {-1, 7, 3}, {-1, 7, 3}, {0, 7, 1}, {0, 7, -1},
		{1, 8, 1}, {1, 8, -1}, {1, 8, 0}, {-1, 8, -1}, {-1, 8, 1}, {-1, 8, 0}, {0, 8, 1}, {0, 8, -1},
		{2, 8, 2}, {2, 8, -2}, {2, 8, 0}, {-2, 8, -2}, {-2, 8, 2}, {-2, 8, 0}, {0, 8, 2}, {0, 8, -2},
		{2, 8, 1}, {2, 8, -1}, {-2, 8, 1}, {-2, 8, -1}, {1, 8, -2}, {-1, 8, -2}, {-1, 8, 2}, {1, 8, 2}, {1, 8, -2},*/
		{1, 9, 1}, {1, 9, -1}, {1, 9, 0}, {-1, 9, -1}, {-1, 9, 1}, {-1, 9, 0}, {0, 9, 1}, {0, 9, -1},
		{2, 9, 2}, {2, 9, -2}, {2, 9, 0}, {-2, 9, -2}, {-2, 9, 2}, {-2, 9, 0}, {0, 9, 2}, {0, 9, -2},
		{2, 9, 1}, {2, 9, -1}, {-2, 9, 1}, {-2, 9, -1}, {1, 9, -2}, {-1, 9, -2}, {-1, 9, 2}, {1, 9, 2}, {1, 9, -2},
		{1, 10, 1}, {1, 10, -1}, {1, 10, 0}, {-1, 10, -1}, {-1, 10, 1}, {-1, 10, 0}, {0, 10, 1}, {0, 10, -1},
		{2, 10, 2}, {2, 10, -2}, {2, 10, 0}, {-2, 10, -2}, {-2, 10, 2}, {-2, 10, 0}, {0, 10, 2}, {0, 10, -2},
		{2, 10, 1}, {2, 10, -1}, {-2, 10, 1}, {-2, 10, -1}, {1, 10, -2}, {-1, 10, -2}, {-1, 10, 2}, {1, 10, 2}, {1, 10, -2},
	};
	int[][] jungleTreeBlocks = new int[][] {
		{0, 1, 0}, {0, 2, 0}, {0, 3, 0}, {0, 4, 0}, {0, 5, 0}, {0, 6, 0}, {0, 7, 0}, {0, 8, 0}, {0, 9, 0}, {0, 10, 0}
	};
	
	public void spawnJungleTree(int spawnPosX, float spawnPosY, int spawnPosZ) {
		//positions for x,y,z values in block arrays
		int x = 0; 
		int y = 1;
		int z = 2;
		
		for(int[] leaf : jungleLeafBlocks) {
			checkForCorrectChunk(new Vector3f(spawnPosX + leaf[x], spawnPosY + leaf[y], spawnPosZ + leaf[z]), 9);
		}
		for(int[] oakTree : jungleTreeBlocks) {
			checkForCorrectChunk(new Vector3f(spawnPosX + oakTree[x], spawnPosY + oakTree[y], spawnPosZ + oakTree[z]), 8);
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
	
	//add the block to the correct chunk (passed in from checkForCorrectChunk)
	public void addBlockToCorrectChunk(Vector3f position, int type, Chunk chunk) {
		chunk.addToChunkBlocks(new Block(position, type));
		chunk.needsRender = true;
	}
	
}
	