package examples.java.util.list;
import java.util.Arrays;
import java.util.List;

public class T17 {
  public static void main(String args[]) {
    String[] a = new String[] { "a", "c", "b" };

    List l = (Arrays.asList());
    String stuff[] = (String[]) l.toArray(new String[0]);
  }
}