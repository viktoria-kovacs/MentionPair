package weka.util;

import com.google.common.collect.Lists;
import weka.domain.Result;
import weka.domain.ResultLine;
import weka.domain.Word;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static weka.Container.wordService;

public class ResultWriter2 {

	private static List<String> morphologicalAnalysisColumns = Lists.newArrayList("Case", "Number", "Person");
	private static List<String> filter = Arrays.asList(",", ".", "(", ")", "?", "!");

	private String separator;

	public void write(Result result) throws IOException {
		write(result, false);
		write(result, true);
	}

	public void write(Result result, boolean debug) throws IOException {
		String fileName = result.getResultFileName(debug);
		
		System.out.println("Writing file " + fileName);

		PrintWriter writer = new PrintWriter(new File(fileName), "UTF-8");
		this.separator = debug ? "\t" : ",";

		if (debug) {
			writer.print("FileName");
			writer.print(separator);
			writer.print("Pronoun");
			writer.print(separator);
			writer.print("Phrase");
			writer.print(separator);
			writer.print("Word distance");
			writer.print(separator);
			writer.print("CP distance 2");
			writer.print(separator);			
			writer.print("CP distance");
			writer.print(separator);						
			writer.print("NP distance");
			writer.print(separator);					
		//	writer.print("PronType");
		//	writer.print(separator);
			writer.print(getMorphologicalAnalysisColumnHeaders());
			writer.print(separator);
		//	writer.print("PhraseNom");
		//	writer.print(separator);
			writer.print("PhraseSubj");
			writer.print(separator);
			writer.print("PronSubj");
			writer.print(separator);
			writer.print("AgrSubj");
			writer.print(separator);
			writer.print("PhraseObj");
			writer.print(separator);
			writer.print("PronObj");
			writer.print(separator);
			writer.print("AgrObj");
			writer.print(separator);
			writer.print("Phrase PosTag");
			writer.print(separator);			
			writer.print("Pronoun PosTag");
			writer.print(separator);			
			writer.print("Same PosTag");
			writer.print(separator);			
			writer.print("PhrasePropername");
			writer.print(separator);
			writer.print("Pronoun phrase");
			writer.print(separator);
			writer.print("Definite");
			writer.print(separator);						
			writer.print("Phrase word number");
			writer.print(separator);
			writer.print("Min word number");
			writer.print(separator);
			writer.print("Max word number");
			writer.print(separator);
			writer.print("Phase word number 3+");
			writer.print(separator);
			writer.print("FirstNp");
			writer.print(separator);
			writer.print("SecondNp");
			writer.print(separator);
			writer.print("Anaphora");
			writer.println();
		}

		for (ResultLine resultLine : result.getLines()) {
			
			boolean isMin = getMin(result, resultLine).equals(resultLine);
			boolean isMax = getMax(result, resultLine).equals(resultLine);
			
			String phraseTextContent = wordService.getTextContent(resultLine.getPhrase());
			if (debug) {
				writer.print(resultLine.getFileName());
				writer.print(separator);
				writer.print(resultLine.getPronoun().getTextContent());
				writer.print(separator);
				writer.print(phraseTextContent);
				writer.print(separator);
			}

			writer.print(resultLine.getPronoun().getId()
					- resultLine.getPhrase().get(resultLine.getPhrase().size() - 1).getId());
			writer.print(separator);
			writer.print(resultLine.getCpDistance2());
			writer.print(separator);			
			writer.print(resultLine.getCpDistance());
			writer.print(separator);						
			writer.print(resultLine.getNpDistance());
			writer.print(separator);
					
		//	writer.print(resultLine.getPronoun().getMorphologicalAnalysis().getOrDefault("PronType", "?"));
		//	writer.print(separator);
			writer.print(getMorphologicalAnalysisColumnValues(resultLine));
			writer.print(separator);
		//	writer.print(isNomCase(resultLine));
		//	writer.print(separator);
			String phraseUnivDepPosTag = resultLine.getUnivDepPosTag();
			boolean phraseSubj = phraseUnivDepPosTag.equals("SUBJ");
			writer.print(phraseSubj ? "1" : "0");
			writer.print(separator);
			String pronUnivDepPosTag = resultLine.getPronoun().getUnivDepPosTag();
			boolean pronSubj = pronUnivDepPosTag.equals("SUBJ");
			writer.print(pronSubj ? "1" : "0");
			writer.print(separator);
			writer.print((phraseSubj && pronSubj) ? "1" : "0");
			writer.print(separator);
			boolean phraseObj = phraseUnivDepPosTag.equals("OBJ");
			writer.print(phraseObj ? "1" : "0");
			writer.print(separator);
			boolean pronObj = pronUnivDepPosTag.equals("OBJ");
			writer.print(pronObj ? "1" : "0");
			writer.print(separator);
			writer.print((phraseObj && pronObj) ? "1" : "0");
			writer.print(separator);
			writer.print(resultLine.getMainPhrase().getWordClass());			
			writer.print(separator);
			writer.print(resultLine.getPronoun().getWordClass());			
			writer.print(separator);
			writer.print(
					Objects.equals(resultLine.getPronoun().getWordClass(), resultLine.getMainPhrase().getWordClass()) 
					? "1" 
					: "0");			
			writer.print(separator);
			writer.print(resultLine.getPronp());
			writer.print(separator);			
			writer.print(resultLine.isPronounPhrase() ? "1" : "0");
			writer.print(separator);
			writer.print(resultLine.getPhrase().get(0).getMorphologicalAnalysis().getOrDefault("Definite", "?"));
			writer.print(separator);			
			writer.print(resultLine.getWordNumber());
			writer.print(separator);
			writer.print(isMin ? "1" : "0");
			writer.print(separator);	
			writer.print(isMax ? "1" : "0");
			writer.print(separator);
			writer.print(resultLine.getPhrase().size() >= 3 ? "1" : "0");
			writer.print(separator);
			writer.print(resultLine.getIndexOfNpInLeafCp() == 1 ? "1" : "0");
			writer.print(separator);
			writer.print(resultLine.getIndexOfNpInLeafCp() == 2 ? "1" : "0");
			writer.print(separator);			
			writer.print(resultLine.isAnaphora() ? "1" : "0");
			writer.println();
		}

		writer.close();
	}

	private ResultLine getMin(Result result, ResultLine resultLine) {
		ResultLine min = null;
		
		for (ResultLine rl : result.getLines()) {
			if (rl.getPronoun().getId() != resultLine.getPronoun().getId()) {
				continue;
			}
			if (min == null) {
				min = rl;
			} else if (rl.getWordNumber() < min.getWordNumber()) {
				min = rl;
			} else if (rl.getWordNumber() == min.getWordNumber() && rl.getNpDistance() < min.getNpDistance() ) {
				min = rl;
			}
		}
		return min;
	}

	private ResultLine getMax(Result result, ResultLine resultLine) {
		ResultLine max = null;
		
		for (ResultLine rl : result.getLines()) {
			if (rl.getPronoun().getId() != resultLine.getPronoun().getId()) {
				continue;
			}
			if (max == null) {
				max = rl;
			} else if (rl.getWordNumber() > max.getWordNumber()) {
				max = rl;
			} else if (rl.getWordNumber() == max.getWordNumber() && rl.getNpDistance() < max.getNpDistance() ) {
				max = rl;
			}
		}
		return max;
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
