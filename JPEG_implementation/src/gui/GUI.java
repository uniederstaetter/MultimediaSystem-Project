package gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import compression.ForwardDCT;
import compression.HuffmanEncoder;
import compression.Quantization;
import compression.ZigZag;
import data.JPEGCategory;

//import com.sun.tools.sjavac.server.SysInfo;

import decompression.BlockOrganisor;
import decompression.InverseDCT;
import decompression.InverseQuantization;
import decompression.InverseZigZag;
import utils.Utils;

/**
 * This class represents the graphical interface of our application.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 *
 */
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

	/**
	 * Global members of the application.
	 */
	private BufferedImage img; // Image the user chooses to be compressed.
	private String filepath; // File the user opens
	private final static String defaultImgPath = "lena_grey.png"; // The path of the default image.
	private BufferedImage selectedImg;

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
		// Adding the possibility to open a new image
		openFileItem = new JMenuItem("Open");
		menu.add(openFileItem);
		menuBar.add(menu);
		// Adds menu bar to the frame.
		this.setJMenuBar(menuBar);
		// Adds the default image to the frame.
		displayDefault();
		// Adding a button for executing the compression
		JButton doCompression = new JButton("Compress Image");
		this.getContentPane().add(doCompression, BorderLayout.SOUTH);

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

			// Result of the forward DCT applied on each 8x8 blocks of the selected image.
			List<Mat> dct_converted = ForwardDCT.divideBlocksDCT(filepath != null ? filepath : defaultImgPath);
			// Result of the quantization applied on each 8x8 block.
			List<Mat> quantised = Quantization.quantise(dct_converted);
			// Result of zigzag scan applied on each 8x8 block.
			List<double[]> zigZag = ZigZag.zigZag(quantised);
			// Initialization of the list containing the encoded blocks.
			List<String> encodedList = new ArrayList<>();

			/**
			 * For each element of each result of the zigzag scan we create an object of the
			 * type JPEGCategory. To see more information about this type read the
			 * documentation of JPEGCategory.
			 * 
			 * @see JPEGCategory.java in package data.
			 */
			for (double[] zig : zigZag) {
				// Create a JPEGCategory object for the DC element of a block.
				JPEGCategory catDC = HuffmanEncoder.RLEDC(zig[0]);
				// Apply Huffman encoding on the DC element.
				String encodedDC = catDC.huffmanEncodeDC();
				// Add the DC element (before the AC elements) to the list of encoded elements.
				encodedList.add(encodedDC);
				// Create a JPEGCategory for each AC element of the block (all elements of the
				// block except the first one).
				List<JPEGCategory> rle = HuffmanEncoder.RLE(zig);
				// Apply Huffman encoding to each AC element and add it to the list of encoded
				// elements.
				for (JPEGCategory r : rle) {
					encodedList.add(r.huffmanEncode());
				}
			}
			// Compression done!
			// Start of decompression!

			// Reconstruct the blocks after the Huffman encoding.
			List<String[]> encodedBlocks = BlockOrganisor.createBlocks(encodedList);
			// Initialization of the list of JPEGCateroy blocks.
			List<JPEGCategory[]> decodedBlocks = new ArrayList<>();

			for (String[] encodedBlock : encodedBlocks) {
				// Initialization of an empty block with size m. Size varies depending on how
				// many 0s we encountered in the encoding part.
				JPEGCategory[] block = new JPEGCategory[encodedBlock.length];
				// Initialization of default JPEGCategory for the DC element.
				JPEGCategory DCElement = new JPEGCategory();
				// Apply decoding on the Huffman encoded string of the DC element.
				DCElement.huffmanDecodeDC(encodedBlock[0]);
				// Add the DC element to the block.
				block[0] = DCElement;
				for (int i = 1; i < encodedBlock.length; i++) { // i starts with 1 to skip the DC element.
					// Initialization of default JPEGCategory for an AC element.
					JPEGCategory ACElement = new JPEGCategory();
					// Apply decoding on the Huffman encoded string.
					ACElement.huffmanDecodeAC(encodedBlock[i]);
					// Add the AC element to the block.
					block[i] = ACElement;
				}
				// Add each decoded block to the list of decoded blocks.
				decodedBlocks.add(block);
			}
			// Initialization of the list of matrices on which the inverse zigzag scan has
			// been applied.
			List<Mat> inversZig = new ArrayList<>();
			// Apply inverse zigzag scan on each block.
			for (int i = 0; i < decodedBlocks.size(); i++) {
				// Add the Matrix after zigzag scan has been applied to the list.
				inversZig.add(InverseZigZag.invert(decodedBlocks.get(i)));
			}
			// Result of inverse quantization applied on each 8x8 block.
			List<Mat> dequantized = InverseQuantization.inverseQuantise(inversZig);
			// Initialization of the list of matrices on which the inverse DCT has been
			// applied.
			List<Mat> inverseDCT = new ArrayList<>();
			// Apply inverse DCT on each 8x8 block.
			for (Mat m : dequantized)
				// Add matrix after the inverse DCT has been applied.
				inverseDCT.add(InverseDCT.inverseDCT(m));

			// Initialization of the matrix which will be transformed back into an image.
			Mat newMat = new Mat(ForwardDCT.getMatRows(), ForwardDCT.getMatCols(), CvType.CV_64FC1);

			int k = 0; // Index to access each block.
			/**
			 * Adds each row of each 8x8 matrix on the right place of the final output
			 * matrix. The 2 outer for-loops are needed to calculate the position for the
			 * rows. Innermost for loops iterates over each row of the 8x8 matrix and puts
			 * each row on the right position.
			 */
			for (int row = 0; row < ForwardDCT.getMatRows(); row += 8) {
				for (int col = 0; col < ForwardDCT.getMatCols(); col += 8) {
					Mat m = inverseDCT.get(k);
					for (int i = 0; i < 8; i++) {
						newMat.put(row + i, col, Utils.matToArr(m.row(i)));
					}
					k++;
				}
			}

			// Convert the final output matrix.
			Mat finalIMG = new Mat();
			newMat.convertTo(finalIMG, CvType.CV_32FC3);
			// Convert the matrix back to an image.
			Imgcodecs.imwrite("compressed_image.png", finalIMG);
			
			Mat originalMat = Utils.imgToMat(filepath != null ? filepath : defaultImgPath);
			Mat origMat = new Mat();
			originalMat.convertTo(origMat, CvType.CV_32FC3);
			System.out.println("PSNR: " + Core.PSNR(origMat, finalIMG));	// Calculate Peak Signal-to-Noise Ratio

			// Display compressed image
			BufferedImage compressedIMG;
			File compressedFile = new File("compressed_image.png");
			try {
				compressedIMG = ImageIO.read(compressedFile);
				if (compressedIMG != null) {
					removeImage();
					addImage(compressedIMG);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		// Display the window.
		this.pack(); // Adjust components of the window.
		// Sets the dimension and the position of the window. (Window should open in the
		// middle of the screen.)
		int width = this.getWidth();
		int height = this.getHeight();
		this.setBounds(widthscreen / 2 - width / 2, heightscreen / 2 - height / 2, width, height);
		this.setVisible(true); // Make the window visible.

	}

	// Used to set up the default selected picture for the compression
	private void displayDefault() {
		File defaultFile = new File(GUI.defaultImgPath);
		// Setting default image
		try {
			img = ImageIO.read(defaultFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		addImage(img);
	}

	/**
	 * Adds a image to the content pane.
	 * 
	 * @param img The image to be added.
	 */
	private void addImage(BufferedImage img) {
		imgLabel = new JLabel(new ImageIcon(img));
		this.getContentPane().add(imgLabel, BorderLayout.CENTER);
		this.pack();
	}

	/** Removes the image from the content pane. */
	private void removeImage() {
		this.getContentPane().remove(imgLabel);
	}

	/**
	 * A file chooser will be created and displayed. The file chooser allows the
	 * user to select a new image file. Only image files are supported.
	 *
	 * @return The path of the image or null if the user does not choose an image.
	 */
	private String chooseImage() {
		// Initialization of the file chooser.
		JFileChooser fileChooser = new JFileChooser();
		// Initialization of the file.
		File file = null;

		// Allows only images to be read.
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files",
				ImageIO.getReaderFileSuffixes());

		// Attaching Filter to JFileChooser object
		fileChooser.setFileFilter(imageFilter);
		// Action to perform.
		int action = fileChooser.showSaveDialog(null);
		// If the user wants to save the image.
		if (action == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			filepath = fileChooser.getSelectedFile().getAbsolutePath();
		} else { // No image has been selected.
			JOptionPane.showMessageDialog(this, "You did not select a file! Please try again!");
			return null;
		}

		try {
			// Read image.
			selectedImg = ImageIO.read(file);
		} catch (IOException e) {
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