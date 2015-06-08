
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class MyBigramClassifier {
	
	public static void main(String[] args) throws IOException {
		FileInputStream fs = new FileInputStream(
				"/Users/premakolli/Downloads/Final_Yelp_Data.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		List<String> neglistOfFiles = new ArrayList<String>();
		for (int i = 0; i < 8000; ++i)
			neglistOfFiles.add(br.readLine());
		
			HashMap<String, Integer> bigramTotalWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> negWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> bigramNegWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> posWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> bigramPosWordCount = new HashMap<String, Integer>();

			List<HashMap<String, Integer>> bigramArrayHashMaps = new ArrayList<HashMap<String, Integer>>();

			int negCount = -1;
			for (String file : neglistOfFiles) {
				negCount++;
				HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
				if (file != null) {
					Integer reviewNo = negCount;
					if (!(reviewNo >= 0 && reviewNo < 2000)) {
						file = file.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();

						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(file,
								operators, false);
						String previousWord = null;

						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
							if (negWordCount.containsKey(word))

								negWordCount.put(word,
										negWordCount.get(word) + 1);
							else {
								negWordCount.put(word, 1);
							}
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(bigram))

									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
								if (bigramNegWordCount.containsKey(bigram))

									bigramNegWordCount.put(bigram,
											bigramNegWordCount.get(bigram) + 1);
								else {
									bigramNegWordCount.put(bigram, 1);
								}
								if (documentWordCount.containsKey(bigram))

									documentWordCount.put(bigram,
											documentWordCount.get(bigram) + 1);
								else {
									documentWordCount.put(bigram, 1);
								}

							}
							previousWord = word;
						}
						bigramArrayHashMaps.add(documentWordCount);

					}
				}

			}

			List<String> poslistOfFiles = new ArrayList<String>();
			for (int i = 8000; i < 16000; ++i){
				poslistOfFiles.add(br.readLine());
			}
			int posCount = -1;
			for (String file : poslistOfFiles) {
				posCount++;
				HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
				if (file != null) {
					Integer reviewNo = posCount;
					if (!(reviewNo >= 0 && reviewNo < 2000)) {
						file = file.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(file,
								operators, false);
						String previousWord = null;

						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
							if (posWordCount.containsKey(word))

								posWordCount.put(word,
										posWordCount.get(word) + 1);
							else {
								posWordCount.put(word, 1);
							}
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(word))
									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
								if (bigramPosWordCount.containsKey(bigram))

									bigramPosWordCount.put(bigram,
											bigramPosWordCount.get(bigram) + 1);
								else {
									bigramPosWordCount.put(bigram, 1);
								}
								if (documentWordCount.containsKey(bigram))

									documentWordCount.put(bigram,
											documentWordCount.get(bigram) + 1);
								else {
									documentWordCount.put(bigram, 1);
								}

							}
							previousWord = word;

						}
						bigramArrayHashMaps.add(documentWordCount);

					}
				}
			}

			HashMap<String, Double> negProbability = new HashMap<String, Double>();
			Integer uniqueWordCount = bigramTotalWordCount.size();
			int totalWordCountInNegClass = 0;
			for (Integer wordCount : negWordCount.values()) {
				totalWordCountInNegClass = wordCount + totalWordCountInNegClass;
			}

			for (Map.Entry<String, Integer> negWordCountEntry : bigramNegWordCount
					.entrySet()) {

				Integer negWordCountPerClass = negWordCountEntry.getValue();
				String unigram = negWordCountEntry.getKey().split(" ")[0];

				Double probability = ((double) (negWordCountPerClass + 1) / (negWordCount
						.get(unigram) + uniqueWordCount));

				negProbability.put(negWordCountEntry.getKey(), probability);

			}

			HashMap<String, Double> posProbability = new HashMap<String, Double>();

			for (Map.Entry<String, Integer> posWordCountEntry : bigramPosWordCount
					.entrySet()) {
				String unigram = posWordCountEntry.getKey().split(" ")[0];
				Integer posWordCountPerClass = posWordCountEntry.getValue();

				Double probability = ((double) (posWordCountPerClass + 1) / (posWordCount
						.get(unigram) + uniqueWordCount));

				posProbability.put(posWordCountEntry.getKey(), probability);

			}

	
			HashMap<String, Integer> bigramTestTotalWordCount = new HashMap<String, Integer>();

			List<HashMap<String, Integer>> bigramTestArrayHashMaps = new ArrayList<HashMap<String, Integer>>();
			int testNegCount = -1;
			for (String negfile : neglistOfFiles) {
				testNegCount++;
				HashMap<String, Integer> testDocumentWordCount = new HashMap<String, Integer>();
				if (negfile != null) {
					Integer reviewNo = testNegCount;
					if ((reviewNo >= 0 && reviewNo < 2000)) {
						negfile = negfile.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();

						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(negfile,
								operators, false);
						String previousWord = null;

						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTestTotalWordCount
										.containsKey(bigram))

									bigramTestTotalWordCount.put(bigram,
											bigramTestTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTestTotalWordCount.put(bigram, 1);
								}

								if (testDocumentWordCount.containsKey(bigram))

									testDocumentWordCount
											.put(bigram, testDocumentWordCount
													.get(bigram) + 1);
								else {
									testDocumentWordCount.put(bigram, 1);
								}
							}
							previousWord = word;
						}
						bigramTestArrayHashMaps.add(testDocumentWordCount);

					}

				}
			}

			int testPosCount = -1;
			for (String posfile : poslistOfFiles) {
				testPosCount++;
				HashMap<String, Integer> testDocumentWordCount = new HashMap<String, Integer>();
				if (posfile != null) {
					Integer reviewNo = testPosCount;
					if ((reviewNo >= 0 && reviewNo < 2000)) {
						posfile = posfile.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(posfile,
								operators, false);
						String previousWord = null;

						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();


							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTestTotalWordCount
										.containsKey(bigram))
									bigramTestTotalWordCount.put(bigram,
											bigramTestTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTestTotalWordCount.put(bigram, 1);
								}

								if (testDocumentWordCount.containsKey(bigram))

									testDocumentWordCount.put(bigram,
											testDocumentWordCount.get(bigram) + 1);
								else {
									testDocumentWordCount.put(bigram, 1);
								}

							}
							previousWord = word;
						}
						bigramTestArrayHashMaps.add(testDocumentWordCount);
					}
				}
			}
			ArrayList<Double> testPosDocProbability = new ArrayList<Double>();
			ArrayList<Double> testNegDocProbability = new ArrayList<Double>();
			ArrayList<Double> testPresencePosDocProbability = new ArrayList<Double>();
			ArrayList<Double> testPresenceNegDocProbability = new ArrayList<Double>();
			for (HashMap<String, Integer> documentHashMap : bigramTestArrayHashMaps) {
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
						testposSumPresence += -((double) 1 / documentHashMap
								.size())
								* Math.log(posProbability
										.get(wordCountPerDocumentEntry.getKey()));
					}

					else {
						String unigram = wordCountPerDocumentEntry.getKey()
								.split(" ")[0];
						if (posWordCount.containsKey(unigram)) {
							testposSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1
											/ (posWordCount.get(unigram) + uniqueWordCount)));
							testposSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1
											/ (posWordCount.get(unigram) + uniqueWordCount)));
						} else {
							testposSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1 / uniqueWordCount));
							testposSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1 / uniqueWordCount));

						}
					}

					if (negProbability.containsKey(wordCountPerDocumentEntry
							.getKey())) {
						testnegSum += -((double) 1 / TestTotalWordCount)
								* (wordCountPerDocumentEntry.getValue())
								* (Math.log(negProbability
										.get(wordCountPerDocumentEntry.getKey())));
						testnegSumPresence += -((double) 1 / documentHashMap
								.size())
								* (Math.log(negProbability
										.get(wordCountPerDocumentEntry.getKey())));
					}

					else {
						String unigram = wordCountPerDocumentEntry.getKey()
								.split(" ")[0];
						if (negWordCount.containsKey(unigram)) {
							testnegSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1
											/ (negWordCount.get(unigram) + uniqueWordCount)));
							testnegSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1
											/ (negWordCount.get(unigram) + uniqueWordCount)));
						} else {
							testnegSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1 / (uniqueWordCount)));
							testnegSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1 / (uniqueWordCount)));
						}
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
			for (int i = 0; i < 2000; i++) {
				if (testPosDocProbability.get(i) < testNegDocProbability.get(i)) {
					testErrorCount += 1;
				}
			}

			for (int i = 2000; i < 4000; i++) {
				if (testPosDocProbability.get(i) > testNegDocProbability.get(i)) {
					testErrorCount += 1;
				}
			}

			for (int i = 0; i < 2000; i++) {

				if (testPresencePosDocProbability.get(i) < testPresenceNegDocProbability
						.get(i)) {
					testPresenceErrorCount += 1;
				}
			}

			for (int i = 2000; i < 4000; i++) {
				if (testPresencePosDocProbability.get(i) > testPresenceNegDocProbability
						.get(i)) {
					testPresenceErrorCount += 1;
				}
			}
			//System.out.println("Test Frequency Errors: " + testErrorCount);
			System.out.println("Test Frequency Accuracy: " + (double) (4000 - testErrorCount)
					/ 40 + "%");
			
		//	System.out.println("TEST PRESENCE ERRORS " + testPresenceErrorCount);
			System.out.println("Test Presence Accuracy: "+(double) (4000 - testPresenceErrorCount)
					/ 40 + "%");
			br.close();
	}
}
