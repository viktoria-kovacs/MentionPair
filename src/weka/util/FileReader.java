package weka.util;

import static weka.Container.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import weka.domain.Word;

public class FileReader {

	public List<Word> read(File file) throws IOException {
		int wordIndex = 0;
		int sentenceIndex = 1;
		List<Word> words = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) {
					sentenceIndex++;
					continue;
				}

				String[] columns = line.split("\t");
				if ("_".equals(columns[config.idInSentenceIndex]) || "_".equals(columns[config.edgeInSentenceIndex])) {
					// nonexistent word
					continue;
				}

				wordIndex++;

				Word word = new Word(wordIndex, columns[config.textContentIndex]);
				word.setFileName(columns[0]);
				word.setSentenceIndex(sentenceIndex);
				word.setIdInSentence(Double.valueOf(columns[config.idInSentenceIndex]).intValue()); // KOPULA fix
				word.setWordClass(columns[config.wordClassIndex]);
				
				Integer edgeInSentence = Double.valueOf(columns[config.edgeInSentenceIndex]).intValue(); // KOPULA fix
				word.setEdge(edgeInSentence == 0 
						? 0 
						: (wordIndex - word.getIdInSentence() + edgeInSentence));
				
				if (!config.hasPronominalAnaphoraFile) {
					word.setPronominalAnaphoraMark(columns[columns.length - 1]);
				}

				String morphologicalAnalysisssssss = columns[config.morphologicalAnalysisIndex];
				if (!"_".equals(morphologicalAnalysisssssss) && !"null".equals(morphologicalAnalysisssssss)) {
					String[] morphologicalAnalysises = morphologicalAnalysisssssss.split("\\|");
					for (String morphologicalAnalysis : morphologicalAnalysises) {
						String[] morphologicalAnalysisParts = morphologicalAnalysis.split("\\=");
						if (morphologicalAnalysisParts.length != 2) {
							System.out.println("Something's wrong with morphologicalAnalysisParts");
						}
						word.addMorphologicalAnalysis(morphologicalAnalysisParts[0], morphologicalAnalysisParts[1]);
					}
				}

				word.setUnivDepPosTag(columns[config.univDepPosTagIndex]);
				word.setPosTag(columns[config.posTagIndex]);

				words.add(word);
			}
		}

		return words;
	}

}
