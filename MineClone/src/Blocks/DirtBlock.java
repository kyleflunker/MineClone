package Blocks;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class DirtBlock extends GeneratedBlocks {
	
	private String textureString = "dirtTex";
	

	public DirtBlock(Loader loader, Vector3f position) {
		super(loader, position);
			RawModel model = loader.loadToVAO(all_sides_block_vertices, all_sides_block_indices, all_sides_block_uv);
			ModelTexture texture = new ModelTexture(loader.loadTexture(textureString));
			TexturedModel texModel = new TexturedModel(model, texture);
			generated_blocks.add(new Entity(texModel, position, 0, 0, 0, 1));
		}
		
}


