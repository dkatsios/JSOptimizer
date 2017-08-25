package userInterface;

import java.util.ArrayList;

import jsObjectType.DataLogger;
import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

import uiDataPreparators.AttributeChecker;

/**
 * For each chosen attribute value, the lower and upper limits or the alternative objects are defined.
 * In case that more than one limits need to be defined for the specific attribute value,
 * a combo control handles the choice.
 *
 * @author dimitris.katsios
 *
 */
public class LimitsDefiner extends Composite {

    private LimitsDefiner composite;
    public Text minValue;
    public Text maxValue;
    public List list;
    public List leftList;
    public List rightList;
    public Combo combo;
    public Button okButton;
    Button leftButton;
    Button rightButton;
    public Label attributeValue;

    public DataLogger dataLogger;
    @SuppressWarnings("rawtypes")
    public ArrayList<JSObjectAttribute> selectedJSattributes;
    @SuppressWarnings("rawtypes")
    private JSObjectAttribute selectedAttribute = new JSObjectAttribute<>();
    private Label lblNewLabel_3;
    private Button helpButton;
    private Label lblNewLabel_4;
    private Label lblNewLabel_5;
    private Label lblNewLabel_6;
    private Label lblNewLabel_7;
    private String helpMessage = "In this panel user can set the range for each attribute value."
            + "\nFirst select the attribute from the Attribute list. The current value of the attribute is displayed above the list."
            + " If the value contains more than one inner value, choose an inner value from the dropdown list. Repeat for all inner values."
            + " There are three cases for each (inner) value:"
            + "\n1) The (inner) value is boolean (True/False). In this case both possible values are autimatically added, user does nothing."
            + "\n2) The (inner) value is numerical. In this case user inserts the min and max value at the corresponding text boxes"
            + " and clicks on OK button/hits Enter. If the current value is integer, the min and max values are floored to integers."
            + "\n3) The (inner) value is a JaamSim object. In this case user must select all the alternative objects (including the current)"
            + " that JSOptimizer will use as values during optimization. For example one can use this to change the NextComponent object"
            + " of some JaamSim object. It can be used to run different senarios/setups of the original model."
            + " For one object to be handled as alternative value of the selected attribute, user must move it to the Selected alternatives list.";

