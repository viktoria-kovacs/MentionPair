package weka.domain;

import java.util.List;

import com.google.common.collect.Range;

public class ResultLine {

	private Word pronoun;
	private Range<Integer> span;
	private List<Word> phrase;
	private Word mainPhrase;
	private String phraseType;
	private long npDistance;
	private long cpDistance;
	private boolean anaphora;
	private String pronp = "?";
	private boolean pronounPhrase;
	private String pronominalAnaphoraChainHead;
	private long cpDistance2;
	private String univDepPosTag;
	private String posTag;
	private String fileName;
	private long wordNumber;
	private int indexOfNpInLeafCp;

	public long getWordNumber() {
		return wordNumber;
	}

	public void setWordNumber(long wordNumber) {
		this.wordNumber = wordNumber;
	}

	public Word getPronoun() {
		return pronoun;
	}

	public void setPronoun(Word pronoun) {
		this.pronoun = pronoun;
	}

	public Range<Integer> getSpan() {
		return span;
	}

	public void setSpan(Range<Integer> span) {
		this.span = span;
	}

	public List<Word> getPhrase() {
		return phrase;
	}

	public void setPhrase(List<Word> phrase) {
		this.phrase = phrase;
	}

	public Word getMainPhrase() {
		return mainPhrase;
	}

	public void setMainPhrase(Word mainPhrase) {
		this.mainPhrase = mainPhrase;
	}

	public String getPhraseType() {
		return phraseType;
	}

	public void setPhraseType(String phraseType) {
		this.phraseType = phraseType;
	}

	public long getNpDistance() {
		return npDistance;
	}

	public void setNpDistance(long npDistance) {
		this.npDistance = npDistance;
	}

	public long getCpDistance() {
		return cpDistance;
	}

	public void setCpDistance(long cpDistance) {
		this.cpDistance = cpDistance;
	}

	public boolean isAnaphora() {
		return anaphora;
	}

	public void setAnaphora(boolean anaphora) {
		this.anaphora = anaphora;
	}

	public String getPronp() {
		return pronp;
	}

	public void setPronp(String pronp) {
		this.pronp = pronp;
	}

	public boolean isPronounPhrase() {
		return pronounPhrase;
	}

	public void setPronounPhrase(boolean pronounPhrase) {
		this.pronounPhrase = pronounPhrase;
	}

	public String getPronominalAnaphoraChainHead() {
		return pronominalAnaphoraChainHead;
	}

	public void setPronominalAnaphoraChainHead(String pronominalAnaphoraChainHead) {
		this.pronominalAnaphoraChainHead = pronominalAnaphoraChainHead;
	}

	public long getCpDistance2() {
		return cpDistance2;
	}

	public void setCpDistance2(long cpDistance2) {
		this.cpDistance2 = cpDistance2;
	}

	public String getUnivDepPosTag() {
		return univDepPosTag;
	}

	public void setUnivDepPosTag(String univDepPosTag) {
		this.univDepPosTag = univDepPosTag;
	}

	public String getPosTag() {
		return posTag;
	}

	public void setPosTag(String posTag) {
		this.posTag = posTag;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getIndexOfNpInLeafCp() {
		return indexOfNpInLeafCp;
	}

	public void setIndexOfNpInLeafCp(int indexOfNpInLeafCp) {
		this.indexOfNpInLeafCp = indexOfNpInLeafCp;
	}

	@Override
	public String toString() {
		return "ResultLine [pronounId=" + pronoun.getId() + ", span=" + span + ", anaphora=" + anaphora + "]";
	}
}
