package userInterface;

import java.util.ArrayList;

import jsObjectType.DataLogger;
import jsObjectType.JSObject;

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
 * GUI for the user to choose the specific JaamSim objects that JMetal will use to optimize the model.
 * Some or all of the attributes of these objects will have their values varying
 * in order for the optimal combination to be achieved.
 *
 * @author dimitris.katsios
 *
 */
public class ObjectChooser extends Composite {

    private Composite composite;
    Button nextButton;
    Label lblNewLabel;
    List secondList;
    List firstList;
    CCombo categoriesList;
    private ArrayList<JSObject> jsObjectsList;
    private ArrayList<JSObject> selectedJSObjects;
    private DataLogger dataLogger;
    private String helpMessage = "In this panel user can select from the objects list, those objects that contain attributes that"
            + " will be used as optimization variables."
            + "\nTo select an object one has to select its category from the dropdown list and then"
            + " can either double click on the object's name or click and press the \"choose\" button. Multiple selection (Shift/Ctrl+click)"
            + " is also available. The same holds for unselecting an object.";

    public ObjectChooser(Composite parent, int style, UI window) {
        super(parent, style);

        composite = this;
        dataLogger = window.getDataLogger();
        this.selectedJSObjects = dataLogger.getSelectedJSObjects();
        this.jsObjectsList = dataLogger.getJsObjectsList();

        setLayout(new GridLayout(5, false));

        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Oxygen-Sans Sans-Book", 12, SWT.BOLD));
        lblNewLabel.setAlignment(SWT.CENTER);
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
        lblNewLabel.setText("\n                                Objects Selection");

        Button helpButton = new Button(this, SWT.NONE);
        helpButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        helpButton.setText("Help?");

        Label lblNewLabel_1 = new Label(this, SWT.NONE);
        lblNewLabel_1.setText("Categories List");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        categoriesList = new CCombo(this, SWT.BORDER);
        categoriesList.setToolTipText("Select a category to choose the objects\nto be optimized");
        GridData gd_categoriesList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_categoriesList.widthHint = 175;
        categoriesList.setLayoutData(gd_categoriesList);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        categoriesList.setItems(dataLogger.getCategoriesArray());
        new Label(this, SWT.NONE);

        Label lblNewLabel_2 = new Label(this, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText("Unselected Objects");
        new Label(this, SWT.NONE);

        Label lblNewLabel_3 = new Label(this, SWT.NONE);
        lblNewLabel_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
        lblNewLabel_3.setText("Selected Objects");

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
        handleSelectedObjects(null);
        populateSecondList();

        Button movetoFirstList = new Button(composite, SWT.NONE);
        movetoFirstList.setToolTipText("Delete object(s) from the list");
        movetoFirstList.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, true, 1, 1));
        movetoFirstList.setText("remove");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        Button saveButton = new Button(this, SWT.NONE);
        GridData gd_saveButton = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
        gd_saveButton.widthHint = 77;
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
                window.goToNext(1);
            }
        });

        nButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(2);
            }
        });

        categoriesList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                populateFirstList();
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

        secondList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                moveToFirstList();
            }
        });

        movetoFirstList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
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

    public void moveToSecondList() {
        String[] selectedObjects = firstList.getSelection() ;
        if (selectedObjects.length == 0) return;
        ArrayList<JSObject> objectList = new ArrayList<>();
        for (String objString: selectedObjects) {
            for (JSObject obj: jsObjectsList) {
                if (obj.getName().equalsIgnoreCase(objString)) objectList.add(obj);
            }
        }
        handleSelectedObjects(objectList);
    }

    public void moveToFirstList() {
        String[] selectedObjects = secondList.getSelection() ;
        if (selectedObjects.length == 0) return;
        ArrayList<JSObject> objectList = new ArrayList<>();
        for (String objString: selectedObjects) {
            for (JSObject obj: jsObjectsList) {
                if (obj.getName().equalsIgnoreCase(objString)) objectList.add(obj);
            }
        }
        handleSelectedObjects(objectList);
    }

    public void handleSelectedObjects(ArrayList<JSObject> objectList) {
        if(objectList == null || objectList.size() == 0) return;
        for (JSObject obj: objectList) {
            if (selectedJSObjects.contains(obj)) {
                selectedJSObjects.remove(obj);
            } else {
                selectedJSObjects.add(obj);
            }
        }
        populateFirstList();
        populateSecondList();
    }

    public void populateFirstList() {
        if (categoriesList.getSelectionIndex() == -1) {
            firstList.setItems(new String[0]);
            return;
        }
        String selectedCategory = categoriesList.getItems()[categoriesList.getSelectionIndex()];
        ArrayList<JSObject> potentialJSObjects = new ArrayList<>();
        for (JSObject jsObject: jsObjectsList) {
            if (jsObject.getType().equalsIgnoreCase(selectedCategory) && !selectedJSObjects.contains(jsObject) && jsObject.getAttributeList().size() > 0) {
                potentialJSObjects.add(jsObject);
            }
        }
        String[] potentialObjectsArray = new String[potentialJSObjects.size()];
        for (int i = 0; i < potentialJSObjects.size(); i++) {
            potentialObjectsArray[i] = potentialJSObjects.get(i).getName();
        }
        firstList.setItems(potentialObjectsArray);
    }

    public void populateSecondList() {
        if(selectedJSObjects.size() == 0) {
            secondList.setItems(new String[0]);
            return;
        }
        String[] toSecondList = new String[selectedJSObjects.size()];
        for (int i = 0; i < selectedJSObjects.size(); i++) toSecondList[i] = selectedJSObjects.get(i).getName();
        secondList.setItems(toSecondList);
    }

    public ArrayList<JSObject> getSelectedJSObjects() {
        return selectedJSObjects;
    }
}
