package Blocks;

import org.lwjgl.util.vector.Vector3f;

import Models.RawModel;
import RenderEngine.Loader;
import java.util.List;

public class GrassBlock extends GeneratedBlocks {
	
	private static int grassSideTexX = 1;   //x position of grass side texture on blockSheet.png			
	private static int grassSideTexY = 1;   //y position of grass side texture on blockSheet.png	
	private static int grassTopTexX = 3;    //x position of grass top texture on blockSheet.png		
	private static int grassTopTexY = 1;    //y position of grass top texture on blockSheet.png	
	private static int grassBottomTexX = 2; //x position of grass bottom texture on blockSheet.png			
	private static int grassBottomTexY = 1; //y position of grass bottom texture on blockSheet.png	
	private static RawModel model = null;
	

	public GrassBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);
			if (model == null) {
				
				addNonSolidBlockVertAndInd(chunk, position, Vs, Is);
				addNonSolidBlockUV(grassTopTexX, grassTopTexY, grassSideTexX, grassSideTexY, grassBottomTexX, grassBottomTexY, Us);
				
			}
		}	
}


