package weka.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Range;

import weka.domain.Word;

public class WordService {

	public List<Word> getWordsBySpan(Map<Integer, Word> wordsById, Range<Integer> span) {
		return wordsById.entrySet().stream()
				.filter(entry -> span.contains(entry.getKey()))
				.map(entry -> entry.getValue())
				.collect(Collectors.toList());
	}

	public String getTextContent(List<Word> words) {
		return words.stream()
				.map(Word::getTextContent)
				.collect(Collectors.joining(" "));
	}

}
