
package model;

import java.time.LocalDateTime;
import java.util.*;

public class AnalysisResult {

    private final String a, b;
    private final Map<String, Double> scores;
    private final List<String> matches;
    private final List<SentenceMatch> sentenceMatches;
    private final int wordsA, wordsB;
    private final LocalDateTime date = LocalDateTime.now();

    public AnalysisResult(String a, String b,
            Map<String, Double> scores,
            List<String> matches,
            int wa, int wb) {

        this(a, b, scores, matches,
                new ArrayList<>(), wa, wb);
    }

    public AnalysisResult(String a, String b,
            Map<String, Double> scores,
            List<String> matches,
            List<SentenceMatch> sentenceMatches,
            int wa, int wb) {

        this.a = a;
        this.b = b;
        this.scores = new LinkedHashMap<>(scores);
        this.matches = new ArrayList<>(matches);
        this.sentenceMatches = new ArrayList<>(sentenceMatches);
        this.wordsA = wa;
        this.wordsB = wb;
    }

    public String getA() { return a; }

    public String getB() { return b; }

    public Map<String, Double> getScores() {
        return Collections.unmodifiableMap(scores);
    }

    public List<String> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public List<SentenceMatch> getSentenceMatches() {
        return Collections.unmodifiableList(sentenceMatches);
    }

    public int getWordsA() { return wordsA; }

    public int getWordsB() { return wordsB; }

    public LocalDateTime getDate() { return date; }

    public double getAverage() {
        return scores.values().stream()
                .mapToDouble(x -> x)
                .average().orElse(0);
    }
}
