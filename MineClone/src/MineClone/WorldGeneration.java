package MineClone;

import org.lwjgl.util.vector.Vector3f;

import Blocks.DirtBlock;
import Blocks.GrassBlock;
import Blocks.StoneBlock;
import RenderEngine.Loader;
import Tools.Noise;

public class WorldGeneration {
		
	public static void generateWorld(Loader loader, int worldSize, int worldDepth) {
		
		Noise height = new Noise(worldSize, worldDepth, 20, 100);
		
		for(int i = 0; i < worldSize; i++) {
			for(int j = 0; j < worldSize; j++) {
				
				float zVal = height.generateHeight(i, j);
				        
				new GrassBlock(loader, new Vector3f(i, zVal , j));
				
				//for(float k = zVal - 1; k > 0; k--) {
				   //new StoneBlock(loader, new Vector3f(i, k , j));	
				//}
						
					
				
			}
			
		}
	}
	


}
