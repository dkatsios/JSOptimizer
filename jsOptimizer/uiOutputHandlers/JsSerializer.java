package uiOutputHandlers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class that serializes and deserializes objects
 *
 */
public class JsSerializer {

    public static void serialize(Object obj, String serializationPath) throws IOException{

        try{

            FileOutputStream fout = new FileOutputStream(serializationPath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(obj);
            oos.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String deserializationPath){

        T obj;

        try{
            FileInputStream fin = new FileInputStream(deserializationPath);
            ObjectInputStream ois = new ObjectInputStream(fin);
            obj = (T) ois.readObject();
            ois.close();

            return obj;

        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
