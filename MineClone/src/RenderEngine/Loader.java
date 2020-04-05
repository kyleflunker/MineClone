package RenderEngine;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Models.RawModel;

public class Loader {
	
	static List<Integer> vaos = new ArrayList<Integer>();
	static List<Integer> vbos = new ArrayList<Integer>();
	static List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVAO(float[] vertices, int[] indices, float[] uv) { return loadToVAO(vertices, indices, uv, -1); }
	public RawModel loadToVAO(float[] vertices, int[] indices, float[] uv, int vaoID) {
		if (vaoID == -1) vaoID = createVAO();
		GL30.glBindVertexArray(vaoID);
		storeDataInAttributeList(vertices, 0, 3);
		storeDataInAttributeList(uv, 1, 2);
		bindIndicesBuffer(indices);
		GL30.glBindVertexArray(0);
		
		return new RawModel(vaoID, indices.length);
		
	}
	
	private int createVAO() {
		
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		
		return vaoID;
		
	}
	
	static Map<String, Integer> textureCache = new HashMap<String, Integer>();
	public int loadTexture(String fileName) {
		if (textureCache.containsKey(fileName)) return textureCache.get(fileName);

		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("/res/" + fileName + ".PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);

		textureCache.put(fileName, textureID);
		return textureID;
	}
	
	private void storeDataInAttributeList(float[] data, int attributeNumber, int dimentions) {
		
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, dimentions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
	}
	
	IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	public void cleanUp() {
		System.out.println(vaos.size());
		System.out.println(vbos.size());
		System.out.println(textures.size());
		
		for(int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for(int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
		
	}

}
