package service.algorithm;
import java.util.*;
public class Jaccard implements SimilarityAlgorithm {
 public String name(){return "Jaccard";}
 public double calculate(List<String>a,List<String>b){
  Set<String>x=new HashSet<>(a),y=new HashSet<>(b),u=new HashSet<>(x);u.addAll(y);
  if(u.isEmpty())return 0;x.retainAll(y);return 100.0*x.size()/u.size();
 }
}
