package service;
import model.AnalysisResult;import org.apache.pdfbox.pdmodel.*;import org.apache.pdfbox.pdmodel.font.*;import java.nio.file.Path;import java.io.IOException;
public class PdfReportGenerator {
 public void write(AnalysisResult r,Path path)throws IOException{
  try(PDDocument pdf=new PDDocument()){
   PDPage page=new PDPage();pdf.addPage(page);
   try(PDPageContentStream c=new PDPageContentStream(pdf,page)){
    PDType1Font regular=new PDType1Font(Standard14Fonts.FontName.HELVETICA),bold=new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    float y=750;y=line(c,bold,18,50,y,"PLAGIACHECK ANALYSIS REPORT");y-=10;
    y=line(c,regular,11,50,y,"Document A: "+r.getA());y=line(c,regular,11,50,y,"Document B: "+r.getB());
    y=line(c,regular,11,50,y,"Date: "+r.getDate());y-=8;y=line(c,bold,13,50,y,"ALGORITHM SCORES");
    for(var e:r.getScores().entrySet())y=line(c,regular,11,60,y,e.getKey()+": "+String.format("%.2f%%",e.getValue()));
    y-=8;y=line(c,bold,13,50,y,"SUMMARY");y=line(c,regular,11,60,y,"Average similarity: "+String.format("%.2f%%",r.getAverage()));
    y=line(c,regular,11,60,y,"Word counts: "+r.getWordsA()+" / "+r.getWordsB());
    y=line(c,regular,11,60,y,"Exact matching sentences: "+r.getMatches().size());y-=8;y=line(c,bold,13,50,y,"MATCHES");
    for(String s:r.getMatches()){if(y<55)break;y=line(c,regular,9,60,y,s.replaceAll("[^\\x20-\\x7E]","?"));}
    line(c,regular,9,50,45,"Similarity is an indicator, not proof of plagiarism.");
   }pdf.save(path.toFile());
  }
 }
 private float line(PDPageContentStream c,PDType1Font f,int size,float x,float y,String s)throws IOException{
  c.beginText();c.setFont(f,size);c.newLineAtOffset(x,y);c.showText(s.length()>110?s.substring(0,107)+"...":s);c.endText();return y-18;
 }
}
