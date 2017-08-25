package userInterface;

import java.util.ArrayList;

import jsObjectType.DataLogger;
import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CCombo;

/**
 * GUI for the user to choose the attributes of the chosen object that can variate.
 * The values of these attributes will change possibly at every optimization round according to JMetal outputs.
 * User first chooses the object from the combo control and then passes or removes the selected attribute
 * to or from the attributes list.
 *
 * @author dimitris.katios
 *
 */
public class AttributeChooser extends Composite {

    private Composite composite;
    Button nextButton;
    Label lblNewLabel;
    List secondList;
    CCombo JSObjectsList;
    private ArrayList<JSObjectAttribute<Object>> jsAttributesList;
    @SuppressWarnings("rawtypes")
    private ArrayList<JSObjectAttribute> selectedJSattributes;
    private ArrayList<JSObject> selectedJSObjects;
    private DataLogger dataLogger;
    private List firstList;
    private String helpMessage = "In this panel user can select from the attributes list,"
            + " those object attributes that will be used as optimization variables."
            + "\nTo select an object's attribute one has to select the object from the dropdown list and then"
            + " can either double click on the attribute's name or click and press the \"choose\" button. Multiple selection (Shift/Ctrl+click)"
            + " is also available. The same holds for unselecting an attribute.";

    @SuppressWarnings("rawtypes")
    public AttributeChooser(Composite parent, int style, UI window) {
        super(parent, style);

        composite = this;
        dataLogger = window.getDataLogger();
        this.selectedJSObjects = dataLogger.getSelectedJSObjects();
        this.selectedJSattributes = dataLogger.getSelectedJSattributes();


        setLayout(new GridLayout(5, false));

        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Oxygen-Sans Sans-Book", 12, SWT.BOLD));
        lblNewLabel.setAlignment(SWT.CENTER);
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
        lblNewLabel.setText("\n                           Attributes Selection");

        Button helpButton = new Button(this, SWT.NONE);
        helpButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        helpButton.setText("Help?");

        Label lblNewLabel_3 = new Label(this, SWT.NONE);
        lblNewLabel_3.setText("Objects List");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        JSObjectsList = new CCombo(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        JSObjectsList.setToolTipText("Select a JSObject to choose the attributes\nto be optimized");
        GridData gd_JSObjectsList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_JSObjectsList.widthHint = 175;
        JSObjectsList.setLayoutData(gd_JSObjectsList);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        populateJSObjectCombo();

        Label lblNewLabel_1 = new Label(this, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("Unselected Attributes");
        new Label(this, SWT.NONE);

        Label lblNewLabel_2 = new Label(this, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
        lblNewLabel_2.setText("Selected Attributes");

        firstList = new List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_firstList = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
        gd_firstList.heightHint = 202;
        gd_firstList.widthHint = 274;
        firstList.setLayoutData(gd_firstList);

        Button movetoSecondList = new Button(composite, SWT.NONE);
        movetoSecondList.setToolTipText("Choose the object(s) of the selected category\nthat you want to optimize");
        movetoSecondList.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, true, 1, 1));
        movetoSecondList.setText("choose");

        secondList = new List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_secondList = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2);
        gd_secondList.heightHint = 199;
        gd_secondList.widthHint = 241;
        secondList.setLayoutData(gd_secondList);
        handleSelectedAttributes(null);

        Button movetoFirstList = new Button(composite, SWT.NONE);
        movetoFirstList.setToolTipText("Delete object(s) from the list");
        movetoFirstList.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, true, 1, 1));
        movetoFirstList.setText("remove");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        Button saveButton = new Button(this, SWT.NONE);
        GridData gd_saveButton = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
        gd_saveButton.widthHint = 90;
        saveButton.setLayoutData(gd_saveButton);
        saveButton.setText("Save");

        Button previousButton = new Button(this, SWT.NONE);
        previousButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
        previousButton.setText("<<Previous");

        Button nButton = new Button(this, SWT.NONE);
        nButton.setText("Next>>");

        previousButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(3);
            }
        });

        nButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(4);
            }
        });

        JSObjectsList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                JSObject selectedObject = new JSObject();
                String selectedObjectString = JSObjectsList.getItems()[JSObjectsList.getSelectionIndex()];
                for (JSObject obj: selectedJSObjects) {
                    if (selectedObjectString.equals(obj.getName())) {
                        selectedObject = obj; break;
                    }
                }
                jsAttributesList = selectedObject.getAttributeList() ;
                ArrayList<JSObjectAttribute> potentialJSObjectAttributes = new ArrayList<>();
                for (JSObjectAttribute attr: jsAttributesList) {
                    if (!selectedJSattributes.contains(attr)) {
                        potentialJSObjectAttributes.add(attr);
                    }
                }
                String[] potentialObjectsArray = new String[potentialJSObjectAttributes.size()];
                for (int i = 0; i < potentialJSObjectAttributes.size(); i++) {
                    potentialObjectsArray[i] = potentialJSObjectAttributes.get(i).getName();
                }
                firstList.setItems(potentialObjectsArray);
            }
        });

        movetoSecondList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveToSecondList();
            }
        });

        firstList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                moveToSecondList();
            }
        });

        movetoFirstList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveToFirstList();
            }
        });

        secondList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                moveToFirstList();
            }
        });

        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dataLogger.saveFile();
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


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void handleSelectedAttributes(ArrayList<JSObjectAttribute> attributesList) {
        if (attributesList != null) {
            for (JSObjectAttribute attr: attributesList) {
                if (selectedJSattributes.contains(attr)) {
                    selectedJSattributes.remove(attr);
                } else {
                    selectedJSattributes.add(attr);
                }
            }
        }
        String[] toSecondList = new String[selectedJSattributes.size()];
        for (int i = 0; i < selectedJSattributes.size(); i++) {
            StringBuilder attributeString = new StringBuilder();
            attributeString.append(selectedJSattributes.get(i).getJSObject().getName() + "." + selectedJSattributes.get(i).getName());
            toSecondList[i] = (attributeString.toString());

            if (selectedJSattributes.get(i).getType().equals("boolean_list")) {
                fillBooleanListLimits(selectedJSattributes.get(i));
            }
        }
        secondList.setItems(toSecondList);
    }

    @SuppressWarnings("rawtypes")
    public ArrayList<JSObjectAttribute> getselectedJSattributes() {
        return selectedJSattributes;
    }

    public void populateJSObjectCombo() {
        String[] selectedObjectsStrArray = new String[selectedJSObjects.size()];
        for (int i = 0; i < selectedJSObjects.size(); i++) {
            selectedObjectsStrArray[i] = selectedJSObjects.get(i).getName();
        }
        JSObjectsList.setItems(selectedObjectsStrArray);
    }

    public void fillBooleanListLimits(JSObjectAttribute<ArrayList<Boolean>> attribute) {
        for (int i = 0; i < attribute.getValue().size(); i++) {
            attribute.minValueList.set(i, 0.0);
            attribute.maxValueList.set(i, 1.0);
        }
    }

    @SuppressWarnings("rawtypes")
    public void moveToSecondList() {
        String[] firstListSelectedAttributes = firstList.getSelection() ;
        if (firstListSelectedAttributes.length == 0) return;
        ArrayList<JSObjectAttribute> attributesList = new ArrayList<>();
        for (String attrStringFirstList: firstListSelectedAttributes) {
            for (JSObjectAttribute attr: jsAttributesList) {
                if (attr.getName().equalsIgnoreCase(attrStringFirstList)) attributesList.add(attr);
            }
        }
        handleSelectedAttributes(attributesList);
    }

    @SuppressWarnings("rawtypes")
    public void moveToFirstList() {
        String[] secondListSelectedAttributes = secondList.getSelection() ;
        if (secondListSelectedAttributes.length == 0) return;
        ArrayList<JSObjectAttribute> attributesList = new ArrayList<>();
        for (String attrStringSecondList: secondListSelectedAttributes) {
            for (JSObjectAttribute attr: selectedJSattributes) {
                if (attr.getJSObject().getName().equals(attrStringSecondList.split("\\.")[0]) &&
                        attr.getName().equals(attrStringSecondList.split("\\.")[1])) attributesList.add(attr);
            }
        }
        handleSelectedAttributes(attributesList);
    }
}
