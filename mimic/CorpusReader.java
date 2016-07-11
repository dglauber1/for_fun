import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class CorpusReader {
	public static Map<String, Map<String, Integer>> readCorpus(File corpus, Map<String, Integer> letterCount) {
		Map<String, Map<String, Integer>> toReturn = new HashMap<>();
		try {
			FileReader fr = new FileReader(corpus);
			BufferedReader br = new BufferedReader(fr);
			String input = null;
			String firstLetter = null;
			String secondLetter = null;
			while ((input = br.readLine()) != null) {
				int len = input.length();
				if (len == 0) {
					continue;
				}
				secondLetter = input.substring(0, 1);
				incrementIndex(letterCount, secondLetter);
				if (firstLetter != null) { //this isn't the first line
					incrementIndex(toReturn, firstLetter, secondLetter);
				}
				firstLetter = secondLetter;
				for (int i = 1; i < len; i++) {
					secondLetter = input.substring(i, i + 1);
					incrementIndex(letterCount, secondLetter);
					incrementIndex(toReturn, firstLetter, secondLetter);
					firstLetter = secondLetter;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toReturn;
	}

	private static void incrementIndex(Map<String, Map<String, Integer>> map, String str1, String str2) {
		if (!map.containsKey(str1)) {
			map.put(str1, new HashMap<>());
		}
		if (!map.get(str1).containsKey(str2)) {
			map.get(str1).put(str2, 0);
		}
		Integer currentVal = map.get(str1).get(str2);
		map.get(str1).put(str2, currentVal + 1);
	}

	private static void incrementIndex(Map<String, Integer> map, String str) {
		if (!map.containsKey(str)) {
			map.put(str, 0);
		}
		Integer currentVal = map.get(str);
		map.put(str, currentVal + 1);
	}
}