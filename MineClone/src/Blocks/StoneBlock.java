package Blocks;

import org.lwjgl.util.vector.Vector3f;


import Models.RawModel;
import RenderEngine.Loader;
import java.util.List;

public class StoneBlock extends GeneratedBlocks {
	private static RawModel model = null;
	
	public StoneBlock(Loader loader, Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is, List<Float> Us) {
		super(loader, chunk, position);

		if (model == null) {
			for (int f : all_sides_block_indices) Is.add(Vs.size()/3 + f);
			int asd = 0;
			for (float f : all_sides_block_vertices) {
					float offs = 0;
					if (asd == 0) offs = position.x - chunk.position.x;
					if (asd == 1) offs = position.y - chunk.position.y;
					if (asd == 2) offs = position.z - chunk.position.z;
					Vs.add(f + offs);
					asd = (asd + 1) % 3;
				}
			for (float f : all_sides_block_uv_stone) Us.add(f);

		}
	}
}
