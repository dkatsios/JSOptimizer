package userInterface;

import org.eclipse.swt.SWT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import jsObjectType.DataLogger;
import jsObjectType.JSObject;
import jsObjectType.JSObjectAttribute;
import jsObjectType.JSOutput;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import uiDataPreparators.AttributeSetter;
import uiDataPreparators.JSObjectsCreator;
import uiOutputHandlers.JsRunner;
import uiOutputHandlers.JsSerializer;
import uiOutputHandlers.SolutionsComparator;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * The intro GUI of JSOptimizer.
 * User chooses the .cfg or .jsopt file to proceed with.
 * jsopt files are files with stored settings for the optimization of a specific JaamSim model.
 *
 * @author dimitris.katsios
 *
 */
public class UI {

    protected Shell shlJaamsimOptimizer;

    private boolean loadFlag = false;
    private Composite compositeLoadFiles;
    private CategoryChooser categoryChooser;
    private ObjectChooser objectChooser;
    private AttributeChooser attributeChooser;
    private LimitsDefiner limitsDefiner;
    public DataLogger dataLogger;
    public MethodDefiner methodDefiner;
    public LogObserver logObgerver;
    private String[] pathToCfgFile = new String[1];

    public ArrayList<Boolean> categoriesFlag;
    public ArrayList<String> categoriesList;
    private ArrayList<JSObject> selectedJSObjects = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    private ArrayList<JSObjectAttribute> selectedJSattributes = new ArrayList<>();
    private ArrayList<JSOutput> JSoutputList;
    public String[] outputList;
    public boolean optimizeFlag = false;

    static UI window;
    private Button helpButton;
    private Button aboutButton;
    private String helpMessage = "In this panel user can choose a file to load. The file can be one of two types:"
            + "\n1) a JaamSim model file (.cfg): click on \"Load JSmodel\" button"
            + "\n2) a JSOptimizer file (.jsopt): click on \"Load JSoptimization\" button"
            + "\nJSOptimizer files store information about the user's optimization choises for a specific model. Thus the .cfg file of the model"
            + " for which the .jsopt file has been created must be at the same path that it was when the .jsopt file was created."
            + " If it is not the case, user must select No at the pop up window."
            + "\nIf a JammSim model is to be selected user must confirm that:"
            + "\n1) Simulation ouputList is populated with the objectives and constraint values"
            + "\n2) Values have been assigned to all attributes that JSOptimizer will use as variables. If numerical, integer values will be"
            + " treated as integer variables, real values as real variables (i.e. write 5 for units, 5.0 for hours)."
            + "\nIn case that the model runs multiple rounds, the mean of all rounds output values is used.";

    private String aboutMessage = "JSOptimizer is an optimization tool for JaamSim models. The user can load a JaamSim .cfg file"
            + " and select the optimization variables, objectives, constraints and method."
            + " JSOptimizer produces the Pareto frontier using JMetal metaheuristic algorithms."
            + " The output values (variables, objectives and constraints) are stored at VAR and FUN files as well as at the \"JSOptimizerLOG.log\" file"
            + "\n\nVersion: 1.0\nAuthor: Dimitris Katsios";
    private Button compareButton;
    private Label lblNewLabel_1;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            window = new UI();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {

        //		try {
        //			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        //				| UnsupportedLookAndFeelException e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}

        Display display = Display.getDefault();
        createContents();
        shlJaamsimOptimizer.open();
        shlJaamsimOptimizer.layout();
        while (!shlJaamsimOptimizer.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {

        shlJaamsimOptimizer = new Shell();
        shlJaamsimOptimizer.setSize(770, 540);
        shlJaamsimOptimizer.setText("JSOptimizer");
        shlJaamsimOptimizer.setLayout(new FillLayout(SWT.HORIZONTAL));

        compositeLoadFiles = new Composite(shlJaamsimOptimizer, SWT.NONE);
        compositeLoadFiles.setLayout(new GridLayout(4, false));
        new Label(compositeLoadFiles, SWT.NONE);
        new Label(compositeLoadFiles, SWT.NONE);

        aboutButton = new Button(compositeLoadFiles, SWT.NONE);
        aboutButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
        aboutButton.setText("About");

        helpButton = new Button(compositeLoadFiles, SWT.NONE);
        helpButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        helpButton.setText("Help?");

        lblNewLabel_1 = new Label(compositeLoadFiles, SWT.NONE);
        lblNewLabel_1.setAlignment(SWT.CENTER);
        lblNewLabel_1.setImage(SWTResourceManager.getImage(UI.class, "/logo/jsoptimizer_logo.png"));
        GridData gd_lblNewLabel_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 2);
        gd_lblNewLabel_1.widthHint = 291;
        gd_lblNewLabel_1.heightHint = 228;
        lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);

        Button loadCfgButton = new Button(compositeLoadFiles, SWT.NONE);
        loadCfgButton.setToolTipText("Click to load your JaamSim model's .cfg file");
        GridData gd_loadCfgButton = new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 4, 1);
        gd_loadCfgButton.widthHint = 149;
        loadCfgButton.setLayoutData(gd_loadCfgButton);
        loadCfgButton.setAlignment(SWT.RIGHT);
        loadCfgButton.setText("Load JaamSim model");

