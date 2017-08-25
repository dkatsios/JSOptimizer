package uiDataPreparators;

import java.io.IOException;
import java.util.ArrayList;

import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;

/**
 *This class creates the attributes of each JSObject.
 *They are categorized according to its type and
 *if necessary a minValueList, a maxValueList and an objArList is created.
 *Also the output list of JaamSim is populated.
 *
 * @author dimitris.katsios
 *
 */
public class AttributeSetter {

    private static ArrayList<JSObject> jsObectsList = new ArrayList<>();
    private static String outputsLine;

    private final static String BOOLEAN = "boolean";
    private final static String BOOLEAN_LIST = "boolean_list";

    private final static String INTEGER = "int";
    private final static String INTEGER_LIST = "int_list";
    private final static String INTEGER_UNIT = "int_unit";
    private final static String INTEGER_LIST_UNIT = "int_unit_list";

    private final static String DOUBLE = "double";
    private final static String DOUBLE_LIST = "double_list";
    private final static String DOUBLE_UNIT = "double_unit";
    private final static String double_unit_list = "double_unit_list";

    private final static String JSObJECT = "jsObject";
    private final static String JSObJECT_LIST = "jsObject_list";
    private final static String JSObJECT_DOUBLE = "jsObject_double";
    private final static String JSObJECT_DOUBLE_LIST = "jsObject_double_list";

    private final static String NUMBER_OF_UNITS = "numberOfUnits";

    private final static String JSObJECT_EXPRESSION = "jsObject_expression";


    //assign each attribute to the appropriate JSObject
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void setAttributes(ArrayList<ArrayList<String>> cfgList) {
        jsObectsList = JSObjectsCreator.getJSObcetsList();
        for (ArrayList<String> cfgLine: cfgList) {
            if (excludeFromAttributes(cfgLine)) continue;
            JSObjectAttribute attribute = new JSObjectAttribute();
            attribute = createAttribute(cfgLine.get(1), cfgLine.get(2), cfgLine.get(3));
            if (attribute == null) continue;
            attributeToObject(attribute, cfgLine);
            populateAttributeLists(attribute);
        }
    }

    //creates the attributes according to its types
    public static JSObjectAttribute<Object> createAttribute(String name, String value, String line) {
        JSObjectAttribute<Object> attribute = new JSObjectAttribute<>();
        attribute.setName(name);
        attribute.setLine(line);
        value = value.replaceAll("^\\{ | \\}$", "");
        attribute.setValueLine(value);

        if (AttributeChecker.isBoolean(value)) {
            attribute.setValue(AttributeChecker.returnBoolean(value));
            attribute.setType(BOOLEAN);
            return attribute;
        }
        else if (AttributeChecker.isBooleanList(value)) {
            attribute.setValue(AttributeChecker.returnBooleanList(value));
            attribute.setType(BOOLEAN_LIST);
            return attribute;
        }
        else if (AttributeChecker.isInteger(value)){
            attribute.setValue(AttributeChecker.returnInteger(value));
            attribute.setType(INTEGER);
            return attribute;
        }
        else if (AttributeChecker.isDouble(value)){
            attribute.setValue(AttributeChecker.returnDouble(value));
            attribute.setType(DOUBLE);
            return attribute;
        }
        else if (AttributeChecker.isJSObject(value)){
            attribute.setValue(AttributeChecker.returnJSObject(value));
            attribute.setType(JSObJECT);
            return attribute;
        }
        else if (AttributeChecker.isJSObjectList(value)){
            attribute.setValue(AttributeChecker.returnJSObjectList(value));
            attribute.setType(JSObJECT_LIST);
            return attribute;
        }
        else if (AttributeChecker.isIntegerUnit(value)){
            attribute.setValue(AttributeChecker.returnIntegerUnit(value));
            attribute.setType(INTEGER_UNIT);
            return attribute;
        }
        else if (AttributeChecker.isDoubleUnit(value)){
            attribute.setValue(AttributeChecker.returnDoubleUnit(value));
            attribute.setType(DOUBLE_UNIT);
            return attribute;
        }
        else if (AttributeChecker.isIntegerList(value)){
            attribute.setValue(AttributeChecker.returnIntegerList(value));
            attribute.setType(INTEGER_LIST);
            return attribute;
        }
        else if (AttributeChecker.isDoubleList(value)){
            attribute.setValue(AttributeChecker.returnDoubleList(value));
            attribute.setType(DOUBLE_LIST);
            return attribute;
        }
        else if (AttributeChecker.isIntegerUnitList(value)){
            attribute.setValue(AttributeChecker.returnIntegerUnitList(value));
            attribute.setType(INTEGER_LIST_UNIT);
            return attribute;
        }
        else if (AttributeChecker.isDoubleUnitList(value)){
            attribute.setValue(AttributeChecker.returnDoubleUnitList(value));
            attribute.setType(double_unit_list);
            return attribute;
        }
        else if (AttributeChecker.isJSObjectDouble(value)){
            attribute.setValue(AttributeChecker.returnJSObjectDouble(value));
            attribute.setType(JSObJECT_DOUBLE);
            return attribute;
        }
        else if (AttributeChecker.isJSObjectDoubletList(value)){
            attribute.setValue(AttributeChecker.returnJSObjectDoubleList(value));
            attribute.setType(JSObJECT_DOUBLE_LIST);
            return attribute;
        }
        else if (AttributeChecker.isNumberOfUnits(value)){
            attribute.setValue(AttributeChecker.returnNumberOfUnits(value));
            attribute.setType(NUMBER_OF_UNITS);
            return attribute;
        }
        else if (AttributeChecker.isJSObjectExpression(value)){
            attribute.setValue(AttributeChecker.returnJSObjectExpression(value));
            attribute.setType(JSObJECT_EXPRESSION);
            return attribute;
        } return null;
    }

