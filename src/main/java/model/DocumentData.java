package model;
import java.io.File;
public class DocumentData {
 private final File file; private final String content;
 public DocumentData(File file,String content){this.file=file;this.content=content==null?"":content;}
 public File getFile(){return file;} public String getName(){return file.getName();}
 public String getContent(){return content;}
 public int getWordCount(){String s=content.trim();return s.isEmpty()?0:s.split("\\s+").length;}
}
