import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class Mimic {
	
	private void run() {
		REPL();
	}

	public static void main(String[] args) {
		Mimic mimic = new Mimic();
		mimic.run();
	}

	private void REPL() {
		String input = null;
		try {
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			while (!(input = br.readLine()).equals("")) {
				File corpus = new File(input);
				Map<String, Integer> letterCounts = new HashMap<>();
				Map<String, Map<String, Integer>> frequencies = CorpusReader.readCorpus(corpus, letterCounts);
				// for (String letter1 : _frequencies.keySet()) {
				// 	for (String letter2 : _frequencies.get(letter1).keySet()) {
				// 		System.out.println(String.format("%s, %s, %d", letter1, letter2, _frequencies.get(letter1).get(letter2)));
				// 	}
				// }
				System.out.print("Number of words to simulate: ");
				int numWords = Integer.parseInt(br.readLine());
				printWords(frequencies, letterCounts, numWords);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printWords(Map<String, Map<String, Integer>> frequencies, Map<String, Integer> letterCounts, int numWords) {
		int wordsWritten = 0;
		int totalLetters = 0;
		for (String letter : letterCounts.keySet()) {
			totalLetters += letterCounts.get(letter);
		}
		int randomIndex = (int) (Math.random() * totalLetters);
		int count = 0;
		String toWrite = null;
		for (String letter : letterCounts.keySet()) {
			if (count + letterCounts.get(letter) > randomIndex) {
				toWrite = letter;
				break;
			}
			count += letterCounts.get(letter);
		}
		if (toWrite == null) {
			System.out.println("uh oh");
			return;
		}
		int lastLetterIndex = 0;
		while (wordsWritten < numWords) {
			String lastLetter = toWrite.substring(lastLetterIndex, lastLetterIndex + 1);
			randomIndex = (int) (Math.random() * letterCounts.get(lastLetter));
			count = 0;
			String toAppend = "";
			for (String letter : frequencies.get(lastLetter).keySet()) {
				if (count + frequencies.get(lastLetter).get(letter) > randomIndex) {
					toAppend = letter;
					break;
				}
				count += frequencies.get(lastLetter).get(letter);
			}
			toWrite += toAppend;
			lastLetterIndex++;
			if (toAppend.equals(" ")) {
				wordsWritten++;
			}
		}
		System.out.println(toWrite);
	}


}