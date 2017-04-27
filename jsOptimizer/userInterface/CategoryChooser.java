package userInterface;

import java.util.ArrayList;

import jsObjectType.DataLogger;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

/**
 * User chooses from the category list, those object categories that includes objects with attributes
 * that will vary as JMetal input values
 *
 * @author dimitris.katsios
 *
 */
public class CategoryChooser extends Composite {

    public Composite composite;
    public Button nextButton;
    private int firstListCounter;
    private String[] firstListElements;
    private String[] secondListElements;
    private ArrayList<Boolean> categoriesFlag;
    private ArrayList<String> categoriesList;
    public List firstList;
    public List secondList;
    private DataLogger dataLogger;
    private String helpMessage = "In this panel user can select from the categories list, those object categories that include objects with attributes"
            + " that will be used as optimization variables."
            + "\nTo select a category one can either double click on its name or click and press the \">\" button. Multiple selection (Shift/Ctrl+click)"
            + " is also available. The same holds for unselecting a category."
            + "\n\"<<Previous\" and \"Next>>\" buttons are used to navigate among the panels."
            + "\nSave button is used to save the user preferences for the selected .cfg file as a .jsopt file."
            + " User can load it later from the initial menu.";

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public CategoryChooser(Composite parent, int style, UI window) {
        super(parent, style);

        dataLogger = window.getDataLogger();
        this.categoriesFlag = dataLogger.getCategoriesFlag();
        setLayout(new GridLayout(5, false));
        composite = this;
        this.categoriesList = dataLogger.getCategoriesList();

        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Oxygen-Sans Sans-Book", 12, SWT.BOLD));
        lblNewLabel.setAlignment(SWT.CENTER);
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
        lblNewLabel.setText("\n                        Categories Selection");

        Button helpButton = new Button(this, SWT.NONE);
        helpButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        helpButton.setText("Help?");

        Label lblNewLabel_1 = new Label(this, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("Unselected Categories");
        new Label(this, SWT.NONE);

        Label lblNewLabel_2 = new Label(this, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
        lblNewLabel_2.setText("Selected Categories");

        firstList = new List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_firstList = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
        gd_firstList.heightHint = 202;
        gd_firstList.widthHint = 274;
        firstList.setLayoutData(gd_firstList);

        Button movetoSecondList = new Button(composite, SWT.NONE);
        movetoSecondList.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, true, 1, 1));
        movetoSecondList.setText(">");

        secondList = new List(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_secondList = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 2);
        gd_secondList.heightHint = 199;
        gd_secondList.widthHint = 241;
        secondList.setLayoutData(gd_secondList);
        populateLists();

        Button movetoFirstList = new Button(composite, SWT.NONE);
        movetoFirstList.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, true, 1, 1));
        movetoFirstList.setText("<");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        Button saveButton = new Button(this, SWT.NONE);
        saveButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
        saveButton.setText("Save");

        Button previousButton = new Button(this, SWT.NONE);
        previousButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        previousButton.setText("<<Previous");

        nextButton = new Button(this, SWT.NONE);
        nextButton.setToolTipText("go");
        nextButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
        nextButton.setText("Next>>");

        movetoSecondList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
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

        previousButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(-1);
            }
        });

        nextButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dataLogger.setCategoriesFlag(categoriesFlag);
                dataLogger.setCategoriesArray(secondListElements);
                window.goToNext(0);
            }
        });

        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dataLogger.saveFile();
            }
        });

        firstList.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                moveToSecondList();
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
        String[] selectedCategories = firstList.getSelection();
        for (String category: selectedCategories) {
            int thisIndex = categoriesList.indexOf(category);
            categoriesFlag.set(thisIndex, true);
            firstListCounter--;
        }
        int firstIndex = 0; int secondIndex = 0;
        firstListElements = new String[firstListCounter];
        secondListElements = new String[categoriesList.size() - firstListCounter];
        for (int i = 0; i < categoriesList.size(); i++) {
            if (categoriesFlag.get(i)) {
                secondListElements[secondIndex] =  categoriesList.get(i);
                secondIndex++;
            } else {
                firstListElements[firstIndex] =  categoriesList.get(i);
                firstIndex++;
            }
        }
        firstList.setItems(firstListElements);
        secondList.setItems(secondListElements);
    }

    public void moveToFirstList() {
        String[] selectedCategories = secondList.getSelection();
        for (String category: selectedCategories) {
            int thisIndex = categoriesList.indexOf(category);
            categoriesFlag.set(thisIndex, false);
            firstListCounter++;
        }
        int firstIndex = 0; int secondIndex = 0;
        firstListElements = new String[firstListCounter];
        secondListElements = new String[categoriesList.size() - firstListCounter];
        for (int i = 0; i < categoriesList.size(); i++) {
            if (categoriesFlag.get(i)) {
                secondListElements[secondIndex] =  categoriesList.get(i);
                secondIndex++;
            } else {
                firstListElements[firstIndex] =  categoriesList.get(i);
                firstIndex++;
            }
        }
        firstList.setItems(firstListElements);
        secondList.setItems(secondListElements);
    }

    public void populateLists() {
        firstListCounter = 0;
        for (Boolean flag: categoriesFlag) {
            if (!flag) firstListCounter++;
        }
        int firstIndex = 0; int secondIndex = 0;
        firstListElements = new String[firstListCounter];
        secondListElements = new String[categoriesList.size() - firstListCounter];
        for (int i = 0; i < categoriesList.size(); i++) {
            if (categoriesFlag.get(i)) {
                secondListElements[secondIndex] =  categoriesList.get(i);
                secondIndex++;
            } else {
                firstListElements[firstIndex] =  categoriesList.get(i);
                firstIndex++;
            }
        }
        if (firstListElements.length != 0) firstList.setItems(firstListElements);
        if (secondListElements.length != 0) secondList.setItems(secondListElements);
    }

}
