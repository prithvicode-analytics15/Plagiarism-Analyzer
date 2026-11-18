package service;
import model.AnalysisResult;import java.sql.*;import java.util.*;
public class DatabaseManager {
 private Connection connect()throws SQLException{return DriverManager.getConnection("jdbc:sqlite:plagiacheck.db");}
 public DatabaseManager()throws SQLException{try(Connection c=connect();Statement s=c.createStatement()){s.executeUpdate("CREATE TABLE IF NOT EXISTS history(id INTEGER PRIMARY KEY AUTOINCREMENT, doc_a TEXT, doc_b TEXT, average REAL, details TEXT, matches INTEGER, date TEXT)");}}
 public void save(AnalysisResult r)throws SQLException{
  try(Connection c=connect();PreparedStatement p=c.prepareStatement("INSERT INTO history(doc_a,doc_b,average,details,matches,date) VALUES(?,?,?,?,?,?)")){
   p.setString(1,r.getA());p.setString(2,r.getB());p.setDouble(3,r.getAverage());p.setString(4,r.getScores().toString());p.setInt(5,r.getMatches().size());p.setString(6,r.getDate().toString());p.executeUpdate();
  }
 }
 public List<String[]> history()throws SQLException{
  List<String[]>rows=new ArrayList<>();try(Connection c=connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT id,doc_a,doc_b,average,matches,date FROM history ORDER BY id DESC")){
   while(r.next())rows.add(new String[]{r.getString(1),r.getString(2),r.getString(3),String.format("%.2f%%",r.getDouble(4)),r.getString(5),r.getString(6)});
  }return rows;
 }
 public void clear()throws SQLException{try(Connection c=connect();Statement s=c.createStatement()){s.executeUpdate("DELETE FROM history");}}
}
