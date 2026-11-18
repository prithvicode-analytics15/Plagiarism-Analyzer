package service.algorithm;
import java.util.*;
public class BigramJaccard implements SimilarityAlgorithm {
 public String name(){return "Bigram Jaccard";}
 public double calculate(List<String>a,List<String>b){
  Set<String>x=grams(a),y=grams(b),u=new HashSet<>(x);u.addAll(y);if(u.isEmpty())return 0;x.retainAll(y);return 100.0*x.size()/u.size();
 }
 private Set<String>grams(List<String>w){Set<String>s=new HashSet<>();for(int i=0;i+1<w.size();i++)s.add(w.get(i)+" "+w.get(i+1));return s;}
}
