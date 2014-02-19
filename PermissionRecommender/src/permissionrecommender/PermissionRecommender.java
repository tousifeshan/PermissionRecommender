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

    public static Vector<User_Ratings> allData = new Vector<User_Ratings>();
    public static ItemNames names= new ItemNames(GlobalConstants.number_of_items);
    public static Vector<User_User> users_correlation=new Vector<User_User>();
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
                        allData.add(new User_Ratings(line,GlobalConstants.number_of_items));
                }
                else
                {
                    allData.add(new User_Ratings(line,GlobalConstants.number_of_items));
                    
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PermissionRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void set_item_name()
    {
        String[] name={"Phone_State","Record_Audio","Camera", "Contacts","PS_RA",
            "RA_C","C_Co","PS_C","PS_Co","RA_Co","PS_RA_C","PS_RA_Co","PS_C_Co","RA_C_Co","PS_RA_C_Co"};
        names.set_item_names(name);
    }
    
    public static void set_user_user()
    {
        
        for(int i=0;i<allData.size();i++)
        {
           
            User_Ratings user_1=allData.get(i);
          //  user_corr.add_user1(user_1);
            for(int j=i+1; j<allData.size();j++)
            {
                User_User user_corr=new User_User();
                User_Ratings user_2=allData.get(j);
                user_corr.add_users(user_1, user_2);
                user_corr.add_user2(user_2);
                user_corr.calculate_similarity();
                System.out.print("User 1:  "+user_1.get_user_id()+"\n User 2: "+
                        user_2.get_user_id()+"\n Cosine Similarity: "+user_corr.get_cosine_similarity()+
                        "\n Pearson Correlation: "+user_corr.get_pearson_correlation()+"\n Constraint Pearson: "+
                        user_corr.get_constraint_pearson_correlation()+"\n");
                System.out.println("-----------------------------------------------------------------------------------");
                users_correlation.add(user_corr);
                
            }
        }
    }
    public static void print_data()
    {
        for(int j=0;j<allData.size();j++)
        {
            User_Ratings d=allData.get(j);
            System.out.println("User: "+j);
            System.out.println("User Id:  "+d.get_user_id());
            for(int i=0;i<GlobalConstants.number_of_items;i++)
            {
               // if(i==0)
                   // System.out.println(names.get_names(i)+" "+d.get_user_id());
                //else
                System.out.println(names.get_names(i)+" "+d.get_specific_rating(i));
            }
            System.out.println("Average: "+d.get_mean());
            System.out.println("-------------------------------------------");
        }
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        set_item_name();
        readFile(true, "fake_dataset.csv");
       // print_data();
        set_user_user();
    }
}
