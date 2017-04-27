package uiDataPreparators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.nio.file.Files;

import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;

/**
 * Creates a JSObject for each element of the simulation (eg. EntityGenerator1).
 * Each JSObject has its type, name and attributes.
 * Also creates and assigns the corresponding attributes.
 *
 * @author dimitris.katsios
 *
 */
public class JSObjectsCreator {
    public static String programName = "test/";
    public static String workingDir = System.getProperty("user.dir") + "/";
    public static String mainPath = workingDir + "/TMP/";
    public static String targetPath = mainPath + "target.cfg";
    public static String pathToDatFile = mainPath + "target.dat";
    public static String confPath = ""; //workingDir + programName;
    public static String pathToLog = workingDir + "JSOptimizerLOG.log";
    public static String pathToAttributeList = mainPath + "JSAttributeList.ser";
    public static String pathToJaamSimJar = mainPath + "JaamSim.jar";
    public static String pathToUniversalFunFile = workingDir + "front";
    public static String pathTocomparisonResultsFile = workingDir + "ComparisonResults.txt";

    private static ArrayList<JSObject> jsObjectList = new ArrayList<>();
    private static ArrayList<String> jsCategoriesList = new ArrayList<>();
    private static ArrayList<String> excludedList = new ArrayList<>();
    private static String cfgPath = "/home/dimitris/simple.cfg";

    //list of JSObject types to be excluded from the procedure
    public static void exludedListInitialization() {
        excludedList.add("ColladaModel");
        excludedList.add("DisplayEntity");
        excludedList.add("OverlayClock");
        excludedList.add("OverlayText");
        excludedList.add("TextModel");
        excludedList.add("View");
        excludedList.add("Text");
        excludedList.add("Graph");
        excludedList.add("EntityLabel");
        excludedList.add("Arrow");
        excludedList.add("ShapeModel");
        excludedList.add("InputBox");
    }

    public static ArrayList<String> objNamesSplitter(String names) {
        ArrayList<String> namesString = new ArrayList<>(Arrays.asList(names.replaceAll("^\\{ | \\}$", "").split(" +")));
        return namesString;
    }

    //adds elements to the ObjectList (ArrayList<JSObject>)
    public static void addToJsObjectList(ArrayList<String> element) {
        ArrayList<String> listOfNames = objNamesSplitter(element.get(2));
        jsCategoriesList.add(element.get(1));
        for (String name: listOfNames) {
            JSObject tempObject = new JSObject(element.get(1), name );
            jsObjectList.add(tempObject);
        }
    }

    public static ArrayList<String> getJSCategoriesList() {
        return jsCategoriesList;
    }

    public static boolean notInExcludedList(String input) {
        for(String comp: excludedList) {
            if (input.equals(comp)) return false;
        } return true;
    }

    //creates the elements if they are not of the exludedList type
    public static void createObjects(ArrayList<ArrayList<String>> cfgList) {
        exludedListInitialization();
        for(ArrayList<String> element: cfgList) {
            if ((element.get(0).equals("Define")) && (notInExcludedList(element.get(1)))) addToJsObjectList(element);
        }
    }

    //returns a reversed-sorted list of the JSObjects
    public static ArrayList<JSObject> getJSObcetsList() {
        ArrayList<String> list = new ArrayList<>();
        for (JSObject obj: jsObjectList) {
            list.add(obj.getName());
        }
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        ArrayList<JSObject> newList = new ArrayList<>();
        for (String name: list) {
            for(JSObject obj: jsObjectList) {
                if (obj.getName().equalsIgnoreCase(name)) {
                    newList.add(obj);
                }
            }
        }
        Collections.reverse(newList);
        return newList;
    }

    //prints the JSObjects with its attributes, values and .cfg file lines
    public static void printJSObcects(ArrayList<JSObject> jsObectsList) {
        for (JSObject jsObject: jsObjectList) {
            System.out.println(jsObject.getType() + " " + jsObject.getName() + " " + jsObject.getAttributeList().size());
            for (JSObjectAttribute<Object> attribute: jsObject.getAttributeList()) {
                if (attribute == null) continue;
                System.out.println("    " + attribute.getName() + " " + attribute.getValue().toString() + " " + attribute.getType());
                System.out.println("        " + attribute.getLine());
            }
        }
    }

    //Creates a copy of the source .cfg file in order to modify it later without affecting the original file
    public static void copyFile( File sourceFile, File targetFile ) throws IOException {
        if ( targetFile.exists() ) { targetFile.delete(); }
        Files.copy( sourceFile.toPath(), targetFile.toPath() );
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //method to create the TMP directory, copy the .cfg file and prepare it for processing
    public static void prepareFile(String[] args) throws IOException {
        File tmpFile = new File (workingDir, "TMP");
        deleteDir(tmpFile);
        tmpFile.mkdir();
        if (args.length !=0) cfgPath = args[0];
        File sourceFile = new File(cfgPath);
        File targetFile = new File(targetPath);
        copyFile(sourceFile, targetFile);
        CfgFilePreparator.prepareCfg(targetPath);
    }

    //main method that creates the JSObjects, the list containing them and the corresponding attributes
    public static void main(String[] args) throws IOException {
        prepareFile(args);
        ArrayList<ArrayList<String>> cfgList = CfgFileReader.getCfgList(targetPath);
        createObjects(cfgList);
        AttributeSetter.setAttributes(cfgList);

        //		printJSObcects(getJSObcetsList());
    }
}
