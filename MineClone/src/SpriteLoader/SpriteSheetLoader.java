package SpriteLoader;

import java.math.BigDecimal;

public class SpriteSheetLoader {
	
	public final static float spriteSheetWidth = 512.0f;
	public final static float spriteSheetHeight = 512.0f;
	public final static float textureSize = 128.0f;
	
	public static float[] getUVCoords(float texPosX, float texPosY, int numOfFaces) {
		
		float leftX = getLeftXTexCoord(texPosX);
		float rightX = getRightXTexCoord(texPosX);
		float topY = getTopYTexCoord(texPosY);
		float bottomY = getBottomYTexCoord(texPosY);
		
		if(numOfFaces == 6) {
			float[] UVCoords = {
					
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY
					
			};
			return UVCoords;
			
		} else if(numOfFaces == 4) {
			
			float[] UVCoords = {
					
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY	
			};
			return UVCoords;
			
		} else if(numOfFaces == 1) {
			
			float[] UVCoords = {
					
					leftX, topY,
					leftX, bottomY,
					rightX, bottomY,
					rightX, topY,
					
			};			
			return UVCoords;
			
		}
		return null;
	}
	
	
	public static float getRightXTexCoord(float texPosX) {
		float rightX = (float) (((textureSize * texPosX) / spriteSheetWidth) - .001);
		return rightX;
	}
	
	public static float getTopYTexCoord(float texPosY) {
		BigDecimal test = BigDecimal.valueOf(((textureSize * texPosY) / spriteSheetHeight) + .001);
		BigDecimal test1 = BigDecimal.valueOf(textureSize / spriteSheetHeight);
		float topY = test.subtract(test1).floatValue();
    return topY;
	}	
	
	public static float getLeftXTexCoord(float texPosX) {
		 BigDecimal test = BigDecimal.valueOf(((textureSize * texPosX) / spriteSheetWidth) + .001);
		 BigDecimal test1 = BigDecimal.valueOf(textureSize / spriteSheetWidth);
		float leftX = test.subtract(test1).floatValue();
		return leftX;
	}
	
	public static float getBottomYTexCoord(float texPosY) {
		float bottomY = (float) (((textureSize * texPosY) / spriteSheetHeight) - .001);
		return bottomY;
	}	
		
		
		
}
	
		
	
	
	
	
	
	
	
