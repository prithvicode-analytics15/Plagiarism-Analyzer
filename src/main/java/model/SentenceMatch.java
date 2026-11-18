
package model;

public class SentenceMatch {

    private final String sentenceA;
    private final String sentenceB;
    private final double similarity;
    private final String type;

    public SentenceMatch(String a, String b, double score, String type) {
        this.sentenceA = a;
        this.sentenceB = b;
        this.similarity = score;
        this.type = type;
    }

    public String getSentenceA() {
        return sentenceA;
    }

    public String getSentenceB() {
        return sentenceB;
    }

    public double getSimilarity() {
        return similarity;
    }

    public String getType() {
        return type;
    }
}
