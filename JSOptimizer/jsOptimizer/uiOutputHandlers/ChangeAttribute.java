package uiOutputHandlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import uiDataPreparators.AttributeChecker;
import uiDataPreparators.JSObjectsCreator;
import userInterface.LogObserver;
import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;
import jsObjectType.NumberUnitJSObject;
/**
 * ChangeAttribute class contains methods for each type of attributes to be changed.
 * The changeType method receives the current .cfg file line and creates a new one with the values
 * contained in doubleVar (output of JMetal). Flowingly, replaces the line at the .cfg file
 * as well as at the attribute object.
 *
 * @author dimitris.katsios
 *
 */


public class ChangeAttribute {
    private static String pathToLog = JSObjectsCreator.pathToLog;
    private static final String TARGET_PATH = JSObjectsCreator.targetPath;
    private static ArrayList<String> value = new ArrayList<>();

    public static int[] changeBoolean(String line, JSObjectAttribute<Boolean> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        String val = (doubleVar[index[0]] == 1) ? "TRUE" : "FALSE";
        value.add(val);
        writeToLog(attribute, value);
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { " + val + " }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {1,0};
    }
    public static int[] changeBooleanList(String line, JSObjectAttribute<ArrayList<Boolean>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String val = (doubleVar[index[0] + i] == 1) ? "TRUE" : "FALSE";
            value.add(val);
            newLine.append(" " + val + " ");
        }
        writeToLog(attribute, value);
        newLine.append(" }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {attribute.getValue().size(), 0};
    }
    public static int[] changeInt(String line, JSObjectAttribute<Integer> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { " + doubleVar[index[0]] + " }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        value.add(String.valueOf(doubleVar[index[0]]));
        writeToLog(attribute, value);
        attribute.setLine(newLine.toString());
        return new int[] {1,0};
    }
    public static int[] changeIntList(String line, JSObjectAttribute<ArrayList<Integer>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String val = String.valueOf(doubleVar[index[0] + i]);
            newLine.append(" " + val + " ");
            value.add(val);
        }
        writeToLog(attribute, value);
        newLine.append(" }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {attribute.getValue().size(), 0};
    }
    public static int[] changeIntUnit(String line, JSObjectAttribute<NumberUnitJSObject<Integer, String>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        value.add(String.valueOf(doubleVar[index[0]]));
        writeToLog(attribute, value);
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { " + doubleVar[index[0]] + "  " + attribute.getValue().getUnit() + " }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {1,0};
    }
    public static int[] changeIntUnitList(String line, JSObjectAttribute<ArrayList<NumberUnitJSObject<Integer, String>>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String val = String.valueOf(doubleVar[index[0] + i]);
            value.add(val);
            newLine.append(" " + val + " ");
            if (!attribute.getValue().get(i).equals(null)) newLine.append(attribute.getValue().get(i) + " ");
        }
        writeToLog(attribute, value);
        newLine.append(" }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {attribute.getValue().size(), 0};
    }
    public static int[] changeDouble(String line, JSObjectAttribute<Double> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        value.add(String.valueOf(doubleVar[index[1]]));
        writeToLog(attribute, value);
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { " + doubleVar[index[1]] + " }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {0,1};
    }
    public static int[] changeDoubleList(String line, JSObjectAttribute<ArrayList<Double>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String val = String.valueOf(doubleVar[index[1] + i]);
            newLine.append(" " + val + " ");
            value.add(val);
        }
        writeToLog(attribute, value);
        newLine.append(" }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {0, attribute.getValue().size()};
    }
    public static int[] changeDoubleUnit(String line, JSObjectAttribute<NumberUnitJSObject<Double, String>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { " + doubleVar[index[1]] + "  " + attribute.getValue().getUnit() + " }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        value.add(String.valueOf(doubleVar[index[1]]));
        writeToLog(attribute, value);
        return new int[] {0,1};
    }
    public static int[] changeDoubleUnitList(String line, JSObjectAttribute<ArrayList<NumberUnitJSObject<Double, String>>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String val = String.valueOf(doubleVar[index[1] + i]);
            newLine.append(" " + val + " ");
            if (!attribute.getValue().get(i).equals(null)) newLine.append(attribute.getValue().get(i) + " ");
            value.add(val);
        }
        writeToLog(attribute, value);
        newLine.append(" }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {0, attribute.getValue().size()};
    }
    public static int[] changeJsObject(String line, JSObjectAttribute<String> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        String val = attribute.getObjList().get((int) doubleVar[index[0]]).getName();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { " + val + " }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        //		System.out.println(attribute.getJSObject().getName() + " " + attribute.getName() + " : " + val + "\n");
        //		System.out.println(val);
        value.add(val);
        writeToLog(attribute, value);
        return new int[] {1,0};
    }
    public static int[] changeJsObjectList(String line, JSObjectAttribute<ArrayList<JSObject>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String val = attribute.getObjArList().get(i).get((int) doubleVar[index[0] + i]).getName();
            newLine.append(" " + val + " ");
            value.add(val);
        }
        writeToLog(attribute, value);
        newLine.append(" }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {attribute.getValue().size(), 0};
    }
    public static int[] changeJsObjectDouble(String line, JSObjectAttribute<NumberUnitJSObject<Double, JSObject>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        String val1 = attribute.getObjList().get((int) doubleVar[index[0]]).getName();
        String val2 = String.valueOf(doubleVar[index[1]]);
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { " + val1 + "  " + val2 + " }");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        value.add(val1); value.add(val2);
        writeToLog(attribute, value);
        return new int[] {1,1};
    }
    public static int[] changeJsObjectDoubleList(String line, JSObjectAttribute<ArrayList<NumberUnitJSObject<Double, JSObject>>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String obj = attribute.getObjArList().get(i).get((int)doubleVar[index[0] + i]).getName();
            double doub =  doubleVar[index[1 + i]];
            newLine.append("{ " + obj + " " + doub + " } ");
            value.add(obj); value.add(String.valueOf(doub));
        }
        writeToLog(attribute, value);
        newLine.append("}");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {attribute.getValue().size(), attribute.getValue().size()};
    }
    public static int[] changeNumberOfUnits(String line, JSObjectAttribute<ArrayList<String>> attribute, double[] doubleVar, int[] index) {
        StringBuilder newLine = new StringBuilder();
        newLine.append(attribute.getJSObject().getName() + " " + attribute.getName() + " { ");
        int numberCounter = 0;
        for(int i = 0; i < attribute.getValue().size(); i++) {
            String obj;
            if (AttributeChecker.isDouble(attribute.getValue().get(i))) {
                obj = String.valueOf(doubleVar[index[0] + i]);
                newLine.append("{ " + obj + " } ");
                //				numberCounter++;
            } else {
                obj = attribute.getObjArList().get(i - numberCounter).get((int) doubleVar[index[0] + i]).getName();
                newLine.append("{ " + obj + " } ");
            }
            value.add(obj);
        }
        writeToLog(attribute, value);
        newLine.append("}");
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {attribute.getValue().size(), 0};
    }
    public static int[] changeJsObjectExpression(String line, JSObjectAttribute<ArrayList<JSObject>> attribute, double[] doubleVar, int[] index) {
        String[] valueArray = line.split(" ", 3);
        String valueString = valueArray[2];
        valueString = valueString.replaceAll("^\\{ *| *\\}$", "");
        ArrayList<JSObject> currentList = returnJSObjectExpression(valueString);

        StringBuilder newLine = new StringBuilder(line);
        int fromIndex = 0;
        int thisIndex = 0;
        for (int i = 0; i < currentList.size(); i++) {
            String val = attribute.getObjArList().get(i).get((int) doubleVar[index[0] + i]).getName();
            thisIndex = newLine.indexOf(currentList.get(i).getName(), fromIndex);
            newLine.replace(thisIndex, thisIndex + currentList.get(i).getName().length(), val);
            fromIndex = thisIndex + 1;
            value.add(val);
        }
        writeToLog(attribute, value);
        ReplaceAtCfgFile.replaceSelected(TARGET_PATH, line, newLine.toString());
        attribute.setLine(newLine.toString());
        return new int[] {attribute.getValue().size(), 0};
    }

    //method to handle expression attributes
    public static ArrayList<JSObject> returnJSObjectExpression(String value) {
        ArrayList<JSObject> valueList = new ArrayList<>();
        String[] valueSplitted = value.split(" *\\[ *| *\\] *");
        for (int i = 1; i < valueSplitted.length; i++) {
            for(JSObject posObj: InputsCreator.getJsObjectList()) {
                if(posObj.getName().equals(valueSplitted[i])) valueList.add(posObj);
            }
        }
        return valueList;
    }

    @SuppressWarnings("rawtypes")
    public static void writeToLog(JSObjectAttribute attribute, ArrayList<String> value) {
        if (LogObserver.optimizationCompleted == false) {
            value.clear();
            return;
        }
        StringBuilder output = CfgChanger.output;
        output.append(attribute.getJSObject().getName() + "." + attribute.getName());
        for(String str: value) output.append("\t " + str);
        value.clear();

        try(FileWriter fw = new FileWriter(pathToLog, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
        {
            out.println(output);
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) throws IOException {

    }
}
