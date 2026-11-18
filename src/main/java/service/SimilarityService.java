
package service;

import model.*;
import service.algorithm.*;
import java.util.*;

public class SimilarityService {

    private final TextProcessor processor = new TextProcessor();

    private final List<SimilarityAlgorithm> algorithms =
            Arrays.asList(
                new Jaccard(),
                new Cosine(),
                new TfIdfCosine(),
                new BigramJaccard()
            );

    private static final double MATCH_THRESHOLD = 0.60;

    public AnalysisResult analyze(DocumentData a, DocumentData b) {

        if (a == null || b == null) {
            throw new IllegalArgumentException(
                    "Both documents are required.");
        }

        List<String> wordsA = processor.tokens(a.getContent());
        List<String> wordsB = processor.tokens(b.getContent());

        Map<String, Double> scores = new LinkedHashMap<>();

        for (SimilarityAlgorithm algorithm : algorithms) {

            double score = algorithm.calculate(wordsA, wordsB);

            score = Math.max(0, Math.min(100, score));

            scores.put(algorithm.name(), score);
        }

        List<String> sentencesA =
                processor.sentences(a.getContent());

        List<String> sentencesB =
                processor.sentences(b.getContent());

        List<String> exactMatches = new ArrayList<>();
        List<SentenceMatch> allMatches = new ArrayList<>();

        for (String sa : sentencesA) {

            String na = processor.normalize(sa);

            if (na.isBlank()) continue;

            String bestSentence = null;
            double bestScore = 0;
            boolean exact = false;

            for (String sb : sentencesB) {

                String nb = processor.normalize(sb);

                if (nb.isBlank()) continue;

                if (na.equals(nb)) {
                    bestSentence = sb;
                    bestScore = 100;
                    exact = true;
                    break;
                }

                Set<String> ta =
                        new HashSet<>(processor.tokens(sa));

                Set<String> tb =
                        new HashSet<>(processor.tokens(sb));

                // Ignore very short sentences for approximate matching
                if (ta.size() < 5 || tb.size() < 5) {
                    continue;
                }

                double similarity = jaccard(ta, tb);

                if (similarity > bestScore) {
                    bestScore = similarity;
                    bestSentence = sb;
                }
            }

            if (exact) {

                exactMatches.add(sa);

                allMatches.add(new SentenceMatch(
                        sa, bestSentence, 100, "EXACT"));

            } else if (bestSentence != null
                    && bestScore >= MATCH_THRESHOLD) {

                allMatches.add(new SentenceMatch(
                        sa,
                        bestSentence,
                        bestScore * 100,
                        "SIMILAR"));
            }
        }

        return new AnalysisResult(
                a.getName(),
                b.getName(),
                scores,
                exactMatches,
                allMatches,
                a.getWordCount(),
                b.getWordCount()
        );
    }

    private double jaccard(Set<String> a, Set<String> b) {

        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);

        Set<String> union = new HashSet<>(a);
        union.addAll(b);

        if (union.isEmpty()) return 0;

        return (double) intersection.size() / union.size();
    }
}
