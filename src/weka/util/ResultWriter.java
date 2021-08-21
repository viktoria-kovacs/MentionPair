package weka.util;

import static weka.Container.wordService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import weka.domain.Result;
import weka.domain.ResultLine;
import weka.domain.Word;

public class ResultWriter {

	private static List<String> morphologicalAnalysisColumns = Lists.newArrayList("Case", "Number", "Person");
	private static List<String> filter = Arrays.asList(",", ".", "(", ")", "?", "!");

	private String separator;

	public void write(Result result) throws IOException {
		write(result, false);
		write(result, true);
	}

	public void write(Result result, boolean debug) throws IOException {
		String fileName = result.getResultFileName(debug);

		PrintWriter writer = new PrintWriter(new File(fileName), "UTF-8");
		this.separator = debug ? "\t" : ",";

		if (debug) {
			writer.print("Pronoun");
			writer.print(separator);
			writer.print("Phrase");
			writer.print(separator);
			writer.print("Phrase word number");
			writer.print(separator);
			// writer.print("Phrase character number");
			// writer.print("\t");
			writer.print("CP distance");
			writer.print(separator);
			writer.print("NP distance");
			writer.print(separator);
			writer.print("Phrase Type");
			writer.print(separator);
			writer.print("PronType");
			writer.print(separator);
			writer.print(getMorphologicalAnalysisColumnHeaders());
			writer.print(separator);
			writer.print("PhraseNom");
			writer.print(separator);
			writer.print("Pronp");
			writer.print(separator);
			writer.print("Definite");
			writer.print(separator);
			writer.print("Phase word number 3+");
			writer.print(separator);
			writer.print("Phrase word class");
			writer.print(separator);
			writer.print("Word distance");
			writer.print(separator);
			writer.print("Pronoun phrase");
			writer.print(separator);
//			writer.print("Anaphora chain head");
//			writer.print(separator);
			writer.print("CP distance 2");
			writer.print(separator);
			writer.print("CP distance group");
			writer.print(separator);
			writer.print("univDepPosTag");
			writer.print(separator);
			writer.print("posTag");
			writer.print(separator);
			writer.print("Anaphora");
			writer.println();
		}

		for (ResultLine resultLine : result.getLines()) {
			String phraseTextContent = wordService.getTextContent(resultLine.getPhrase());
			if (debug) {
				writer.print(resultLine.getPronoun().getTextContent());
				writer.print(separator);
				writer.print(phraseTextContent);
				writer.print(separator);
			}
			
			long wordNumber = resultLine.getPhrase().stream()
					.filter(word -> !filter.contains(word.getTextContent()))
					.count();
			writer.print(wordNumber);
			// writer.print("\t");
			// writer.print(phraseTextContent.length());
			writer.print(separator);
			writer.print(resultLine.getCpDistance());
			writer.print(separator);
			writer.print(resultLine.getNpDistance());
			writer.print(separator);
			writer.print(resultLine.getPhraseType());
			writer.print(separator);
			writer.print(resultLine.getPronoun().getMorphologicalAnalysis().getOrDefault("PronType", "?"));
			writer.print(separator);
			writer.print(getMorphologicalAnalysisColumnValues(resultLine));
			writer.print(separator);
			writer.print(isNomCase(resultLine));
			writer.print(separator);
			writer.print(resultLine.getPronp());
			writer.print(separator);
			writer.print(resultLine.getPhrase().get(0).getMorphologicalAnalysis().getOrDefault("Definite", "?"));
			writer.print(separator);
			writer.print(resultLine.getPhrase().size() >= 3 ? "1" : "0");
			writer.print(separator);
			writer.print(resultLine.getMainPhrase().getWordClass());
			writer.print(separator);
			writer.print(resultLine.getPronoun().getId()
					- resultLine.getPhrase().get(resultLine.getPhrase().size() - 1).getId());
			writer.print(separator);
			writer.print(resultLine.isPronounPhrase() ? "1" : "0");
//			writer.print(separator);
//			String anaphoraChainHead = resultLine.getPronominalAnaphoraChainHead() != null
//					? resultLine.getPronominalAnaphoraChainHead()
//					: "-";
//			writer.print(anaphoraChainHead);
			writer.print(separator);
			writer.print(resultLine.getCpDistance2());
			writer.print(separator);
			writer.print(getCpDistanceGroup(resultLine.getCpDistance()));
			writer.print(separator);
			writer.print(resultLine.getUnivDepPosTag());
			writer.print(separator);
			writer.print(resultLine.getPosTag());
			writer.print(separator);
			writer.print(resultLine.isAnaphora() ? "1" : "0");
			writer.println();
		}

		writer.close();
	}

	private String isNomCase(ResultLine resultLine) {
		String casee = resultLine.getMainPhrase().getMorphologicalAnalysis().get("Case");
		return "Nom".equals(casee) ? "1" : "0";
	}

	private String getMorphologicalAnalysisColumnHeaders() {
		return morphologicalAnalysisColumns.stream()
				.map(morphologicalAnalysisColumn -> "MA/PRONOUN/" + morphologicalAnalysisColumn + separator + "MA/PHRASE/"
						+ morphologicalAnalysisColumn + separator + "MA/SAME/" + morphologicalAnalysisColumn)
				.collect(Collectors.joining(separator));
	}

	private String getMorphologicalAnalysisColumnValues(ResultLine resultLine) {
		Map<String, String> pronounMorphologicalAnalysis = resultLine.getPronoun().getMorphologicalAnalysis();
		Word mainPhrase = resultLine.getPhraseType().equals("NP") ? resultLine.getMainPhrase() : null;
		Map<String, String> phraseMorphologicalAnalysis = mainPhrase != null ? mainPhrase.getMorphologicalAnalysis()
				: new HashMap<>();

		return morphologicalAnalysisColumns.stream().map(morphologicalAnalysisColumn -> {
			String pronounValue = pronounMorphologicalAnalysis.getOrDefault(morphologicalAnalysisColumn, "?");
			String phraseValue = phraseMorphologicalAnalysis.getOrDefault(morphologicalAnalysisColumn, "?");
			String equals = ("?".equals(pronounValue) || "?".equals(phraseValue)) 
					? "?"
					: (pronounValue.equals(phraseValue) ? "1" : "0");
			return pronounValue + separator + phraseValue + separator + equals;
		}).collect(Collectors.joining(separator));
	}

	private long getCpDistanceGroup(long cpDistance) {
		if (cpDistance < 2) {
			return 1;
		} else if (cpDistance < 8) {
			return 2;
		} else {
			return 3;
		}
	}

}
