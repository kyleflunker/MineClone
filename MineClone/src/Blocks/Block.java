package Blocks;

import org.lwjgl.util.vector.Vector3f;


public class Block {
		
	
	private Vector3f position;
	public int type;

	public Block(Vector3f position, int type) {
		this.position = position;
		this.type = type;
	}	
	
	public Vector3f getBlockPosition() {
		return position;
	}
		
}


