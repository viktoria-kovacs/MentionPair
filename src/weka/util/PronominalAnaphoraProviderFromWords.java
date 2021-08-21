package weka.util;

import static weka.Container.wordService;
import static weka.Container.wordToMarkableConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Range;

import weka.domain.Markable;
import weka.domain.Word;

public class PronominalAnaphoraProviderFromWords {

	private static final Pattern GROUP_PATTERN = Pattern.compile("(\\d+)");

	public Collection<List<Markable>> provide(Map<Integer, Word> allWords) {
		Collection<List<Markable>> pronominalAnaphoras = new ArrayList<>();
		
		/*
		Map<String, String> morphologicalAnalysis = pronoun.getMorphologicalAnalysis();
		if ("Sing".equals(morphologicalAnalysis.get("Number"))
				&& "1".equals(morphologicalAnalysis.get("Person"))) {
			// Single/1 is filtered
			continue;
		}
		if ("Plur".equals(morphologicalAnalysis.get("Number"))
				&& "1".equals(morphologicalAnalysis.get("Person"))) {
			// Plural/1 is filtered
			continue;
		}
		String pronType = morphologicalAnalysis.get("PronType");
		if (pronType != null && pronType.matches("Art|Ind|Int|Neg|Rcp|Tot")) {
			// pronType filter
			continue;
		}
		*/

		List<Word> words = new ArrayList<>(allWords.values());
//		List<Word> words = allWords.stream()
//				.filter(word -> "PRON".equals(word.getWordClass()))
//				.filter(word -> !word.getMorphologicalAnalysis().containsKey("Reflexive"))
//				.filter(word -> !"1".equals(word.getMorphologicalAnalysis().get("Person")))
//				.filter(word -> {
//					String pronType = word.getMorphologicalAnalysis().get("PronType");
//					return pronType == null || !pronType.matches("Art|Ind|Int|Neg|Rcp|Tot");
//				})
//				.collect(Collectors.toList());
//
//		words.addAll(
//				allWords.stream().filter(word -> !"PRON".equals(word.getWordClass())).collect(Collectors.toList()));

		Collections.sort(words, (w1, w2) -> Integer.compare(w1.getId(), w2.getId()));

		for (int index = words.size() - 1; index >= 0; index--) {
			Word currentWord = words.get(index);

			if (!shouldWeCare(currentWord)) {
				continue;
			}

			String pronominalAnaphoraMark = currentWord.getPronominalAnaphoraMark();
			if (currentWord.getMorphologicalAnalysis().get("PronType" ) != null 
					&& pronominalAnaphoraMark.startsWith("(") 
					&& pronominalAnaphoraMark.endsWith(")")) {
				Matcher groupMatcher = GROUP_PATTERN.matcher(pronominalAnaphoraMark);
				if (!groupMatcher.find()) {
					System.out.println("Something's wrong with groupMatcher on " + pronominalAnaphoraMark);
				}
				String group = groupMatcher.group();

				Markable head = wordToMarkableConverter.convert(currentWord);
				
				Range<Integer> footRange = null;

				for (int footIndex = index - 1; footIndex >= 0; footIndex--) {
					Word currentPossibleFootWord = words.get(footIndex);

					String[] possibleFootPronominalAnaphoraMarks = currentPossibleFootWord.getPronominalAnaphoraMark()
							.split("\\|");

					for (String possibleFootPronominalAnaphoraMark : possibleFootPronominalAnaphoraMarks) {
						if (!"_".equals(possibleFootPronominalAnaphoraMark)) {
							if (("(" + group + ")").equals(possibleFootPronominalAnaphoraMark)) { // (57)
								footRange = Range.singleton(currentPossibleFootWord.getId());
								break;
							} else if ((group + ")").equals(possibleFootPronominalAnaphoraMark)) { // 57)
								for (int footStartIndex = footIndex - 1; footStartIndex >= 0; footStartIndex--) {
									Word currentPossibleFootStartWord = words.get(footStartIndex);

									String[] possibleFootStartPronominalAnaphoraMarks = currentPossibleFootStartWord
											.getPronominalAnaphoraMark().split("\\|");

									for (String possibleFootStartPronominalAnaphoraMark : possibleFootStartPronominalAnaphoraMarks) {
										if (("(" + group).equals(possibleFootStartPronominalAnaphoraMark)) {
											footRange = Range.closed(currentPossibleFootStartWord.getId(),
													currentPossibleFootWord.getId());
											break;
										}
									}
									if (footRange != null) {
										break;
									}
									}
								break;
							}
						}
					}
					if (footRange != null) {
						break;
					}
				}

				if (footRange == null) {
					System.out.print("Something's wrong with footRange of " + wordService.getWordsBySpan(allWords, head.getSpan()));
					System.out.println();
				} else {
					Markable foot = new Markable();
					foot.setSpan(footRange);

					pronominalAnaphoras.add(Arrays.asList(foot, head));
				}
			}
		}

		return pronominalAnaphoras;
	}

	public boolean shouldWeCare(Word pronoun) {
				
		Map<String, String> morphologicalAnalysis = pronoun.getMorphologicalAnalysis();
		String pronType = morphologicalAnalysis.get("PronType");
		if (pronType != null && pronType.matches("Art|Ind|Int|Neg|v|Default|Rcp|Refl|Tot")) {
			// pronType filter
			return false;
		}

		return true;
	}

}
