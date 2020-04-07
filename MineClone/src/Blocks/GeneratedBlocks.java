package Blocks;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import RenderEngine.Loader;
import SpriteLoader.SpriteSheetLoader;

public class GeneratedBlocks {	
	
	
	
	public static float[] all_sides_block_vertices = {
			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,		
			
			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,0.5f,
			-0.5f,0.5f,-0.5f,
			0.5f,0.5f,-0.5f,
			0.5f,0.5f,0.5f,
			
			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
	};
	
	public static int[] all_sides_block_indices = {
			0,1,3,	
			3,1,2,	
			4,5,7,
			7,5,6,
			8,9,11,
			11,9,10,
			12,13,15,
			15,13,14,	
			16,17,19,
			19,17,18,
			20,21,23,
			23,21,22
	};
		
	
public static float[] side_block_vertices = {			
				
			
			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,	
			
			
	};
	
	public static int[] side_block_indices = {
			0,1,3,	
			3,1,2,	
			4,5,7,
			7,5,6,
			8,9,11,
			11,9,10,
			12,13,15,
			15,13,14,	
			
	};
	
	
	public static float[] top_side_block_vertices = {
		-0.5f,0.5f,0.5f,
		-0.5f,0.5f,-0.5f,
		0.5f,0.5f,-0.5f,
		0.5f,0.5f,0.5f,
		
			
	};
	
	public static float[] bottom_side_block_vertices = {
			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f			
				
		};
	
	public static int[] single_side_block_indices = {
			0,1,3,	
			3,1,2,				
			
	};
	
	//gets and adds the vertex and index coordinates needed for a non-solid (not the same texture on each face) block
	public static void addNonSolidBlockVertAndInd(Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is) {
		float[] combinedVert = new float[side_block_vertices.length + top_side_block_vertices.length + bottom_side_block_vertices.length];
		int[] combinedInd = new int[side_block_indices.length + single_side_block_indices.length + single_side_block_indices.length];
		System.arraycopy(side_block_vertices, 0, combinedVert, 0, side_block_vertices.length);
		System.arraycopy(side_block_indices, 0, combinedInd, 0, side_block_indices.length);
		System.arraycopy(top_side_block_vertices, 0, combinedVert, side_block_vertices.length, top_side_block_vertices.length);
		System.arraycopy(single_side_block_indices, 0, combinedInd, side_block_indices.length, single_side_block_indices.length);
		System.arraycopy(bottom_side_block_vertices, 0, combinedVert, side_block_vertices.length+top_side_block_vertices.length, bottom_side_block_vertices.length);
		System.arraycopy(single_side_block_indices, 0, combinedInd, side_block_indices.length+single_side_block_indices.length, single_side_block_indices.length);
		for (int i = side_block_indices.length; i < single_side_block_indices.length + side_block_indices.length; ++i) {
			combinedInd[i] += side_block_vertices.length / 3;
		}

		for (int i = single_side_block_indices.length + side_block_indices.length; i < side_block_indices.length + single_side_block_indices.length + single_side_block_indices.length; ++i) {
			combinedInd[i] += (side_block_vertices.length + top_side_block_vertices.length) / 3;
		}
		for (int f : combinedInd) Is.add(Vs.size()/3 + f);
		int count = 0;
		for (float f : combinedVert) {
			float offs = 0;
			if (count == 0) offs = position.x - chunk.position.x;
			if (count == 1) offs = position.y - chunk.position.y;
			if (count == 2) offs = position.z - chunk.position.z;
			Vs.add(f + offs);
			count = (count + 1) % 3;
		}
		
		
	}
	
	//gets and adds the UV coordinates needed for a non-solid (not the same texture on each face) block
	public static void addNonSolidBlockUV(int texX1, int texY1, int texX2, int texY2, int texX3, int texY3, List<Float> Us) {
		float[] topUV = SpriteSheetLoader.getUVCoords(texX1, texY1, 1);
		float[] sideUV = SpriteSheetLoader.getUVCoords(texX2, texY2, 4);
		float[] botUV = SpriteSheetLoader.getUVCoords(texX3, texY3, 1);
		float[] combinedUV = new float[topUV.length + sideUV.length + botUV.length];
		System.arraycopy(sideUV, 0, combinedUV, 0, sideUV.length);
		System.arraycopy(topUV, 0, combinedUV, sideUV.length, topUV.length);
		System.arraycopy(botUV, 0, combinedUV, sideUV.length + topUV.length, botUV.length);
		for (float f : combinedUV) Us.add(f);
	}
	
	
	//gets and adds the vertex and index coordinates needed for a solid (same texture on each face) block
	public static void addSolidBlockVertAndInd(Chunk chunk, Vector3f position, List<Float> Vs, List<Integer> Is) {
		    for (int f : all_sides_block_indices) Is.add(Vs.size()/3 + f);
			int count = 0;
			for (float f : all_sides_block_vertices) {
					float offs = 0;
					if (count == 0) offs = position.x - chunk.position.x;
					if (count == 1) offs = position.y - chunk.position.y;
					if (count == 2) offs = position.z - chunk.position.z;
					Vs.add(f + offs);
					count = (count + 1) % 3;
				}
		
		
	}
	
	
	//gets and adds the UV coordinates needed for a solid (same texture on each face) block
	public static void addSolidBlockUV(int texX, int texY, List<Float> Us) {
		for (float f : SpriteSheetLoader.getUVCoords(texX, texY, 6)) Us.add(f);
		
	}
	
	
	private Loader loader;	
	private Vector3f position;
	private Chunk chunk;
	
	public GeneratedBlocks(Loader loader, Chunk chunk, Vector3f position) {
		this.loader = loader;
		this.chunk = chunk;
		this.position = position;
	}
	
	
}
