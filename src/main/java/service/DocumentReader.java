package service;
import model.DocumentData;
import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.Files;import java.util.Locale;
import org.apache.pdfbox.Loader;import org.apache.pdfbox.pdmodel.PDDocument;import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
public class DocumentReader {
 public DocumentData read(File f)throws Exception{
  if(f==null||!f.isFile())throw new IOException("Select a valid file.");
  String n=f.getName().toLowerCase(Locale.ROOT),t;
  if(n.endsWith(".txt"))t=Files.readString(f.toPath(),StandardCharsets.UTF_8);
  else if(n.endsWith(".pdf")){try(PDDocument p=Loader.loadPDF(f)){t=new PDFTextStripper().getText(p);}}
  else if(n.endsWith(".docx")){try(InputStream in=new FileInputStream(f);XWPFDocument d=new XWPFDocument(in);XWPFWordExtractor x=new XWPFWordExtractor(d)){t=x.getText();}}
  else throw new IOException("Only TXT, PDF and DOCX files are supported.");
  if(t.trim().isEmpty())throw new IOException("No readable text in "+f.getName());
  return new DocumentData(f,t);
 }
}
