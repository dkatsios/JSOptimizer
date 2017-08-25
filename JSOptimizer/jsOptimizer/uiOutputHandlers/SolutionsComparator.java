package uiOutputHandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import uiDataPreparators.AttributeChecker;
import uiDataPreparators.JSObjectsCreator;
import jmetal.qualityIndicator.Epsilon;
import jmetal.qualityIndicator.GeneralizedSpread;
import jmetal.qualityIndicator.GenerationalDistance;
import jmetal.qualityIndicator.Hypervolume;
import jmetal.qualityIndicator.InvertedGenerationalDistance;
import jmetal.qualityIndicator.Spread;
import jmetal.util.ExtractParetoFront;

/**
 * SolutionsComparator class contains methods for comparing different optimization FUN files for the same problem category
 * Produces ComparisonResults.txt file with quality indicators for each FUN file, as well as the Universal Pareto Front
 *
 * @author dimitris.katsios
 *
 */
public class SolutionsComparator {
    public static boolean succesionFlag = true;
    public static String pathToUniversalFunFile = JSObjectsCreator.pathToUniversalFunFile;
    public static String pathTocomparisonResultsFile = JSObjectsCreator.pathTocomparisonResultsFile;
    public static PrintWriter writer;
    public static int numberOfObjectives = 0;

    public static void produceUniversalFunFile(String[] args) throws IOException {
        writer = new PrintWriter(pathToUniversalFunFile, "UTF-8");
        for (String filePath: args) {
            loadToUniversalFunFile(filePath);
        }
        writer.close();
    }

    public static void loadToUniversalFunFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (!isOfFunFormat(line)) succesionFlag = false;
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isOfFunFormat(String line) {
        if (line.length() < 2) return false;
        String[] elements = line.substring(0, line.length() - 1).split(" ");
        if (numberOfObjectives == 0) numberOfObjectives = elements.length;
        if (numberOfObjectives != 0 && elements.length != numberOfObjectives) return false;
        for (String element: elements) {
            if (!AttributeChecker.isDouble(element)) return false;
        }
        return true;
    }

    public static void produceFrontFile() {
        String[] args = new String[2];
        args[0] = pathToUniversalFunFile;
        args[1] = Integer.toString(numberOfObjectives);
        ExtractParetoFront.main(args);
        try {
            Files.delete(Paths.get(pathToUniversalFunFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void produceQualityIndicatorsFile(String[] args) throws IOException {
        try {
            if(new File(pathTocomparisonResultsFile).isFile())
            Files.delete(Paths.get(pathTocomparisonResultsFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(FileWriter fw = new FileWriter(pathTocomparisonResultsFile);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter qiOut = new PrintWriter(bw))

        {
            StringBuilder qualityIndicatorsData = new StringBuilder();
            qualityIndicatorsData.append("Quality Indicators:" + "\n");
            qiOut.println(qualityIndicatorsData.toString());
        } catch (IOException e) {
        }
        String[] qiArgs = new String[3];
        qiArgs[0] = pathToUniversalFunFile + ".pf";
        qiArgs[2] = Integer.toString(numberOfObjectives);
        for (int i = 0; i < args.length; i++) {
            qiArgs[1] = args[i];
            runQualityIndicators(qiArgs);
        }
    }

    public static void runQualityIndicators(String[] qiArgs) {
        try(FileWriter fw = new FileWriter(pathTocomparisonResultsFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter qiOut = new PrintWriter(bw))

        {
            StringBuilder qualityIndicatorsData = new StringBuilder();
            qualityIndicatorsData.append("FUN File: " + qiArgs[1] + "\n");
            qualityIndicatorsData.append("Epsilon: ");
            Epsilon.main(qiArgs);
            qualityIndicatorsData.append(Epsilon.ind_value + "\n");
//            qualityIndicatorsData.append("Spread: ");
//            Spread.main(qiArgs);
//            qualityIndicatorsData.append(Spread.value + "\n");
            qualityIndicatorsData.append("Hypervolume: ");
            Hypervolume.main(qiArgs);
            qualityIndicatorsData.append(Hypervolume.value + "\n");
            qualityIndicatorsData.append("Generalized Spread: ");
            GeneralizedSpread.main(qiArgs);
            qualityIndicatorsData.append(GeneralizedSpread.value + "\n");
            qualityIndicatorsData.append("Generational Distance: ");
            GenerationalDistance.main(qiArgs);
            qualityIndicatorsData.append(GenerationalDistance.value + "\n");
            qualityIndicatorsData.append("Inverted Generational Distance: ");
            InvertedGenerationalDistance.main(qiArgs);
            qualityIndicatorsData.append(InvertedGenerationalDistance.value + "\n");
            qualityIndicatorsData.append("========================================================================" + "\n");
            qiOut.println(qualityIndicatorsData.toString());
        } catch (IOException e) {
        }
    }

    public static void writeGeneralParetoFrontToFile() {

        try(FileWriter fw = new FileWriter(pathTocomparisonResultsFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter qiOut = new PrintWriter(bw))
        {
            StringBuilder qualityIndicatorsData = new StringBuilder();
            qualityIndicatorsData.append("General Pareto Front: " + "\n\n");
            try (BufferedReader br = new BufferedReader(new FileReader(pathToUniversalFunFile + ".pf")))
            {
                String line;
                while ((line = br.readLine()) != null) {
                    qualityIndicatorsData.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            qiOut.println(qualityIndicatorsData.toString());
            Files.delete(Paths.get(pathToUniversalFunFile + ".pf"));
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            succesionFlag = false;
            return;
        }
        try {
            produceUniversalFunFile(args);
            if (!succesionFlag) return;
            produceFrontFile();
            if (!succesionFlag) return;
            produceQualityIndicatorsFile(args);
            writeGeneralParetoFrontToFile();
            java.awt.Desktop.getDesktop().open(new File(pathTocomparisonResultsFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
