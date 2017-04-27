package uiDataPreparators;

import java.io.IOException;
import java.util.ArrayList;

import jsObjectType.JSObject;
import jsObjectType.NumberUnitJSObject;

/**
 * AttributeChecker class contains methods to check each value received.
 * The value is the 3rd part of the .cfg file lines (except from the define lines).
 * There are 16 different types of values accepted.
 * For each type there are two methods, named isType and returnType.
 * The first validates that the value is of type Type and the second returns the value at its right type.
 *
 * The possible types are: boolean, ArrayList<Boolean>, int, ArrayList<Integer>, double, ArrayList<Double>,
 * JSObject, ArrayList<JSObject>, NumberUnitJSObject, ArrayList<NumberUnitJSObject>, ArrayList<String>
 *
 * @author dimitris.katsios
 *
 */
public class AttributeChecker {

    private static ArrayList<JSObject> JSOBJECTLIST = JSObjectsCreator.getJSObcetsList();

    public static boolean isBoolean(String value) {
        return (value.equalsIgnoreCase("true")||value.equalsIgnoreCase("false")) ? true : false;
    }
    public static boolean returnBoolean(String value) {
        return (value.equalsIgnoreCase("true")) ? true : false;
    }

    public static boolean isBooleanList(String value) {
        String[] valueList = value.split(" +");
        if (valueList.length <= 1) return false;
        for (String elem: valueList) {
            if (!isBoolean(elem)) return false;
        }
        return true;
    }
    public static ArrayList<Boolean> returnBooleanList(String value) {
        String[] valueSplitted = value.split(" +");
        ArrayList<Boolean> valueList = new ArrayList<>();
        for (String item: valueSplitted) {
            valueList.add(Boolean.parseBoolean(item));
        }
        return valueList;
    }

