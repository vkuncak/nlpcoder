package examples.java.util.hashtable;
import java.util.Hashtable;

public class T2 {
  public static void main(String[] s) {
    Hashtable<String,String> table = new Hashtable<String,String>();
    table.put("key1", "value1");
    table.put("key2", "value2");
    table.put("key3", "value3");

    table.clear();

    System.out.println(table);
  }
}