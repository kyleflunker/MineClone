package MineClone;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import Blocks.Chunk;
import Entities.Camera;
import Entities.Entity;
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
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class MainGame {

	private volatile boolean isRunning = false;
	private Thread glThread;
	public static Loader loader1 = null;
	public static StaticShader shader1 = null;
	public static int seedInteger = 0;
	public static Noise noiseGenerator;  //noiseGenerator is defined in MainGame to be used by other classes (Camera, WorldGeneration, etc)
	ImageIcon titleImage = new ImageIcon("resources/res/title.png");
	ImageIcon backgroundImage = new ImageIcon("resources/res/background.png");



	public static void main(String[] args) throws IOException {
		new MainGame().startTitleWindow();
	}

	public void startTitleWindow() {
		final JFrame titleWindow = new JFrame("MineClone");
		titleWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        titleWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
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
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image image = backgroundImage.getImage(); // transform it
        Image newimg = image.getScaledInstance(screenSize.width , screenSize.height,  java.awt.Image.SCALE_SMOOTH);  
        backgroundImage = new ImageIcon(newimg);        
        
        
        JLabel seedInputLabel = new JLabel();
        seedInputLabel.setText("Enter Seed Number:");
        seedInputLabel.setPreferredSize(new Dimension(500,80));
        seedInputLabel.setForeground(new Color(186, 176, 171));
        seedInputLabel.setFont(new Font("Dialog", Font.BOLD,40));
        titleScreenPane.add(seedInputLabel);
        titleLayout.putConstraint(SpringLayout.WEST, seedInputLabel, 600, SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, seedInputLabel, 650, SpringLayout.NORTH, titleScreenPane);
        
        JTextField seedInputField = new JTextField();
        seedInputField.setPreferredSize(new Dimension(300,80));
        seedInputField.setFont(new Font("Dialog", Font.ITALIC, 40));
        titleScreenPane.add(seedInputField);
        titleLayout.putConstraint(SpringLayout.WEST, seedInputField, 1000, SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, seedInputField, 650, SpringLayout.NORTH, titleScreenPane);
        
        JLabel seedLabel = new JLabel();
        seedLabel.setText("");
        seedLabel.setPreferredSize(new Dimension(400,70));
        seedLabel.setForeground(new Color(186, 176, 171));
        seedLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
        titleScreenPane.add(seedLabel);
        titleLayout.putConstraint(SpringLayout.WEST, seedLabel, 1000, SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, seedLabel, 720, SpringLayout.NORTH, titleScreenPane);       
        
        
        seedInputField.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent key) {
        		if(seedInputField.getText().length() < 6 || key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	        		if (key.getKeyChar() >= '0' && key.getKeyChar() <= '9' || key.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
	        			seedInputField.setEditable(true);
	        			seedLabel.setText("");
	        		} else {
	        			seedInputField.setEditable(false);
	        			seedLabel.setText("*Only Numerical Values*");
	        			
	        		}
        		} else {
        			//if user tries to enter an integer greater than 6 places, limit it
        			seedInputField.setText(seedInputField.getText().substring(0, 6));
        		}
        	}
        });
        
        
        JButton playButton = new JButton("PLAY");
        playButton.setPreferredSize(new Dimension(700, 100));
        playButton.setFont(new Font("Dialog", Font.BOLD, 100));
        titleScreenPane.add(playButton);
        titleLayout.putConstraint(SpringLayout.WEST, playButton, ((screenSize.width / 2) - (350)), SpringLayout.WEST, titleScreenPane);
        titleLayout.putConstraint(SpringLayout.NORTH, playButton, ((screenSize.height / 2) - (50) + (screenSize.height / 3)), SpringLayout.NORTH, titleScreenPane);
        
        playButton.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent b) {
        	  //if player hasn't entered a seed to use, then set a random seed
        	  if(seedInputField.getText().isEmpty()) {
        		  Random random = new Random();
        		  seedInteger = random.nextInt(999999);
        	  } else {
        		  seedInteger = Integer.parseInt(seedInputField.getText());
        	  }
        	  noiseGenerator = new Noise(100, 14, 20, MainGame.getSeed());  
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
    }	

	public static int getSeed() {
		return seedInteger;
	}

	public void setSeed(int seed) {
		this.seedInteger = seed;
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
		        
		        Mouse.setGrabbed(true);
		        
		        long millis = System.currentTimeMillis();
		        long frames = 0;
		
		        while(isRunning) {
			        camera.move(); 
			        renderer.prepare();
			        shader.start();
			        shader.loadViewMatrix(camera);
			        
			        for (Chunk chunks : WorldGeneration.getRenderedChunks()) {
				        for(Entity entity : chunks.getRenderedEntities()) {
				        	renderer.render(entity, shader); 
				        } 
			        }
			        
			        WorldGeneration.chunkController();
			        shader.stop();
			        DisplayManager.updateDisplay();
			        
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

	private void stopGL() {
        isRunning = false;
        try {
            glThread.join();
        } catch (InterruptedException e) {
            //handle exception
            e.printStackTrace();
        }
    }

}