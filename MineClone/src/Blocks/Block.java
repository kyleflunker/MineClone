package Blocks;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;

public class Block {
		
	private ArrayList<Entity> blockEntities = new ArrayList<Entity>();
	private Vector3f position;

	public Block(Vector3f position, Entity entity1, Entity entity2, Entity entity3) {
		    this.position = position;
		    this.blockEntities.add(entity1);
		    this.blockEntities.add(entity2);
		    this.blockEntities.add(entity3);			
		}
	
	public Block(Vector3f position, Entity entity1, Entity entity2) {
		this.position = position;
	    this.blockEntities.add(entity1);
	    this.blockEntities.add(entity2);		
	}
	
	public Block(Vector3f position, Entity entity1) {
		this.position = position;
	    this.blockEntities.add(entity1);	
	}
	
	public ArrayList<Entity> getBlockEntities() {
		return blockEntities;
	}
	
	public Vector3f getBlockPosition() {
		return position;
	}
		
}


