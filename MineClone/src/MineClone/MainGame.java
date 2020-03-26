package MineClone;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

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

	public static void main(String[] args) {
		DisplayManager.createDisplay();	
				
		Loader loader = new Loader();
		loader1 = loader;
		StaticShader shader = new StaticShader();
		shader1 = shader;
		MasterRenderer renderer = new MasterRenderer(shader);
		
		
		WorldGeneration.generateWorld(loader, 50, 5);
		Camera camera = new Camera(new Vector3f(0, 0, 0), 0 ,0, 0);
		
		while(!Display.isCloseRequested()) {
			
			camera.move();			
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			
			for (Entity entity : GeneratedBlocks.generated_blocks) {
				renderer.render(entity, shader);
			}
			
			shader.stop();
			DisplayManager.updateDisplay();
			
		}
		
		DisplayManager.closeDisplay();

	}

}
