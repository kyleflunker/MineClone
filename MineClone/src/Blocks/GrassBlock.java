package Blocks;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class GrassBlock extends GeneratedBlocks {
	
	private String textureString1 = "grassSide";
	private String textureString2 = "grassTop";
	private String textureString3 = "dirtTex";
	


	public GrassBlock(Loader loader, Vector3f position) {
		super(loader, position);
			RawModel model1 = loader.loadToVAO(side_block_vertices, side_block_indices, side_block_uv);
			ModelTexture texture1 = new ModelTexture(loader.loadTexture(textureString1));
			TexturedModel texModel1 = new TexturedModel(model1, texture1);
			generated_blocks.add(new Entity(texModel1, position, 0, 0, 0, 1));
			RawModel model2 = loader.loadToVAO(top_side_block_vertices, single_side_block_indices, single_side_block_uv);
			ModelTexture texture2 = new ModelTexture(loader.loadTexture(textureString2));
			TexturedModel texModel2 = new TexturedModel(model2, texture2);
			generated_blocks.add(new Entity(texModel2, position, 0, 0, 0, 1));
			RawModel model3 = loader.loadToVAO(bottom_side_block_vertices, single_side_block_indices, single_side_block_uv);
			ModelTexture texture3 = new ModelTexture(loader.loadTexture(textureString3));
			TexturedModel texModel3 = new TexturedModel(model3, texture3);
			generated_blocks.add(new Entity(texModel3, position, 0, 0, 0, 1));
		}
		
}


