package Blocks;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class TreeBlock extends GeneratedBlocks {
	
	private String treetexture1 = "TreeBlockSide";
	private String treetexture2 = "TreeBlockTop";
	private String treetexture3 = "TreeBlockTop";
	
	public TreeBlock(Loader loader, Chunk chunk, Vector3f position) {
		super(loader, chunk, position);
		
		RawModel model = loader.loadToVAO(side_block_vertices, side_block_indices, side_block_uv);
		ModelTexture texture = new ModelTexture(loader.loadTexture(treetexture1));
		TexturedModel texModel = new TexturedModel(model, texture);
		chunk.addTo_chunk_blocks(new Entity(texModel, position, 0, 0, 0, 1, sideFaces));
		
		RawModel model2 = loader.loadToVAO(top_side_block_vertices, single_side_block_indices, single_side_block_uv);
		ModelTexture texture2 = new ModelTexture(loader.loadTexture(treetexture2));
		TexturedModel texModel2 = new TexturedModel(model2, texture2);
		chunk.addTo_chunk_blocks(new Entity(texModel2, position, 0, 0, 0, 1, topFace));
		
		RawModel model3 = loader.loadToVAO(bottom_side_block_vertices, single_side_block_indices, single_side_block_uv);
		ModelTexture texture3 = new ModelTexture(loader.loadTexture(treetexture3));
		TexturedModel texModel3 = new TexturedModel(model3, texture3);
		chunk.addTo_chunk_blocks(new Entity(texModel3, position, 0, 0, 0, 1, bottomFace));
		
		
	}
	
	 
	
	
	
	
	
}