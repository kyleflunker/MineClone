package Blocks;

import org.lwjgl.util.vector.Vector3f;


import Models.RawModel;
import RenderEngine.Loader;

import java.util.List;

public class SandBlock extends GeneratedBlocks {
	
	private int sandTexX = 1;  //x position of sand texture on blockSheet.png	
	private int sandTexY = 4;  //y position of sand texture on blockSheet.png
	private static RawModel model = null;
	
	public SandBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);

		if (model == null) {
			
			addSolidBlockVertAndInd(chunk, position, Vs, Is);  
			addSolidBlockUV(sandTexX, sandTexY, Us);  
			
		}
	}
}
