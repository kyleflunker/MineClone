package Blocks;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class StoneBlock extends GeneratedBlocks {
	
	private String textureString = "stoneTex";
	
	public StoneBlock(Loader loader, Vector3f position) {
		super(loader, position);
		RawModel model = loader.loadToVAO(GeneratedBlocks.block_vertices, GeneratedBlocks.block_indices, GeneratedBlocks.block_uv);
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureString));
		TexturedModel texModel = new TexturedModel(model, texture);
		generated_blocks.add(new Entity(texModel, position, 0, 0, 0, 1));
	}


;
	


}
