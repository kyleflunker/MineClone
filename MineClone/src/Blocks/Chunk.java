package Blocks;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;

public class Chunk {
	
	public static int chunkSize = 10;	
	
	private Block[] chunkBlocks = new Block[1000];
	private ArrayList<Entity> renderedEntities = new ArrayList<Entity>();  // list of all entities that exist in the chunk and should be rendered
	
	private int xStartCoord;
	private int yStartCoord;
	private int zStartCoord;	
	private String chunkID; 
	
	
	public Chunk(int xCoord, int yCoord, int zCoord) {
		this.xStartCoord = xCoord;
		this.yStartCoord = yCoord;
		this.zStartCoord = zCoord;
		this.setChunkID(xCoord + "-" + yCoord + "-" + zCoord);
	}
	
	// this decides which entities in the chunk should be rendered (to save resources)
	public void chooseRenderedBlocks() {
		renderedEntities.clear();	
		
		for(Block block : chunkBlocks) {
			if(block != null) {
				checkForAdjacentBlocks(block);
			}
		}
	}
		
	public void checkForAdjacentBlocks(Block block) {
		Vector3f blockPos = block.getBlockPosition();
		boolean topFaceCovered = (chunkBlocks[determineArrayPosition(blockPos.x, blockPos.y + 1, blockPos.z)] != null);
		boolean bottomFaceCovered = (chunkBlocks[determineArrayPosition(blockPos.x, blockPos.y - 1, blockPos.z)] != null);
		boolean sideFacesCovered = (chunkBlocks[determineArrayPosition(blockPos.x + 1, blockPos.y, blockPos.z)] != null &&
				chunkBlocks[determineArrayPosition(blockPos.x - 1, blockPos.y, blockPos.z)] != null &&
				chunkBlocks[determineArrayPosition(blockPos.x, blockPos.y, blockPos.z + 1)] != null &&
				chunkBlocks[determineArrayPosition(blockPos.x, blockPos.y, blockPos.z - 1)] != null			
				);
		for(Entity blockEntity : block.getBlockEntities()) {
			if(blockEntity.getEntityType().equals("solid")) {
				if(!topFaceCovered || !bottomFaceCovered || !sideFacesCovered) {
					renderedEntities.add(blockEntity);
				}
			} else if (blockEntity.getEntityType().equals("top")) {
				if(!topFaceCovered) {
					renderedEntities.add(blockEntity);
				}
			} else if (blockEntity.getEntityType().equals("bottom")) {
				if(!bottomFaceCovered) {
					renderedEntities.add(blockEntity);
				}
			} else if (blockEntity.getEntityType().equals("sides")) {
				if(!sideFacesCovered) {
					renderedEntities.add(blockEntity);
				}
			}
		}
		
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
