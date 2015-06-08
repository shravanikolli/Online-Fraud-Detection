import java.util.*;
import java.io.*;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class MyRLLogRegLibLinear {
		public static void main(String[] args) throws IOException {
			FileInputStream fs = new FileInputStream(
					"/Users/premakolli/Downloads/Final_Yelp_Data.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			List<String> neglistOfFiles = new ArrayList<String>();
			for (int i = 0; i < 8000; ++i)
				neglistOfFiles.add(br.readLine());
			
			TreeMap<String, Integer> totalWordCount = new TreeMap<String, Integer>();
			List<TreeMap<String, Integer>> arrayHashMaps = new ArrayList<TreeMap<String, Integer>>();
			List<Integer> aHashMaps = new ArrayList<Integer>();

			Feature[][] feature2DArray = new Feature[12000][];
			int j = 0;
			int negCount = -1;
			for (String file : neglistOfFiles) {
				negCount++;
				TreeMap<String, Integer> documentWordCount = new TreeMap<String, Integer>();
				if (file != null) {
					
					Integer reviewNo = negCount;
					if (!(reviewNo >= 0 && reviewNo < 2000)) {
						file = file.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
						aHashMaps.add(file.length());
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
						aHashMaps.add(file.length());

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

			Feature[][] testFeature2DArray = new Feature[4000][];
			j = 0;
			int testNegCount = -1;
			List<TreeMap<String, Integer>> testArrayHashMaps = new ArrayList<TreeMap<String, Integer>>();
			List<Integer> tHashMaps = new ArrayList<Integer>();

			for (String negfile : neglistOfFiles) {
				testNegCount++;
				TreeMap<String, Integer> testDocumentWordCount = new TreeMap<String, Integer>();
				if (negfile != null) {
					Integer reviewNo = testNegCount;
					if ((reviewNo >= 0 && reviewNo < 2000)) {
						negfile = negfile.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
						tHashMaps.add(negfile.length());
						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(negfile,
								operators, false);
						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
							if (totalWordCount.containsKey(word))
								totalWordCount.put(word,
										totalWordCount.get(word) + 1);
							else {
								totalWordCount.put(word, 1);
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
				TreeMap<String, Integer> testDocumentWordCount = new TreeMap<String, Integer>();
				if (posfile != null) {
					Integer reviewNo = testPosCount;
					if ((reviewNo >= 0 && reviewNo < 2000)) {
						posfile = posfile.replaceAll("[^A-Za-z ]", " ")
								.replaceAll("\\s+", " ").toLowerCase();
						tHashMaps.add(posfile.length());

						String operators = " ";
						StringTokenizer tokens = new StringTokenizer(posfile,
								operators, false);
						while (tokens.hasMoreTokens()) {
							String word = tokens.nextToken();
													if (totalWordCount.containsKey(word))
								totalWordCount.put(word,
										totalWordCount.get(word) + 1);
							else {
								totalWordCount.put(word, 1);
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

		/*	Integer uniqueWordCount = totalWordCount.size();
			TreeMap<String, Integer> indexMap = new TreeMap<String, Integer>();
			int position = 1;
			for (String word : totalWordCount.keySet()) {
				indexMap.put(word, position);
				position++;
			}
			*/
			j = 0;
			for (Integer rl : aHashMaps) {
				Feature[] featureArray = new FeatureNode[1];
					FeatureNode featureNode = new FeatureNode(1,
							rl);
					featureArray[0] = featureNode;
				

				feature2DArray[j] = featureArray;
				j++;
			}

			double[] targetValues = new double[12000];
			for (int k = 0; k < 6000; k++) {
				targetValues[k] = 1;
			}
			for (int k = 6000; k < 12000; k++) {
				targetValues[k] = 0;
			}

			j = 0;
			for (Integer rl : tHashMaps) {
				Feature[] featureArray = new FeatureNode[1];
					FeatureNode featureNode = new FeatureNode(1,
							rl);
					featureArray[0] = featureNode;
				

					testFeature2DArray[j] = featureArray;
				j++;
			}
			
				Problem problem = new Problem();
			problem.l = 12000;
			problem.n = 1; // number of features
			problem.x = feature2DArray;
			problem.y = targetValues;// target values
			problem.bias = 1;

			SolverType solver = SolverType.L1R_LR; // -s 0
			double C = 1.0; // cost of constraints violation
			double eps = 0.01; // stopping criteria

			Parameter parameter = new Parameter(solver, C, eps);
			Model model = Linear.train(problem, parameter);
			File modelFile = new File("model");
			model.save(modelFile);
			model = Model.load(modelFile);
			int testErrorCount = 0;
			for (int k = 0; k < 2000; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 0) {
					testErrorCount++;
				}
			}
			for (int k = 2000; k < 4000; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 1)
					testErrorCount++;
			}
		
					System.out.println("Test Frequency Errors: " + testErrorCount);
			System.out.println("Test Frequency Accuracy: " + (double) (4000 - testErrorCount)
					/ 40 + "%");
			
			br.close();
	}

}
