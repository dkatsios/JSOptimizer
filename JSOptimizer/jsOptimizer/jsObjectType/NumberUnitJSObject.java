package jsObjectType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * NumberUnitJSObject is an object type that holds a number (integer or double)
 * and a "unit" which can be either string or a JSObject.
 * Some types of attributes use this class for their values (eg. jsObject_double)
 *
 * @author dimitris
 *
 * @param <T>
 * @param <K>
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class NumberUnitJSObject<T, K> extends ArrayList implements Serializable{

    private T number;
    private K unit;

    public NumberUnitJSObject() {
        this.unit = null;
        this.number = null;
    }

    public NumberUnitJSObject( T number, K unit) {
        this.unit = unit;
        this.number = number;
    }

    public T getNumber() {
        return this.number;
    }
    public void setNumber(T number) {
        this.number = number;
    }

    public K getUnit() {
        return this.unit;
    }
    public void setUnit(K unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        if (this.getUnit() == null) return (this.getNumber().toString());
        return (this.getNumber().toString() + " " + this.getUnit().toString());
    }

    @Override
    public int size() {
        return 1;
    }
}