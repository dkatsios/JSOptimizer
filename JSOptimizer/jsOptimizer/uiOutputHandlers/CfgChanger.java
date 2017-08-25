package uiOutputHandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import uiDataPreparators.JSObjectsCreator;
import jsObjectType.JSObjectAttribute;

/**
 * CfgChanger class takes the outputs variable values from JMetal
 * and produces the new .cfg file for JaamSim to run.
 *
 * @author dimitris.katsios
 *
 */
public class CfgChanger {
    private static String pathToAttributeList = JSObjectsCreator.pathToAttributeList;
    private static String pathToCfgFile = JSObjectsCreator.targetPath;
    public static StringBuilder output = new StringBuilder();

    //main method that calls the others. Uses the serialized file attrList with the attributes to be changed
    @SuppressWarnings("rawtypes")
    public static void cfgChange(double[] doubleVar) throws IOException {
        ArrayList<JSObjectAttribute> attrList;
        attrList = JsSerializer.deserialize(pathToAttributeList);
        replaceValues(attrList, doubleVar);
        JsSerializer.serialize(attrList, pathToAttributeList);
    }

    //replaces the values at the .cfg file according to the doubleVar content (output double[] from JMetal)
    @SuppressWarnings("rawtypes")
    public static void replaceValues(ArrayList<JSObjectAttribute> attrList, double[] doubleVar) {
        String line;
        int[] index = {0, InputsCreator.getNumIntVariables()};
        for (int i = 0; i < attrList.size(); i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(pathToCfgFile)))
            {
                while ((line = br.readLine()) != null) {
                    if (line.equals(attrList.get(i).getLine())) {
                        index = replaceLine(line, attrList.get(i), doubleVar, index);
                        System.out.println(line + "\n" + attrList.get(i).getLine() + "\n");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to replace each line at the .cfg file that needs to be changed
    //the replacements takes into consideration the type of each attribute
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static int[] replaceLine(String line, JSObjectAttribute attribute, double[] doubleVar, int[] index) throws IOException {
        output = new StringBuilder();
        int[] tempIndex;
        switch (attribute.getType()) {
        case "boolean" :
            tempIndex = ChangeAttribute.changeBoolean(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "boolean_list" :
            tempIndex = ChangeAttribute.changeBooleanList(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "int" :
            tempIndex = ChangeAttribute.changeInt(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "int_list" :
            tempIndex = ChangeAttribute.changeIntList(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "int_unit" :
            tempIndex = ChangeAttribute.changeIntUnit(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "int_list_unit" :
            tempIndex = ChangeAttribute.changeIntUnitList(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "double" :
            tempIndex = ChangeAttribute.changeDouble(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "double_list" :
            tempIndex = ChangeAttribute.changeDoubleList(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "double_unit" :
            tempIndex = ChangeAttribute.changeDoubleUnit(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "double_list_unit" :
            tempIndex = ChangeAttribute.changeDoubleUnitList(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "jsObject" :
            tempIndex = ChangeAttribute.changeJsObject(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "jsObject_list" :
            tempIndex = ChangeAttribute.changeJsObjectList(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "jsObject_double" :
            tempIndex = ChangeAttribute.changeJsObjectDouble(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "jsObject_double_list" :
            tempIndex = ChangeAttribute.changeJsObjectDoubleList(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "numberOfUnits" :
            tempIndex = ChangeAttribute.changeNumberOfUnits(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;
        case "jsObject_expression" :
            tempIndex = ChangeAttribute.changeJsObjectExpression(line, attribute, doubleVar, index);
            index[0] += tempIndex[0];
            index[1] += tempIndex[1];
            return index;

        } return index;
    }
    public static void main (String args[]) {

    }

}
