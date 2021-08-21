package weka.domain;

import java.io.FilenameFilter;

public class Config {

	public static final Config ownCorpus = new Config();
	static {
		ownCorpus.hasPronominalAnaphoraFile = true;
		ownCorpus.textContentIndex = 1;
		ownCorpus.idInSentenceIndex = 0;
		ownCorpus.wordClassIndex = 3;
		ownCorpus.edgeInSentenceIndex = 5;
		ownCorpus.morphologicalAnalysisIndex = 4;
		ownCorpus.pathPrefix = "";
		ownCorpus.fileSetNamePostfix = ".txt$";
		ownCorpus.filenameFilter = (directory, fileName) -> fileName.endsWith(".txt");
	}

	public static final Config szegedCorpus = new Config();
	static {
		szegedCorpus.hasPronominalAnaphoraFile = false;
		szegedCorpus.textContentIndex = 2;
		szegedCorpus.idInSentenceIndex = 1;
		szegedCorpus.wordClassIndex = 5;
		szegedCorpus.edgeInSentenceIndex = 9;
		szegedCorpus.morphologicalAnalysisIndex = 7;
		szegedCorpus.pathPrefix = "mmax/";
		szegedCorpus.fileSetNamePostfix = "";
		szegedCorpus.filenameFilter = (directory, fileName) -> fileName.matches(".*merge_\\d+$");
	}

	public static final Config modifiedSzegedCorpus = new Config();
	static {
		modifiedSzegedCorpus.hasPronominalAnaphoraFile = false;
		modifiedSzegedCorpus.textContentIndex = 2;
		modifiedSzegedCorpus.idInSentenceIndex = 1;
		modifiedSzegedCorpus.wordClassIndex = 4;
		modifiedSzegedCorpus.edgeInSentenceIndex = 6;
		modifiedSzegedCorpus.univDepPosTagIndex = 7;
		modifiedSzegedCorpus.posTagIndex = 4;
		modifiedSzegedCorpus.morphologicalAnalysisIndex = 5;
		modifiedSzegedCorpus.pathPrefix = "mmax/";
		modifiedSzegedCorpus.fileSetNamePostfix = "";
//		bizeraltSzegedCorpus.filenameFilter = (directory, fileName) -> fileName.matches(".*merge_\\d+$") ;
		modifiedSzegedCorpus.filenameFilter = (directory, fileName) -> fileName.matches(".*xtsv");
	}

	public static final Config modifiedSzegedCorpus2 = new Config();
	static {
		modifiedSzegedCorpus2.hasPronominalAnaphoraFile = false;
		modifiedSzegedCorpus2.textContentIndex = 1;
		modifiedSzegedCorpus2.idInSentenceIndex = 0;
		modifiedSzegedCorpus2.wordClassIndex = 3;
		modifiedSzegedCorpus2.edgeInSentenceIndex = 5;
		modifiedSzegedCorpus2.univDepPosTagIndex = 6;
		modifiedSzegedCorpus2.posTagIndex = 3;
		modifiedSzegedCorpus2.morphologicalAnalysisIndex = 4;
		modifiedSzegedCorpus2.pathPrefix = "mmax/";
		modifiedSzegedCorpus2.fileSetNamePostfix = "";
//		bizeraltSzegedCorpus.filenameFilter = (directory, fileName) -> fileName.matches(".*merge_\\d+$") ;
		modifiedSzegedCorpus2.filenameFilter = (directory, fileName) -> fileName.matches(".*txt");
	}

	public boolean includeCp;
	public String postfix;

	public boolean hasPronominalAnaphoraFile;
	public int textContentIndex;
	public int idInSentenceIndex;
	public int wordClassIndex;
	public int edgeInSentenceIndex;
	public int morphologicalAnalysisIndex;
	public int univDepPosTagIndex = 11;
	public int posTagIndex = 12;
	public String pathPrefix;
	public String fileSetNamePostfix;
	public FilenameFilter filenameFilter;
	public int threePointMultiplier;

}
