package Blocks;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class JungleTreeBlock extends GeneratedBlocks {
	
	private int jungleTreeSideTexX = 2;  //x position of jungle tree side texture on blockSheet.png		
	private int jungleTreeSideTexY = 3;  //y position of jungle tree side texture on blockSheet.png
	private int jungleTreeTopTexX = 1;  //x position of jungle tree top texture on blockSheet.png		
	private int jungleTreeTopTexY = 3;  //y position of jungle tree top texture on blockSheet.png
	private static RawModel model = null;
	
	public JungleTreeBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);
		
		if (model == null) {
			
			addNonSolidBlockVertAndInd(chunk, position, Vs, Is);
			addNonSolidBlockUV(jungleTreeTopTexX, jungleTreeTopTexY, jungleTreeSideTexX, jungleTreeSideTexY, jungleTreeTopTexX, jungleTreeTopTexY, Us);
			
		}	
	}
}