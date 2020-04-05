package Blocks;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;



import Entities.Entity;
import MineClone.MainGame;
import MineClone.WorldGeneration;
import Models.RawModel;
import Models.TexturedModel;
import Textures.ModelTexture;

public class Chunk {
	
	public static int chunkSize = 10;	
	
	private Block[] chunkBlocks = new Block[1001];
	private ArrayList<Entity> renderedEntities = new ArrayList<Entity>();  // list of all entities that exist in the chunk and should be rendered
	
	private int xStartCoord;
	private int yStartCoord;
	private int zStartCoord;
	public Vector3f position;	
	private String chunkID; 
	public boolean needsRender = true;
	private int vaoID = -1;
	
	
	public Chunk(int xCoord, int yCoord, int zCoord) {
		this.xStartCoord = xCoord;
		this.yStartCoord = yCoord;
		this.zStartCoord = zCoord;
		this.position = new Vector3f(xCoord, yCoord, zCoord);
		this.setChunkID(xCoord + "-" + yCoord + "-" + zCoord);
	}
	
	// this decides which entities in the chunk should be rendered (to save resources)
	public void chooseRenderedBlocks() {
		needsRender = false;
		renderedEntities.clear();


		List<Float> vert = new ArrayList<>();
		List<Integer> ndx = new ArrayList<>();
		List<Float> uv = new ArrayList<>();

		for(Block block : chunkBlocks) {
			if(block != null) {
				if (checkForAdjacentBlocks(block)) {
					if (block.type == 0) {
						new GrassBlock(MainGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
					} else if (block.type == 1) {
						new StoneBlock(MainGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
					} else if (block.type == 2) {
						//new DirtBlock(MainGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
					} else if (block.type == 3) {
						//new TreeBlock(MainGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
					} else if (block.type == 4) {
						//new LeafBlock(MainGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
					}
				}
			}
		}

		Vector3f pos = position;
		float[] vert_ = new float[vert.size()];
		int[] ndx_ = new int[ndx.size()];
		float[] uv_ = new float[uv.size()];

		for (int i = 0; i < vert_.length; ++i) vert_[i] = vert.get(i);
		for (int i = 0; i < ndx_.length; ++i) ndx_[i] = ndx.get(i);
		for (int i = 0; i < uv_.length; ++i) uv_[i] = uv.get(i);

		RawModel model = MainGame.loader1.loadToVAO(vert_, ndx_, uv_, vaoID);
		vaoID = model.getVaoID();
		ModelTexture texture = new ModelTexture(MainGame.loader1.loadTexture("spriteSheet"));
		TexturedModel texModel = new TexturedModel(model, texture);
		renderedEntities.add(new Entity(texModel, pos, 0, 0, 0, 1));
	}
		

	public boolean checkForAdjacentBlocks(Block block) {
		return checkForAdjacentBlocks(block.getBlockPosition());
	}

	public static boolean checkForAdjacentBlocks(Vector3f blockPos) {
		
		Vector3f p = blockPos;
		int x = (int)p.x;
		int y = (int)p.y;
		int z = (int)p.z;
		boolean secondWay =
		!WorldGeneration.isBlockSolid(x-1, y  , z  ) ||
		!WorldGeneration.isBlockSolid(x+1, y  , z  ) ||
		!WorldGeneration.isBlockSolid(x  , y-1, z  ) ||
		!WorldGeneration.isBlockSolid(x  , y+1, z  ) ||
		!WorldGeneration.isBlockSolid(x  , y  , z-1) ||
		!WorldGeneration.isBlockSolid(x  , y  , z+1);

		return secondWay;
		
	}	
		

	public Block[] getChunkBlocks() {
		return chunkBlocks;
	}


	public ArrayList<Entity> getRenderedEntities() {
		return renderedEntities;
	}


	
	public void addToChunkBlocks(Block block) {		
		chunkBlocks[determineArrayPosition(block.getBlockPosition().x, block.getBlockPosition().y, block.getBlockPosition().z)] = block;		
	}
	
	public int determineArrayPosition(float x1, float y1, float z1) {
		int x = (int) Math.abs(x1) % Chunk.chunkSize;
		int z = (int) Math.abs(z1) % Chunk.chunkSize;
		int y = (int) Math.abs(y1) % Chunk.chunkSize;
		int arrayPos = (x * 100) + (z * 10) + (y);
		if(arrayPos < 0 || arrayPos > 1000) {
			arrayPos = 1001;
		}
		return arrayPos;		
	}


	public int getxStartCoord() {
		return xStartCoord;
	}


	public int getyStartCoord() {
		return yStartCoord;
	}
	

	public int getzStartCoord() {
		return zStartCoord;
	}


	public String getChunkID() {
		return chunkID;
	}

	public void setChunkID(String chunkID) {
		this.chunkID = chunkID;
	}

}
