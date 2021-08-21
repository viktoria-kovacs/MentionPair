package weka.domain;

import java.util.HashMap;
import java.util.Map;

public class Word {

    private int id;
    private int sentenceIndex;
    private int idInSentence;
    private String wordClass;
    private String textContent;
    private int edge;
    private Map<String, String> morphologicalAnalysis = new HashMap<>();
    private String pronominalAnaphoraMark;
    private String univDepPosTag;
    private String posTag;
    private String fileName;

    public Word() {
    }

    public Word(int id, String textContent) {
        this.id = id;
        this.textContent = textContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSentenceIndex() {
        return sentenceIndex;
    }

    public void setSentenceIndex(int sentenceIndex) {
        this.sentenceIndex = sentenceIndex;
    }

    public int getIdInSentence() {
        return idInSentence;
    }

    public void setIdInSentence(int idInSentence) {
        this.idInSentence = idInSentence;
    }

    public String getWordClass() {
        return wordClass;
    }

    public void setWordClass(String wordClass) {
        this.wordClass = wordClass;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }

    public Map<String, String> getMorphologicalAnalysis() {
        return morphologicalAnalysis;
    }

    public void addMorphologicalAnalysis(String key, String value) {
        this.morphologicalAnalysis.put(key, value);
    }

    public String getPronominalAnaphoraMark() {
        return pronominalAnaphoraMark;
    }

    public void setPronominalAnaphoraMark(String pronominalAnaphoraMark) {
        this.pronominalAnaphoraMark = pronominalAnaphoraMark;
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

    @Override
    public String toString() {
        return "Word [id=" + id + ", fileName=" + fileName + ", idInSentence=" + idInSentence + ", wordClass="
                + wordClass + ", textContent=" + textContent + ", edge=" + edge + ", morphologicalAnalysis="
                + morphologicalAnalysis + ", pronominalAnaphoraMark=" + pronominalAnaphoraMark + "]";
    }

}
