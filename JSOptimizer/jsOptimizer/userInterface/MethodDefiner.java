package userInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jsObjectType.DataLogger;
import jsObjectType.JSObjectAttribute;
import jsObjectType.JSOutput;
import uiDataPreparators.JSObjectsCreator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uiDataPreparators.AttributeChecker;
import uiOutputHandlers.InputsCreator;
import uiOutputHandlers.ReplaceAtCfgFile;

//import org.eclipse.wb.swt.SWTResourceManager;

/**
 * GUI for the user to define which JaamSim outputs are objectives (to minimize) and which are constraints.
 * For each constraint, a lower and an upper limit must be defined.
 * User also chooses the metaheuristic algorithm for JMetal optimization.
 * Setting values for the chosen algorithm can be modified (or restored to defaults) through this GUI.
 *
 * @author dimitris.katsios
 *
 */
public class MethodDefiner extends Composite {

    private Text minValue;
    private Text maxValue;
    private Combo outputCombo;
    private Combo methodCombo;
    private Button okButton;
    private Button minimizeRadioButton;
    private Button constraintRadioButton;
    private Button optimizeButton;
    private Label lblNewLabel_5;

    public ArrayList<JSOutput> JSoutputList;
    public int numberOfObjectives;
    public int numberOfConstraints;
    public int numberOfIntegerConstraints;
    public ArrayList<double[]> constraints = new ArrayList<>();
    public String[] outputList;
    public DataLogger dataLogger;
    @SuppressWarnings("rawtypes")
    public ArrayList<JSObjectAttribute> selectedJSattributes;
    public static String confPath = JSObjectsCreator.confPath;
    private String[] metaheuristicAlgorithmList= {"NSGAII", "AbYSS", "CellDE", "cMOEAD", "dMOPSO", "GDE3",
            "IBEA", "MOCell", "MOEAD_DRA", "MOEAD", "NSGAIIAdaptive",
            "NSGAIIRandom", "OMOPSO", "PAES", "RandomSearch", "SMPSO",
            "SMPSOhv", "SMSEMOA", "SPEA2"};
    private static String pathToLog = JSObjectsCreator.pathToLog;
    private String metaheuristicAlgorithm = null;
    private Combo settingsCombo;
    private Text settingsText;
    private Button settingsOkButton;
    private HashMap<String, String> settingsHashMap = new HashMap<>();
    private Label lblNewLabel_6;
    private Button restoreButton;
    private Button helpButton;
    private Label lblNewLabel_7;
    private String helpMessage = "In this panel user can set the optimization parameters. There are two sections:"
            + "\n1) JaamSim outputs: From this dropdown list user can set how JSOptimizer will handle each Simulation output,"
            + " either as value to be minimized or as a constraint."
            + "\nIn the second case user has to enter the min and max limit and click on OK button/hit Enter."
            + " All output values must be categorized (either as objectives or as constraints)!"
            + "\n2) Optimization method: From this dropdown list user can select the optimization metaheuristic algorithm that JSOptimizer will use."
            + " User can change the selected algorithm's default cofiguration values from the Algorithm configuration list"
            + " by selecting a parameter, write the new value at the text box and click on OK button/hit Enter."
            + " Clicking on \"Restore All\" button will restore all the configuration default values of the selected algorithm."
            + "\n\nThe actual value of mutationProbability value is 1.0 / number of variables (not -1.0 thus the # before the parameter name).";
    private Label lblNewLabel_8;

