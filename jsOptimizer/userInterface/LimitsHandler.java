package userInterface;

import java.util.ArrayList;

import uiDataPreparators.AttributeChecker;
import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;
import jsObjectType.NumberUnitJSObject;

/**
 * Class with helping methods for LimitsDefiner to handle user's choices.
 *
 * @author dimitris.katsios
 *
 */
public class LimitsHandler {

    public static void isBoolean(JSObjectAttribute<Object> selectedAttribute, LimitsDefiner limitsDefiner) {
        limitsDefiner.minValue.setEnabled(false);
        limitsDefiner.maxValue.setEnabled(false);
    }

    public static void isInt(JSObjectAttribute<Object> selectedAttribute, LimitsDefiner limitsDefiner) {
        setMinMax(selectedAttribute, limitsDefiner);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void isIntList(JSObjectAttribute selectedAttribute, LimitsDefiner limitsDefiner) {
        setMinMax(selectedAttribute, limitsDefiner);

        limitsDefiner.combo.setEnabled(true);
        populateCombo(selectedAttribute, limitsDefiner);
    }

    public static void isJSObject(JSObjectAttribute<Object> selectedAttribute, LimitsDefiner limitsDefiner) {
        limitsDefiner.minValue.setEnabled(false);
        limitsDefiner.maxValue.setEnabled(false);

        limitsDefiner.leftList.setEnabled(true);
        limitsDefiner.rightList.setEnabled(true);
        populateLists(selectedAttribute, limitsDefiner);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void isJSObjectList(JSObjectAttribute selectedAttribute, LimitsDefiner limitsDefiner) {
        limitsDefiner.combo.setEnabled(true);
        populateCombo(selectedAttribute, limitsDefiner);

        limitsDefiner.minValue.setEnabled(false);
        limitsDefiner.maxValue.setEnabled(false);

        limitsDefiner.leftList.setEnabled(true);
        limitsDefiner.rightList.setEnabled(true);
        populateLists(selectedAttribute, limitsDefiner);
    }

    public static void isJSObjectDouble(JSObjectAttribute<Object> selectedAttribute, LimitsDefiner limitsDefiner) {
        double min = selectedAttribute.minValueList.get(0);
        double max = selectedAttribute.maxValueList.get(0);

        limitsDefiner.minValue.setText(String.valueOf(min));
        limitsDefiner.maxValue.setText(String.valueOf(max));

        limitsDefiner.leftList.setEnabled(true);
        limitsDefiner.rightList.setEnabled(true);
        populateSingleObject(selectedAttribute.objList, limitsDefiner);
    }

    public static void isJSObjectDoubleList(@SuppressWarnings("rawtypes") JSObjectAttribute<ArrayList> selectedAttribute, LimitsDefiner limitsDefiner) {
        limitsDefiner.combo.setEnabled(true);
        populateCombo(selectedAttribute, limitsDefiner);

        int index = limitsDefiner.combo.getSelectionIndex();
        if (index == -1) return;

        double min = selectedAttribute.minValueList.get(index*2);
        double max = selectedAttribute.maxValueList.get(index*2);

        limitsDefiner.minValue.setText(String.valueOf(min));
        limitsDefiner.maxValue.setText(String.valueOf(max));

        limitsDefiner.leftList.setEnabled(true);
        limitsDefiner.rightList.setEnabled(true);
        populateSingleObject(selectedAttribute.objArList.get(index), limitsDefiner);
    }

    public static void isNumberOfUnits(JSObjectAttribute<ArrayList<String>> selectedAttribute, LimitsDefiner limitsDefiner) {

        limitsDefiner.combo.setEnabled(true);
        populateCombo(selectedAttribute, limitsDefiner);

        limitsDefiner.minValue.setEnabled(false);
        limitsDefiner.maxValue.setEnabled(false);

        limitsDefiner.leftList.setEnabled(false);
        limitsDefiner.rightList.setEnabled(false);
    }

    public static void handleNumberOfUnits(JSObjectAttribute<ArrayList<String>> selectedAttribute, LimitsDefiner limitsDefiner) {

        int index = limitsDefiner.combo.getSelectionIndex();
        if (index == -1) return;

        String value = selectedAttribute.getValue().get(index);
        if (AttributeChecker.isDouble(value)) {
            setMinMax(selectedAttribute, limitsDefiner);
            limitsDefiner.minValue.setEnabled(true);
            limitsDefiner.maxValue.setEnabled(true);

            limitsDefiner.leftList.setEnabled(false);
            limitsDefiner.rightList.setEnabled(false);

        } else {
            limitsDefiner.minValue.setEnabled(false);
            limitsDefiner.maxValue.setEnabled(false);

            limitsDefiner.leftList.setEnabled(true);
            limitsDefiner.rightList.setEnabled(true);
            populateSingleObject(selectedAttribute.objArList.get(index), limitsDefiner);
        }
    }

    public static void handleJSObjectDoubleList(JSObjectAttribute<ArrayList<NumberUnitJSObject<Double, JSObject>>> selectedAttribute, LimitsDefiner limitsDefiner) {
        int index = limitsDefiner.combo.getSelectionIndex();
        if (index == -1) return;

        double min = selectedAttribute.minValueList.get(index*2);
        double max = selectedAttribute.maxValueList.get(index*2);

        limitsDefiner.minValue.setText(String.valueOf(min));
        limitsDefiner.maxValue.setText(String.valueOf(max));

        limitsDefiner.leftList.setEnabled(true);
        limitsDefiner.rightList.setEnabled(true);
        populateSingleObject(selectedAttribute.objArList.get(index), limitsDefiner);
    }

    @SuppressWarnings("rawtypes")
    public static <T extends ArrayList> void populateCombo(JSObjectAttribute<T> selectedAttribute, LimitsDefiner limitsDefiner) {
        String[] comboValues = new String[selectedAttribute.getValue().size()];
        switch (selectedAttribute.getType()) {
        case "int_list" : case "int_unit_list" : case "double_list" : case "double_unit_list" :
        case "jsObject_list" : case "jsObject_double_list" : case "jsObject_expression" : case "numberOfUnits" :
            for (int i = 0; i < selectedAttribute.getValue().size(); i++) {
                comboValues[i] = selectedAttribute.getValue().get(i).toString();
            }
        }
        limitsDefiner.combo.setItems(comboValues);
    }

    @SuppressWarnings("rawtypes")
    public static void setMinMax(JSObjectAttribute selectedAttribute, LimitsDefiner limitsDefiner) {
        if (limitsDefiner.combo.isEnabled()) {
            int index = limitsDefiner.combo.getSelectionIndex();
            if (index == -1) return;
            double min = (double) selectedAttribute.getMinValueList().get(index);
            double max = (double) selectedAttribute.getMaxValueList().get(index);
            if (selectedAttribute.getType().equals("jsObject_double")) {
                min = (double) selectedAttribute.getMinValueList().get(index * 2);
                max = (double) selectedAttribute.getMaxValueList().get(index * 2);
            }

            limitsDefiner.minValue.setText(String.valueOf(min));
            limitsDefiner.maxValue.setText(String.valueOf(max));
        } else {
            double min = selectedAttribute.getMinValue();
            double max = selectedAttribute.getMaxValue();
            if (selectedAttribute.getType().equals("jsObject_double")) {
                min = (double) selectedAttribute.getMinValueList().get(0);
                max = (double) selectedAttribute.getMaxValueList().get(0);
            }

            limitsDefiner.minValue.setText(String.valueOf(min));
            limitsDefiner.maxValue.setText(String.valueOf(max));
        }
    }

    public static void populateLists(JSObjectAttribute<Object> selectedAttribute, LimitsDefiner limitsDefiner) {
        if (limitsDefiner.combo.isEnabled()) {
            int index = limitsDefiner.combo.getSelectionIndex();
            if (index == -1) return;
            ArrayList<JSObject> objSelected = selectedAttribute.getObjArList().get(index);
            populateSingleObject(objSelected, limitsDefiner);
            if (!selectedAttribute.getType().equals("jsObject_double_list")) {
                selectedAttribute.maxValueList.set(index, (double) objSelected.size() - 1);
                limitsDefiner.maxValue.setText(String.valueOf(objSelected.size() - 1));
                limitsDefiner.minValue.setText("0");
            } else {

                selectedAttribute.maxValueList.set(index*2 + 1, (double) objSelected.size() - 1);
            }
        } else {
            ArrayList<JSObject> objSelected = selectedAttribute.getObjList();
            populateSingleObject(objSelected, limitsDefiner);

            selectedAttribute.setMaxValue((double) objSelected.size() - 1);
            limitsDefiner.maxValue.setText(String.valueOf(objSelected.size() - 1));
            limitsDefiner.minValue.setText("0");
        }
    }

    public static void populateSingleObject(ArrayList<JSObject> objSelected, LimitsDefiner limitsDefiner) {
        ArrayList<JSObject> objects = limitsDefiner.dataLogger.getJsObjectsList();
        ArrayList<JSObject> leftListObj = new ArrayList<>();
        ArrayList<JSObject> rightListObj = new ArrayList<>();
        for (JSObject obj: objects) {
            if (!(objSelected == null) && objSelected.contains(obj)) rightListObj.add(obj);
            else leftListObj.add(obj);
        }
        String[] leftListString = new String[leftListObj.size()];
        String[] rightListString = new String[rightListObj.size()];
        for (int i = 0; i < leftListObj.size(); i++) leftListString[i] = leftListObj.get(i).getName();
        for (int i = 0; i < rightListObj.size(); i++) rightListString[i] = rightListObj.get(i).getName();
        limitsDefiner.leftList.setItems(leftListString);
        limitsDefiner.rightList.setItems(rightListString);
    }

    public static void main(String[] args) {

    }

}
