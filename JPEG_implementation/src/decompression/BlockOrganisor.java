package decompression;

import java.util.*;

/**
 * Organization of blocks after Huffman encoding.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 *
 */
public class BlockOrganisor {

	/**
	 * Splitting the list of Huffman encoded strings into blocks.
	 * 
	 * @param encodedList The list of Huffman encoded strings.
	 * @return Blocks of Huffman encoded strings.
	 */
	public static List<String[]> createBlocks(List<String> encodedList) {
		List<String[]> result = new ArrayList<>();
		String block = "";

		for (String encoded : encodedList) {
			if (encoded != "") {
				if (!encoded.equals("1010")) {
					block += encoded + ";"; // If not end of block add it to the string and add a separator.
				} else { // Found end of block
					result.add(block.split(";")); // Split the string by the separator and add it as a block to the
													// list.
					block = ""; // Reset the block.
				}
			}
		}

		return result;
	}
}