        Button loadJSOptButton = new Button(compositeLoadFiles, SWT.NONE);
        loadJSOptButton.setToolTipText("Click to load previous saved JSOptimization file");
        GridData gd_loadJSOptButton = new GridData(SWT.CENTER, SWT.TOP, true, false, 4, 1);
        gd_loadJSOptButton.widthHint = 149;
        loadJSOptButton.setLayoutData(gd_loadJSOptButton);
        loadJSOptButton.setText("Load JSoptimizer file");

        loadJSOptButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadJSOptFile();
                if (!loadFlag) return;
                loadFlag = false;

                compositeLoadFiles.setVisible(!compositeLoadFiles.isVisible());
                compositeLoadFiles.dispose();

                categoryChooser = new CategoryChooser(shlJaamsimOptimizer, SWT.NONE, window);
                shlJaamsimOptimizer.layout();
            }
        });

        compareButton = new Button(compositeLoadFiles, SWT.NONE);
        GridData gd_compareButton = new GridData(SWT.CENTER, SWT.TOP, true, true, 4, 1);
        gd_compareButton.widthHint = 149;
        compareButton.setLayoutData(gd_compareButton);
        compareButton.setText("Compare Solutions");

        compareButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadCompareFiles();
                if (!loadFlag) return;
                loadFlag = false;
                MessageBox dialog =
                        new MessageBox(shlJaamsimOptimizer, SWT.ICON_QUESTION | SWT.OK);
                dialog.setText("Solutions Comparison");
                if (SolutionsComparator.succesionFlag) {
                    dialog.setMessage("To obtain the quality indicators for each solution open the file:\n" + JSObjectsCreator.pathTocomparisonResultsFile);
                } else {
                    dialog.setMessage("Error in proccesing selected FUN files\nPlease try again");
                }
                dialog.open();
            }
        });

        dataLogger = new DataLogger();

        loadCfgButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadCfgFile();
                if (!loadFlag) return;
                loadFlag = false;
                dataLogger.setJsObjectsList(JSObjectsCreator.getJSObcetsList());
                try {
                    dataLogger.setOutputList(AttributeSetter.returnOutputList(pathToCfgFile[0]));
                    outputList = dataLogger.getOutputList();
                    JSoutputList = new ArrayList<>();
                    for (int i = 0; i < outputList.length; i++) {
                        JSOutput jsOutput = new JSOutput(outputList[i], i);
                        jsOutput.setChecked(false);
                        JSoutputList.add(jsOutput);
                    }
                    dataLogger.setJSoutputList(JSoutputList);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                compositeLoadFiles.setVisible(!compositeLoadFiles.isVisible());
                compositeLoadFiles.dispose();

                categoriesFlag = new ArrayList<>();

                ArrayList<String> JSCategoriesList = new ArrayList<>();
                for (JSObject obj: JSObjectsCreator.getJSObcetsList()) {
                    if(obj.getAttributeList().size()>0 && !JSCategoriesList.contains(obj.getType())) JSCategoriesList.add(obj.getType());
                }

                dataLogger.setCategoriesList(JSCategoriesList);
                categoriesList= dataLogger.getCategoriesList();
                for (int i = 0; i < categoriesList.size(); i++) categoriesFlag.add(false);
                dataLogger.setCategoriesFlag(categoriesFlag);


                categoryChooser = new CategoryChooser(shlJaamsimOptimizer, SWT.NONE, window);
                shlJaamsimOptimizer.layout();
            }
        });

        helpButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MessageBox dialog =
                        new MessageBox(shlJaamsimOptimizer, SWT.ICON_QUESTION | SWT.OK);
                dialog.setText("Help");
                dialog.setMessage(helpMessage);
                dialog.open();
            }
        });

        aboutButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MessageBox dialog =
                        new MessageBox(shlJaamsimOptimizer, SWT.ICON_QUESTION | SWT.OK);
                dialog.setText("About");
                dialog.setMessage(aboutMessage);
                dialog.open();
            }
        });
    }

    public Shell getShlJaamsimOptimizer() {
        return shlJaamsimOptimizer;
    }

    public void loadCfgFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CFG files", "cfg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            pathToCfgFile[0] = chooser.getSelectedFile().getPath();
            shlJaamsimOptimizer.setText("JSOptimizer - " + pathToCfgFile[0]);
            try {
                uiDataPreparators.JSObjectsCreator.main(pathToCfgFile);
                dataLogger.setPathToCfgFile(pathToCfgFile[0]);
                loadFlag = true;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public String[] getPathToCfgFile() {
        return pathToCfgFile;
    }

    public void setPathToCfgFile(String[] pathToCfgFile) {
        this.pathToCfgFile = pathToCfgFile;
    }

    public void loadJSOptFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JSOptimizer files .jsopt", "jsopt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String[] pathToSerFile = {chooser.getSelectedFile().getPath()};
            shlJaamsimOptimizer.setText("JSOptimizer - " + pathToSerFile[0]);
            dataLogger = JsSerializer.deserialize(pathToSerFile[0]);
            askCfgFile();
            try {
                uiDataPreparators.JSObjectsCreator.prepareFile(new String[] {dataLogger.getPathToCfgFile()});
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadFlag = true;
        }
    }

    public void loadCompareFiles() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            String[] pathsToFunFiles = new String[selectedFiles.length];
            for (int i = 0; i < selectedFiles.length; i++) pathsToFunFiles[i] = selectedFiles[i].getPath();
            SolutionsComparator.main(pathsToFunFiles);
            loadFlag = true;
        }
    }

    public void askCfgFile() {
        MessageBox dialog =
                new MessageBox(shlJaamsimOptimizer, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        dialog.setText("cfg file");
        dialog.setMessage("Would you like to keep the default cfg file?\nCAUTION! Select No only if the path to the corresponding"
                + " cfg file has changed");
        int val = dialog.open();
        if (val == SWT.NO ) {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "CFG files", "cfg");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                pathToCfgFile[0] = chooser.getSelectedFile().getPath();
                dataLogger.setPathToCfgFile(pathToCfgFile[0]);
            }
        }
    }

    public void goToNext(int index) {
        if (index == 0) {
            //Categories to Objects Next
            categoryChooser.dispose();
            objectChooser = new ObjectChooser(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Categories to File Previous
        else if (index == -1) {
            categoryChooser.dispose();
            window.open();
        }
        //Objects to Categories Previous
        else if (index == 1) {
            selectedJSObjects = objectChooser.getSelectedJSObjects();
            dataLogger.setSelectedJSObjects(selectedJSObjects);
            objectChooser.dispose();
            categoryChooser = new CategoryChooser(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Objects to Attributes Next
        else if (index == 2) {
            selectedJSObjects = objectChooser.getSelectedJSObjects();
            dataLogger.setSelectedJSObjects(selectedJSObjects);
            objectChooser.dispose();
            attributeChooser = new AttributeChooser(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Attributes to Objects Previous
        else if (index == 3) {
            selectedJSattributes = attributeChooser.getselectedJSattributes();
            dataLogger.setSelectedJSattributes(selectedJSattributes);
            attributeChooser.dispose();
            objectChooser = new ObjectChooser(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Attributes to Limits Next
        else if (index == 4) {
            selectedJSattributes = attributeChooser.getselectedJSattributes();
            dataLogger.setSelectedJSattributes(selectedJSattributes);
            attributeChooser.dispose();
            limitsDefiner = new LimitsDefiner(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Limits to Attributes Previous
        else if (index == 5) {
            limitsDefiner.dispose();
            attributeChooser = new AttributeChooser(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Limits to Method Next
        else if (index == 6) {
            limitsDefiner.dispose();
            methodDefiner = new MethodDefiner(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Method to Limits Previous
        else if (index == 7) {
            methodDefiner.dispose();
            limitsDefiner = new LimitsDefiner(shlJaamsimOptimizer, SWT.NONE, window);
        }
        //Method to LogObserver Next
        else if (index == 8) {
            methodDefiner.dispose();
            logObgerver = new LogObserver(shlJaamsimOptimizer, SWT.NONE, window);
            JsRunner.logObserver = logObgerver;
        }

        shlJaamsimOptimizer.layout();
    }

    public DataLogger getDataLogger() {
        return dataLogger;
    }
}
