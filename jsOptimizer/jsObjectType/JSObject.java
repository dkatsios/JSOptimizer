package jsObjectType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *JSObject is the type of each entity of the model (eg. EntityGenerator1).
 *Each JSObject has its type, name and attributes.
 *
 * @author dimitris.katsios
 *
 */

@SuppressWarnings("serial")
public class JSObject implements Serializable{
    private static ArrayList<JSObject> jsObjectList = new ArrayList<>();
    private String type;
    private String name;
    ArrayList<JSObjectAttribute<Object>> attributeList = new ArrayList<>();

    public JSObject(String type, String name) {
        this.type = type;
        this.name = name;
        jsObjectList.add(this);
    }

    public JSObject(String type) {
        this(type, null);
    }

    public JSObject() {
        this(null, null);
    }

    public String getName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.name = type;
    }

    public void addAttribute(JSObjectAttribute<Object> attribute) {
        this.attributeList.add(attribute);
    }
    public void removeAttribute(JSObjectAttribute<Object> attribute) {
        this.attributeList.remove(attribute);
    }

    public ArrayList<JSObjectAttribute<Object>> getAttributeList() {
        return this.attributeList;
    }

    public boolean isSameObject(JSObject object) {
        return (this.name.equals(object.name) && this.type.equals(object.type)) ? true : false;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static void main(String[] args) {

    }
}