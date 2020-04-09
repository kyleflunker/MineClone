package Blocks;

import org.lwjgl.util.vector.Vector3f;


import Models.RawModel;
import RenderEngine.Loader;

import java.util.List;

public class StoneBlock extends GeneratedBlocks {
	
	private int stoneTexX = 4;  //x position of stone texture on blockSheet.png	
	private int stoneTexY = 1;  //y position of stone texture on blockSheet.png
	private static RawModel model = null;
	
	public StoneBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);

		if (model == null) {
			
			addSolidBlockVertAndInd(chunk, position, Vs, Is);  
			addSolidBlockUV(stoneTexX, stoneTexY, Us);  
			
		}
	}
}
