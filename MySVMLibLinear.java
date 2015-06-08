import java.util.*;
import java.io.*;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class MySVMLibLinear {
		public static void main(String[] args) throws IOException {
			FileInputStream fs = new FileInputStream(
					"/Users/premakolli/Downloads/Final_Yelp_Data.csv");
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			List<String> neglistOfFiles = new ArrayList<String>();
			for (int i = 0; i < 8000; ++i)
				neglistOfFiles.add(br.readLine());
			
			TreeMap<String, Integer> totalWordCount = new TreeMap<String, Integer>();
			List<TreeMap<String, Integer>> arrayHashMaps = new ArrayList<TreeMap<String, Integer>>();
			Feature[][] feature2DArray = new Feature[12000][];
			Feature[][] featurePresence2DArray = new Feature[12000][];
			Feature[][] testFeaturePresence2DArray = new Feature[4000][];
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
//						if (file.length() <= 2) continue;

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
						//if (file.length() <= 2) continue;
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
			// System.out.println(totalWordCount.size());
			// System.out.println(arrayHashMaps.size());

			//TreeMap<String, Integer> testTotalWordCount = new TreeMap<String, Integer>();
			Feature[][] testFeature2DArray = new Feature[4000][];
			j = 0;
			int testNegCount = -1;
			List<TreeMap<String, Integer>> testArrayHashMaps = new ArrayList<TreeMap<String, Integer>>();
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
			// System.out.println(testArrayHashMaps.size());

			Integer uniqueWordCount = totalWordCount.size();
			TreeMap<String, Integer> indexMap = new TreeMap<String, Integer>();
			int position = 1;
			for (String word : totalWordCount.keySet()) {
				indexMap.put(word, position);
				position++;
			}
			for (TreeMap<String, Integer> documentWordCount : arrayHashMaps) {
				Feature[] featureArray = new FeatureNode[documentWordCount
						.size()];
				Feature[] featurePresenceArray = new FeatureNode[documentWordCount
						.size()];
				int i = 0;
				for (Map.Entry<String, Integer> wordCountEntry : documentWordCount
						.entrySet()) {
					int index = indexMap.get(wordCountEntry.getKey());
					FeatureNode featureNode = new FeatureNode(index,
							wordCountEntry.getValue());
					FeatureNode featurePresenceNode = new FeatureNode(index, 1);
					featureArray[i] = featureNode;
					featurePresenceArray[i] = featurePresenceNode;
					i++;
				}

				feature2DArray[j] = featureArray;
				featurePresence2DArray[j] = featurePresenceArray;
				j++;
			}
			// System.out.println(feature2DArray.toString());

			double[] targetValues = new double[12000];
			for (int k = 0; k < 6000; k++) {
				targetValues[k] = 1;
			}
			for (int k = 6000; k < 12000; k++) {
				targetValues[k] = 0;
			}

			j = 0;
			for (TreeMap<String, Integer> documentWordCount : testArrayHashMaps) {
				Feature[] featureArray = new FeatureNode[documentWordCount
						.size()];
				Feature[] featurePresenceArray = new FeatureNode[documentWordCount
						.size()];
				int i = 0;
				for (Map.Entry<String, Integer> wordCountEntry : documentWordCount
						.entrySet()) {
					int index = indexMap.get(wordCountEntry.getKey());
					FeatureNode featureNode = new FeatureNode(index,
							wordCountEntry.getValue());
					FeatureNode featurePresenceNode = new FeatureNode(index, 1);
					featureArray[i] = featureNode;
					featurePresenceArray[i] = featurePresenceNode;
					i++;
				}
				testFeature2DArray[j] = featureArray;
				testFeaturePresence2DArray[j] = featurePresenceArray;
				j++;
			}
			// System.out.println(testFeature2DArray.toString());
			// System.out.println("In training");
			/*for (Feature[] f : feature2DArray){

				for (Feature F: f){
					if (F.getValue() == 0.0){
						for (Map.Entry<String, Integer> word : totalWordCount
						.entrySet()){
							if (word.getValue()==F.getIndex()){
								System.out.println(word.getKey() +" "+F.getValue());
								break;
							}
						}
					}	
						//System.out.println(F.getIndex()+" "+F.getValue());
				}
				
			}*/
			Problem problem = new Problem();
			problem.l = 12000;
			problem.n = uniqueWordCount; // number of features
			problem.x = feature2DArray;
			problem.y = targetValues;// target values
			problem.bias = 1;

			SolverType solver = SolverType.L2R_L2LOSS_SVC_DUAL; // -s 0
			double C = 1.0; // cost of constraints violation
			double eps = 0.01; // stopping criteria

			Parameter parameter = new Parameter(solver, C, eps);
			Model model = Linear.train(problem, parameter);
			File modelFile = new File("model");
			model.save(modelFile);
			// load model or use it directly
			model = Model.load(modelFile);
			// System.out.println("In prediction");
			int testErrorCount = 0;
			for (int k = 0; k < 2000; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 0) {
					// System.out.println("freq err no in neg"+ k);
					testErrorCount++;
				}
			}
			for (int k = 2000; k < 4000; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 1)
					testErrorCount++;
			}
		//	System.out.println("Test Error Count " + testErrorCount);
		//	System.out.println("Accuracy " + (double) (400 - testErrorCount)
		//			/ 4 + "%");
		//	sumfreq += (double) (400 - testErrorCount) / 4;

			Problem presenceproblem = new Problem();
			presenceproblem.l = 12000;
			presenceproblem.n = uniqueWordCount; // number of features
			presenceproblem.x = featurePresence2DArray;
			presenceproblem.y = targetValues;// target values

			// stopping criteria

			Parameter presenceparameter = new Parameter(solver, C, eps);
			Model presencemodel = Linear.train(presenceproblem,
					presenceparameter);
			File presencemodelFile = new File("model");
			presencemodel.save(presencemodelFile);
			// load model or use it directly
			presencemodel = Model.load(presencemodelFile);
			// System.out.println("In prediction");
			int testpresenceErrorCount = 0;
			for (int k = 0; k < 2000; k++) {
				Feature[] instance = testFeaturePresence2DArray[k];
				double prediction = Linear.predict(presencemodel, instance);
				if (prediction == 1) {
					// System.out.println("pres err no in neg"+ k);
					testpresenceErrorCount++;
				}
			}
			for (int k = 2000; k < 4000; k++) {
				Feature[] instance = testFeaturePresence2DArray[k];
				double prediction = Linear.predict(presencemodel, instance);
				if (prediction == 0)
					testpresenceErrorCount++;
			}
			System.out.println("Test Frequency Errors: " + testErrorCount);
			System.out.println("Test Frequency Accuracy: " + (double) (4000 - testErrorCount)
					/ 40 + "%");
			
		    System.out.println("TEST PRESENCE ERRORS " + testpresenceErrorCount);
			System.out.println("Test Presence Accuracy: "+(double) (4000 - testpresenceErrorCount)
					/ 40 + "%");
			br.close();
	}

}