    public MethodDefiner(Composite parent, int style, UI window) {
        super(parent, SWT.NONE);
        setLayout(new GridLayout(5, false));
        dataLogger = window.getDataLogger();
        this.selectedJSattributes = dataLogger.getSelectedJSattributes();
        this.outputList = dataLogger.getOutputList();
        this.JSoutputList = dataLogger.getJSoutputList();
        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Oxygen-Sans Sans-Book", 12, SWT.BOLD));
        lblNewLabel.setAlignment(SWT.CENTER);
        lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 4, 1));
        lblNewLabel.setText("\n                         Optimization Parameters Selection");

        helpButton = new Button(this, SWT.NONE);
        helpButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        helpButton.setText("Help?");

        Label lblNewLabel_1 = new Label(this, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 1, 1));
        lblNewLabel_1.setText("JaamSim outputs");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        outputCombo = new Combo(this, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_outputCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_outputCombo.widthHint = 210;
        outputCombo.setLayoutData(gd_outputCombo);

        minimizeRadioButton = new Button(this, SWT.RADIO);
        minimizeRadioButton.setAlignment(SWT.CENTER);
        minimizeRadioButton.setText("To minimize");

        minimizeRadioButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = outputCombo.getSelectionIndex();
                if (index == -1) return;
                JSoutputList.get(index).setToMinimize(true);
                JSoutputList.get(index).setChecked(true);
                enableConstraintHandlers(false);
            }
        });

        Label lblNewLabel_2 = new Label(this, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        lblNewLabel_2.setAlignment(SWT.CENTER);
        lblNewLabel_2.setText("min");
        new Label(this, SWT.NONE);

        Label lblNewLabel_3 = new Label(this, SWT.NONE);
        lblNewLabel_3.setAlignment(SWT.CENTER);
        lblNewLabel_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        lblNewLabel_3.setText("max");
        new Label(this, SWT.NONE);

        constraintRadioButton = new Button(this, SWT.RADIO);
        constraintRadioButton.setAlignment(SWT.CENTER);
        constraintRadioButton.setText("Constraint");

        constraintRadioButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = outputCombo.getSelectionIndex();
                if (index == -1) return;
                JSoutputList.get(index).setToMinimize(false);
                JSoutputList.get(index).setChecked(true);
                enableConstraintHandlers(true);
            }
        });

        minValue = new Text(this, SWT.BORDER);
        minValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

        okButton = new Button(this, SWT.NONE);
        GridData gd_okButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_okButton.widthHint = 57;
        okButton.setLayoutData(gd_okButton);
        okButton.setText("ok");

        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
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

                int index = outputCombo.getSelectionIndex();
                if (index == -1) return;
                JSoutputList.get(index).setMin(min);
                JSoutputList.get(index).setMax(max);
            }
        });

        maxValue = new Text(this, SWT.BORDER);
        maxValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

        lblNewLabel_8 = new Label(this, SWT.NONE);
        lblNewLabel_8.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        Label lblNewLabel_4 = new Label(this, SWT.NONE);
        lblNewLabel_4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lblNewLabel_4.setText("Optimization Algorithm");

        lblNewLabel_7 = new Label(this, SWT.NONE);
        lblNewLabel_7.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
        lblNewLabel_7.setText("Algorithm configuration");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);

        methodCombo = new Combo(this, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_methodCombo = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
        gd_methodCombo.widthHint = 203;
        methodCombo.setLayoutData(gd_methodCombo);
        methodCombo.setItems(metaheuristicAlgorithmList);

        settingsCombo = new Combo(this, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_settingsCombo = new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1);
        gd_settingsCombo.widthHint = 171;
        settingsCombo.setLayoutData(gd_settingsCombo);

        settingsCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = settingsCombo.getSelectionIndex();
                if (index == -1) return;
                String key = settingsCombo.getItem(index);
                settingsText.setText(settingsHashMap.get(key));
                settingsText.forceFocus();
            }
        });

        settingsText = new Text(this, SWT.BORDER);
        settingsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        settingsOkButton = new Button(this, SWT.NONE);
        settingsOkButton.setToolTipText("Replace a real number with another real, even with zero decimals\n(e.g. replace 10.0 with 8.0 not 8)");
        GridData gd_settingsOkButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_settingsOkButton.widthHint = 47;
        settingsOkButton.setLayoutData(gd_settingsOkButton);
        settingsOkButton.setText("ok");
        new Label(this, SWT.NONE);

        lblNewLabel_6 = new Label(this, SWT.NONE);
        lblNewLabel_6.setToolTipText("The actual value of mutationProbability is 1.0 / number of variables\nIf you want to replace it write a double and press ok");
        lblNewLabel_6.setFont(SWTResourceManager.getFont("Oxygen-Sans Sans-Book", 7, SWT.ITALIC));
        lblNewLabel_6.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
        lblNewLabel_6.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 3, 1));
        lblNewLabel_6.setText("Actual value of mutationProbability: 1.0 / number of variables");
        lblNewLabel_6.setVisible(false);

        restoreButton = new Button(this, SWT.NONE);
        restoreButton.setToolTipText("Click to restore all setting values for the chosen optimization algorithm");
        restoreButton.setText("Restore All");
        new Label(this, SWT.NONE);

        lblNewLabel_5 = new Label(this, SWT.NONE);
        lblNewLabel_5.setAlignment(SWT.RIGHT);
        lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 4, 1));
        lblNewLabel_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        lblNewLabel_5.setFont(SWTResourceManager.getFont("Oxygen-Sans Sans-Book", 8, SWT.ITALIC));
        lblNewLabel_5.setText("All output values must be categorized as objectives or constraints!");
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        lblNewLabel_5.setEnabled(false);
        lblNewLabel_5.setVisible(false);

        Button saveButton = new Button(this, SWT.NONE);
        GridData gd_saveButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_saveButton.widthHint = 63;
        saveButton.setLayoutData(gd_saveButton);
        saveButton.setText("Save");

        Button previousButton = new Button(this, SWT.NONE);
        previousButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        previousButton.setText("<<Previous");

        optimizeButton = new Button(this, SWT.NONE);
        optimizeButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        optimizeButton.setText("Next>>");

        outputCombo.setItems(outputList);
        enableConstraintHandlers(false);

        outputCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                lblNewLabel_5.setEnabled(false);
                window.getShlJaamsimOptimizer().setDefaultButton(okButton);
                int index = outputCombo.getSelectionIndex();
                if (index == -1) {
                    return;
                }
                if (JSoutputList.get(index).isChecked()) {
                    if (JSoutputList.get(index).isToMinimize()) {
                        minimizeRadioButton.setSelection(true);
                        constraintRadioButton.setSelection(false);
                        enableConstraintHandlers(false);
                    } else {
                        constraintRadioButton.setSelection(true);
                        minimizeRadioButton.setSelection(false);
                        enableConstraintHandlers(true);
                        double min = JSoutputList.get(index).getMin();
                        double max = JSoutputList.get(index).getMax();
                        minValue.setText(String.valueOf(min));
                        maxValue.setText(String.valueOf(max));
                    }
                } else {
                    minimizeRadioButton.setSelection(false);
                    constraintRadioButton.setSelection(false);
                    enableConstraintHandlers(false);
                }
            }
        });

        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                dataLogger.setNumberOfObjectives(numberOfObjectives);
                dataLogger.setNumberOfConstraints(numberOfConstraints);
                dataLogger.setConstraints(constraints);
                dataLogger.setJSoutputList(JSoutputList);
                dataLogger.saveFile();
            }
        });

        previousButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(7);
            }
        });

        methodCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.getShlJaamsimOptimizer().setDefaultButton(settingsOkButton);
                int index = methodCombo.getSelectionIndex();
                if (index == -1) return;
                metaheuristicAlgorithm = metaheuristicAlgorithmList[index];
                settingsHashMap.clear();

                InputStream confFile = UI.class
                        .getClassLoader().getResourceAsStream(metaheuristicAlgorithm + ".conf");

                try (FileOutputStream fos = new FileOutputStream(JSObjectsCreator.mainPath + metaheuristicAlgorithm + ".conf");){
                    byte[] buf = new byte[2048];
                    int r;
                    while(-1 != (r = confFile.read(buf))) {
                        fos.write(buf, 0, r);
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

                populateSettingsCombo(metaheuristicAlgorithm);
                settingsText.setText("");
            }
        });

        restoreButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = methodCombo.getSelectionIndex();
                if (index == -1) return;
                metaheuristicAlgorithm = metaheuristicAlgorithmList[index];
                restoreSettingValues(metaheuristicAlgorithm);
                populateSettingsCombo(metaheuristicAlgorithm);
                settingsText.setText("");
            }
        });

        settingsOkButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String newValue = settingsText.getText();
                if(!AttributeChecker.isDouble(newValue)) {
                    settingsText.setText("");
                    return;
                }
                boolean isInteger;
                String key = settingsCombo.getItem(settingsCombo.getSelectionIndex());
                String oldValue = settingsHashMap.get(key);

                isInteger = (AttributeChecker.isInteger(oldValue)) ? true : false;
                if (isInteger && !AttributeChecker.isInteger(newValue)) {
                    settingsText.setText("");
                    return;
                }

                settingsHashMap.put(key, newValue);
                String path = JSObjectsCreator.mainPath + metaheuristicAlgorithm + ".conf";
                String replace1 = key + "=" + String.valueOf(oldValue);
                if (key.contains("#")) {
                    key = key.substring(1);
                    settingsHashMap.put( key, settingsHashMap.remove( "#" + key ) );
                }
                String replace2 = key + "=" + settingsHashMap.get(key);
                ReplaceAtCfgFile.replaceSelected(path, replace1, replace2);
                populateSettingsCombo(metaheuristicAlgorithm);
                settingsText.setText("");
            }
        });

        optimizeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                MessageBox dialog =
                        new MessageBox(window.getShlJaamsimOptimizer() , SWT.ICON_QUESTION | SWT.OK);
                dialog.setText("Missing Values");


                if (metaheuristicAlgorithm == null) {
                    dialog.setMessage("A metahuristic algorithm must be selected!");
                    dialog.open();
                    return;
                }
                for(JSOutput jsOutput: JSoutputList) {
                    if (!(jsOutput.isChecked())) {
                        dialog.setMessage("All JaamSim outputs must be labeled as objectives or constraints!");
                        dialog.open();
                        return;
                    }
                }
                rearrangeJSOutputList();
                InputStream jaamSimJar = UI.class
                        .getClassLoader().getResourceAsStream("JaamSim.jar");

                try (FileOutputStream fos = new FileOutputStream(JSObjectsCreator.mainPath + "JaamSim.jar");){
                    byte[] buf = new byte[2048];
                    int r;
                    while(-1 != (r = jaamSimJar.read(buf))) {
                        fos.write(buf, 0, r);
                    }
                    InputsCreator.createInputs(dataLogger);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                dataLogger.setAlgorithm(metaheuristicAlgorithm);
                //				initiateLogger();
                window.goToNext(8);
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

    public void rearrangeJSOutputList() {
        numberOfObjectives = 0;
        numberOfConstraints = 0;
        ArrayList<JSOutput> newList = new ArrayList<>();
        for(JSOutput jsOutput: JSoutputList) {
            if(jsOutput.isToMinimize())	{
                newList.add(0, jsOutput);
                numberOfObjectives++;
            }
            else {
                newList.add(jsOutput);
                double[] tempDouble = {jsOutput.getMin(), jsOutput.getMax()};
                constraints.add(tempDouble);
                numberOfConstraints++;
            }
        }
        int[] outputindices = new int[JSoutputList.size()];
        for (int i = 0; i < JSoutputList.size(); i++) {
            JSoutputList.set(i, newList.get(i));
            outputindices[i] = newList.get(i).getOutputIndex();
        }
        dataLogger.setNumberOfObjectives(numberOfObjectives);
        dataLogger.setNumberOfConstraints(numberOfConstraints);
        dataLogger.setConstraints(constraints);
        dataLogger.setJSoutputList(JSoutputList);
        dataLogger.setOutputindices(outputindices);
    }

    public void enableConstraintHandlers(boolean flag) {
        if (flag) {
            okButton.setEnabled(true);
            minValue.setEnabled(true);
            maxValue.setEnabled(true);
            minValue.forceFocus();
        } else {
            okButton.setEnabled(false);
            minValue.setEnabled(false);
            maxValue.setEnabled(false);
        }
    }

    public void populateSettingsCombo(String metaheuristicAlgorithm) {
        settingsHashMap.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(JSObjectsCreator.mainPath + metaheuristicAlgorithm + ".conf")))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("=")) {
                    String[] pair = line.split("=");
                    if(pair.length != 2) continue;
                    settingsHashMap.put(pair[0], pair[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] settingsComboItems = settingsHashMap.keySet().toArray(new String[settingsHashMap.keySet().size()]);
        settingsCombo.setItems(settingsComboItems);
    }

    public void restoreSettingValues(String metaheuristicAlgorithm) {
        HashMap<String, String> newHashMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(JSObjectsCreator.mainPath + metaheuristicAlgorithm + ".conf")))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(":")) {
                    String[] pair = line.split(":");
                    if(pair.length != 2) continue;
                    newHashMap.put(pair[0].substring(1), pair[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(JSObjectsCreator.mainPath + metaheuristicAlgorithm + ".conf")))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("=")) {
                    String[] pair = line.split("=");
                    if(pair.length != 2) continue;
                    if(pair[0].equals("mutationProbability")) pair[0] = "#" + pair[0];
                    if(newHashMap.containsKey(pair[0])) {
                        String newLine = pair[0] + "=" + newHashMap.get(pair[0]);

                        String path = JSObjectsCreator.mainPath + metaheuristicAlgorithm + ".conf";
                        ReplaceAtCfgFile.replaceSelected(path, line, newLine);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiateLogger() {
        String pathToCfgFile = dataLogger.getPathToCfgFile();
        try(FileWriter fw = new FileWriter(pathToLog);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))

        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            out.println("========================================================================");
            out.println("JSOptimizer");
            out.println("Target file: " + pathToCfgFile);
            out.println("Metaheuristic algorithm: " + metaheuristicAlgorithm);
            out.println(dateFormat.format(date));
            out.println("========================================================================" + "\n\n");
        } catch (IOException e) {
        }
    }

    public static void main() {

    }
}
