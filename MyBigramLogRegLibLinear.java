

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class MyBigramLogRegLibLinear {
	private static Scanner reader;
	private static Scanner reader2;
	private static Scanner reader3;
	private static Scanner reader4;

	public static void main(String[] args) throws IOException {
		double sumfreq=0;
        double sumpresence=0;
        FileInputStream fs = new FileInputStream(
				"/Users/premakolli/Downloads/Final_Yelp_Data.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		List<String> neglistOfFiles = new ArrayList<String>();
		for (int i = 0; i < 8000; ++i)
			neglistOfFiles.add(br.readLine());
		
			Feature[][] feature2DArray = new Feature[12000][];
			Feature[][] featurePresence2DArray = new Feature[12000][];
			Feature[][] testFeaturePresence2DArray = new Feature[4000][];
			int j = 0;
			
			TreeMap<String, Integer> bigramTotalWordCount = new TreeMap<String, Integer>();
	
			List<TreeMap<String, Integer>> bigramArrayHashMaps = new ArrayList<TreeMap<String, Integer>>();

			int negCount = -1;
			for (String file : neglistOfFiles) {
				negCount++;
				TreeMap<String, Integer> documentWordCount = new TreeMap<String, Integer>();
				if (file != null) {
					Integer reviewNo = negCount;
					if (!(reviewNo >= 0 && reviewNo < 2000)) {
						file = file.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
//						if (file.length() <= 2) continue;

						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(file,
								operators, false);
						String previousWord = null;

						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
							
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(bigram))

									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
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
				TreeMap<String, Integer> documentWordCount = new TreeMap<String, Integer>();
				if (file != null) {
					Integer reviewNo = posCount;
					if (!(reviewNo >= 0 && reviewNo < 2000)) {
						file = file.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
						//if (file.length() <= 2) continue;
						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(file,
								operators, false);
						String previousWord = null;

						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(bigram))
									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
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
			//System.out.println(bigramTotalWordCount.size());
				Feature[][] testFeature2DArray = new Feature[4000][];
			j = 0;
			List<TreeMap<String, Integer>> bigramTestArrayHashMaps = new ArrayList<TreeMap<String, Integer>>();
			int testNegCount = -1;
			//List<TreeMap<String, Integer>> testArrayHashMaps = new ArrayList<TreeMap<String, Integer>>();
			for (String negfile : neglistOfFiles) {
				testNegCount++;
				TreeMap<String, Integer> testDocumentWordCount = new TreeMap<String, Integer>();
				if (negfile != null) {
					Integer reviewNo = testNegCount;
					if ((reviewNo >= 0 && reviewNo < 2000)) {
						negfile = negfile.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
						//if (negfile.length() <= 2) continue;

						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(negfile,
								operators, false);
						String previousWord = null;

						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
							if (previousWord != null) {
								String bigram = previousWord + " " + word;
								if (bigramTotalWordCount.containsKey(bigram))
									bigramTotalWordCount.put(bigram,
											bigramTotalWordCount.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
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
				TreeMap<String, Integer> testDocumentWordCount = new TreeMap<String, Integer>();
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

								
								if (bigramTotalWordCount.containsKey(bigram))
									bigramTotalWordCount.put(bigram,
											bigramTotalWordCount.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
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
			
			Integer uniqueWordCount = bigramTotalWordCount.size();
			TreeMap<String, Integer> indexMap = new TreeMap<String, Integer>();
			int position = 1;
			for (String word : bigramTotalWordCount.keySet()) {
				indexMap.put(word, position);
				position++;
			}
			for (TreeMap<String, Integer> documentWordCount : bigramArrayHashMaps) {
				Feature[] featureArray = new FeatureNode[documentWordCount
						.size()];
				Feature[] featurePresenceArray = new FeatureNode[documentWordCount
				                 						.size()];
				int i = 0;
				for (Map.Entry<String, Integer> wordCountEntry : documentWordCount
						.entrySet()) {
					int index = indexMap.get(wordCountEntry.getKey());
					FeatureNode featureNode = new FeatureNode(index,
							wordCountEntry.getValue().doubleValue());
					FeatureNode featurePresenceNode = new FeatureNode(index,
							1);
					featureArray[i] = featureNode;
					featurePresenceArray[i]=featurePresenceNode;
					i++;
				}

				feature2DArray[j] = featureArray;
				featurePresence2DArray[j]=featurePresenceArray;
				j++;
			}
		//	System.out.println(feature2DArray.toString());
			
			double[] targetValues = new double[12000];
			for (int k = 0; k < 6000; k++) {
				targetValues[k] = 0;
			}
			for (int k = 6000; k < 12000; k++) {
				targetValues[k] = 1;
			}

			j = 0;
			for (TreeMap<String, Integer> documentWordCount : bigramTestArrayHashMaps) {
				Feature[] featureArray = new FeatureNode[documentWordCount
						.size()];
				Feature[] featurePresenceArray = new FeatureNode[documentWordCount
							                 						.size()];
				int i = 0;
				for (Map.Entry<String, Integer> wordCountEntry : documentWordCount
						.entrySet()) {
					int index = indexMap.get(wordCountEntry.getKey());
					FeatureNode featureNode = new FeatureNode(index,
							wordCountEntry.getValue().doubleValue());
					FeatureNode featurePresenceNode = new FeatureNode(index,
							1);
					featureArray[i] = featureNode;
					featurePresenceArray[i]=featurePresenceNode;
					i++;
				}
				testFeature2DArray[j] = featureArray;
				testFeaturePresence2DArray[j]=featurePresenceArray;
				j++;
			}
			
			Problem problem = new Problem();
			problem.l = 12000;
			problem.n = uniqueWordCount; // number of features
			problem.x = feature2DArray;
			problem.y = targetValues;// target values
			problem.bias=1;

			SolverType solver = SolverType.L2R_LR; // -s 0
			double C = 1.0; // cost of constraints violation
			double eps = 0.01; // stopping criteria

			Parameter parameter = new Parameter(solver, C, eps);
			Model model = Linear.train(problem, parameter);
			File modelFile = new File("model");
			model.save(modelFile);
			// load model or use it directly
			model = Model.load(modelFile);
			int testErrorCount = 0;
			for (int k = 0; k < 2000; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 1){
			
					testErrorCount++;
				}
			}
			for (int k = 2000; k < 4000; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 0)
					testErrorCount++;
			}
			
		
			
		
			Problem presenceproblem = new Problem();
			presenceproblem.l = 12000;
			presenceproblem.n = uniqueWordCount; // number of features
			presenceproblem.x = featurePresence2DArray;
			presenceproblem.y = targetValues;// target values

			Parameter presenceparameter = new Parameter(solver, C, eps);
			Model presencemodel = Linear.train(presenceproblem, presenceparameter);
			File presencemodelFile = new File("model");
			presencemodel.save(presencemodelFile);
			// load model or use it directly
			presencemodel = Model.load(presencemodelFile);
			int testpresenceErrorCount = 0;
			for (int k = 0; k < 2000; k++) {
				Feature[] instance = testFeaturePresence2DArray[k];
				double prediction = Linear.predict(presencemodel, instance);
				if (prediction == 1){
				
					testpresenceErrorCount++;
				}
			}
			for (int k = 2000; k < 4000; k++) {
				Feature[] instance = testFeaturePresence2DArray[k];
				double prediction = Linear.predict(presencemodel, instance);
				if (prediction == 0)
					testpresenceErrorCount++;
			}
			//System.out.println("Test Frequency Errors: " + testErrorCount);
			System.out.println("Test Frequency Accuracy: " + (double) (4000 - testErrorCount)
					/ 40 + "%");
			
		//	System.out.println("TEST PRESENCE ERRORS " + testPresenceErrorCount);
			System.out.println("Test Presence Accuracy: "+(double) (4000 - testpresenceErrorCount)
					/ 40 + "%");
			br.close();
	}
	
}
