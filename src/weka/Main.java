package weka;

import static weka.Container.config;
import static weka.Container.resultWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weka.domain.Config;
import weka.domain.Result;

public class Main {

	public static void main(String[] args) throws Exception {
		config = Config.modifiedSzegedCorpus2;
		config.includeCp = false;
		config.threePointMultiplier = 3;
		config.postfix = "sample";

		File rootFolder = new File("resources/sample");

//		runAsExperiments(rootFolder);
		runTest(rootFolder);
		runTrain(rootFolder);
//		runExperimentsAsTrain(rootFolder);
//		runExperimentsAsTest(rootFolder);

		System.out.println("DONE.");
	}

	private static void runAsExperiments(File rootFolder) throws Exception {
		File[] experimentFolders = rootFolder.listFiles(file -> file.getName().matches("\\d+"));
		
		List<Result> tests = new ArrayList<>();
		List<Result> trains = new ArrayList<>();
		
		for (File experimentFolder : experimentFolders) {
			Run testRun = new Run(experimentFolder.getAbsolutePath(), true);
			Result testResult = testRun.run();
			tests.add(testResult);
			
			Run trainRun = new Run(experimentFolder.getAbsolutePath(), false);
			Result trainResult = trainRun.run();
			trains.add(trainResult);
		}
		
		for (int test = 0; test < experimentFolders.length; test++) {
			Result testResult = tests.get(test);
			resultWriter.write(testResult);
			
			Result trainResult = new Result(testResult.getFolder(), false);
			
			for (int train = 0; train < experimentFolders.length; train++) {
				if (test == train) {
					continue;
				}
				Result currentTrainResult = trains.get(train);
				trainResult.getLines().addAll(currentTrainResult.getLines());
			}
			resultWriter.write(trainResult);
		}
		
//		for (File testFolder : experimentFolders) {
//			Run testRun = new Run(testFolder.getAbsolutePath(), true);
//			Result testResult = testRun.run();
//
//			resultWriter.write(testResult);
//
//			Result trainResult = new Result(testFolder, false);
//
//			for (File experimentFolder : experimentFolders) {
//				if (testFolder.equals(experimentFolder)) {
//					continue;
//				}
//
//				Run experimentRun = new Run(experimentFolder.getAbsolutePath(), false);
//				Result experimentResult = experimentRun.run();
//				trainResult.getLines().addAll(experimentResult.getLines());
//			}
//
//			resultWriter.write(trainResult);
//		}
	}

	private static void runExperimentsAsTest(File rootFolder) throws Exception {
		Result result = new Result(rootFolder, true);

		File[] experimentFolders = rootFolder.listFiles();
		for (File experimentFolder : experimentFolders) {
			Run experimentRun = new Run(experimentFolder.getAbsolutePath(), true);
			Result experimentResult = experimentRun.run();
			result.getLines().addAll(experimentResult.getLines());
		}

		resultWriter.write(result);
	}

	private static void runExperimentsAsTrain(File rootFolder) throws Exception {
		Result result = new Result(rootFolder, false);

		File[] experimentFolders = rootFolder.listFiles();
		for (File experimentFolder : experimentFolders) {
			Run experimentRun = new Run(experimentFolder.getAbsolutePath(), false);
			Result experimentResult = experimentRun.run();
			result.getLines().addAll(experimentResult.getLines());
		}

		resultWriter.write(result);
	}

	private static void runTest(File rootFolder) throws Exception {
		Result result = new Run(rootFolder.getAbsolutePath(), true).run();
		resultWriter.write(result);
	}

	private static void runTrain(File rootFolder) throws Exception {
		Result result = new Run(rootFolder.getAbsolutePath(), false).run();
		resultWriter.write(result);
	}

}
