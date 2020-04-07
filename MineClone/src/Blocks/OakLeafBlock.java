package Blocks;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class OakLeafBlock extends GeneratedBlocks {
	
	private int oakLeafTexX = 3;  //x position of oak leaf texture on blockSheet.png		
	private int oakLeafTexY = 4;  //y position of oak leaf texture on blockSheet.png
	private static RawModel model = null;
	
	public OakLeafBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);	
		
		if (model == null) {
			
			addSolidBlockVertAndInd(chunk, position, Vs, Is);  
			addSolidBlockUV(oakLeafTexX, oakLeafTexY, Us);  
			
		}

	}
}