package Blocks;

import java.util.ArrayList;
import Entities.Entity;

public class Chunk {
	
	public static int chunkSize = 10;
	
	private ArrayList<Entity> chunk_blocks = new ArrayList<Entity>();  // list of all blocks that exist in the chunk
	private ArrayList<Entity> rendered_blocks = new ArrayList<Entity>();  // list of blocks that exist in the chunk and should be rendered
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
	
	// this decides which blocks in the chunk should be rendered (to save resources)
	public void chooseRenderedBlocks() {
		rendered_blocks.clear();
		for(Entity block : chunk_blocks) {
			//if block is solid, check if it's completely surrounded with other blocks.
			if(block.getEntityType().equals("solid")) {
			   if(!isSolidBlockSurrounded(block)) {
				   //if at least one face is visible, render the block
				   rendered_blocks.add(block);
			   }
			//if block only consists of sides, check to see if blocks exist on each side
			} else if (block.getEntityType().equals("sides")) {
				if(!areSidesSurrounded(block)) {
					//if at least one side face is visible, render the block
					rendered_blocks.add(block);
				}
			//if block only consists of the top face, check to see if a block exists above
			} else if (block.getEntityType().equals("top")) {
				if(!isTopSurrounded(block)) {
					//if the top face is visible, render the block
					rendered_blocks.add(block);
				}
			//if the block only consists of the bottom face, check to see if a block exists below
			} else if (block.getEntityType().equals("bottom")) {
				if(!isBottomSurrounded(block)) {
					//if the bottom face is visible, render the block
					rendered_blocks.add(block);
				}
			}
		}
	}
	
	//checks to see if there exists an adjacent block on every face of the argument block
	private boolean isSolidBlockSurrounded(Entity block) {
		int faces_covered = 0;
		
		//2D array of every possible adjacent block (6 possible blocks)
		float [][] adjacentBlocks = new float[][]{
			{block.getPosition().x + 1, block.getPosition().y, block.getPosition().z},
			{block.getPosition().x - 1, block.getPosition().y,  block.getPosition().z},
			{block.getPosition().x, block.getPosition().y + 1, block.getPosition().z},
			{block.getPosition().x, block.getPosition().y - 1, block.getPosition().z},
			{block.getPosition().x, block.getPosition().y, block.getPosition().z + 1},
			{block.getPosition().x, block.getPosition().y, block.getPosition().z - 1}	
		};
		
		//for loop checks/counts how many of the faces are covered by an adjacent block
		for(Entity chunkBlock : chunk_blocks) {
			for (float[] b : adjacentBlocks) {
				if(b[0] == chunkBlock.getPosition().x && b[1] == chunkBlock.getPosition().y && b[2] == chunkBlock.getPosition().z) {
					if(chunkBlock.getEntityType().equals("sides") || chunkBlock.getEntityType().equals("solid")) {
					   faces_covered += 1;
					}
				}
			}
		}
		
		//if all faces are covered (6 total), the block shouldn't be rendered
		if(faces_covered == 6) {
			return true;
		} else {
			return false;
		}
		
	}
	
	//checks to see if there exists an adjacent block on each side of the argument block (which in this case only consists of sides)
	private boolean areSidesSurrounded(Entity block) {
		int faces_covered = 0;
		
		//2D array of every possible adjacent block (4 possible blocks)
		float [][] adjacentBlocks = new float[][]{
			{block.getPosition().x + 1, block.getPosition().y, block.getPosition().z},
			{block.getPosition().x - 1, block.getPosition().y,  block.getPosition().z},
			{block.getPosition().x, block.getPosition().y, block.getPosition().z + 1},
			{block.getPosition().x, block.getPosition().y, block.getPosition().z - 1}
		};
		
		//for loop checks/counts how many of the faces are covered by an adjacent block
		for(Entity chunkBlock : chunk_blocks) {
			for (float[] b : adjacentBlocks) {
				if(b[0] == chunkBlock.getPosition().x && b[1] == chunkBlock.getPosition().y && b[2] == chunkBlock.getPosition().z) {
					if(chunkBlock.getEntityType().equals("sides") || chunkBlock.getEntityType().equals("solid")) {
					   faces_covered += 1;
					}
				}
			}
		}
		
		//if all sides are covered (4 total), the block shouldn't be rendered
		if(faces_covered == 4) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//checks to see if there exists an adjacent block above the argument block 
	private boolean isTopSurrounded(Entity block) {		
		float [][] adjacentBlocks = new float[][]{			
			{block.getPosition().x, block.getPosition().y + 1, block.getPosition().z}
		};
		for(Entity chunkBlock : chunk_blocks) {
			for (float[] b : adjacentBlocks) {
				if(b[0] == chunkBlock.getPosition().x && b[1] == chunkBlock.getPosition().y && b[2] == chunkBlock.getPosition().z) {
					return true;
				}
			}
		}
		return false;
	}
	
	//checks to see if there exists an adjacent block below the argument block 
	private boolean isBottomSurrounded(Entity block) {		
		float [][] adjacentBlocks = new float[][]{			
			{block.getPosition().x, block.getPosition().y - 1, block.getPosition().z}
		};
		for(Entity chunkBlock : chunk_blocks) {
			for (float[] b : adjacentBlocks) {
				if(b[0] == chunkBlock.getPosition().x && b[1] == chunkBlock.getPosition().y && b[2] == chunkBlock.getPosition().z) {					
					return true;
				}
			}
		}
		return false;
	}
	


	public ArrayList<Entity> getChunk_blocks() {
		return chunk_blocks;
	}


	public ArrayList<Entity> getRendered_blocks() {
		return rendered_blocks;
	}


	
	public void addTo_chunk_blocks(Entity e) {
		chunk_blocks.add(e);		
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
