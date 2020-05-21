
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class GUI {
    
    private static final String WINDOW_NAME = "JPEG Compression_implementation";


    private Mat srcGray = new Mat();
    private Mat dst = new Mat();
    
    
    
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem openFileItem;
    private JLabel imgLabel;
    private Image img; //Image the user chooses to be compressed.
    private String filepath;//file the user opens
    private String imageFile;
    Image selectedImg;

    
    
    public GUI() {
    	
        // Create and set up the window.
        frame = new JFrame(WINDOW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //creates the menu bar.
        menuBar=new JMenuBar();
        
        //build the menu.
        menu=new JMenu("Menu");
        menuBar.add(menu);
        
        openFileItem=new JMenuItem("Open");//adding the possibility to open a new image
        menu.add(openFileItem);
        
        openFileItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
        	
        		imageFile=chooseImage();
        		
        		if (selectedImg!=null) {
        			deleteComponentsOfPane(frame.getContentPane(), img);//delete the old image
                	addComponentsToPane(frame.getContentPane(), selectedImg);//add the new image
        		}
        		
        		
        
        	}
        });
        
        
        //adds menu bar to the frame.
        frame.setJMenuBar(menuBar);
        
        //displays default image
        displayDefault();
        
        //adding a button for executing the compression
        JButton doCompression=new JButton("Compress Image");
        frame.getContentPane().add(doCompression, BorderLayout.SOUTH);
        doCompression.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//Mat dct_mat=JPEG_compr.dct(imageFile);//convert image to dct.
				List<Mat> dct_converted=DCT.divideBlocksDCT(imageFile);
				//Quantization.fillMat();
				List<Mat> quantised=Quantization.quantise(dct_converted);
				/*int width=DCT.height;
				int height=DCT.width;
				Mat zigzag=ZigZag.zigZag(quantised, width, height);
				*/
				
				
			}
		});
        //add here code for executing algorithm
       
        
      
        // Set up the content pane.
        //Image img = HighGui.toBufferedImage(srcGray);
        //addComponentsToPane(frame.getContentPane(), img);
        
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        frame.pack();//adjust it self automatically to the contents inside the frame.
        frame.setVisible(true);
        
    }
    
    //used to set up the default selected picture for the compression
    private void displayDefault() {
    	 File defaultFile= new File("lena_grey.png");
    	 imageFile="lena_grey.png";
         //Setting default image
         try {
			img=ImageIO.read(defaultFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         addComponentsToPane(frame.getContentPane(),img);
    }
  
    
  
    //add an to the panel
    private void addComponentsToPane(Container panel, Image img) {
    	
    	imgLabel = new JLabel(new ImageIcon(img));
        panel.add(imgLabel, BorderLayout.CENTER);
        frame.pack();
    }
    
    //delete an image from the panel
    private void deleteComponentsOfPane(Container panel, Image img) {
    	 panel.remove(imgLabel);
    	
    }
    
    //user chooses a file from its own directory for the compression
    private String chooseImage() {
    	
    	JFileChooser fileChooser= new JFileChooser();
    	File file=null;
    	
    	//allows only images to be read.
    	FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
    		    "Image files", ImageIO.getReaderFileSuffixes());

    	//Attaching Filter to JFileChooser object
    	fileChooser.setFileFilter(imageFilter);
    	
    	int action=fileChooser.showSaveDialog(null);
    	
    	if (action == JFileChooser.APPROVE_OPTION) {
    		
    		file=fileChooser.getSelectedFile();
    		filepath=fileChooser.getSelectedFile().getAbsolutePath();
    		
    	}else {
    		JOptionPane.showMessageDialog(frame, "You did not select a file! Please try again!");
    		return null;//user did not choose an image, so null is returned that the old image is not deleted from frame.
    	}
    	
    	try {
			 selectedImg=ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return filepath;
    	
    }
    
    
    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}