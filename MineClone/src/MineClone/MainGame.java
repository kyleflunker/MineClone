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
	
	public static Loader loader1 = null;
	public static StaticShader shader1 = null;

	public static void main(String[] args) throws IOException {
		DisplayManager.createDisplay();	
				
		Loader loader = new Loader();
		loader1 = loader;
		StaticShader shader = new StaticShader();
		shader1 = shader;
		MasterRenderer renderer = new MasterRenderer(shader);
		
		
		Camera camera = new Camera(new Vector3f(5, 5, 5), 0 ,0, 0);
		

		long millis = System.currentTimeMillis();
		long frames = 0;


		while(!Display.isCloseRequested()) {
			
			camera.move();			
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			
			
			for (Chunk chunks : WorldGeneration.getRenderedChunks()) {
				for(Entity entity : chunks.getRenderedEntities()) {
					renderer.render(entity, shader);					
				}				
			}
			
			
			WorldGeneration.chunkController();
			
			shader.stop();
			DisplayManager.updateDisplay();
			
			frames++;
			if (System.currentTimeMillis() - millis > 1000) {
				System.out.printf("FPS: %f\n", frames / ((System.currentTimeMillis() - millis) / (double)1000));
				millis = System.currentTimeMillis();
				frames = 0;
			}
		}
		
		DisplayManager.closeDisplay();

	}

}
