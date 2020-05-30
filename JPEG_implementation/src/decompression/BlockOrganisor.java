package decompression;

import java.util.*;

public class BlockOrganisor {
	

	public static List<String[]> createBlocks(List<String> encodedList) {
		List<String[]> result = new ArrayList<>();
		String block = "";
		int c = 0;
		for (String encoded : encodedList) {
			if (encoded != "") {
				if (!encoded.equals("1010")) {
					block += encoded + ";";
				} else {
					result.add(block.split(";"));
					block = "";
					c++;
				}
			}

		}
		System.out.println(c);
		return result;
	}
}