    public static boolean isInteger(String value) {
        try {
            Long.parseLong(value);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    public static long returnInteger(String value) {
        return (Long.parseLong(value));
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    public static double returnDouble(String value) {
        return (Double.parseDouble(value));
    }

    public static boolean isString(String value) {
        if (value == null) return false;
        String comp = value.replaceAll("^[^a-zA-Z]", "");
        return value.equals(comp) ? true : false;
    }
    public static String returnString(String value) {
        return value;
    }

    public static boolean isJSObject(String value) {
        if (!isString(value)) return false;
        for (JSObject object: JSOBJECTLIST) {
            if (value.equals(object.getName())) return true;
        }
        return false;
    }
    public static JSObject returnJSObject(String value) {
        for (JSObject object: JSOBJECTLIST) {
            if (value.equals(object.getName())) return object;
        } return null;
    }

    public static boolean isJSObjectList(String value) {
        String[] valueList = value.split(" +");
        if (valueList.length <= 1) return false;
        for (String elem: valueList) {
            if (!isJSObject(elem)) return false;
        }
        return true;
    }
    public static ArrayList<JSObject> returnJSObjectList(String value) {
        String[] valueSplitted = value.split(" +");
        ArrayList<JSObject> valueList = new ArrayList<>();
        for (String item: valueSplitted) {
            for (JSObject object: JSOBJECTLIST) {
                if (item.equals(object.getName())) valueList.add(object);;
            }

        }
        return valueList;
    }

    public static boolean isIntegerUnit(String value) {
        String[] temp = value.split(" +");
        if (temp.length != 2) return false;
        if ((!isInteger(temp[0])) || (!isString(temp[1]))) return false;
        return true;
    }
    public static NumberUnitJSObject<Integer, String> returnIntegerUnit(String value) {
        String[] temp = value.split(" +");
        NumberUnitJSObject<Integer, String> numUnit = new NumberUnitJSObject<>();
        numUnit.setNumber(Integer.parseInt(temp[0]));
        numUnit.setUnit(temp[1]);
        return numUnit;
    }

    public static boolean isDoubleUnit(String value) {
        String[] temp = value.split(" +");
        if(temp.length != 2) return false;
        if ((!isDouble(temp[0])) || (!isString(temp[1]))) return false;
        return true;
    }
    public static NumberUnitJSObject<Double, String> returnDoubleUnit(String value) {
        String[] temp = value.split(" +");
        NumberUnitJSObject<Double, String> numUnit = new NumberUnitJSObject<>();
        numUnit.setNumber(Double.parseDouble(temp[0]));
        numUnit.setUnit(temp[1]);
        return numUnit;
    }

    public static boolean isIntegerList(String value) {
        String[] valueList = value.split(" +");
        if (valueList.length <= 1) return false;
        for (String elem: valueList) {
            if (!isInteger(elem)) return false;
        }
        return true;
    }
    public static ArrayList<Integer> returnIntegerList(String value) {
        String[] valueSplitted = value.split(" +");
        ArrayList<Integer> valueList = new ArrayList<>();
        for (String item: valueSplitted) {
            valueList.add(Integer.parseInt(item));
        }
        return valueList;
    }

    public static boolean isDoubleList(String value) {
        String[] valueList = value.split(" +");
        if (valueList.length <= 1) return false;
        for (String elem: valueList) {
            if (!isDouble(elem)) return false;
        }
        return true;
    }
    public static ArrayList<Double> returnDoubleList(String value) {
        String[] valueSplitted = value.split(" +");
        ArrayList<Double> valueList = new ArrayList<>();
        for (String item: valueSplitted) {
            valueList.add(Double.parseDouble(item));
        }
        return valueList;
    }

    public static boolean isIntegerUnitList(String value) {
        String[] valueList = value.split(" +");
        for (int i = 0; i < valueList.length - 1; i++) {
            if (!isInteger(valueList[i]) && !isString(valueList[i])) return false;
        }
        for (int i = 0; i < valueList.length - 1; i++){
            String val = valueList[i] + " " + valueList[i+1];
            if (isIntegerUnit(val)) return true;
        }
        return false;
    }

    public static ArrayList<NumberUnitJSObject<Integer, String>> returnIntegerUnitList(String value) {
        String[] valueSplitted = value.split(" +");
        ArrayList<NumberUnitJSObject<Integer, String>> intUnitList = new ArrayList<>();
        NumberUnitJSObject<Integer, String> intUnit = new NumberUnitJSObject<>();
        for (int i = 0; i < valueSplitted.length; i++) {
            intUnit.setNumber(Integer.parseInt(valueSplitted[i]));
            intUnitList.add(intUnit);
            if (i < valueSplitted.length - 1 && isString(valueSplitted[i+1])) {
                intUnitList.get(intUnitList.size() - 1).setUnit(valueSplitted[i+1]);
                i++;
            }
        }
        return intUnitList;
    }

    public static boolean isDoubleUnitList(String value) {
        String[] valueList = value.split(" +");
        if (valueList.length < 3) return false;
        for (int i = 0; i < valueList.length - 1; i++) {
            if (!isDouble(valueList[i]) && !isString(valueList[i])) return false;
        }
        for (int i = 0; i < valueList.length - 1; i++){
            String val = valueList[i] + " " + valueList[i+1];
            if (isDoubleUnit(val)) return true;
        }
        return false;
    }

    public static ArrayList<NumberUnitJSObject<Double, String>> returnDoubleUnitList(String value) {
        String[] valueSplitted = value.split(" +");
        ArrayList<NumberUnitJSObject<Double, String>> doubleUnitList = new ArrayList<>();
        for (int i = 0; i < valueSplitted.length; i++) {
            NumberUnitJSObject<Double, String> doubleUnit = new NumberUnitJSObject<>();
            doubleUnit.setNumber(Double.parseDouble(valueSplitted[i]));
            doubleUnitList.add(doubleUnit);
            if (i < valueSplitted.length - 1 && isString(valueSplitted[i+1])) {
                doubleUnitList.get(doubleUnitList.size() - 1).setUnit(valueSplitted[i+1]);
                i++;
            }
        }
        return doubleUnitList;
    }

    public static boolean isJSObjectDouble(String value) {
        String[] temp = value.split(" +");
        if (temp.length != 2) return false;
        if ((!isJSObject(temp[0])) || (!isDouble(temp[1]))) return false;
        return true;
    }
    public static NumberUnitJSObject<Double, JSObject> returnJSObjectDouble(String value) {
        String[] temp = value.split(" +", 2);
        for (JSObject object: JSOBJECTLIST) {
            if (temp[0].equals(object.getName())) return new NumberUnitJSObject<>(Double.parseDouble(temp[1]), object);
        }
        return null;
    }

    public static boolean isJSObjectDoubletList(String value) {
        value = value.replaceAll("^ *\\{ *| *\\} *$", "");
        String[] valueList = value.split(" *\\} *\\{ *");
        if (valueList.length < 2) return false;
        for (int i = 0; i < valueList.length; i++) {
            if(!isJSObjectDouble(valueList[i])) return false;
        }
        return true;
    }
    public static ArrayList<NumberUnitJSObject<Double, JSObject>> returnJSObjectDoubleList(String value) {
        value = value.replaceAll("^ *\\{ *| *\\} *$", "");
        String[] valueSplitted = value.split(" *\\} *\\{ *");
        ArrayList<NumberUnitJSObject<Double, JSObject>> valueList = new ArrayList<>();
        for (String val: valueSplitted) {
            valueList.add(returnJSObjectDouble(val));
        }
        return valueList;
    }

    public static boolean isNumberOfUnits(String value) {
        String[] valueList = value.split(" +\\} +\\{ +|^ *\\{ *| *\\} *");
        if(valueList.length < 2) return false;
        for (int i = 1; i < valueList.length; i++) {
            valueList[i] = valueList[i].replaceAll(" *' *", "");
            if (!(isJSObject(valueList[i]) || isDouble(valueList[i]))) {
                return false;
            }
        }
        return true;
    }
    public static ArrayList<String> returnNumberOfUnits(String value) {
        String[] valueSplitted = value.split(" +\\} +\\{ +|^ *\\{ *| *\\} *");
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 1; i < valueSplitted.length; i++) {
            valueSplitted[i] = valueSplitted[i].replaceAll(" *' *", "");
            valueList.add(valueSplitted[i]);
        }
        return valueList;
    }

    public static boolean isJSObjectExpression(String value) {
        String[] valueList = value.split(" *\\[ *| *\\] *");
        if(valueList.length < 1) return false;
        for (int i = 1; i < valueList.length; i++) {
            if (isJSObject(valueList[i])) {
                return true;
            }
        }
        return false;
    }
    public static ArrayList<JSObject> returnJSObjectExpression(String value) {
        ArrayList<JSObject> valueList = new ArrayList<>();
        String[] valueSplitted = value.split(" *\\[ *| *\\] *");
        for (int i = 1; i < valueSplitted.length; i++) {
            if (isJSObject(valueSplitted[i])) {
                for(JSObject posObj: JSOBJECTLIST) {
                    if(posObj.getName().equals(valueSplitted[i])) valueList.add(posObj);
                }
            }
        }
        return valueList;
    }

    public static void main(String[] args) throws IOException {
        String num = "214748360";
        if(isInteger(num)) System.out.println("yes");
        else System.out.println("no");
    }

}
