package Blocks;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class BirchTreeBlock extends GeneratedBlocks {
	
	private int birchTreeSideTexX = 2;  //x position of birch tree side texture on blockSheet.png		
	private int birchTreeSideTexY = 2;  //y position of birch tree side texture on blockSheet.png
	private int birchTreeTopTexX = 1;  //x position of birch tree top texture on blockSheet.png		
	private int birchTreeTopTexY = 2;  //y position of birch tree top texture on blockSheet.png
	private static RawModel model = null;
	
	public BirchTreeBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);
		
		if (model == null) {
			
			addNonSolidBlockVertAndInd(chunk, position, Vs, Is);
			addNonSolidBlockUV(birchTreeTopTexX, birchTreeTopTexY, birchTreeSideTexX, birchTreeSideTexY, birchTreeTopTexX, birchTreeTopTexY, Us);
			
		}	
	}
}