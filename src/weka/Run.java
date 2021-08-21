package weka;

import static weka.Container.config;
import static weka.Container.convXmlFileProvider;
import static weka.Container.fileReader;
import static weka.Container.markableProvider;
import static weka.Container.markableService;
import static weka.Container.pronominalAnaphoraProviderFromFile;
import static weka.Container.pronominalAnaphoraProviderFromWords;
import static weka.Container.wordService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Range;

import weka.domain.FileSet;
import weka.domain.Markable;
import weka.domain.Result;
import weka.domain.ResultLine;
import weka.domain.Word;

public class Run {

	private static final int NO_ANAPHORA_CP_LIMIT = 2;
	private static final int NO_ANAPHORA_NP_LIMIT = 5;
	private static List<String> filter = Arrays.asList(",", ".", "(", ")", "?", "!");
	private static List<String> ignorePhrases = 
			Arrays.asList("és", "is", "csak", "még", "mégis", "de", "e", "hát", "kb.", "pl.", "hogy", "már");

	private static Logger logger = Logger.getLogger(Run.class.getName());

	private String folderPath;
	private boolean test;

	public Run(String folderPath, boolean test) {
		this.folderPath = folderPath;
		this.test = test;
	}

	public Result run() throws Exception {
		File folder = new File(folderPath);
		Result result = new Result(folder, test);

		byte[] a = { -17, -69, -65, 65 }; // workaround for String value [, A]

		for (final FileSet fileSet : convXmlFileProvider.provide(folder)) {
			logger.info("Reading file set: " + fileSet);

			List<Word> allWords = fileReader.read(fileSet.getParsedFile());
			List<Word> pronouns = allWords.stream()
					.filter(word -> word.getMorphologicalAnalysis().containsKey("PronType"))
					.collect(Collectors.toList());
			logger.info(pronouns.toString());

			// Map<Integer, Word> words = wordFileReader.readById(fileSet.getWordFile());
			Map<Integer, Word> words = allWords.stream().collect(Collectors.toMap(Word::getId, word -> word));
			logger.info(words.toString());

			Collection<List<Markable>> pronominalAnaphoras = config.hasPronominalAnaphoraFile
					? pronominalAnaphoraProviderFromFile.provide(fileSet.getPronominalAnaphoraFile())
					: pronominalAnaphoraProviderFromWords.provide(words);
			logger.info(pronominalAnaphoras.toString());
			
//			pronominalAnaphoras.stream()
//					.forEach(markables -> {
//							markables.forEach(markable -> System.out.println(wordService.getTextContent(wordService.getWordsBySpan(words, markable.getSpan()))));
//							System.out.println();
//					});

			List<Markable> cpMarkables = markableProvider.provide(fileSet.getCpFile());
			logger.info(cpMarkables.toString());
			List<Markable> cpMarkableLeafs = markableService.filterLeafs(cpMarkables);
			logger.info(cpMarkableLeafs.toString());

			List<Markable> npMarkables = markableProvider.provide(fileSet.getNpFile());
			long count = npMarkables.stream().filter(m -> m.getSpan().lowerEndpoint() == 336).count();
			System.out.println(count);
			logger.info(npMarkables.toString());
			List<Markable> npMarkableLeafs = markableService.filterLeafs(npMarkables);
			logger.info(npMarkableLeafs.toString());

			logger.info("");

			for (Word pronoun : pronouns) {
				List<ResultLine> resultLinesForPronoun = new ArrayList<>();

				if (!pronominalAnaphoraProviderFromWords.shouldWeCare(pronoun)) {
					continue;
				}

				Range<Integer> pronounRange = Range.singleton(pronoun.getId());

				Markable anaphora = getAnaphora(pronominalAnaphoras, pronounRange);

				if (config.includeCp) {
					List<Markable> cps = filterMarkables(cpMarkables, pronoun, anaphora, NO_ANAPHORA_CP_LIMIT);
					
					for (Markable cpMarkable : cps) {
						ResultLine resultLine = new ResultLine();
						resultLine.setFileName(pronoun.getFileName());
						resultLine.setPronoun(pronoun);
						resultLine.setSpan(cpMarkable.getSpan());
						List<Word> cpPhrase = wordService.getWordsBySpan(words, cpMarkable.getSpan());
						String cpPhraseText = wordService.getTextContent(cpPhrase).trim();
						if (Arrays.equals(a, cpPhraseText.getBytes())) {
							continue;
						}
						resultLine.setPhrase(cpPhrase);
						resultLine.setPhraseType("CP");

						Optional<Word> mainPhrase = resultLine.getPhrase().stream()
								.filter(phrase -> !cpMarkable.getSpan().contains(phrase.getEdge())).findFirst();

						if (!mainPhrase.isPresent()) {
							throw new IllegalStateException("mainPhrase should be always present");
						}

						resultLine.setMainPhrase(mainPhrase.get());
						
						String pronounPerson = pronoun.getMorphologicalAnalysis().getOrDefault("Person", "?");
						String phrasePerson = resultLine.getMainPhrase().getMorphologicalAnalysis().getOrDefault("Person", "?");
						if (pronounPerson != "?" && phrasePerson != "?" && !Objects.equals(pronounPerson, phrasePerson)) {
							continue;
						}

						resultLine.setCpDistance(
								markableService.getCpsBetween(cpMarkables, cpMarkable.getSpan(), pronounRange));
						resultLine.setCpDistance2(
								markableService.getCpsBetween2(cpMarkables, cpMarkable.getSpan(), pronounRange, pronoun.getMorphologicalAnalysis().get("PronType")));
						resultLine.setNpDistance(
								markableService.getNpsBetween(npMarkables, cpMarkable.getSpan(), pronounRange));

						resultLine.setAnaphora(cpMarkable.equals(anaphora));
//						if (anaphora != null) {
//							resultLine.setPronominalAnaphoraChainHead(anaphora.getChainHead());
//						}

						resultLine.setWordNumber(resultLine.getPhrase().stream()
								.filter(word -> !filter.contains(word.getTextContent()))
								.count());
						
						resultLinesForPronoun.add(resultLine);

						// result.addLine(resultLine);
					}
				}

				List<Markable> nps = filterMarkables(npMarkables, pronoun, anaphora, NO_ANAPHORA_NP_LIMIT);

				for (Markable npMarkable : nps) {
					try {
						if (npMarkable.getSpan().equals(Range.singleton(pronoun.getId()))) {
							// do not add self
							continue;
						}

						ResultLine resultLine = new ResultLine();
						resultLine.setFileName(pronoun.getFileName());
						resultLine.setPronoun(pronoun);
						resultLine.setSpan(npMarkable.getSpan());
						List<Word> npPhrase = wordService.getWordsBySpan(words, npMarkable.getSpan());
						String npPhraseText = wordService.getTextContent(npPhrase).trim();
						if (Arrays.equals(a, npPhraseText.getBytes())) {
							continue;
						}
						if (ignorePhrases.contains(npPhraseText)) {
							continue;
						}
						resultLine.setPhrase(npPhrase);
						resultLine.setPhraseType("NP");

						Optional<Word> mainPhrase = resultLine.getPhrase().stream()
								.filter(phrase -> !npMarkable.getSpan().contains(phrase.getEdge())).findFirst();

						if (!mainPhrase.isPresent()) {
							throw new IllegalStateException("mainPhrase should be always present");
						}

						resultLine.setMainPhrase(mainPhrase.get());

						String pronounPerson = pronoun.getMorphologicalAnalysis().getOrDefault("Person", "?");
						String phrasePerson = resultLine.getMainPhrase().getMorphologicalAnalysis()
								.getOrDefault("Person", "?");
						if (pronounPerson != "?" && phrasePerson != "?"
								&& !Objects.equals(pronounPerson, phrasePerson)) {
							continue;
						}

						resultLine.setCpDistance(
								markableService.getCpsBetween(cpMarkables, npMarkable.getSpan(), pronounRange));
						resultLine.setCpDistance2(markableService.getCpsBetween2(cpMarkables, npMarkable.getSpan(),
								pronounRange, pronoun.getMorphologicalAnalysis().get("PronType")));
						resultLine.setNpDistance(
								markableService.getNpsBetween(npMarkables, npMarkable.getSpan(), pronounRange));

//					resultLine.setAnaphora(
//						test 
//							? isAnaphora2(pronominalAnaphoras, npMarkable, pronounRange)
//							: isAnaphora(pronominalAnaphoras, npMarkable, pronounRange));
//					resultLine.setAnaphora(isAnaphora(pronominalAnaphoras, npMarkable, pronounRange, npMarkableLeafs.contains(npMarkable)));
						resultLine.setAnaphora(isAnaphora2(pronominalAnaphoras, npMarkable, pronounRange));

						boolean isPropn = false;
						List<Word> npWords = wordService.getWordsBySpan(words, npMarkable.getSpan());
						for (Word npWord : npWords) {
							if (npWord.getWordClass().equals("PROPN")) {
								isPropn = true;
								break;
							}
						}
						resultLine.setPronp(isPropn ? "1" : "0");
//					if (anaphora != null) {
//						resultLine.setPronominalAnaphoraChainHead(anaphora.getChainHead());
//					}

						resultLinesForPronoun.add(resultLine);

						resultLine.setPronounPhrase("PRON".equals(mainPhrase.get().getWordClass()));

						resultLine.setUnivDepPosTag(mainPhrase.get().getUnivDepPosTag());
						resultLine.setPosTag(mainPhrase.get().getPosTag());

						resultLine.setWordNumber(resultLine.getPhrase().stream()
								.filter(word -> !filter.contains(word.getTextContent())).count());

						Optional<Markable> leafCpOptional = cpMarkableLeafs.stream()
								.filter(cp -> cp.getSpan().encloses(npMarkable.getSpan())).findFirst();

						if (!leafCpOptional.isPresent()) {
							System.out.println("leafCpOptional should be always present");
						} else {
							Markable leafCp = leafCpOptional.get();

							List<Markable> npsInLeafCp = nps.stream()
									.filter(np -> leafCp.getSpan().encloses(np.getSpan())).collect(Collectors.toList());

							int indexOfNpInLeafCp = npsInLeafCp.indexOf(npMarkable);

							if (indexOfNpInLeafCp == -1) {
								throw new IllegalStateException("indexOfNpInLeafCp not found");
							}

							resultLine.setIndexOfNpInLeafCp(indexOfNpInLeafCp + 1);
						}

//					result.addLine(resultLine);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				resultLinesForPronoun.sort((r1, r2) -> ComparisonChain.start()
						.compare(r1.getNpDistance(), r2.getNpDistance())
						.compare(r2.getPhraseType(), r1.getPhraseType())
						.result());
				
				if (!resultLinesForPronoun.isEmpty() && !test) {
					Optional<ResultLine> firstAnaphora = resultLinesForPronoun.stream()
							.filter(rl -> rl.isAnaphora())
							.findFirst();
					List<ResultLine> filteredResultLines = new ArrayList<>();
					if (firstAnaphora.isPresent()) {
						boolean found = false;
						for (ResultLine r : resultLinesForPronoun) {
							if (r.isAnaphora() || !found) {
								filteredResultLines.add(r);
								if (r.equals(firstAnaphora.get())) {
									found = true;
								}
							}
						}
					}
					resultLinesForPronoun = filteredResultLines;
//					if (firstAnaphora.isPresent()) {
//						resultLinesForPronoun = resultLinesForPronoun.stream()
//								.filter(rl -> 
//									rl.isAnaphora() || rl.getSpan().lowerEndpoint() >= firstAnaphora.get().getSpan().lowerEndpoint())
//								.collect(Collectors.toList());
//					}
				}

				resultLinesForPronoun.stream().forEach(resultLine -> result.addLine(resultLine));
			}

		}
		
		return result;
	}

	private static boolean isAnaphora(Collection<List<Markable>> pronominalAnaphoraPairs, Markable markable,
			Range<Integer> pronounRange, boolean isLeaf) {
		for (List<Markable> pronominalAnaphoraPair : pronominalAnaphoraPairs) {
			if (pronominalAnaphoraPair.get(0).getSpan().equals(markable.getSpan())
					&& pronominalAnaphoraPair.get(1).getSpan().equals(pronounRange)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isAnaphora2(Collection<List<Markable>> pronominalAnaphoraPairs, Markable markable,
			Range<Integer> pronounRange) {
//		Optional<List<Markable>> pairOfPronounOpt = getPronominalAnaphoraPair(pronominalAnaphoraPairs, 1, pronounRange);
//		if (!pairOfPronounOpt.isPresent()) {
//			return false;
//		}
//		List<Markable> pairOfPronoun = pairOfPronounOpt.get();
//		Range<Integer> foot = pairOfPronoun.get(0).getSpan();
//		while (foot != null) {
//			Optional<List<Markable>> pairOfFoot = getPronominalAnaphoraPair(pronominalAnaphoraPairs, 1, foot);
//			foot = pairOfFoot.map(p -> p.get(0).getSpan()).orElse(null);
//			if (markable.getSpan().equals(foot)) {
//				return true;
//			}
//		}
		
		Range<Integer> head = pronounRange;
		do {
			Optional<List<Markable>> pairOfHead = getPronominalAnaphoraPair(pronominalAnaphoraPairs, 1, head);
			head = pairOfHead.map(p -> p.get(0).getSpan()).orElse(null);
			if (markable.getSpan().equals(head)) {
				return true;
			}
		} while (head != null);
		
		return false;
	}
	
	private static Optional<List<Markable>> getPronominalAnaphoraPair(Collection<List<Markable>> pronominalAnaphoraPairs, 
			int indexInPair, Range<Integer> range) {
		return pronominalAnaphoraPairs.stream()
			.filter(pronominalAnaphoraPair -> pronominalAnaphoraPair.get(indexInPair).getSpan().equals(range))
			.findFirst();
	}

	private static Markable getAnaphora(Collection<List<Markable>> pronominalAnaphoraPairs,
			Range<Integer> pronounRange) {
		for (List<Markable> pronominalAnaphoraPair : pronominalAnaphoraPairs) {
			if (pronominalAnaphoraPair.get(1).getSpan().equals(pronounRange)) {
				return pronominalAnaphoraPair.get(0);
			}
		}
		return null;
	}

	private List<Markable> filterMarkables(List<Markable> markables, Word pronoun, Markable anaphora, int limit) {
		List<Markable> stream = markables.stream()
				.filter(cp -> cp.getSpan().upperEndpoint() < pronoun.getId())
				.collect(Collectors.toList());
		if (!test) { // only for train files
			if (anaphora != null) {
//				return stream.stream()
//						.filter(cp -> cp.getSpan().lowerEndpoint() >= anaphora.getSpan().lowerEndpoint())
//						.collect(Collectors.toList());
				return stream;
			} else {
				return new ArrayList<>();
				
//				result = stream.collect(Collectors.toList());
//				if (result.size() > limit) {
//					result = result.subList(result.size() - limit, result.size()); 
//				}
			}
		} else {
			return stream;
		}
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

}
