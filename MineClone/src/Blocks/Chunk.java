package Blocks;

import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;
import java.util.List;
import Entities.Entity;
import MainGame.RunGame;
import MainGame.WorldGeneration;
import Models.RawModel;
import Models.TexturedModel;
import Textures.ModelTexture;

public class Chunk {
	
	public static int chunkSize = 10;  // each chunk is 10x10x10	
	private Block[] chunkBlocks = new Block[1001];
	private Entity chunkEntity = null;  //each chunk is made out of one entity
	private int xStartCoord;
	private int yStartCoord;
	private int zStartCoord;
	public Vector3f position;	
	private String chunkID; 
	public boolean needsRender = true;
	private int vaoID = -1;	
	public long unloadFrame = 0;
	public long unloadTime = 0;
	public boolean unloading= false;
	public boolean unloaded = false;
	public boolean needsPopIn = false;
	
	
	public Chunk(int xCoord, int yCoord, int zCoord) {
		this.xStartCoord = xCoord;
		this.yStartCoord = yCoord;
		this.zStartCoord = zCoord;
		this.position = new Vector3f(xCoord, yCoord, zCoord);
		this.setChunkID(xCoord + "-" + yCoord + "-" + zCoord);
	}
	
	// this decides which blocks in the chunk should be rendered (to save resources)
	public void chooseRenderedBlocks() {
		needsRender = false;

		List<Float> vert = new ArrayList<>();
		List<Integer> ndx = new ArrayList<>();
		List<Float> uv = new ArrayList<>();

		for(Block block : chunkBlocks) {
			if(block != null) {
				if (checkForAdjacentBlocks(block)) {
					switch(block.type) {
					case 0:
						new GrassBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 1:
						new StoneBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 2:
						new DirtBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 3:
						new SandBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 4:
						new OakTreeBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 5:
						new OakLeafBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 6:
						new BirchTreeBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 7:
						new BirchLeafBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 8:
						new JungleTreeBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 9:
						new JungleLeafBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;
					case 10:
						new CactusBlock(RunGame.loader1, this, block.getBlockPosition(), vert, ndx, uv);
						break;							
					}
					
				} 
			}
		}

		if (ndx.size() > 0) {
			
	 		Vector3f pos = position;
	 		float[] vert_ = new float[vert.size()];
	 		int[] ndx_ = new int[ndx.size()];
	 		float[] uv_ = new float[uv.size()];

			for (int i = 0; i < vert_.length; ++i) vert_[i] = vert.get(i);
			for (int i = 0; i < ndx_.length; ++i) ndx_[i] = ndx.get(i);
			for (int i = 0; i < uv_.length; ++i) uv_[i] = uv.get(i);

			RawModel model = RunGame.loader1.loadToVAO(vert_, ndx_, uv_, vaoID);
	 		vaoID = model.getVaoID();
	 		ModelTexture texture = new ModelTexture(RunGame.loader1.loadTexture("blockSheet"));
	 		TexturedModel texModel = new TexturedModel(model, texture);
	
			chunkEntity = new Entity(texModel, pos, 0.0f, 0.0f, 0.0f, 1.0f, this);			 		
			
		}
	}
		

	public boolean checkForAdjacentBlocks(Block block) {
		return checkForAdjacentBlocks(block.getBlockPosition());
	}

	//check to see if the argument block is surrounded on each side
	public static boolean checkForAdjacentBlocks(Vector3f blockPos) {	
		int x = (int)blockPos.x;
		int y = (int)blockPos.y;
		int z = (int)blockPos.z;
		boolean shouldRender =
		!WorldGeneration.isBlockSolid(x-1, y  , z  ) ||
		!WorldGeneration.isBlockSolid(x+1, y  , z  ) ||
		!WorldGeneration.isBlockSolid(x  , y-1, z  ) ||
		!WorldGeneration.isBlockSolid(x  , y+1, z  ) ||
		!WorldGeneration.isBlockSolid(x  , y  , z-1) ||
		!WorldGeneration.isBlockSolid(x  , y  , z+1);

		return shouldRender;
	}	
		

	public Block[] getChunkBlocks() {
		return chunkBlocks;
	}


	public Entity getChunkEntity() {
		return chunkEntity;
	}


	//when adding a block to a chunk, add it to the chunkBlocks array so it can be easily accessed based upon position
	public void addToChunkBlocks(Block block) {		
		chunkBlocks[determineArrayPosition(block.getBlockPosition().x, block.getBlockPosition().y, block.getBlockPosition().z)] = block;		
	}
	
	
	//uses simple algorithm to determine where to store the block object in the chunkBlocks array
	public int determineArrayPosition(float x1, float y1, float z1) {
		int x = (int) Math.abs(x1) % Chunk.chunkSize;
		int z = (int) Math.abs(z1) % Chunk.chunkSize;
		int y = (int) Math.abs(y1) % Chunk.chunkSize;
		int arrayPos = (x * 100) + (z * 10) + (y);
		
		return arrayPos;		
	}	
	
	
	public void unloadChunk() {
		if (unloading || unloaded) return;
			unloading = true;
			unloaded = false;
			unloadTime = System.currentTimeMillis();
		}
	
	public void tickUnload() {
		if (!unloading) return;
		if (System.currentTimeMillis() - unloadTime > 1000) { unloading = false; unloaded = true; }
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
