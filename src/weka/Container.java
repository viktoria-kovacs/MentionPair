package weka;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import weka.domain.Config;
import weka.util.*;
import weka.util.converter.ElementToMarkableConverter;
import weka.util.converter.ElementToPronominalAnaphoraConverter;
import weka.util.converter.ElementToWordConverter;
import weka.util.converter.WordToMarkableConverter;

public class Container {

	public static Config config;

	public static FileSetProvider convXmlFileProvider;
	public static DocumentBuilder documentBuilder;
	public static WordFileReader wordFileReader;
	public static PronominalAnaphoraProviderFromWords pronominalAnaphoraProviderFromWords;
	public static PronominalAnaphoraProviderFromFile pronominalAnaphoraProviderFromFile;
	public static ElementToPronominalAnaphoraConverter elementToPronominalAnaphoraConverter;
	public static WordToMarkableConverter wordToMarkableConverter;
	public static MarkableProvider markableProvider;
	public static ElementToMarkableConverter elementToMarkableConverter;
	public static MarkableService markableService;
	public static ElementToWordConverter elementToWordConverter;
	public static WordService wordService;
	public static FileReader fileReader;
	public static ResultWriter2 resultWriter;

	static {
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void initialize() throws Exception {
		initializeLogging();

		convXmlFileProvider = new FileSetProvider();
		documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		wordFileReader = new WordFileReader();
		pronominalAnaphoraProviderFromWords = new PronominalAnaphoraProviderFromWords();
		pronominalAnaphoraProviderFromFile = new PronominalAnaphoraProviderFromFile();
		elementToPronominalAnaphoraConverter = new ElementToPronominalAnaphoraConverter();
		wordToMarkableConverter = new WordToMarkableConverter();
		markableProvider = new MarkableProvider();
		elementToMarkableConverter = new ElementToMarkableConverter();
		markableService = new MarkableService();
		elementToWordConverter = new ElementToWordConverter();
		wordService = new WordService();
		fileReader = new FileReader();
		resultWriter = new ResultWriter2();
	}

	private static void initializeLogging() {
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.FINEST);
	}

}
