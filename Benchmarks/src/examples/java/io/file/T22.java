package examples.java.io.file;

import java.io.File;
public class T22 {
  public static void main(String[] a) {
    File file = new File("c:\\test\\test\\");
    file.mkdirs();
  }
}
