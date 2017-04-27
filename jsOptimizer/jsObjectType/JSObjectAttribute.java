package jsObjectType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * JSObjectAttribute is the type of attributes of JSObjects.
 * Each instance has its name, type, value of type <T>, the jsObject at which it belongs
 * the .cfg file line that generated it, min and max values (or lists of them)
 * and objList (of list of them) if their values are of type JSObjects.
 *
 * @author dimitris.katsios
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class JSObjectAttribute<T>  implements Serializable{
    private JSObject jsObject;
    private String name;
    private String type;
    private T value;
    private String line;
    private double minValue;
    private double maxValue;
    public ArrayList<Double> minValueList = new ArrayList<>();
    public ArrayList<Double> maxValueList = new ArrayList<>();
    public ArrayList<JSObject> objList = new ArrayList<>();
    public ArrayList<ArrayList<JSObject>> objArList;
    private String valueLine;

    public JSObjectAttribute(String name, T value, String unit) {
        this.name = name;
        this.value = value;
        this.minValue = 0;
        this.maxValue = 1;
        objList = new ArrayList<>();

    }
    public JSObjectAttribute(String name, T value) {
        this(name, value, null);
    }
    public JSObjectAttribute(String name) {
        this(name, null);
    }
    public JSObjectAttribute() {
        this(null);
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setValue(T value) {
        this.value = value;
    }
    public T getValue() {
        return this.value;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return this.type;
    }

    public void setLine(String line) {
        this.line = line;
    }
    public String getLine() {
        return this.line;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
    public double getMinValue() {
        return this.minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    public double getMaxValue() {
        return this.maxValue;
    }

    public void setMinValueList(ArrayList<Double> minValueList) {
        this.minValueList = minValueList;
    }
    public ArrayList<Double> getMinValueList() {
        return this.minValueList;
    }

    public void setMaxValueList(ArrayList<Double> maxValueList) {
        this.maxValueList = maxValueList;
    }
    public ArrayList<Double> getMaxValueList() {
        return this.maxValueList;
    }

    public void setObjList(ArrayList<JSObject> objList) {
        this.objList = objList;
    }
    public ArrayList<JSObject> getObjList() {
        return this.objList;
    }

    public void setJSObject(JSObject jsObject) {
        this.jsObject = jsObject;
    }
    public JSObject getJSObject() {
        return this.jsObject;
    }
    public void addToObjList(JSObject obj) {
        objList.add(obj);
    }
    public void removeFromObjList(JSObject obj) {
        if(objList.size() == 0) return;
        for (int i = 0; i < objList.size(); i++) {
            if (objList.get(i) == obj) objList.remove(obj);
        }
    }

    public void setObjArList(ArrayList<ArrayList<JSObject>> objArList) {
        this.objArList = objArList;
    }
    public ArrayList<ArrayList<JSObject>> getObjArList() {
        return this.objArList;
    }
    public void addToObjArList(int index, JSObject obj) {
        this.objArList.get(index).add(obj);
    }
    public void removeFromObjArList(int index, JSObject obj) {
        if(objArList.size() <= index) return;

        for (int i = 0; i < objArList.get(index).size(); i++) {
            if (objArList.get(index).get(i) == obj) {
                objArList.get(index).remove(obj);
                return;
            }
        }
    }

    public String getValueLine() {
        return valueLine;
    }
    public void setValueLine(String valueLine) {
        this.valueLine = valueLine;
    }

    public static void main(String[] args) {

    }

    public String printMinValue() {
        switch (type) {
        case "boolean" : case "int" : case "int_unit" : case "double" : case "double_unit" : case "jsObject" :
            return String.valueOf(minValue);
        default :
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < minValueList.size(); i++) {
                str.append(minValueList.get(i)); str.append(" ");
            }
            return String.valueOf(str);
        }
    }

    public String printMaxValue() {
        switch (type) {
        case "boolean" : case "int" : case "int_unit" : case "double" : case "double_unit" : case "jsObject" :
            return String.valueOf(maxValue);
        default :
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < maxValueList.size(); i++) {
                str.append(maxValueList.get(i)); str.append(" ");
            }
            return String.valueOf(str);
        }
    }

}
