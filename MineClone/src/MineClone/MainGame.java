package MineClone;

import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Blocks.Chunk;
import Blocks.GeneratedBlocks;
import Entities.Camera;
import Entities.Entity;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import Shaders.StaticShader;
import Tools.Noise;

public class MainGame {
	//test comment
	public static Loader loader1 = null;
	public static StaticShader shader1 = null;

	public static void main(String[] args) throws IOException {
		DisplayManager.createDisplay();	
				
		Loader loader = new Loader();
		loader1 = loader;
		StaticShader shader = new StaticShader();
		shader1 = shader;
		MasterRenderer renderer = new MasterRenderer(shader);
		
		
		WorldGeneration.setLoader(loader);
		Camera camera = new Camera(new Vector3f(0, 0, 0), 0 ,0, 0);
		
		while(!Display.isCloseRequested()) {
			
			camera.move();			
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			
			
			for (Chunk chunks : WorldGeneration.getRenderedChunks()) {					
				for(Entity entity : chunks.getRendered_blocks()) {
					renderer.render(entity, shader);					
				}				
			}
			
			
			WorldGeneration.chunkController();
			
			shader.stop();
			DisplayManager.updateDisplay();
			
		}
		
		DisplayManager.closeDisplay();

	}

}
