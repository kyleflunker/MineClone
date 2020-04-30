package Entities;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import Blocks.GeneratedBlocks;
import MainGame.RunGame;
import Models.RawModel;
import Models.TexturedModel;
import SpriteLoader.SpriteSheetLoader;
import Textures.ModelTexture;

public class PlayerHand {
	private static Vector3f position;
	private static float rotX;
	private static float rotY;
	private static float rotZ;
	private static float scale;
	private static ArrayList<Entity> playerHand = new ArrayList<Entity>();
	private static Entity playerCrosshair;
	private static boolean staticEntity;
	private static int sideX = 1;
	private static int sideY = 1;
	private static int topX = 3;
	private static int topY = 1;
	private static int selectedBlock = 0;
	private static boolean isHand = true; //Used to decide whether we're using the hand or block
	
	
	public PlayerHand(Vector3f position, float rotX, float rotY, float rotZ, float scale, boolean staticEntity) {
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.staticEntity = staticEntity;
		createHandEntities();
		createPlayerCrosshair();
	}
	
	public static void changeTexture(int x1, int y1, int x2, int y2) {
		sideX = x1;
		sideY = y1;
		topX = x2;
		topY = y2;
		createHandEntities();
	}

	private static void createHandEntities() {
		if (isHand == false)//use this to switch hand vs block
		{
			playerHand.clear();
			RawModel sideModel = RunGame.loader1.loadToVAO(GeneratedBlocks.side_block_vertices, GeneratedBlocks.side_block_indices, SpriteSheetLoader.getUVCoords(sideX, sideY, 4), -1);
		 	ModelTexture sideTexture = new ModelTexture(RunGame.loader1.loadTexture("blockSheet"));
		 	TexturedModel sideTexModel = new TexturedModel(sideModel, sideTexture);		
			playerHand.add(new Entity(sideTexModel, position, rotX - 2, rotY, rotZ - 2, scale, staticEntity, "hand")); //Trying to rotate the block
			RawModel topModel = RunGame.loader1.loadToVAO(GeneratedBlocks.top_side_block_vertices, GeneratedBlocks.single_side_block_indices, SpriteSheetLoader.getUVCoords(topX, topY, 1), -1);
		 	ModelTexture topTexture = new ModelTexture(RunGame.loader1.loadTexture("blockSheet"));
		 	TexturedModel topTexModel = new TexturedModel(topModel, topTexture);		
			playerHand.add(new Entity(topTexModel, position, rotX, rotY, rotZ, scale, staticEntity, "hand"));
		}else
		{
			//This one uses the hand sheet, it doesn't apply the texture correctly yet, started with just a block
			playerHand.clear();
			RawModel sideHand = RunGame.loader1.loadToVAO(GeneratedBlocks.side_block_vertices, GeneratedBlocks.side_block_indices, SpriteSheetLoader.getUVCoords(sideX, sideY, 4), -1);
		 	ModelTexture sideTexture = new ModelTexture(RunGame.loader1.loadTexture("hand"));
		 	TexturedModel sideTexModel = new TexturedModel(sideHand, sideTexture);		
			playerHand.add(new Entity(sideTexModel, position, rotX, rotY, rotZ, scale, staticEntity, "hand"));
			RawModel topHand = RunGame.loader1.loadToVAO(GeneratedBlocks.top_side_block_vertices, GeneratedBlocks.single_side_block_indices, SpriteSheetLoader.getUVCoords(topX, topY, 1), -1);
		 	ModelTexture topTexture = new ModelTexture(RunGame.loader1.loadTexture("hand"));
		 	TexturedModel topTexModel = new TexturedModel(topHand, topTexture);		
			playerHand.add(new Entity(topTexModel, position, rotX, rotY, rotZ, scale, staticEntity, "hand"));
		}
		
	}	
	
	private static void createPlayerCrosshair() {
		float[] cross_vert = {
			-0.5f,0.10f,0.5f,	
			-0.5f,-0.10f,0.5f,	
			0.5f,-0.10f,0.5f,	
			0.5f,0.10f,0.5f,
			-0.1f,0.5f,0.5f,	
			-0.1f,-0.5f,0.5f,	
			0.1f,-0.5f,0.5f,	
			0.1f,0.5f,0.5f,
		};
		int[] cross_ind = {
			0,1,3,	
			3,1,2,	
			4,5,7,
			7,5,6
		};
		float[] cross_uv = {				
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		RawModel crosshairModel = RunGame.loader1.loadToVAO(cross_vert, cross_ind, cross_uv, -1);
	 	ModelTexture crosshairTexture = new ModelTexture(RunGame.loader1.loadTexture("crosshair"));
	 	TexturedModel crosshairTexModel = new TexturedModel(crosshairModel, crosshairTexture);		
		playerCrosshair = new Entity(crosshairTexModel, position, 0, 0, 0, .02f, staticEntity, "crosshair");
	}
	
	

	public void checkForInput() {
		if(Keyboard.isKeyDown(Keyboard.KEY_0)) {
			//changeTexture(1, 1, 3, 1);
			isHand  = true;
			selectedBlock = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
			changeTexture(4, 1, 4, 1);
			selectedBlock = 1;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
			changeTexture(2, 1, 2, 1);
			selectedBlock = 2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_3)) {
			changeTexture(1, 4, 1, 4);
			selectedBlock = 3;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_4)) {
			changeTexture(4, 2, 3, 2);
			selectedBlock = 4;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_5)) {
			changeTexture(3, 3, 3, 3);
			selectedBlock = 5;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_6)) {
			changeTexture(2, 2, 1, 2);
			selectedBlock = 6;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_7)) {
			changeTexture(4, 3, 4, 3);
			selectedBlock = 7;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_8)) {
			changeTexture(2, 3, 1, 3);
			selectedBlock = 8;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_9)) {
			changeTexture(3, 4, 3, 4);
			selectedBlock = 9;
		}		
		
	}	
	
	public static int getSelectedBlock() {
		return selectedBlock;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public float getScale() {
		return scale;
	}

	public ArrayList<Entity> getPlayerHand() {
		return playerHand;
	}
	
	public Entity getPlayerCrosshair() {
		return playerCrosshair;
	}

	public boolean isStaticEntity() {
		return staticEntity;
	}
	 
}
