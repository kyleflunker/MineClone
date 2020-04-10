package Blocks;

import Blocks.Chunk;
import MineClone.WorldGeneration;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import Blocks.OakTreeBlock;
import Blocks.OakLeafBlock;
import Models.*; import Textures.*; import Entities.*; import Blocks.*;


public class TreeSpawner {
	
	
	
	
	public TreeSpawner(Chunk blockChunk, int i, float k, int j, float zVal) {
		float height = zVal+7;
		int x = 1;
		while(x <= 7) {
			
		blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j), 4));
		if(x == 4 || x == 5) {
			for(int r=1; r<=2;) {
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i+r, k+x, j-r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i-r, k+x, j-r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i+r, k+x, j+r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i-r, k+x, j+r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i+r, k+x, j), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i-r, k+x, j), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j-r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j+r), 5));
			r++;
			if(r == 2) {
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i+(2*1/r), k+x, j-(2*1/r)), 5));
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i-(2*1/r), k+x, j-(2*1/r)), 5));
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i+(2*1/r), k+x, j+(2*1/r)), 5));
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i-(2*1/r), k+x, j+(2*1/r)), 5));
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i+(2*1/r), k+x, j), 5));
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i-(2*1/r), k+x, j), 5));
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j-(2*1/r)), 5));
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j+(2*1/r)), 5));
			}
			}
		
		}
		if(x == 6) {
			for(int r=1; r<= 1; r++) {
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i+r, k+x, j-r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i-r, k+x, j-r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i+r, k+x, j+r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i-r, k+x, j+r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i+r, k+x, j), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i-r, k+x, j), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j-r), 5));
			blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j+r), 5));
			}
		}
		if(x == 7) {
				blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j), 5));
		}
		x++;
		}
		
	}
}


