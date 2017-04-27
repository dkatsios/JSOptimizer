package uiOutputHandlers;

import java.io.IOException;
import java.util.ArrayList;

import uiDataPreparators.JSObjectsCreator;
import jsObjectType.DataLogger;
import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;

/**
 * InputsCreator prepares the inputs for JMetal. Specifically, for each attribute to be changed,
 * createInputs method adds a double array of two doubles, min and max, to ArrayList<Double[]> variables
 * If a boolean attribute is chosen, 0 and 1 are assigned.
 * If a JSObject is chosen, 0 and (size - 1) are assigned, where size is the number of alternatives for this JSObject.
 *
 * The integer values (variables to be treated as integers from JMetal) are to be placed at the beginning of variables
 * while doubles (reals) to the end.
 * The attributes are also rearranged in a new ArrayList (attributesList), with the corresponding order.
 *
 * @author dimitris.katsios
 *
 */
public class InputsCreator {

    private static ArrayList<Double[]> variables = new ArrayList<Double[]>();
    private static int numberOfIntVariables;
    private static int numberOfObjectives;
    private static int numberOfConstraints;
    private static ArrayList<double[]> constraints = new ArrayList<double[]>();
    //	private static String serializationPath = "cfgFiles/JSObcetsList.ser";
    private static String pathToAttributeList = JSObjectsCreator.pathToAttributeList;
    private static int attrListIntIndex;
    private static ArrayList<JSObject> jsObjectList;
    @SuppressWarnings("rawtypes")
    private static ArrayList<JSObjectAttribute> selectedJSattributes;
    private static int[] outputindices;

    @SuppressWarnings("rawtypes")
    public static void createInputs(DataLogger dataLogger) throws IOException {
        dataLogger.getSelectedJSObjects();
        selectedJSattributes = dataLogger.getSelectedJSattributes();
        numberOfObjectives = dataLogger.getNumberOfObjectives();
        numberOfConstraints = dataLogger.getNumberOfConstraints();
        constraints = dataLogger.getConstraints();
        jsObjectList = dataLogger.getJsObjectsList();
        setOutputindices(dataLogger.getOutputindices());

        ArrayList<JSObjectAttribute> attributesList = new ArrayList<JSObjectAttribute>();
        numberOfIntVariables = 0;
        attrListIntIndex = 0;
        attributeToList(selectedJSattributes, attributesList);
        JsSerializer.serialize(attributesList, pathToAttributeList);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void attributeToList(ArrayList<JSObjectAttribute> selectedJSattributes, ArrayList<JSObjectAttribute> attributesList) {
        for (JSObjectAttribute<Object> attr: selectedJSattributes) {
            Double[] v = new Double[2];
            switch (attr.getType()) {

            case "boolean" : case "int" : case "int_unit" : case "jsObject" :

                v[0] = attr.getMinValue();
                v[1] = attr.getMaxValue();
                variables.add(numberOfIntVariables, v);
                numberOfIntVariables++;
                attributesList.add(attrListIntIndex, attr);
                attrListIntIndex++;
                break;

            case "boolean_list" : case "int_list" : case "int_list_unit" : case "jsObject_list" : case "jsObject_expression" :

                for(int i = 0; i < attr.getMaxValueList().size(); i++){
                    Double[] v1 = new Double[2];
                    v1[0] = attr.getMinValueList().get(i);
                    v1[1] = attr.getMaxValueList().get(i);
                    variables.add(numberOfIntVariables, v1);
                    numberOfIntVariables++;
                }
                attributesList.add(attrListIntIndex, attr);
                attrListIntIndex++;

                break;

            case "double" : case "double_unit" :

                v[0] = attr.getMinValue();
                v[1] = attr.getMaxValue();
                variables.add(v);
                attributesList.add(attr);
                break;

            case "double_list" : case "double_list_unit" :

                for(int i = 0; i < attr.getMaxValueList().size(); i++){
                    Double[] v1 = new Double[2];
                    v1[0] = attr.getMinValueList().get(i);
                    v1[1] = attr.getMaxValueList().get(i);
                    variables.add(v1);
                }
                attributesList.add(attr);
                break;

            case "numberOfUnits" :

                for(int i = 0; i < attr.getMaxValueList().size(); i++){
                    Double[] v1 = new Double[2];
                    v1[0] = attr.getMinValueList().get(i);
                    v1[1] = attr.getMaxValueList().get(i);
                    variables.add(numberOfIntVariables, v1);
                    numberOfIntVariables++;
                }
                attributesList.add(attrListIntIndex, attr);
                attrListIntIndex++;
                break;

            case "jsObject_double" :

                v[0] = attr.getMinValueList().get(0);
                v[1] = attr.getMaxValueList().get(0);
                variables.add(numberOfIntVariables, v);
                numberOfIntVariables++;
                attributesList.add(attrListIntIndex, attr);
                attrListIntIndex++;

                v[0] = attr.getMinValueList().get(1);
                v[1] = attr.getMaxValueList().get(1);
                variables.add(v);
                break;

            case "jsObject_double_list" :

                for(int i = 0; i < attr.getMaxValueList().size(); i+=2){
                    Double[] v1 = new Double[2];
                    v1[0] = attr.getMinValueList().get(i);
                    v1[1] = attr.getMaxValueList().get(i);
                    variables.add(numberOfIntVariables, v1);
                    numberOfIntVariables++;

                    Double[] v2 = new Double[2];
                    v2[0] = attr.getMinValueList().get(i+1);
                    v2[1] = attr.getMaxValueList().get(i+1);
                    variables.add(v2);
                }
                attributesList.add(attrListIntIndex, attr);
                attrListIntIndex++;
                break;
            }
        }
    }

    public static ArrayList<Double[]> getVariables() {
        return variables;
    }

    public static void setVariables(ArrayList<Double[]> variables) {
        InputsCreator.variables = variables;
    }

    public static int getNumberOfIntVariables() {
        return numberOfIntVariables;
    }

    public static void setNumberOfIntVariables(int numberOfIntVariables) {
        InputsCreator.numberOfIntVariables = numberOfIntVariables;
    }

    public static int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public static void setNumberOfObjectives(int numberOfObjectives) {
        InputsCreator.numberOfObjectives = numberOfObjectives;
    }

    public static int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public static void setNumberOfConstraints(int numberOfConstraints) {
        InputsCreator.numberOfConstraints = numberOfConstraints;
    }

    public static ArrayList<double[]> getConstraints() {
        return constraints;
    }

    public static void setConstraints(ArrayList<double[]> constraints) {
        InputsCreator.constraints = constraints;
    }

    public static int getNumIntVariables() {
        return numberOfIntVariables;
    }

    public static int[] getOutputindices() {
        return outputindices;
    }

    public static void setOutputindices(int[] outputindices) {
        InputsCreator.outputindices = outputindices;
    }

    public static ArrayList<JSObject> getJsObjectList() {
        return jsObjectList;
    }

    public static void setJsObjectList(ArrayList<JSObject> jsObjectList) {
        InputsCreator.jsObjectList = jsObjectList;
    }

    public static void main(String[] args) {

    }

}
