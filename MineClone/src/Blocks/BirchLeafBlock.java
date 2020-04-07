package Blocks;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class BirchLeafBlock extends GeneratedBlocks {
	
	private int birchLeafTexX = 4;  //x position of birch leaf texture on blockSheet.png		
	private int birchLeafTexY = 3;  //y position of birch leaf texture on blockSheet.png
	private static RawModel model = null;
	
	public BirchLeafBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);	
		
		if (model == null) {
			
			addSolidBlockVertAndInd(chunk, position, Vs, Is);  
			addSolidBlockUV(birchLeafTexX, birchLeafTexY, Us);  
			
		}

	}
}