package uiDataPreparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * CfgFileReader creates an ArrayList<ArrayList<String>> which contains the three parts
 * of each line/attribute in the .cfg file.
 *
 * @author dimitris.katsios
 *
 */
public class CfgFileReader {

    static public ArrayList<ArrayList<String>> cfgList = new ArrayList<>();

    //adds each line of the .cfg file to the cfgList
    public static void cfgListing(String line) {
        ArrayList<String> elements = new ArrayList<>(Arrays.asList(line.split(" +", 3)));
        cfgList.add(elements);
        cfgList.get(cfgList.size() - 1).add(line);
    }

    //exports .cfg to cfgList
    public static void cfgReader(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\"")) continue;
                cfgListing(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns an arraylist of string arraylists with the components of the .cfg file
    public static ArrayList<ArrayList<String>> getCfgList(String path) throws IOException{
        cfgReader(path);
        return cfgList;
    }

    public static void main(String... args) {

    }

}
