package service.algorithm;
import java.util.*;
public class TfIdfCosine implements SimilarityAlgorithm {
 public String name(){return "TF-IDF Cosine";}
 public double calculate(List<String>a,List<String>b){
  Set<String>v=new HashSet<>(a);v.addAll(b);if(v.isEmpty())return 0;
  double dot=0,na=0,nb=0;
  for(String w:v){int df=(a.contains(w)?1:0)+(b.contains(w)?1:0);double idf=Math.log(3.0/(1+df))+1;
   double x=tf(a,w)*idf,y=tf(b,w)*idf;dot+=x*y;na+=x*x;nb+=y*y;}
  return na==0||nb==0?0:100*dot/(Math.sqrt(na)*Math.sqrt(nb));
 }
 private double tf(List<String>l,String w){int c=0;for(String x:l)if(x.equals(w))c++;return l.isEmpty()?0:(double)c/l.size();}
}
