package jsObjectType;

import java.io.Serializable;

/**
 * JSOutput is the class of classJaamSim outputs.
 * Each JSOutput has a toMinimize flag, which indicates whether it is an objective value to be minimized or a constraint
 * a min and a max double value in case it is a constraint
 * and a checked flag which tells whether the specific instance has been categorized.
 *
 * @author dimitris.katsios
 *
 */
@SuppressWarnings("serial")
public class JSOutput implements Serializable {

    private boolean toMinimize;
    private int outputIndex;
    private double min = 0;
    private double max = 0;
    private String name;
    private double value;
    private boolean checked = false;

    public JSOutput(String name, int outputIndex) {
        this.name = name;
        this.outputIndex = outputIndex;
    }
    public JSOutput() {

    }

    public boolean isToMinimize() {
        return toMinimize;
    }

    public void setToMinimize(boolean toMinimize) {
        this.toMinimize = toMinimize;
    }

    public int getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(int outputIndex) {
        this.outputIndex = outputIndex;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static void main(String[] args) {

    }

}