    public LimitsDefiner(Composite parent, int style, UI window) {
        super(parent, style);
        setLayout(new GridLayout(6, false));

        composite = this;
        dataLogger = window.getDataLogger();
        this.selectedJSattributes = dataLogger.getSelectedJSattributes();

        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Oxygen-Sans Sans-Book", 12, SWT.BOLD));
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true, 5, 1));
        lblNewLabel.setAlignment(SWT.CENTER);
        lblNewLabel.setText("\n                             Decision variables Constraints Selection\n");

        helpButton = new Button(this, SWT.NONE);
        helpButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        helpButton.setText("Help?");
        new Label(this, SWT.NONE);

        lblNewLabel_3 = new Label(this, SWT.NONE);
        lblNewLabel_3.setAlignment(SWT.CENTER);
        lblNewLabel_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_3.setText("Current value:");

        Label lblNewLabel_1 = new Label(this, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
        lblNewLabel_1.setAlignment(SWT.CENTER);
        lblNewLabel_1.setText("min");
        new Label(this, SWT.NONE);

        Label lblNewLabel_2 = new Label(this, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 2, 1));
        lblNewLabel_2.setAlignment(SWT.CENTER);
        lblNewLabel_2.setText("max");

        attributeValue = new Label(this, SWT.HORIZONTAL | SWT.CENTER);
        attributeValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        attributeValue.setToolTipText(selectedAttribute.getValueLine());

        minValue = new Text(this, SWT.BORDER);
        GridData gd_minValue = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
        gd_minValue.widthHint = 84;
        minValue.setLayoutData(gd_minValue);

        okButton = new Button(this, SWT.NONE);
        GridData gd_okButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_okButton.widthHint = 40;
        okButton.setLayoutData(gd_okButton);
        okButton.setText("OK");
        window.getShlJaamsimOptimizer().setDefaultButton(okButton);

        maxValue = new Text(this, SWT.BORDER);
        GridData gd_maxValue = new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1);
        gd_maxValue.widthHint = 71;
        maxValue.setLayoutData(gd_maxValue);
        new Label(this, SWT.NONE);

        lblNewLabel_4 = new Label(this, SWT.NONE);
        lblNewLabel_4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_4.setText("Attributes List");

        lblNewLabel_5 = new Label(this, SWT.NONE);
        lblNewLabel_5.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
        lblNewLabel_5.setText("Inner values");

        list = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_list = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 7);
        gd_list.heightHint = 243;
        gd_list.widthHint = 214;
        list.setLayoutData(gd_list);

        list.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings({ "rawtypes" })
            @Override
            public void widgetSelected(SelectionEvent e) {
                String[] selectedAttributeString = list.getSelection() ;
                if (selectedAttributeString.length == 0) return;
                for (JSObjectAttribute attr: selectedJSattributes) {
                    if (attr.getJSObject().getName().equals(selectedAttributeString[0].split("\\.")[0]) &&
                            attr.getName().equals(selectedAttributeString[0].split("\\.")[1])) selectedAttribute = attr;
                }
                handleSelectedAttribute(selectedAttribute);
            }
        });

        combo = new Combo(this, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_combo = new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1);
        gd_combo.widthHint = 176;
        combo.setLayoutData(gd_combo);
        combo.select(0);

        combo.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void widgetSelected(SelectionEvent e) {
                switch (selectedAttribute.getType()) {
                case "int_list" : case "int_unit_list" : case "double_list" : case "double_unit_list" :
                    LimitsHandler.setMinMax(selectedAttribute, composite); break;
                case "jsObject_list" : case "jsObject_expression" :
                    LimitsHandler.populateLists(selectedAttribute, composite); break;
                case "jsObject_double_list" :
                    LimitsHandler.handleJSObjectDoubleList(selectedAttribute, composite); break;
                case "numberOfUnits" :
                    LimitsHandler.handleNumberOfUnits(selectedAttribute, composite); break;
                }

            }
        });

        lblNewLabel_6 = new Label(this, SWT.NONE);
        lblNewLabel_6.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_6.setText("Unselected alternatives");
        new Label(this, SWT.NONE);

        lblNewLabel_7 = new Label(this, SWT.NONE);
        lblNewLabel_7.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
        lblNewLabel_7.setText("Selected alternatives");
        populateList();

        leftList = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_leftList = new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 5);
        gd_leftList.widthHint = 170;
        gd_leftList.heightHint = 224;
        leftList.setLayoutData(gd_leftList);

        leftList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                moveToSecondList();
            }
        });
        new Label(this, SWT.NONE);

        rightList = new List(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_rightList = new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 5);
        gd_rightList.heightHint = 224;
        gd_rightList.widthHint = 170;
        rightList.setLayoutData(gd_rightList);

        rightList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                moveToFirstList();
            }
        });
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        rightButton = new Button(this, SWT.NONE);
        rightButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        rightButton.setText(">>");

        leftButton = new Button(this, SWT.NONE);
        leftButton.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, true, 1, 1));
        leftButton.setText("<<");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        Button saveButton = new Button(this, SWT.NONE);
        GridData gd_saveButton = new GridData(SWT.RIGHT, SWT.TOP, false, true, 1, 1);
        gd_saveButton.widthHint = 88;
        saveButton.setLayoutData(gd_saveButton);
        saveButton.setText("Save");

        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dataLogger.saveFile();
            }
        });
        new Label(this, SWT.NONE);

        Button previousButton = new Button(this, SWT.NONE);
        previousButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, true, 1, 1));
        previousButton.setText("<<Previous");

        Button nextButton = new Button(this, SWT.CENTER);
        GridData gd_nextButton = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
        gd_nextButton.widthHint = 84;
        nextButton.setLayoutData(gd_nextButton);
        nextButton.setText("Next>>");

        previousButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(5);
            }
        });

        nextButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(6);
            }
        });

        okButton.addSelectionListener(new SelectionAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!minValue.isEnabled()) return;
                if (!AttributeChecker.isDouble(minValue.getText())) {
                    minValue.setText(""); return;
                }
                if (!AttributeChecker.isDouble(maxValue.getText())) {
                    maxValue.setText(""); return;
                }
                double min = Double.parseDouble(minValue.getText());
                double max = Double.parseDouble(maxValue.getText());
                if (min > max) {
                    minValue.setText("");
                    maxValue.setText("");
                    return;
                }
                if (!combo.isEnabled()) {
                    selectedAttribute.setMinValue(min);
                    selectedAttribute.setMaxValue(max);
                    switch (selectedAttribute.getType()) {
                    case "int" : case "int_list" : case "int_unit" : case "int_unit_list" : case "numberOfUnits" :
                        selectedAttribute.setMinValue(Math.floor(min));
                        selectedAttribute.setMaxValue(Math.floor(max));
                        break;
                    case "jsObject_double" :
                        selectedAttribute.minValueList.set(0, min);
                        selectedAttribute.maxValueList.set(0, max);
                        break;
                    }
                } else {
                    int index = combo.getSelectionIndex();
                    if (index == -1) return;
                    selectedAttribute.minValueList.set(index, min);
                    selectedAttribute.maxValueList.set(index, max);

                    switch (selectedAttribute.getType()) {
                    case "int" : case "int_list" : case "int_unit" : case "int_unit_list" : case "numberOfUnits" :
                        selectedAttribute.minValueList.set(index, Math.floor(min));
                        selectedAttribute.maxValueList.set(index, Math.floor(max));
                        break;
                    case "jsObject_double_list" :
                        selectedAttribute.minValueList.set(index * 2, min);
                        selectedAttribute.maxValueList.set(index * 2, max);
                        break;
                    }
                }
                LimitsHandler.setMinMax(selectedAttribute, composite);
            }
        });

        rightButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveToSecondList();
            }
        });

        leftButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveToFirstList();
            }
        });

        helpButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MessageBox dialog =
                        new MessageBox(window.getShlJaamsimOptimizer() , SWT.ICON_QUESTION | SWT.OK);
                dialog.setText("Help");
                dialog.setMessage(helpMessage);
                dialog.open();

            }
        });
    }

    public void populateList() {
        String[] toList = new String[selectedJSattributes.size()];
        for (int i = 0; i < selectedJSattributes.size(); i++) {
            StringBuilder attributeString = new StringBuilder();
            attributeString.append(selectedJSattributes.get(i).getJSObject().getName() + "." +
                    selectedJSattributes.get(i).getName());
            toList[i] = (attributeString.toString());
        }
        list.setItems(toList);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void handleSelectedAttribute(JSObjectAttribute selectedAttribute) {
        emptyCells();
        switch (selectedAttribute.getType()) {
        case "boolean" : case "boolean_list" : LimitsHandler.isBoolean(selectedAttribute, composite); break;

        case "int" : case "int_unit" : case "double" : case "double_unit" : LimitsHandler.isInt(selectedAttribute, composite); break;
        case "int_list" : case "int_unit_list" : case "double_list" : case "double_unit_list" :
            LimitsHandler.isIntList(selectedAttribute, composite); break;

        case "jsObject" : LimitsHandler.isJSObject(selectedAttribute, composite); break;
        case "jsObject_list" : case "jsObject_expression" : LimitsHandler.isJSObjectList(selectedAttribute, composite); break;

        case "jsObject_double" : LimitsHandler.isJSObjectDouble(selectedAttribute, composite); break;
        case "jsObject_double_list" : LimitsHandler.isJSObjectDoubleList(selectedAttribute, composite); break;

        case "numberOfUnits" : LimitsHandler.isNumberOfUnits(selectedAttribute, composite); break;
        }
    }

    public void emptyCells() {
        minValue.setText("");
        maxValue.setText("");
        combo.setItems(new String[0]);
        leftList.setItems(new String[0]);
        rightList.setItems(new String[0]);

        minValue.setEnabled(true);
        maxValue.setEnabled(true);

        combo.setEnabled(false);
        leftList.setEnabled(false);
        rightList.setEnabled(false);

        attributeValue.setText(selectedAttribute.getValueLine());
        attributeValue.setToolTipText(selectedAttribute.getValueLine());
        minValue.forceFocus();
    }

    @SuppressWarnings("unchecked")
    public void moveToSecondList() {
        if (leftList.getSelection().length == 0) return;
        ArrayList<JSObject> selectedObjects = returnSelectedJSObjectList(leftList.getSelection());
        if (!combo.isEnabled()) {
            for (JSObject obj: selectedObjects) selectedAttribute.addToObjList(obj);
        } else {
            int index = combo.getSelectionIndex();
            if (index == -1) return;
            for (JSObject obj: selectedObjects) selectedAttribute.addToObjArList(index, obj);
        }
        LimitsHandler.populateLists(selectedAttribute, composite);
    }

    @SuppressWarnings("unchecked")
    public void moveToFirstList() {
        if (rightList.getSelection().length == 0) return;
        ArrayList<JSObject> selectedObjects = returnSelectedJSObjectList(rightList.getSelection());
        if (!combo.isEnabled()) {
            for (JSObject obj: selectedObjects) selectedAttribute.removeFromObjList(obj);
        } else {
            int index = combo.getSelectionIndex();
            if (index == -1) return;
            for (JSObject obj: selectedObjects) selectedAttribute.removeFromObjArList(index, obj);
        }
        LimitsHandler.populateLists(selectedAttribute, composite);
    }

    public ArrayList<JSObject> returnSelectedJSObjectList(String[] AttributeListString) {
        String[] SelectedObjectsString = AttributeListString;
        ArrayList<JSObject> selectedJSObjectList = new ArrayList<>();
        for (String objString: SelectedObjectsString) {
            for (JSObject obj: dataLogger.getJsObjectsList()) {
                if (obj.getName().equals(objString)) {
                    selectedJSObjectList.add(obj);
                }
            }
        }
        return selectedJSObjectList;
    }
}
