package Blocks;

import org.lwjgl.util.vector.Vector3f;

import Models.RawModel;
import RenderEngine.Loader;
import java.util.List;

public class GrassBlock extends GeneratedBlocks {
	

	private static RawModel model1 = null;

	private static float[] topside_vert;
	private static int[] topside_idx;
	private static float[] topside_uv;

	public GrassBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);
			if (model1 == null) {
				topside_vert = new float[side_block_vertices.length + top_side_block_vertices.length + bottom_side_block_vertices.length];
				topside_idx = new int[side_block_indices.length + single_side_block_indices.length + single_side_block_indices.length];
				topside_uv = new float[side_block_uv.length + single_side_block_uv_grass_top.length + single_side_block_uv_grass_bot.length];

				System.arraycopy(side_block_vertices, 0, topside_vert, 0, side_block_vertices.length);
				System.arraycopy(side_block_indices, 0, topside_idx, 0, side_block_indices.length);
				System.arraycopy(side_block_uv, 0, topside_uv, 0, side_block_uv.length);

				System.arraycopy(top_side_block_vertices, 0, topside_vert, side_block_vertices.length, top_side_block_vertices.length);
				System.arraycopy(single_side_block_indices, 0, topside_idx, side_block_indices.length, single_side_block_indices.length);
				System.arraycopy(single_side_block_uv_grass_top, 0, topside_uv, side_block_uv.length, single_side_block_uv_grass_top.length);

				System.arraycopy(bottom_side_block_vertices, 0, topside_vert, side_block_vertices.length+top_side_block_vertices.length, bottom_side_block_vertices.length);
				System.arraycopy(single_side_block_indices, 0, topside_idx, side_block_indices.length+single_side_block_indices.length, single_side_block_indices.length);
				System.arraycopy(single_side_block_uv_grass_bot, 0, topside_uv, side_block_uv.length+single_side_block_uv_grass_top.length, single_side_block_uv_grass_bot.length);

				for (int i = side_block_indices.length; i < single_side_block_indices.length + side_block_indices.length; ++i) {
					topside_idx[i] += side_block_vertices.length / 3;
				}

				for (int i = single_side_block_indices.length + side_block_indices.length; i < side_block_indices.length + single_side_block_indices.length + single_side_block_indices.length; ++i) {
					topside_idx[i] += (side_block_vertices.length + top_side_block_vertices.length) / 3;
				}

				for (int f : topside_idx) Is.add(Vs.size()/3 + f);
				int asd = 0;
				for (float f : topside_vert) {
					float offs = 0;
					if (asd == 0) offs = position.x - chunk.position.x;
					if (asd == 1) offs = position.y - chunk.position.y;
					if (asd == 2) offs = position.z - chunk.position.z;
					Vs.add(f + offs);
					asd = (asd + 1) % 3;
				}
				for (float f : topside_uv) Us.add(f);
			}
			
		}	
		
}


