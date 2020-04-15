package MineClone;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import Blocks.Chunk;
import Blocks.GeneratedBlocks;
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
import java.awt.BorderLayout;
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
public static int seed = 0;
private static String value = null;

ImageIcon img1 = new ImageIcon("resources/res/title.png");
ImageIcon img2 = new ImageIcon("resources/res/background.png");



public static void main(String[] args) throws IOException {

new MainGame().runTester();
}

public void runTester()
{
final JFrame frame = new JFrame("MineClone");
frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we){
                int result = JOptionPane.showConfirmDialog(frame, "Do you want to quit the Application?");
                if(result == JOptionPane.OK_OPTION){
                    frame.setVisible(false);
                    frame.dispose(); 
                }
            }
        });

        Container mainPanel = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        mainPanel.setLayout(layout);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image image = img2.getImage(); // transform it
        Image newimg = image.getScaledInstance(screenSize.width , screenSize.height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        img2 = new ImageIcon(newimg);        
        
        
        JLabel seedLabel = new JLabel();
        seedLabel.setText("Enter Seed Number:");
        seedLabel.setPreferredSize(new Dimension(500,80));
        seedLabel.setForeground(new Color(186, 176, 171));
        seedLabel.setFont(new Font("Dialog", Font.BOLD,40));
        mainPanel.add(seedLabel);
        layout.putConstraint(SpringLayout.WEST, seedLabel, 600, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, seedLabel, 650, SpringLayout.NORTH, mainPanel);
        
        JTextField seedField = new JTextField();
        seedField.setPreferredSize(new Dimension(300,80));
        seedField.setFont(new Font("Dialog", Font.ITALIC, 40));
        mainPanel.add(seedField);
        layout.putConstraint(SpringLayout.WEST, seedField, 1000, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, seedField, 650, SpringLayout.NORTH, mainPanel);
        
        JLabel seedLabel2 = new JLabel();
        seedLabel2.setText("");
        seedLabel2.setPreferredSize(new Dimension(400,70));
        seedLabel2.setForeground(new Color(186, 176, 171));
        seedLabel2.setFont(new Font("Dialog", Font.PLAIN, 30));
        mainPanel.add(seedLabel2);
        layout.putConstraint(SpringLayout.WEST, seedLabel2, 1000, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, seedLabel2, 720, SpringLayout.NORTH, mainPanel);
        
        
        
        seedField.addKeyListener(new KeyAdapter() {
        	public void keyPressed(KeyEvent ke) {
        		value = seedField.getText();
        		if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
        			seedField.setEditable(true);
        			seedLabel2.setText("");
        		} else {
        			seedField.setEditable(false);
        			seedLabel2.setText("*Only Numerical Values*");
        			
        		}
        	}
        });
        
        
        
        
        JButton button = new JButton("PLAY");
        button.setPreferredSize(new Dimension(700, 100));
        button.setFont(new Font("Dialog", Font.BOLD, 100));
        mainPanel.add(button);
        layout.putConstraint(SpringLayout.WEST, button, ((screenSize.width / 2) - (350)), SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, button, ((screenSize.height / 2) - (50) + (screenSize.height / 3)), SpringLayout.NORTH, mainPanel);
        
        frame.setVisible(true);
        
        button.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent b)
          {
        	  
         seed = Integer.parseInt(seedField.getText());
         startGL();
          }
        });
        
        JLabel background1 = new JLabel("", img1, JLabel.CENTER);
        mainPanel.add(background1);        
        layout.putConstraint(SpringLayout.WEST, background1, ((screenSize.width / 2) - (img1.getIconWidth() / 2)) , SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, background1, ((screenSize.height / 2) - (img1.getIconHeight() / 2)), SpringLayout.NORTH, mainPanel);
        
        JLabel background2 = new JLabel();
        background2.setIcon(img2);
        background2.setVerticalAlignment(JLabel.TOP);
        //background2.setPreferredSize(new Dimension(frame.WIDTH, frame.HEIGHT));
        mainPanel.add(background2);
        layout.putConstraint(SpringLayout.WEST, background2, 5, SpringLayout.WEST, mainPanel);
        layout.putConstraint(SpringLayout.NORTH, background2, 5, SpringLayout.NORTH, mainPanel);
    }	

	public static int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}


private void startGL()
{
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
        
        Noise height = new Noise(100, 14, 20, MainGame.getSeed());        
        Camera camera = new Camera(new Vector3f(5, height.generateHeight(5, 5) + 3, 5), 0 ,0, 0);
        
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