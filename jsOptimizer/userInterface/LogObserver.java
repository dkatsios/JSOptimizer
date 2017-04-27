package userInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jmetal.experiments.MainC;
import jmetal.util.JMException;
import jsObjectType.DataLogger;
import uiDataPreparators.JSObjectsCreator;
import uiOutputHandlers.JsRunner;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * LogObserver class is responsible for the logging of the procedure. It initializes the log file
 * calls MainC class from JMetal and keeps tracking of the percentage to completion
 *
 * @author dimitris.katsios
 *
 */
public class LogObserver extends Composite {

    public static Text logText;
    public static DataLogger dataLogger;
    private static String pathToLog = JSObjectsCreator.pathToLog;
    public static boolean optimizationCompleted = false;
    public static Date initDate;
    public static Date completionDate;
    public static long totalEvaluations = 0;

    public LogObserver(Composite parent, int style, UI window) {
        super(parent, style);

        dataLogger = window.getDataLogger();
        setLayout(new GridLayout(4, false));
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        logText = new Text(this, SWT.READ_ONLY | SWT.WRAP);
        logText.setFont(SWTResourceManager.getFont("Cantarell", 12, SWT.NORMAL));
        GridData gd_lblSomething = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
        gd_lblSomething.widthHint = 455;
        gd_lblSomething.heightHint = 288;
        logText.setLayoutData(gd_lblSomething);
        logText.setText("Click to start optimization...");

        Label lblNewLabel = new Label(this, SWT.NONE);
        lblNewLabel.setEnabled(false);
        lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        lblNewLabel.setText("New Label");
        lblNewLabel.setVisible(false);

        Button optimizeButton = new Button(this, SWT.NONE);
        optimizeButton.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 2, 1));
        optimizeButton.setText("Optimize!");

        Button newOptButton = new Button(this, SWT.NONE);
        newOptButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1));
        newOptButton.setText("New");

        optimizeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String algorithm = new String(dataLogger.getAlgorithm());
                String[] arg = {algorithm, "JaamSimProblem"};
                CalculateTotalEvaluations(dataLogger.getAlgorithm());
                String[] cfgFilePath = dataLogger.getPathToCfgFile().split("[.]|/|\\\\");
                String cfgFileName = cfgFilePath[cfgFilePath.length - 2];
                JSObjectsCreator.pathToLog = JSObjectsCreator.workingDir + cfgFileName + "_" + algorithm + ".log";
                pathToLog = JSObjectsCreator.pathToLog;
                initiateLogger();
                LogObserver.logText.update();
                logText.setText("Optimization has started, please wait...\n");
                LogObserver.logText.update();

                try {
                    MainC.main(arg);
                } catch (SecurityException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException
                        | JMException | IOException e2) {
                    e2.printStackTrace();
                }
                logText.setText("Optimization completed.\r\nTo obtain the results open: " + JSObjectsCreator.pathToLog);
                try {
                    writeResultsToLog();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    java.awt.Desktop.getDesktop().open(new File(JSObjectsCreator.pathToLog));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        newOptButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                window.goToNext(-1);
            }
        });

    }

    public void initiateLogger() {
        initDate = new Date();
        String pathToCfgFile = dataLogger.getPathToCfgFile();
        try(FileWriter fw = new FileWriter(pathToLog);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))

        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            StringBuilder loggerInitialData = new StringBuilder();
            loggerInitialData.append("========================================================================" + "\r\n");
            loggerInitialData.append("JSOptimizer" + "\r\n");
            loggerInitialData.append("Target file: " + pathToCfgFile + "\r\n");
            loggerInitialData.append("Metaheuristic algorithm: " + dataLogger.getAlgorithm() + "\r\n\r\n");
            loggerInitialData.append("Algorithm parameters:" + "\r\n");
            loggerInitialData.append(returnParameters() + "\r\n");
            loggerInitialData.append("Started at: " + dateFormat.format(initDate) + "\r\n");
            out.println(loggerInitialData.toString());
            logText.setText(loggerInitialData.toString());
        } catch (IOException e) {
        }
    }

    public String returnParameters() {
        StringBuilder parametersString = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(JSObjectsCreator.mainPath + dataLogger.getAlgorithm() + ".conf")))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("=")) {
                    if (line.startsWith("#")) {
                        parametersString.append("mutationProbability=1.0 / number of variables\r\n");
                    } else {
                        parametersString.append(line + "\r\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parametersString.toString();
    }

    public void writeResultsToLog() throws InterruptedException {
        optimizationCompleted = true;
        completionDate = new Date();
        long optimizationDurationSeconds = (completionDate.getTime() - initDate.getTime()) / 1000;
        try(FileWriter fw = new FileWriter(pathToLog, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
        {
            out.println("Duration: " + optimizationDurationSeconds + " seconds");
            out.println("========================================================================");
        } catch (IOException e) {
        }
        try (BufferedReader br = new BufferedReader(new FileReader(JSObjectsCreator.workingDir + "VAR")))
        {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.substring(0, line.length()-1);
                String[] stringVar = line.split(" ");
                double[] doubleVar = new double[stringVar.length];
                for (int i = 0; i < stringVar.length; i++) {
                    doubleVar[i] = Double.parseDouble(stringVar[i]);
                }
                JsRunner.returnResults(doubleVar, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CalculateTotalEvaluations(String algorithmName) {
        long populationSize = 0;
        long maxEvaluations = 0;
        long maxIterations = 0;
        long swarmSize = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(JSObjectsCreator.mainPath + algorithmName + ".conf")))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("maxEvaluations=")) {
                    String[] pair = line.split("=");
                    if(pair.length != 2) continue;
                    maxEvaluations = Integer.parseInt(pair[1]);
                    totalEvaluations = maxEvaluations;
                    return;
                }
                if (line.contains("swarmSize=")) {
                    String[] pair = line.split("=");
                    if(pair.length != 2) continue;
                    swarmSize = Integer.parseInt(pair[1]);
                }
                if (line.contains("maxIterations=")) {
                    String[] pair = line.split("=");
                    if(pair.length != 2) continue;
                    maxIterations = Integer.parseInt(pair[1]) + 1;
                }
                if (line.contains("populationSize=")) {
                    String[] pair = line.split("=");
                    if(pair.length != 2) continue;
                    populationSize = Integer.parseInt(pair[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (swarmSize > 0) {
            totalEvaluations = swarmSize * maxIterations;
        }
        else {
            totalEvaluations = populationSize * maxIterations;
        }
    }


    public static void main(String[] args) {
    }
}
