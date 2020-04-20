package Entities;

import org.lwjgl.util.vector.Vector3f;

import Blocks.Chunk;
import Models.TexturedModel;

public class Entity {
	
	private Chunk chunk;
	public Vector3f position;
	float rotX, rotY, rotZ;
	float scale;
	private long age = 0;
	
	TexturedModel model;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Chunk chunk) {
		
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.chunk = chunk;
	}
		
	
	public Vector3f getPosition() {
		return position;
	}
	
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public void increaseScale(float scale) {
		this.scale += scale;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public float getRotX() {
		return rotX;
	}
	
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}
	
	public float getRotY() {
		return rotY;
	}
	
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}
	
	public float getRotZ() {
		return rotZ;
	}
	
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
	
	public float getScale() {
		if (chunk.unloaded) {
			throw new Error();
		}
		if (chunk.unloading) {
			long chunkAge = System.currentTimeMillis() - chunk.unloadTime;
			return scale * (((1-(chunkAge / 1000f)) * (1-(chunkAge / 1000f))));
		}
		if (chunk.needsPopIn) {
			chunk.needsPopIn = false;
			age = System.currentTimeMillis();
		}
		if (age != 0) {
			long chunkAge = System.currentTimeMillis() - age;
			if (chunkAge < 1000) {
				if (Vector3f.sub(Camera.position, position, null).lengthSquared() < 300) age = 0;
				return scale * (1- ((1-(chunkAge / 1000f)) * (1-(chunkAge / 1000f))));
			}
		}
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	

}
