package BlockObjects;

import org.lwjgl.util.vector.Vector3f;
import Blocks.*;
import MainGame.WorldGeneration;

public class CactiSpawner {
	//2d block array positions are relative to the spawn position that are passed in the spawn method
	int[][] cactiBlocks = new int[][] {
		{0, 1, 0}, {0, 2, 0}, {0, 3, 0}
	};
	
	public void spawnCacti(int spawnPosX, float spawnPosY, int spawnPosZ) {
		//positions for x,y,z values in block arrays
		int x = 0;
		int y = 1;
		int z = 2;
		
		for(int[] cacti : cactiBlocks) {
			checkForCorrectChunk(new Vector3f(spawnPosX + cacti[x], spawnPosY + cacti[y], spawnPosZ + cacti[z]), 10);
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