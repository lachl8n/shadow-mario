import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.List;

/**
 * Class that contains methods to read a CSV file and a properties file.
 * Adapted from SWEN20003 Project 1 Solution by Dimuthu Kariyawasan & Tharun Dharmawickrema.
 *
 * @author Lachlan Chue
 * @version 1.16
 */
public class IOUtils {

    /***
     * Method that reads a CSV file and return a list of String arrays
     * @param csvFile: the path to the CSV file
     * @return: List<String[]>. Each String[] array represents elements in a single line in the CSV file
     */
    public static List<String[]> readCsv(String csvFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            List<String[]> lines = new ArrayList<String[]>();
            String textRead;

            while ((textRead = reader.readLine()) != null) {
                String[] splitText = textRead.split(",");
                lines.add(splitText);
            }
            return lines;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    /***
     * Method that reads a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return: Properties object
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return appProps;
    }
}