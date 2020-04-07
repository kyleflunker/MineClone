package Blocks;

import org.lwjgl.util.vector.Vector3f;


import Models.RawModel;
import RenderEngine.Loader;

import java.util.List;

public class CactusBlock extends GeneratedBlocks {
	
	private int cactusTexX = 2;  //x position of cactus texture on blockSheet.png	
	private int cactusTexY = 4;  //y position of cactus texture on blockSheet.png
	private static RawModel model = null;
	
	public CactusBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);

		if (model == null) {
			
			addSolidBlockVertAndInd(chunk, position, Vs, Is);  
			addSolidBlockUV(cactusTexX, cactusTexY, Us);  
			
		}
	}
}
