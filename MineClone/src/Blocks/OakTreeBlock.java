package Blocks;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class OakTreeBlock extends GeneratedBlocks {
	
	private int oakTreeSideTexX = 4;  //x position of oak tree side texture on blockSheet.png		
	private int oakTreeSideTexY = 2;  //y position of oak tree side texture on blockSheet.png
	private int oakTreeTopTexX = 3;  //x position of oak tree top texture on blockSheet.png		
	private int oakTreeTopTexY = 2;  //y position of oak tree top texture on blockSheet.png
	private static RawModel model = null;
	
	public OakTreeBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);
		
		if (model == null) {
			
			addNonSolidBlockVertAndInd(chunk, position, Vs, Is);
			addNonSolidBlockUV(oakTreeTopTexX, oakTreeTopTexY, oakTreeSideTexX, oakTreeSideTexY, oakTreeTopTexX, oakTreeTopTexY, Us);
			
		}	
	}
}