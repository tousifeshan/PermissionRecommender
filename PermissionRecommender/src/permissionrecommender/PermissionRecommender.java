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
    public static ItemNames names= new ItemNames(GlobalConstants.number_of_items);
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
                {
                    if(i!=0)
                        allData.add(new Data(line,GlobalConstants.number_of_items));
                }
                else
                {
                    allData.add(new Data(line,GlobalConstants.number_of_items));
                    
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PermissionRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void set_item_name()
    {
        String[] name={"User Id","Phone_State","Record_Audio","Camera", "Contacts","PS_RA",
            "RA_C","C_Co","PS_C","PS_Co","RA_Co","PS_RA_C","PS_RA_Co","PS_C_Co","RA_C_Co","PS_RA_C_Co"};
        names.set_item_names(name);
    }
    public static void print_data()
    {
        for(int j=0;j<allData.size();j++)
        {
            Data d=allData.get(j);
            System.out.println("User: "+j);
            for(int i=0;i<GlobalConstants.number_of_items;i++)
            {
                if(i==0)
                    System.out.println(names.get_names(i)+" "+d.get_user_id());
                else
                    System.out.println(names.get_names(i)+" "+d.get_specific_rating(i));
            }
            System.out.println("-------------------------------------------");
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        set_item_name();
        readFile(true, "fake_dataset.csv");
        print_data();
    }
}
