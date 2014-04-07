/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author touahmed
 */
public class PermissionRecommender {

    public static Vector<User_Ratings> allUserRatings = new Vector<User_Ratings>();
    public static ItemNames names= new ItemNames(GlobalConstants.number_of_items);
    public static Vector<User_User> users_correlation=new Vector<User_User>();
    public static Vector<Neighbours> users_nearest_neighbours=new Vector<Neighbours>();
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
                    if(i!=0 && line.length()>0)
                        allUserRatings.add(new User_Ratings(line,GlobalConstants.number_of_items));
                }
                else
                {
                    allUserRatings.add(new User_Ratings(line,GlobalConstants.number_of_items));
                    
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
    
    public static void set_user_user(int user_1_index,int metric_type)
    {
        
     //   for(int i=0;i<allUserRatings.size();i++)
      //  {
           
            User_Ratings user_1=allUserRatings.get(user_1_index);
            SortByMetric sorted_map=new SortByMetric(user_1.get_user_id());
          //  user_corr.add_user1(user_1);
            for(int j=0; j<allUserRatings.size();j++)
            {
                if(j!=user_1_index)
                {
                    User_User user_corr=new User_User();
                    User_Ratings user_2=allUserRatings.get(j);
                    user_corr.add_users(user_1, user_2);
                    user_corr.add_user2(user_2);
                    user_corr.calculate_similarity(metric_type);
                    //if(metric_type==GlobalConstants.metric_cosine_similarity)
                    sorted_map.addValue(user_2.get_user_id(), user_corr.get_correlation());
                    
                 //   System.out.println(" User 1: "+user_1.get_user_id()+"\n User 2: "+
         //              user_2.get_user_id()+"\nCorrelation "+ user_corr.get_correlation());
                    
                    /* Print Correllation Values
              //      System.out.print(" User 1: "+user_1.get_user_id()+"\n User 2: "+
               //         user_2.get_user_id()+"\n Cosine Similarity: "+user_corr.get_cosine_similarity()+
               //         "\n Pearson Correlation: "+user_corr.get_pearson_correlation()+"\n Constraint Pearson: "+
              //          user_corr.get_constraint_pearson_correlation()+"\n");
                //    System.out.println("-----------------------------------------------------------------------------------");*/
                    users_correlation.add(user_corr);
                }
              
                
            }
            
            sorted_map.sortMap();
            add_neighbours(user_1,sorted_map.get_nearest_ids());
         //   System.out.println("Active User: "+sorted_map.get_active_user_id());
          //  System.out.println("Unsorted Map");
       //     System.out.println("-----------------------------------------------------------------------------------");
            
          //  sorted_map.printUnsortedMap();
            
         //   System.out.println("Sorted Map");
           // System.out.println("-----------------------------------------------------------------------------------");
           // sorted_map.printeSortedMap();
            //Collections.sort(users_correlation);
         //   SortByMetric sorted_map=new SortByMetric(user_1.get_user_id());
      //      System.out.println("User Correlation Size"+ users_correlation.size());
      //  }
    }
    
    public static User_Ratings get_user_by_id(int userId)
    {
        User_Ratings user=new User_Ratings();
        for(int i=0;i<allUserRatings.size();i++)
        {
            if(allUserRatings.get(i).get_user_id()==userId)
            {
                user= allUserRatings.get(i);
                break;
            }
        }
        return user;
    }
    public static void add_neighbours(User_Ratings active_user,ArrayList<Integer> sorted_ids)
    {
        Neighbours nn=new Neighbours(active_user);
        for(int i=0;i<GlobalConstants.number_of_neighbours;i++)
        {
            User_Ratings neighbour=get_user_by_id(sorted_ids.get(i).intValue());
            nn.add_nearest_neighbours(neighbour);
        }
        users_nearest_neighbours.add(nn);
    }
    
    public static void print_data()
    {
        for(int j=0;j<allUserRatings.size();j++)
        {
            User_Ratings d=allUserRatings.get(j);
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
    public static void print_nearest_neighbours()
    {
        for(int i=0;i<users_nearest_neighbours.size();i++)
        {
         //   System.out.println("Active User: "+ users_nearest_neighbours.get(i).get_active_user_id());
             System.out.println("-------------------------------------------");
             for(int j=0;j<GlobalConstants.number_of_neighbours;j++)
             {
                 System.out.println("Neighbour "+ j+ " : " + users_nearest_neighbours.get(i).get_neighbours_id(j));
             }
             System.out.println("-------------------------------------------");
            
        }
    }
    
    public static double predict_ratings_based_on_similarity(User_Ratings active_user,Neighbours nn, int index)
    {
        double predicted_rating=0.0;
        double sum_correlation=0.0;
        double sum_correlation_ratings=0.0;
        int active_user_id=active_user.get_user_id();
        for(int i=0;i<GlobalConstants.number_of_neighbours;i++)
        {
            int neighbour_id=nn.get_neighbours_id(i);
            for(int j=0;j<users_correlation.size();j++)
            {
               // System.out.println("Neighbours Id: "+ neighbour_id+ "User-User Id: "+users_correlation.get(j).get_user2().get_user_id());
                if((users_correlation.get(j).get_user1().get_user_id()==active_user_id) && (users_correlation.get(j).get_user2().get_user_id()==neighbour_id) )
                {
                    //System.out.println("Index: "+j);
                    sum_correlation_ratings+=(users_correlation.get(j).get_correlation())*(users_correlation.get(j).get_user2().get_specific_rating(index)-users_correlation.get(j).get_user2().get_mean());
                    //System.out.println("Correlation: "+ users_correlation.get(j).get_correlation());
                    sum_correlation+=Math.abs(users_correlation.get(j).get_correlation());
                    break;
                }
               //System.out.println("Out Index:"+j );
            }
        }
      //  System.out.println("Correlation: "+ sum_correlation_ratings+ ", Sum: "+sum_correlation);
        predicted_rating=active_user.get_mean()+(sum_correlation_ratings/sum_correlation);
        return predicted_rating;
    }
    
    
    public static void leave_one_out_evaluation()
    {
        double total_mae=0.0;
        double item_mae=0.0;
        for(int i=0;i<allUserRatings.size();i++)
        {
            User_Ratings active_user=allUserRatings.get(i);
            item_mae=0.0;
            for(int j=0;j<active_user.get_total_used();j++)
            {
                int index=active_user.leave_nth_one(j);
             //   System.out.println("Index "+ index);
                double actual_value=active_user.get_specific_rating(index);
                active_user.set_specific_rating(0.0, index);
               // System.out.println("Actual Rating "+actual_value);
                users_correlation.clear();
                set_user_user(i,GlobalConstants.metric_contsrained_pearson_corrlation);
                Neighbours nearest_set=users_nearest_neighbours.get(0);
                double predicted_rating=nearest_set.predict_average_rating(index);
                predicted_rating=predict_ratings_based_on_similarity(active_user, nearest_set,index);
                item_mae+=Math.abs(actual_value-predict_ratings_based_on_similarity(active_user, nearest_set,index));
            //    print_nearest_neighbours();
              /*   System.out.println("Cosine Similarity ");
                System.out.println("-------------------------------------------");
                System.out.println("Predicted Rating By Average: "+predicted_rating);
                System.out.println("Predicted rating By Correlation: "+predict_ratings_based_on_similarity(active_user, nearest_set,index));
                System.out.println("-------------------------------------------");*/
                users_nearest_neighbours.clear();
           /*     System.out.println("Pearson Correlation ");
                System.out.println("-------------------------------------------");
                set_user_user(i,GlobalConstants.metric_pearson_corrlation);
                nearest_set=users_nearest_neighbours.get(0);
                predicted_rating=nearest_set.predict_average_rating(index);
                System.out.println("Predicted Rating By Average: "+predicted_rating);
                System.out.println("Predicted rating By Correlation: "+predict_ratings_based_on_similarity(active_user, nearest_set,index));
                System.out.println("-------------------------------------------");
                
                System.out.println("Constrained Pearson Correlation ");
                System.out.println("-------------------------------------------");
                set_user_user(i,GlobalConstants.metric_pearson_corrlation);
                nearest_set=users_nearest_neighbours.get(0);
                predicted_rating=nearest_set.predict_average_rating(index);
                System.out.println("Predicted Rating By Average: "+predicted_rating);
                System.out.println("Predicted rating By Correlation: "+predict_ratings_based_on_similarity(active_user, nearest_set,index));
                System.out.println("-------------------------------------------");
              // System.out.println(active_user.get_specific_rating(index));*/
                
                active_user.set_specific_rating(actual_value, index);
               // System.out.println(active_user.get_specific_rating(index));
            }
         //   System.out.println("Id: "+active_user.get_user_id()+", Used Items "+active_user.get_total_used());
          //  System.out.println("MAE "+item_mae);
            item_mae=item_mae/(double)active_user.get_total_used();
            total_mae+=item_mae;
          //  System.out.println("MAE "+item_mae);
        }
        System.out.println("Total MAE "+total_mae/(allUserRatings.size()*6));
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        set_item_name();
        readFile(true, "fake_dataset.csv");
        //print_data();
       // set_user_user(GlobalConstants.metric_cosine_similarity);
      //  print_nearest_neighbours();
        leave_one_out_evaluation();
    }
}
