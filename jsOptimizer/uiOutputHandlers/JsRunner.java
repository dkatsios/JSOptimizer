package uiOutputHandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import uiDataPreparators.JSObjectsCreator;
import userInterface.LogObserver;

/**
 *JsRunner class runs JaamSim with the modified .cfg file and
 *returns the output results to JMetal for objectives and constraints evaluation
 *
 * @author dimitris.katsios
 *
 */
public class JsRunner {

    public static LogObserver logObserver;
    private static String pathToJaamSimJar = JSObjectsCreator.pathToJaamSimJar;
    private static String pathToCfgFile = JSObjectsCreator.targetPath;
    private static String pathToDatFile = JSObjectsCreator.pathToDatFile;
    private static String pathToLog = JSObjectsCreator.pathToLog;
    private static String[] outputNames;
    public static long totalEvaluations;
    public static double tempEvaluationsPercent = 0.0;

    public static double[] returnResults(double[] doubleVar, long evaluationIndex) throws IOException, InterruptedException {
        CfgChanger.cfgChange(doubleVar);
        jsExcecute();
        ArrayList<String> dataOutputsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToDatFile)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                dataOutputsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputNames = dataOutputsList.get(0).split("\t");
        int numberOfOutputs = outputNames.length;
        double[] results = new double[numberOfOutputs];
        for (int i = 2; i < dataOutputsList.size(); i++) {
            String[] outputLine = dataOutputsList.get(i).split("\t");
            for (int j = 0; j < numberOfOutputs; j++) {
                results[j] += Double.parseDouble(outputLine[j]);
            }
        }
        for (int i = 0; i < numberOfOutputs; i++) {
            results[i] /= (dataOutputsList.size() - 2);
        }
        writeToLog(results, evaluationIndex);
        return results;
    }

    //this method executes JaamSim in non RealTime mode, minimized without graphics (faster execution)
    public static void jsExcecute() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", pathToJaamSimJar, pathToCfgFile, "-b", "-m");
        Process process = pb.start();
        process.waitFor();
    }

    public static void writeToLog(double[] output, long evaluationIndex) {
        totalEvaluations = LogObserver.totalEvaluations;
        if (LogObserver.optimizationCompleted == false) {
            if (totalEvaluations == 0) return;
            double evaluationsPercent = (evaluationIndex / (double) totalEvaluations) * 100;
            if (evaluationsPercent != tempEvaluationsPercent) {
                tempEvaluationsPercent = evaluationsPercent;
                System.out.println((int)evaluationsPercent + "%");
                LogObserver.logText.setText("Optimization has started, please wait...\n" + (int)evaluationsPercent + "%");
                LogObserver.logText.update();
            }
            return;
        }
        pathToLog = JSObjectsCreator.pathToLog;
        try(FileWriter fw = new FileWriter(pathToLog, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
        {
            out.println("\r\nObjectives and Constraints: ");
            for(int i = 0; i < outputNames.length; i++) {
                out.println(outputNames[i] + "\t" + output[i]);
            }
            out.println("========================================================================\r\n");
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {

    }

}
