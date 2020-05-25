package compression;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class GUI extends JFrame {

	/** Just until I found the settings for this warning! */
	private static final long serialVersionUID = 1L;

	/**
	 * Members for window settings.
	 */
	private static String WINDOW_NAME = "JPEG Compression_implementation";
	private int widthscreen = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int heightscreen = Toolkit.getDefaultToolkit().getScreenSize().height;

	/**
	 * Components of the window.
	 */
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem openFileItem;
	private JLabel imgLabel;

	private Mat srcGray = new Mat();
	private Mat dst = new Mat();

	private Image img; // Image the user chooses to be compressed.
	private String filepath; // File the user opens
	private final static String defaultImgPath = "lena_grey.png"; // The path of the default image.
	Image selectedImg;

	public GUI() {
		// Create and set up the window.
		super(WINDOW_NAME);
		// If the user closes the window the program stops.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Disables the functionality to resize the window.
		this.setResizable(false);

		// Creates the menu bar.
		menuBar = new JMenuBar();

		// Build the menu and add it to the menu bar.
		menu = new JMenu("Menu");
		// adding the possibility to open a new image
		openFileItem = new JMenuItem("Open");
		menu.add(openFileItem);
		menuBar.add(menu);
		// Adds menu bar to the frame.
		this.setJMenuBar(menuBar);

		// Adds the default image to the frame.
		displayDefault();
		

		// adding a button for executing the compression
		JButton doCompression = new JButton("Compress Image");
		this.getContentPane().add(doCompression, BorderLayout.SOUTH);
		
		// add here code for executing algorithm

		// Set up the content pane.
		// Image img = HighGui.toBufferedImage(srcGray);
		// addComponentsToPane(frame.getContentPane(), img);

		/** Action listeners. */

		/**
		 * Creates an action listener for the menu item "Open". When the user clicks on
		 * the item a file chooser will open. After selecting an image the default image
		 * or the image already chosen will be removed an the new selected image will be
		 * added to the container.
		 */
		openFileItem.addActionListener(event -> { // Lambda expression to simplify code.

			filepath = chooseImage();

			if (selectedImg != null) {
				removeImage();
				addImage(selectedImg); // Add the new image
			}
		});
		
		doCompression.addActionListener(event -> {
				
				List<Mat> dct_converted = ForwardDCT.divideBlocksDCT(defaultImgPath);
				
				List<Mat> quantised = Quantization.quantise(dct_converted);
				
				List<double []>zigZag=ZigZag.zigZag(quantised);
				
				for (double[] zig : zigZag) {
					List<JPEGCategory> rle = HuffmanEncoder.RLE(zig);
					double rng  = DPCM.getRange(zigZag);
					double lvl = DPCM.quantiseError(zig[0], rng);
					String DCElement=DPCM.encode(lvl);
					//System.out.println(DPCM.encode(lvl) + "<<< DC element");
					for (JPEGCategory r : rle) {
						//System.out.println(r.convertToBinary());
						System.out.println(r.huffmanEncode());
					}
					
				}
//				List<JPEGCategory>rle=HuffmanEncoder.RLE(zigZag.get(0));
//				for(JPEGCategory r: rle) {
//					System.out.println(r.huffmanEncode());
//				}
				
				 
		});

		// Display the window.
		this.pack(); 			// Adjust it self automatically to the contents inside the frame. What?
		// Sets the dimension and the position of the window. (Window should open in the
		// middle of the screen.)
		int width = this.getWidth();
		int height = this.getHeight();
		this.setBounds(widthscreen / 2 - width / 2, heightscreen / 2 - height / 2, width, height);
		this.setVisible(true);	// Make the window visible.

	}

	// Used to set up the default selected picture for the compression
	private void displayDefault() {
		File defaultFile = new File(GUI.defaultImgPath);
		// Setting default image
		try {
			img = ImageIO.read(defaultFile);
		} catch (IOException e) {
			// TODO Open error dialog here.
			e.printStackTrace();
		}
		addImage(img);
	}

	/**
	 * Adds a image to the content pane.
	 * @param img The image to be added.
	 */
	private void addImage(Image img) {
		imgLabel = new JLabel(new ImageIcon(img));
		this.getContentPane().add(imgLabel, BorderLayout.CENTER);
		this.pack();
	}

	/** Removes the image from the content pane. */
	private void removeImage() {
		this.getContentPane().remove(imgLabel);
	}

	/**
	 * A file chooser will be created and displayed.
	 * The file chooser allows the user to select a new image  file.
	 * Only image files are supported.
	 *
	 * @return The path of the image or null if the user does not choose an image.
	 */
	private String chooseImage() {

		JFileChooser fileChooser = new JFileChooser();
		File file = null;

		// Allows only images to be read.
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());

		// Attaching Filter to JFileChooser object
		fileChooser.setFileFilter(imageFilter);

		int action = fileChooser.showSaveDialog(null);

		
		if (action == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			filepath = fileChooser.getSelectedFile().getAbsolutePath();
		} else {
			JOptionPane.showMessageDialog(this, "You did not select a file! Please try again!");
			return null;
		}

		/**
		 * Thinking about to do it in a different way!
		 */
		try {
			selectedImg = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filepath;
	}

	public static void main(String[] args) {
		// Load the native OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// Start the application!
		new GUI();
	}
}