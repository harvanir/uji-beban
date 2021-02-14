package org.harvanir.ujibeban.benchmark;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * https://mkyong.com/java/java-properties-file-examples/
 *
 * @author Harvan Irsyadi
 */
public class PropertiesLoaderUtils {

  static Properties loadAllProperties(String fileNameOnClassPath) {
    Properties prop = new Properties();
    try (InputStream input =
        PropertiesLoaderUtils.class.getClassLoader().getResourceAsStream(fileNameOnClassPath)) {

      if (input == null) {
        System.out.printf("Sorry, unable to find '%s'%n", fileNameOnClassPath);
      }

      // load a properties file from class path, inside static method
      prop.load(input);

    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return prop;
  }
}
