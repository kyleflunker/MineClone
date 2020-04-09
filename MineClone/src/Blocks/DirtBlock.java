package Blocks;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class DirtBlock extends GeneratedBlocks {
	
	private int dirtTexX = 2;  //x position of dirt texture on blockSheet.png	
	private int dirtTexY = 1;  //y position of dirt texture on blockSheet.png
	private static RawModel model = null;
	

	public DirtBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);
		
		if (model == null) {
			
			addSolidBlockVertAndInd(chunk, position, Vs, Is);  
			addSolidBlockUV(dirtTexX, dirtTexY, Us);  
			
		}
	}
		
}


