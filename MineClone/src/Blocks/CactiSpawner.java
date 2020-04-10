package Blocks;

import MineClone.WorldGeneration;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

import Models.*; import Textures.*; import Entities.*; import Blocks.*;

public class CactiSpawner {
	
	
	public CactiSpawner(Chunk blockChunk, int i, float k, int j, float zVal) {
		
		int x = 1;
		while(x <= 3) {
			
		blockChunk.addToChunkBlocks(new Block(new Vector3f(i, k+x, j), 10));
		x++;
		}
	}
}