package service.algorithm;
import java.util.*;
public class Cosine implements SimilarityAlgorithm {
 public String name(){return "Cosine";}
 public double calculate(List<String>a,List<String>b){
  Map<String,Integer>x=freq(a),y=freq(b);Set<String>all=new HashSet<>(x.keySet());all.addAll(y.keySet());
  double dot=0,nx=0,ny=0;for(String w:all){int p=x.getOrDefault(w,0),q=y.getOrDefault(w,0);dot+=(double)p*q;nx+=(double)p*p;ny+=(double)q*q;}
  return nx==0||ny==0?0:100*dot/(Math.sqrt(nx)*Math.sqrt(ny));
 }
 private Map<String,Integer>freq(List<String>l){Map<String,Integer>m=new HashMap<>();for(String w:l)m.merge(w,1,Integer::sum);return m;}
}
