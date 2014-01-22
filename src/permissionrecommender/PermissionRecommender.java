/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author touahmed
 */
public class PermissionRecommender {

    public static Vector<Data> allData = new Vector<Data>();
    ItemNames names= new ItemNames();
    /**
     * @param args the command line arguments
     */
    
    public static void readFile(boolean header, String filename)
    {
    // Variables
        BufferedReader datain;
        String line;
        

        // Open File
        try {
            datain = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            datain = null;
            Logger.getLogger(PermissionRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Read The Whole File and Create Dataset
        try {
            for (int i = 0; (line=datain.readLine())!= null; i++) {
              //  System.out.println(line);
                //skip Header
                if(header)
                    
                if(i!=0)
                    allData.add(new Data(i, line));
            }
        } catch (IOException ex) {
            Logger.getLogger(PermissionRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
