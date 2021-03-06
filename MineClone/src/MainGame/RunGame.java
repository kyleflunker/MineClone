package MainGame;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import Blocks.Chunk;
import Entities.Camera;
import Entities.Entity;
import Entities.PlayerHand;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import Shaders.StaticShader;
import Tools.Noise;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class RunGame {

	private volatile boolean isRunning = false;  //is the game running or not
	private Thread glThread;
	public static Loader loader1 = null;
	public static StaticShader shader1 = null;
	public static int seedInteger = 0;  //seed to be used in the noiseGenerator (for terrain randomization purposes)
	public static Noise noiseGenerator;  //noiseGenerator is defined in MainGame to be used by other classes (Camera, WorldGeneration, etc)
	ImageIcon titleImage = new ImageIcon("resources/res/title.png");
	ImageIcon backgroundImage = new ImageIcon("resources/res/background.png");
	ImageIcon singlePlayerButton = new ImageIcon("resources/res/singleplayerButton.PNG");
	Font minecraftFont;

	//open the title window only when application is started
	public static void main(String[] args) {
		new RunGame().startTitleWindow();
	}

	public void startTitleWindow() {
		final JFrame titleWindow = new JFrame("MineClone");
		titleWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        titleWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);        
        
        //create the Minecraft font from resources
        try {
			minecraftFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/res/minecraft.ttf")).deriveFont(40f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/res/minecraft.ttf")));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}     
        
        //creates listener for when user tries to exit the application window
        titleWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we){
                int userSelection = JOptionPane.showConfirmDialog(titleWindow, "Would you like to quit MineClone?");
                if(userSelection == JOptionPane.OK_OPTION){
                    titleWindow.setVisible(false);
                    titleWindow.dispose(); 
                    System.exit(0);
                }
            }
        });

        Container titleScreenPane = titleWindow.getContentPane();
        SpringLayout titleLayout = new SpringLayout();
        titleScreenPane.setLayout(titleLayout);
        
        //scales the background image to fit the entire window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image image = backgroundImage.getImage(); // transform it
        Image newimg = image.getScaledInstance(screenSize.width , screenSize.height,  java.awt.Image.SCALE_SMOOTH);  
        backgroundImage = new ImageIcon(newimg);    
        
        JLabel seedInputLabel = new JLabel();
        seedInputLabel.setText("Enter Seed Number");
        seedInputLabel.setPreferredSize(new Dimension(600,80));        
        seedInputLabel.setFont(minecraftFont);
        seedInputLabel.setForeground(Color.white);
        titleScreenPane.add(seedInputLabel);
        titleLayout.putConstraint(SpringLayout.WEST, seedInputLabel, ((screenSize.width / 2) - 400), SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, seedInputLabel, ((screenSize.height / 2) + (screenSize.height / 10)), SpringLayout.NORTH, titleScreenPane);
        
        JLabel seedLabel = new JLabel();
        seedLabel.setText("");
        seedLabel.setPreferredSize(new Dimension(600,70));
        seedLabel.setForeground(Color.white);
        seedLabel.setFont(minecraftFont);
        titleScreenPane.add(seedLabel);
        titleLayout.putConstraint(SpringLayout.WEST, seedLabel, ((screenSize.width / 2) + 50), SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, seedLabel, ((screenSize.height / 2) + (screenSize.height / 5)), SpringLayout.NORTH, titleScreenPane);   
        
        JTextField seedInputField = new JTextField();
        seedInputField.setPreferredSize(new Dimension(230,55));
        seedInputField.setFont(minecraftFont);
        seedInputField.setForeground(new Color(110, 110, 110));
        titleScreenPane.add(seedInputField);
        titleLayout.putConstraint(SpringLayout.WEST, seedInputField, ((screenSize.width / 2) + 100), SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, seedInputField, ((screenSize.height / 2) + (screenSize.height / 10) + 10), SpringLayout.NORTH, titleScreenPane);        
        
        //creates listener for when user enters something into the seed input field
        seedInputField.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent key) {
        		if(seedInputField.getText().length() < 6 || key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	        		if (key.getKeyChar() >= '0' && key.getKeyChar() <= '9' && !(key.getKeyCode() == '-') || key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	        			seedInputField.setEditable(true);
	        			seedLabel.setText("");
	        		} else {
	        			seedInputField.setEditable(false);
	        			seedLabel.setText("Numbers Only");
	        		}
        		} else {
        			//if user tries to enter an integer greater than 6 places, limit it
        			if (key.getKeyChar() >= '0' && key.getKeyChar() <= '9' && !(key.getKeyCode() == '-') || key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	        			seedInputField.setText(seedInputField.getText().substring(0, 6));
	        			seedLabel.setText("");
	        		} else {
	        			seedInputField.setEditable(false);
	        			seedLabel.setText("Numbers Only");
	        		}
        			
        		}
        	}
        });        
       
        JButton playButton = new JButton();
        playButton.setIcon(singlePlayerButton);
        titleScreenPane.add(playButton);
        titleLayout.putConstraint(SpringLayout.WEST, playButton, ((screenSize.width / 2) - (200)), SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, playButton, ((screenSize.height / 2) - (20) + (screenSize.height / 3)), SpringLayout.NORTH, titleScreenPane);
        playButton.setPreferredSize(new Dimension(400, 40));
        
        //creates listener for when user presses the Play button
        playButton.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent b) {
        	  //if player hasn't entered a seed to use, then set a random seed
        	  SoundController.playSound("resources/res/buttonClick.wav", "button");
        	  if(seedInputField.getText().isEmpty()) {
        		  Random random = new Random();
        		  seedInteger = random.nextInt(999999);
        	  } else {
        		  seedInteger = Integer.parseInt(seedInputField.getText());
        	  }
        	  noiseGenerator = new Noise(100, 14, 20, RunGame.getSeed());  //create the noise generator using the seed 
        	  startGameWindow();
        	  
          }
        });
        
        JLabel titleImageLabel = new JLabel("", titleImage, JLabel.CENTER);
        titleScreenPane.add(titleImageLabel);        
        titleLayout.putConstraint(SpringLayout.WEST, titleImageLabel, ((screenSize.width / 2) - (titleImage.getIconWidth() / 2)) , SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, titleImageLabel, ((screenSize.height / 2) - (titleImage.getIconHeight() / 2)), SpringLayout.NORTH, titleScreenPane);
        
        JLabel backgroundImageLabel = new JLabel();
        backgroundImageLabel.setIcon(backgroundImage);
        backgroundImageLabel.setVerticalAlignment(JLabel.TOP);
        titleScreenPane.add(backgroundImageLabel);
        titleLayout.putConstraint(SpringLayout.WEST, backgroundImageLabel, 5, SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, backgroundImageLabel, 5, SpringLayout.NORTH, titleScreenPane);
        
        titleWindow.setVisible(true);
        
        
        SoundController.playLoopedSound("resources/res/music.wav", "Music");
        
    }
	
	private void startGameWindow() {
		glThread = new Thread(new Runnable() {
        @Override
	        public void run() {
	            isRunning = true;
	            try {
	            Display.setFullscreen(true);
	                Display.create();
	            } catch (LWJGLException e) {
	                //handle exception
	                e.printStackTrace();
	            } 
	
		        Loader loader = new Loader();
		        loader1 = loader;
		        StaticShader shader = new StaticShader();
		        shader1 = shader;
		        MasterRenderer renderer = new MasterRenderer(shader);
		              
		        Camera camera = new Camera(new Vector3f(5, noiseGenerator.generateHeight(5, 5) + 10, 5), 0 ,0, 0);
		        PlayerHand playerHand = new PlayerHand(new Vector3f(Camera.getPosition().x,Camera.getPosition().y, Camera.getPosition().z), 0.0f, 20f, 0.0f, .36f, true);		      
		        
		        Mouse.setGrabbed(true);  //attach the mouse to the OpenGL window
		        
		        long millis = System.currentTimeMillis();
		        long frames = 0;
		        
		        while(isRunning) {
			        camera.move(); //handle player movement and positioning
			        playerHand.checkForInput(); //handle changes in the block that the player is holding
			        renderer.prepare();
			        shader.start();
			        
			        //render the crosshair in the middle of the screen
			        shader.loadViewMatrix(playerHand.getPlayerCrosshair());
			        renderer.render(playerHand.getPlayerCrosshair(), shader);
			        
			        //render the two entities that make up the block that the player holds
			        shader.loadViewMatrix(playerHand.getPlayerHand().get(0));			        
			        for(Entity playerHandEntity : playerHand.getPlayerHand()) {
			        	renderer.render(playerHandEntity, shader);
			        }
			        
			        //render the chunk entities
			        shader.loadViewMatrix(camera);
			        for (Chunk chunk : WorldGeneration.getRenderedChunks()) {
			        	if(chunk.getChunkEntity() != null) {
			        		renderer.render(chunk.getChunkEntity(), shader);
			        	}
			        }
			        
			        WorldGeneration.chunkController();  //control which chunks should be rendered/created
			        shader.stop();
			        DisplayManager.updateDisplay();
			        
			        //keep track of FPS and print it to console
			        frames++;
			        if (System.currentTimeMillis() - millis > 1000) {
				        System.out.printf("FPS: %f\n", frames / ((System.currentTimeMillis() - millis) / (double)1000));
				        millis = System.currentTimeMillis();
				        frames = 0;
				    }
		        }
		        
		        DisplayManager.closeDisplay();
            }
        }, "LWJGL Thread");
        glThread.start();
	}
	
	public static int getSeed() {
		return seedInteger;
	}

	public void setSeed(int seed) {
		this.seedInteger = seed;
	}

}