    //assigns each attribute to the right JSObject
    public static void attributeToObject(JSObjectAttribute<Object> attribute,ArrayList<String> cfgLine) {
        for (int i = 0; i < jsObectsList.size(); i++) {
            if (jsObectsList.get(i).getName().equals(cfgLine.get(0))) {
                attribute.setJSObject(jsObectsList.get(i));
                jsObectsList.get(i).addAttribute(attribute);
            }
        }
    }

    //list of attribute types that are excluded from the procedure
    public static boolean excludeFromAttributes(ArrayList<String> cfgLine) {
        if (cfgLine.size() != 4) return true;
        if (cfgLine.get(0).equalsIgnoreCase("Define")) return true;
        if (cfgLine.get(1).equalsIgnoreCase("Position")) return true;
        if (cfgLine.get(1).equalsIgnoreCase("Points")) return true;
        if (cfgLine.get(1).equalsIgnoreCase("Alignment")) return true;
        if (cfgLine.get(1).equalsIgnoreCase("UnitType")) return true;
        if (cfgLine.get(1).equalsIgnoreCase("UnitTypeList")) return true;
        if (cfgLine.get(0).equalsIgnoreCase("Simulation")) return true;
        if (cfgLine.get(1).equalsIgnoreCase("Size")) return true;
        if (cfgLine.get(1).equalsIgnoreCase("TextHeight")) return true;
        return false;
    }

    //For each attribute that has more than one inner values, this method creates and populates a minValueList
    //and a maxValueList for the user to set limits. The same holds for the objArLists.
    @SuppressWarnings("rawtypes")
    public static <T extends ArrayList> void populateAttributeLists(JSObjectAttribute<T> attribute) {
        ArrayList<String> includedTypes = new ArrayList<>();
        includedTypes.add(BOOLEAN_LIST); includedTypes.add(INTEGER_LIST); includedTypes.add(INTEGER_LIST_UNIT);
        includedTypes.add(DOUBLE_LIST); includedTypes.add(double_unit_list); includedTypes.add(JSObJECT_LIST);
        includedTypes.add(JSObJECT_DOUBLE); includedTypes.add(JSObJECT_DOUBLE_LIST); includedTypes.add(NUMBER_OF_UNITS);
        includedTypes.add(JSObJECT_EXPRESSION);

        if (!includedTypes.contains(attribute.getType())) return;
        for (int i = 0; i < attribute.getValue().size(); i++) {
            attribute.minValueList.add(0.0);
            attribute.maxValueList.add(1.0);
            if (attribute.getType().equals(JSObJECT_DOUBLE_LIST)) {
                attribute.minValueList.add(0.0);
                attribute.maxValueList.add(1.0);
            }
        }
        if (attribute.getType().equals(JSObJECT_DOUBLE)) {
            attribute.minValueList.add(0.0);
            attribute.maxValueList.add(1.0);
        }
        if (attribute.getType().equals(JSObJECT_LIST) || attribute.getType().equals(JSObJECT_EXPRESSION) ||
                attribute.getType().equals(NUMBER_OF_UNITS) || attribute.getType().equals(JSObJECT_DOUBLE_LIST)){
            attribute.objArList = new ArrayList<>();
            for (int i = 0; i < attribute.getValue().size(); i++) attribute.objArList.add(new ArrayList<JSObject>());
        }

    }

    // Creates and returns the JaamSim outputs list that will be either objectives or constraints
    public static String[] returnOutputList(String cfgPath) throws IOException {
        new CfgFileReader();
        ArrayList<ArrayList<String>> cfgList = CfgFileReader.getCfgList(cfgPath);
        for (ArrayList<String> cfgLine: cfgList) {
            if(cfgLine.get(0).equalsIgnoreCase("Simulation") && cfgLine.get(1).equalsIgnoreCase("RunOutputList")) outputsLine = cfgLine.get(2);
        }
        if (outputsLine == null) System.out.println("No outputs have been defined at Simulation RunOutputList");
        String[] value = outputsLine.replaceAll("^ *\\{ *| *\\} *$| *|\t*", "").replaceAll("^\\{|\\}$", "").split(" *\\} *\\{ *");
        return value;
    }

    public static void main(String[] args) {

    }

}
