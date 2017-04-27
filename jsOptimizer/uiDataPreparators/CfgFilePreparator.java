package uiDataPreparators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import uiOutputHandlers.ReplaceAtCfgFile;

/**
 * CfgFilePreparator class prepares .cfg file.
 * Specifically turns RealTime off in order for the simulation to run as fast as possible,
 * deconstructs groups of objects,
 * includes external .cfg file contents called from within the source .cfg,
 * turns to non-visible all the JaamSim windows,
 * and deletes all the position and points characteristics of the objects.
 *
 * @author dimitris.katsios
 *
 */
public class CfgFilePreparator {

    public static void prepareCfg(String targetPath) {
        ReplaceAtCfgFile.replaceSelected(targetPath, "Simulation RealTime { TRUE }", "Simulation RealTime { FALSE }");
        replaceIncludes(targetPath);
        replaceShowFlags(targetPath);
        deletePosition(targetPath);
        groupSeparating(targetPath);
    }

    public static void replaceIncludes(String targetPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(targetPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("Include")) continue;
                String[] lineContents = line.split(" +", 2);
                if (lineContents.length != 2) continue;
                String newCfgPath = lineContents[1].replaceAll("'", "");
                String content = readFile(newCfgPath, Charset.defaultCharset());
                //				StringBuilder newFileContents = new StringBuilder();
                //				newFileContents.append(newCfgPath);
                ReplaceAtCfgFile.replaceSelected(targetPath, line, content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //turns to non-visible all the JaamSim windows
    public static void replaceShowFlags(String targetPath) {
        String[] showObjects = {"ShowModelBuilder", "ShowObjectSelector", "ShowInputEditor", "ShowOutputViewer", "ShowPropertyViewer", "ShowLogViewer"};
        for(String str: showObjects) {
            ReplaceAtCfgFile.replaceSelected(targetPath, "Simulation " + str + " { TRUE }", "Simulation " + str + " { FALSE }");
        }
        ReplaceAtCfgFile.replaceSelected(targetPath, "View1 ShowWindow { TRUE }", "View1 ShowWindow { FALSE }");
    }

    //deletes all the position, DisplayModel and points characteristics of the objects, as well as ColladaFile graphic objects
    public static void deletePosition(String targetPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(targetPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineContents = line.split(" +", 3);
                if (lineContents.length != 3) continue;
                if(lineContents[1].equals("Position") ||
                        lineContents[1].equals("Points") || lineContents[1].equals("DisplayModel") || lineContents[1].equals("ColladaFile")) {
                    ReplaceAtCfgFile.replaceSelected(targetPath, line, "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //if there are Groups of objects in the source .cfg file, this method deconstructs them
    //by replacing them with new lines for each object in the group at the target .cfg file
    @SuppressWarnings("unchecked")
    public static void groupSeparating(String targetPath) {
        HashMap<String, ArrayList<String>> groupContents = new HashMap<>();
        ArrayList<String> groupNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(targetPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\"")) continue;
                String[] lineContents = line.split(" +", 3);
                if (lineContents.length != 3) continue;
                if(lineContents[0].equals("Define") &&
                        lineContents[1].equals("Group")) {
                    groupNames.add(lineContents[2].replaceAll(" *\\{ *| *\\} *", ""));
                    ReplaceAtCfgFile.replaceSelected(targetPath, line, "");

                }
                else if(groupNames.contains(lineContents[0]) && lineContents[1].equals("List")) {
                    String[] listContents = lineContents[2].replaceAll(" *\\{ *| *\\} *", "").split(" +");
                    ArrayList<String> arrayContents = new ArrayList<>(Arrays.asList(listContents));
                    ArrayList<String> tempArrayContents = (ArrayList<String>) arrayContents.clone();
                    for(String listContent: tempArrayContents) {
                        if (groupNames.contains(listContent)) {
                            ArrayList<String> newContents = (ArrayList<String>) groupContents.get(listContent).clone();
                            arrayContents.remove(listContent);
                            arrayContents.addAll(newContents);
                        }
                    }
                    groupContents.put(lineContents[0], arrayContents);
                    ReplaceAtCfgFile.replaceSelected(targetPath, line, "");
                }
                else if(groupNames.contains(lineContents[0])) {
                    StringBuilder newLine = new StringBuilder();
                    ArrayList<String> listNames = groupContents.get(lineContents[0]);
                    for(String name: listNames) {
                        newLine.append(name + " " + lineContents[1] + " " + lineContents[2] + "\n");
                    }

                    ReplaceAtCfgFile.replaceSelected(targetPath, line, newLine.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
