import java.util.*;
import java.io.*;

public class MyUnigramClassifier {

	public static void main(String[] args) throws IOException {
		FileInputStream fs = new FileInputStream(
				"/Users/premakolli/Downloads/Final_Yelp_Data.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		List<String> neglistOfFiles = new ArrayList<String>();
		for (int i = 0; i < 8000; ++i)
			neglistOfFiles.add(br.readLine());
		HashMap<String, Integer> totalWordCount = new HashMap<String, Integer>();
		HashMap<String, Integer> negWordCount = new HashMap<String, Integer>();
		HashMap<String, Integer> posWordCount = new HashMap<String, Integer>();
		List<HashMap<String, Integer>> arrayHashMaps = new ArrayList<HashMap<String, Integer>>();
		int negCount = -1;
		for (String file : neglistOfFiles) {
			negCount++;
			HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
			if (file != null) {
				Integer reviewNo = negCount;
				if (!(reviewNo >= 0 && reviewNo < 1000)) {
					file = file.replaceAll("[^A-Za-z ]", " ")
							.replaceAll("\\s+", " ").toLowerCase();
					if (file.length() <= 3) continue;

					String operators = " ";
					StringTokenizer tokens = new StringTokenizer(file,
							operators, false);
					while (tokens.hasMoreTokens()) {
						String word = tokens.nextToken();
						if (totalWordCount.containsKey(word))

							totalWordCount.put(word,
									totalWordCount.get(word) + 1);
						else {
							totalWordCount.put(word, 1);
						}
						if (negWordCount.containsKey(word))

							negWordCount.put(word, negWordCount.get(word) + 1);
						else {
							negWordCount.put(word, 1);
						}
						if (documentWordCount.containsKey(word))

							documentWordCount.put(word,
									documentWordCount.get(word) + 1);
						else {
							documentWordCount.put(word, 1);
						}
					}
					arrayHashMaps.add(documentWordCount);

				}
			}

		}

		List<String> poslistOfFiles = new ArrayList<String>();
		for (int i = 8000; i < 16000; ++i)
			poslistOfFiles.add(br.readLine());
		int posCount = -1;
		for (String file : poslistOfFiles) {
			posCount++;
			HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
			if (file != null) {
				Integer reviewNo = posCount;
				if (!(reviewNo >= 0 && reviewNo < 1000)) {
					file = file.replaceAll("[^A-Za-z ]", " ")
							.replaceAll("\\s+", " ").toLowerCase();
					if (file.length() <= 3) continue;

					String operators = " ";
					StringTokenizer tokens = new StringTokenizer(file,
							operators, false);
					while (tokens.hasMoreTokens()) {
						String word = tokens.nextToken();
						if (totalWordCount.containsKey(word))
							totalWordCount.put(word,
									totalWordCount.get(word) + 1);
						else {
							totalWordCount.put(word, 1);
						}
						if (posWordCount.containsKey(word))

							posWordCount.put(word, posWordCount.get(word) + 1);
						else {
							posWordCount.put(word, 1);
						}
						if (documentWordCount.containsKey(word))

							documentWordCount.put(word,
									documentWordCount.get(word) + 1);
						else {
							documentWordCount.put(word, 1);
						}

					}
					arrayHashMaps.add(documentWordCount);

				}
			}
		}

		HashMap<String, Double> negProbability = new HashMap<String, Double>();
		Integer uniqueWordCount = totalWordCount.size();
		int totalWordCountInNegClass = 0;
		for (Integer wordCount : negWordCount.values()) {
			totalWordCountInNegClass = wordCount + totalWordCountInNegClass;
		}

		for (Map.Entry<String, Integer> negWordCountEntry : negWordCount
				.entrySet()) {
			Integer negWordCountPerClass = negWordCountEntry.getValue();

			Double probability = ((double) (negWordCountPerClass + 1) / (totalWordCountInNegClass + uniqueWordCount));

			negProbability.put(negWordCountEntry.getKey(), probability);

		}

		HashMap<String, Double> posProbability = new HashMap<String, Double>();

		int totalWordCountInPosClass = 0;
		for (Integer wordCount : posWordCount.values()) {
			totalWordCountInPosClass = wordCount + totalWordCountInPosClass;
		}

		for (Map.Entry<String, Integer> posWordCountEntry : posWordCount
				.entrySet()) {
			Integer posWordCountPerClass = posWordCountEntry.getValue();

			Double probability = ((double) (posWordCountPerClass + 1) / (totalWordCountInPosClass + uniqueWordCount));

			posProbability.put(posWordCountEntry.getKey(), probability);

		}
/*		
 		// Code for calculating training error percentage/training accuracy - Only needed to check how well the model was trained
		ArrayList<Double> posDocProbability = new ArrayList<Double>();
		ArrayList<Double> negDocProbability = new ArrayList<Double>();
		ArrayList<Double> PresencePosDocProbability = new ArrayList<Double>();
		ArrayList<Double> PresenceNegDocProbability = new ArrayList<Double>();
		for (HashMap<String, Integer> documentHashMap : arrayHashMaps) {
			Double posSum = (double) 0;
			Double negSum = (double) 0;
			Double posSumPresence = (double) 0;
			Double negSumPresence = (double) 0;

			int TrainingTotalWordCount = 0;
			for (Integer wordCount : documentHashMap.values()) {
				TrainingTotalWordCount = wordCount + TrainingTotalWordCount;
			}

			for (Map.Entry<String, Integer> wordCountPerDocumentEntry : documentHashMap
					.entrySet()) {
				if (posProbability.containsKey(wordCountPerDocumentEntry
						.getKey())) {
					posSum += -((double) 1 / TrainingTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log(posProbability
									.get(wordCountPerDocumentEntry.getKey())));
					posSumPresence += -((double) 1 / documentHashMap.size())
							* Math.log(posProbability
									.get(wordCountPerDocumentEntry.getKey()));
				} else {
					posSum += -((double) 1 / TrainingTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log((double) 1
									/ (totalWordCountInPosClass + uniqueWordCount)));
					posSumPresence += -((double) 1 / documentHashMap.size())
							* (Math.log((double) 1
									/ (totalWordCountInPosClass + uniqueWordCount)));
				}

				if (negProbability.containsKey(wordCountPerDocumentEntry
						.getKey())) {
					negSum += -((double) 1 / TrainingTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log(negProbability
									.get(wordCountPerDocumentEntry.getKey())));
					negSumPresence += -((double) 1 / documentHashMap.size())
							* Math.log(negProbability
									.get(wordCountPerDocumentEntry.getKey()));
				} else {
					negSum += -((double) 1 / TrainingTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log((double) 1
									/ (totalWordCountInNegClass + uniqueWordCount)));
					negSumPresence += -((double) 1 / documentHashMap.size())
							* (Math.log((double) 1
									/ (totalWordCountInNegClass + uniqueWordCount)));
				}

			}
			posDocProbability.add(posSum);
			negDocProbability.add(negSum);
			PresencePosDocProbability.add(posSumPresence);
			PresenceNegDocProbability.add(negSumPresence);

		}

		Double ErrorCount = 0.0;
		Double PresenceErrorCount = 0.0;
		for (int i = 0; i < 6000; i++) {
			if (posDocProbability.get(i) < negDocProbability.get(i)) {
				ErrorCount += 1;
			}
		}

		for (int i = 6000; i < 12000; i++) {
			if (posDocProbability.get(i) > negDocProbability.get(i)) {
				ErrorCount += 1;
			}
		}

		for (int i = 0; i < 6000; i++) {

			if (PresencePosDocProbability.get(i) < PresenceNegDocProbability
					.get(i)) {
				PresenceErrorCount += 1;
			}
		}

		for (int i = 6000; i < 12000; i++) {
			if (PresencePosDocProbability.get(i) > PresenceNegDocProbability
					.get(i)) {
				PresenceErrorCount += 1;
			}
		}
		PresenceErrorCount = 1-(PresenceErrorCount/12000);
		ErrorCount = 1-(ErrorCount/12000);

		System.out.println("Training Frequency Accuracy: " + ErrorCount*100);
		System.out.println("Training Presence Accuracy: " + PresenceErrorCount*100);
*/
		// testing

		int testNegCount = -1;
		HashMap<String, Integer> testTotalWordCount = new HashMap<String, Integer>();

		List<HashMap<String, Integer>> testArrayHashMaps = new ArrayList<HashMap<String, Integer>>();
		for (String negfile : neglistOfFiles) {
			testNegCount++;
			HashMap<String, Integer> testDocumentWordCount = new HashMap<String, Integer>();
			if (negfile != null) {
				Integer reviewNo = testNegCount;
				if ((reviewNo >= 0 && reviewNo < 1000)) {
					negfile = negfile.replaceAll("[^A-Za-z ]", " ")
							.replaceAll("\\s+", " ").toLowerCase();
					if (negfile.length() <= 3) continue;

					String operators = " ";
					StringTokenizer tokens = new StringTokenizer(negfile,
							operators, false);
					while (tokens.hasMoreTokens()) {
						String word = tokens.nextToken();
						if (testTotalWordCount.containsKey(word))

							testTotalWordCount.put(word,
									testTotalWordCount.get(word) + 1);
						else {
							testTotalWordCount.put(word, 1);
						}

						if (testDocumentWordCount.containsKey(word))

							testDocumentWordCount.put(word,
									testDocumentWordCount.get(word) + 1);
						else {
							testDocumentWordCount.put(word, 1);
						}
					}
					testArrayHashMaps.add(testDocumentWordCount);

				}

			}
		}
		int testPosCount = -1;
		for (String posfile : poslistOfFiles) {
			testPosCount++;
			HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
			if (posfile != null) {
				Integer reviewNo = testPosCount;
				if ((reviewNo >= 0 && reviewNo < 1000)) {

					posfile = posfile.replaceAll("[^A-Za-z ]", " ")
							.replaceAll("\\s+", " ").toLowerCase();
					if (posfile.length() <= 3) continue;

					String operators = " ";
					StringTokenizer tokens = new StringTokenizer(posfile,
							operators, false);
					while (tokens.hasMoreTokens()) {
						String word = tokens.nextToken();
						if (testTotalWordCount.containsKey(word))
							testTotalWordCount.put(word,
									testTotalWordCount.get(word) + 1);
						else {
							testTotalWordCount.put(word, 1);
						}

						if (documentWordCount.containsKey(word))

							documentWordCount.put(word,
									documentWordCount.get(word) + 1);
						else {
							documentWordCount.put(word, 1);
						}

					}

					testArrayHashMaps.add(documentWordCount);
				}
			}
		}

		ArrayList<Double> testPosDocProbability = new ArrayList<Double>();
		ArrayList<Double> testNegDocProbability = new ArrayList<Double>();
		ArrayList<Double> testPresencePosDocProbability = new ArrayList<Double>();
		ArrayList<Double> testPresenceNegDocProbability = new ArrayList<Double>();
		for (HashMap<String, Integer> documentHashMap : testArrayHashMaps) {
			Double testposSum = (double) 0;
			Double testposSumPresence = (double) 0;
			Double testnegSum = (double) 0;
			Double testnegSumPresence = (double) 0;

			int TestTotalWordCount = 0;
			for (Integer wordCount : documentHashMap.values()) {
				TestTotalWordCount = wordCount + TestTotalWordCount;
			}

			for (Map.Entry<String, Integer> wordCountPerDocumentEntry : documentHashMap
					.entrySet()) {
				if (posProbability.containsKey(wordCountPerDocumentEntry
						.getKey())) {
					testposSum += -((double) 1 / TestTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log(posProbability
									.get(wordCountPerDocumentEntry.getKey())));
					testposSumPresence += -((double) 1 / documentHashMap.size())
							* Math.log(posProbability
									.get(wordCountPerDocumentEntry.getKey()));
				}

				else {
					testposSum += -((double) 1 / TestTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log((double) 1
									/ (totalWordCountInPosClass + uniqueWordCount)));
					testposSumPresence += -((double) 1 / documentHashMap.size())
							* (Math.log((double) 1
									/ (totalWordCountInPosClass + uniqueWordCount)));
				}

				if (negProbability.containsKey(wordCountPerDocumentEntry
						.getKey())) {
					testnegSum += -((double) 1 / TestTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log(negProbability
									.get(wordCountPerDocumentEntry.getKey())));
					testnegSumPresence += -((double) 1 / documentHashMap.size())
							* (Math.log(negProbability
									.get(wordCountPerDocumentEntry.getKey())));
				}

				else {
					testnegSum += -((double) 1 / TestTotalWordCount)
							* (wordCountPerDocumentEntry.getValue())
							* (Math.log((double) 1
									/ (totalWordCountInNegClass + uniqueWordCount)));
					testnegSumPresence += -((double) 1 / documentHashMap.size())
							* (Math.log((double) 1
									/ (totalWordCountInNegClass + uniqueWordCount)));
				}
			}

			testPosDocProbability.add(testposSum);
			testNegDocProbability.add(testnegSum);
			testPresencePosDocProbability.add(testposSumPresence);
			testPresenceNegDocProbability.add(testnegSumPresence);

		}

		// code for counting errors in classification
		int testErrorCount = 0;
		int testPresenceErrorCount = 0;
		for (int i = 0; i < 1000; i++) {
			if (testPosDocProbability.get(i) < testNegDocProbability.get(i)) {
				testErrorCount += 1;
			}
		}

		for (int i = 1000; i < 2000; i++) {
			if (testPosDocProbability.get(i) > testNegDocProbability.get(i)) {
				testErrorCount += 1;
			}
		}

		for (int i = 0; i < 1000; i++) {

			if (testPresencePosDocProbability.get(i) < testPresenceNegDocProbability
					.get(i)) {
				testPresenceErrorCount += 1;
			}
		}

		for (int i = 1000; i < 2000; i++) {
			if (testPresencePosDocProbability.get(i) > testPresenceNegDocProbability
					.get(i)) {
				testPresenceErrorCount += 1;
			}
		}
		System.out.println("Test Frequency Errors: " + testErrorCount);
		System.out.println("Test Frequency Accuracy: "
				+ (double) (2000 - testErrorCount) / 20 + "%");

		System.out.println("TEST PRESENCE ERRORS " + testPresenceErrorCount);
		System.out.println("Test Presence Accuracy: "
				+ (double) (2000 - testPresenceErrorCount) / 20 + "%");
		br.close();

	}
}
