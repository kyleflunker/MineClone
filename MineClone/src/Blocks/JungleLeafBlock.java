package Blocks;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class JungleLeafBlock extends GeneratedBlocks {
	
	private int jungleLeafTexX = 3;  //x position of jungle leaf texture on blockSheet.png		
	private int jungleLeafTexY = 3;  //y position of jungle leaf texture on blockSheet.png
	private static RawModel model = null;
	
	public JungleLeafBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);	
		
		if (model == null) {
			
			addSolidBlockVertAndInd(chunk, position, Vs, Is);  
			addSolidBlockUV(jungleLeafTexX, jungleLeafTexY, Us);  
			
		}

	}
}