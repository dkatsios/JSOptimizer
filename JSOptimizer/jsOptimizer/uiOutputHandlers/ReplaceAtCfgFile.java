package uiOutputHandlers;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;

/**
 * ReplaceAtCfgFile replaces an attribute line with the new one
 * containing the values dictated by JMetal at the target .cfg file
 *
 */
public class ReplaceAtCfgFile {

    public static void replaceSelected( String pathToCfgFile, String replace1, String replace2) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(pathToCfgFile));
            String line;String input = "";

            while ((line = file.readLine()) != null) input += line + '\n';

            file.close();

            input = input.replace(replace1, replace2);

            FileOutputStream fileOut = new FileOutputStream(pathToCfgFile);
            fileOut.write(input.getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

    public static void main(String[] args) {
        replaceSelected(args[0], args[1], args[2]);
    }
}