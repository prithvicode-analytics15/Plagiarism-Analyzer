package service;
import java.util.*;import java.util.regex.*;
public class TextProcessor {
 private static final Set<String> STOP=Set.of("a","an","the","is","are","was","were","in","on","at","of","to","and","or","for","with","by");
 public List<String> tokens(String text){
  List<String> out=new ArrayList<>();Matcher m=Pattern.compile("[\\p{L}\\p{N}]+").matcher(text.toLowerCase(Locale.ROOT));
  while(m.find()){String w=m.group();if(!STOP.contains(w))out.add(w);}return out;
 }
 public List<String> sentences(String text){
  List<String> out=new ArrayList<>();for(String s:text.split("(?<=[.!?])\\s+"))if(!s.trim().isEmpty())out.add(s.trim());return out;
 }
 public String normalize(String s){return s.toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}\\s]"," ").replaceAll("\\s+"," ").trim();}
}
