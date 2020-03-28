package Blocks;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import RenderEngine.Loader;

public class Chunk {
	
	public static int chunkSize = 10;
	
	private ArrayList<Entity> chunk_blocks = new ArrayList<Entity>();
	private ArrayList<Entity> rendered_blocks = new ArrayList<Entity>();
	private ArrayList<Entity> unrendered_blocks = new ArrayList<Entity>();
	private int xStartCoord;
	private int xEndCoord;
	private int yStartCoord;
	private int yEndCoord;
	private int zStartCoord;
	private int zEndCoord;
	
	
	public Chunk(int xCoord, int yCoord, int zCoord) {
		this.xStartCoord = xCoord;
		this.yStartCoord = yCoord;
		this.zStartCoord = zCoord;
		this.xEndCoord = this.xStartCoord + chunkSize;
		this.yEndCoord = this.yStartCoord + chunkSize;
		this.zEndCoord = this.zStartCoord + chunkSize;
	}
	


	public ArrayList<Entity> getChunk_blocks() {
		return chunk_blocks;
	}


	public ArrayList<Entity> getRendered_blocks() {
		return rendered_blocks;
	}


	public ArrayList<Entity> getUnrendered_blocks() {
		return unrendered_blocks;
	}
	
	public void addTo_chunk_blocks(Entity e) {
		chunk_blocks.add(e);
	}


	public int getxStartCoord() {
		return xStartCoord;
	}


	public int getxEndCoord() {
		return xEndCoord;
	}


	public int getyStartCoord() {
		return yStartCoord;
	}


	public int getyEndCoord() {
		return yEndCoord;
	}


	public int getzStartCoord() {
		return zStartCoord;
	}


	public int getzEndCoord() {
		return zEndCoord;
	}
	
	
	
	
}
