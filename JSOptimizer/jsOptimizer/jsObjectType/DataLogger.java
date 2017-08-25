package jsObjectType;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import uiOutputHandlers.JsSerializer;

@SuppressWarnings("serial")
public class DataLogger  implements Serializable{

    public ArrayList<Boolean> categoriesFlag;
    public ArrayList<String> categoriesList;
    private ArrayList<JSObject> jsObjectsList;
    private ArrayList<JSObject> selectedJSObjects = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    private ArrayList<JSObjectAttribute> selectedJSattributes = new ArrayList<>();
    private ArrayList<JSOutput> JSoutputList = new ArrayList<>();
    public ArrayList<double[]> constraints;
    private String[] categoriesArray;
    private String[] outputList;
    private int numberOfObjectives;
    private int numberOfConstraints;
    private int[] outputindices;
    private String pathToCfgFile;
    private String algorithm;

    public DataLogger() {
        super();
    }

    public ArrayList<Boolean> getCategoriesFlag() {
        return categoriesFlag;
    }
    public void setCategoriesFlag(ArrayList<Boolean> categoriesFlag) {
        this.categoriesFlag = categoriesFlag;
    }

    public ArrayList<String> getCategoriesList() {
        return categoriesList;
    }
    public void setCategoriesList(ArrayList<String> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public ArrayList<JSObject> getJsObjectsList() {
        return jsObjectsList;
    }
    public void setJsObjectsList(ArrayList<JSObject> jsObjectsList) {
        this.jsObjectsList = jsObjectsList;
    }

    public ArrayList<JSObject> getSelectedJSObjects() {
        return selectedJSObjects;
    }
    public void setSelectedJSObjects(ArrayList<JSObject> selectedJSObjects) {
        this.selectedJSObjects = selectedJSObjects;
    }

    @SuppressWarnings("rawtypes")
    public ArrayList<JSObjectAttribute> getSelectedJSattributes() {
        return selectedJSattributes;
    }
    @SuppressWarnings("rawtypes")
    public void setSelectedJSattributes(
            ArrayList<JSObjectAttribute> selectedJSattributes) {
        this.selectedJSattributes = selectedJSattributes;
    }

    public String[] getCategoriesArray() {
        return categoriesArray;
    }
    public void setCategoriesArray(String[] categoriesArray) {
        this.categoriesArray = categoriesArray;
    }

    public String[] getOutputList() {
        return outputList;
    }
    public void setOutputList(String[] outputList) {
        this.outputList = outputList;
    }

    public ArrayList<JSOutput> getJSoutputList() {
        return JSoutputList;
    }

    public void setJSoutputList(ArrayList<JSOutput> jSoutputList) {
        JSoutputList = jSoutputList;
    }

    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    public ArrayList<double[]> getConstraints() {
        return constraints;
    }

    public void setConstraints(ArrayList<double[]> constraints) {
        this.constraints = constraints;
    }

    public int[] getOutputindices() {
        return outputindices;
    }

    public void setOutputindices(int[] outputindices) {
        this.outputindices = outputindices;
    }

    public String getPathToCfgFile() {
        return pathToCfgFile;
    }

    public void setPathToCfgFile(String pathToCfgFile) {
        this.pathToCfgFile = pathToCfgFile;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void saveFile(){{
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JSOptimizer files .jsopt", "jsopt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String[] pathToSaveFile = {chooser.getSelectedFile().getPath()};
            if (!pathToSaveFile[0].endsWith(".jsopt")) pathToSaveFile[0] = pathToSaveFile[0] + ".jsopt";
            try {
                JsSerializer.serialize(this, pathToSaveFile[0]);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    }

    public static void main(String[] args) {

    }
}
