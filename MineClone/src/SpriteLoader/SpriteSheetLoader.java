
/*Currently not functioning
package SpriteLoader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class SpriteSheetLoader {
	
	BufferedImage spritesheet = ImageIO.read(new File("resources/TreeBark.png"));
	
	int width;
	int height;
	int rows;
	int columns;
	
	BufferedImage[] sprites;
	
	public SpriteSheetLoader(int width, int height, int rows, int columns) throws IOException {
		
		this.width = width;
		this.height = height;
		this.rows = rows;
		this.columns = columns;
		sprites = new BufferedImage[rows * columns];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				sprites[(i * columns) + j] = spritesheet.getSubimage(i*width, j*height, width, height);
			}
		}
		
	}
	public void draw(Graphics g) {
		g.drawImage(sprites[1], 10, 10, null);
	}
		
	
	
	
	
	
	
	
}*/