/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.ranges.Range;

/**
 *
 * @author touahmed
 */
public class PermissionRecommender {

    public static Vector<User_Ratings> allUserRatings = new Vector<User_Ratings>();
    
    public static Vector<User_User> users_correlation=new Vector<User_User>();
    public static Vector<ItemBased> items_correlation=new Vector<ItemBased>();
    public static Vector<Neighbours> users_nearest_neighbours=new Vector<Neighbours>();
    public static Vector<User_Ratings> generated_users = new Vector<User_Ratings>();
    public static Vector<ItemClass> itemRatings = new Vector<ItemClass>();
    public static Vector<Lattice> lattice=new Vector<Lattice>();
    public static Vector<VariantClass> variant=new Vector<VariantClass>();
    /**
     * @param args the command line arguments
     */
    
    
    //Read File
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
    
    
    public static void addItem_ratings()
    {
        
        for(int i=0;i<allUserRatings.size();i++)
        {
            User_Ratings userRating=allUserRatings.get(i);
            for(int j=0;j<itemRatings.size();j++)
            {
                ItemClass item=itemRatings.get(j);
                item.setRatiings(userRating.get_specific_rating(j), i);
            }
              
            
        }
        
    }
    
    public static void initialize_items()
    {
        int start=16;
        for(int i=0;i<GlobalConstants.number_of_items;i++)
        {
            ItemClass item=new ItemClass(start+i,GlobalConstants.names.get_names(i),allUserRatings.size());
            itemRatings.add(item);
        }
        
     /*   for(int i=0;i<itemRatings.size();i++)
        {
            itemRatings.get(i).print_info();
        }*/
    }
    
    // Unnecessary
    public static void set_item_name()
    {
        String[] name={"Audio","Contacts","Location", "Camera","A_Co",
            "A_L","Co_L","A_C","C_Co","C_L","A_L_Co","C_Co_A","C_L_A","C_L_Co","C_L_Co_A"};
        GlobalConstants.names.set_item_names(name);
    }
    
    // setting up user-user
    public static void set_user_user(int user_1_index,int metric_type,int index)
    {
        
     //   for(int i=0;i<allUserRatings.size();i++)
     //   {
           
            User_Ratings user_1=allUserRatings.get(user_1_index);
            SortByMetric sorted_map=new SortByMetric(user_1.get_user_id());
          //  user_corr.add_user1(user_1);
            for(int j=0; j<allUserRatings.size();j++)
            {
                if(j!=user_1_index)
                {
                    // setting up user 2
                    User_User user_corr=new User_User();
                    User_Ratings user_2=allUserRatings.get(j);
      
              /*  Ncessary if you want to set up generated users
               *
               */
                 //   User_Ratings user_2=generated_user();
                //    generated_users.add(user_2);*/
                    
                    
                    user_corr.add_users(user_1, user_2);
                    user_corr.add_user2(user_2);
                    
                    // Calculate Similarity
                    user_corr.calculate_similarity(metric_type,index);
                //   user_corr.calculate_similarity();
                    //if(metric_type==GlobalConstants.metric_cosine_similarity)
                    
                    // If there is any Correlation thenadd to list
                  //if(user_corr.get_correlation()>0)
                        sorted_map.addValue(user_2.get_user_id(), user_corr.get_correlation());
                   
              //    System.out.println(" User 1: "+user_1.get_user_id()+"\n User 2: "+
                //     user_2.get_user_id()+"\nCorrelation "+ user_corr.get_correlation());
                    
                    // Print Correllation Values
              /*      System.out.print(" User 1: "+user_1.get_user_id()+"\n User 2: "+
                       user_2.get_user_id()+"\n Cosine Similarity: "+user_corr.get_cosine_similarity()+
                       "\n Pearson Correlation: "+user_corr.get_pearson_correlation()+"\n Constraint Pearson: "+
                       user_corr.get_constraint_pearson_correlation()+"\n");
                    System.out.println("-----------------------------------------------------------------------------------");*/
                    users_correlation.add(user_corr);
                }
              
                
            }
            
            
            // Sorting the users based on similarity
            sorted_map.sortMap();
      //      System.out.println("Size: "+sorted_map.get_nearest_ids().size());
          //  add_random_neighbours(user_1,sorted_map.get_nearest_ids()); // random neighbor
         add_neighbours(user_1,sorted_map.get_nearest_ids());
        //    add_neighbours_from_genrated(user_1,sorted_map.get_nearest_ids());
     //       add_random_neighbours(user_1);
     //      System.out.println("Active User: "+sorted_map.get_active_user_id());
          //  System.out.println("Unsorted Map");
       //     System.out.println("-----------------------------------------------------------------------------------");
            
          //  sorted_map.printUnsortedMap();
            
     //       System.out.println("Sorted Map");
      //      System.out.println("-----------------------------------------------------------------------------------");
          //  sorted_map.printeSortedMap();
            //Collections.sort(users_correlation);
         //   SortByMetric sorted_map=new SortByMetric(user_1.get_user_id());
      //      System.out.println("User Correlation Size"+ users_correlation.size());
     //  }
    }
    
    public static void set_item_item()
    {
        
        for(int i=0;i<itemRatings.size();i++)
        {
             ItemClass item_1=itemRatings.get(i);
             SortByMetric sorted_map=new SortByMetric(item_1.get_id());
              for(int j=0; j<itemRatings.size();j++)
              {
                if(j!=i)
                {
                    ItemBased item_corr=new ItemBased();
                    ItemClass item_2=itemRatings.get(j);
                    
                    item_corr.add_items(item_1, item_2);
                    
                    item_corr.calculate_cosine_similarity();
                    
               //     System.out.println(" Item 1: "+item_1.get_id()+"\n Item 2: "+
                //     item_2.get_id()+"\nCorrelation "+ item_corr.get_correlation());
                    
                    items_correlation.add(item_corr);
                    sorted_map.addValue(item_2.get_id(), item_corr.get_correlation());
                    
                }
              }
              sorted_map.sortMap();
          //   System.out.println("Active Item: "+sorted_map.get_active_user_id());
        //      System.out.println("Sorted Map");
      //      System.out.println("-----------------------------------------------------------------------------------");
        //    sorted_map.printeSortedMap();
        }
        
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
    public static User_Ratings get_user_by_id_from_generated(int userId)
    {
        User_Ratings user=new User_Ratings();
        for(int i=0;i<generated_users.size();i++)
        {
            if(generated_users.get(i).get_user_id()==userId)
            {
                user= generated_users.get(i);
                break;
            }
        }
        return user;
    }
    
    public static User_Ratings generated_user()
    {
        User_Ratings user=new User_Ratings(GlobalConstants.number_of_items);
        Random rn=new Random();
        user.set_user_id(rn.nextInt(500)+100);
        for(int i=0;i<GlobalConstants.number_of_items;i++)
        {
            
            int rating=rn.nextInt(5)+1;
            user.set_specific_rating(rating, i);
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
    public static void add_neighbours_from_genrated(User_Ratings active_user,ArrayList<Integer> sorted_ids)
    {
        Neighbours nn=new Neighbours(active_user);
        for(int i=0;i<GlobalConstants.number_of_neighbours;i++)
        {
            User_Ratings neighbour=get_user_by_id_from_generated(sorted_ids.get(i).intValue());
            nn.add_nearest_neighbours(neighbour);
        }
        users_nearest_neighbours.add(nn);
    }
    
    public static void add_random_neighbours(User_Ratings active_user)
    {
        Neighbours nn=new Neighbours(active_user);
        for(int i=0;i<GlobalConstants.number_of_neighbours;i++)
        {
            User_Ratings neighbour=generated_user();
            nn.add_nearest_neighbours(neighbour);
        }
        users_nearest_neighbours.add(nn);
    }
    
    public static void add_random_neighbours(User_Ratings active_user,ArrayList<Integer> sorted_ids)
    {
        Neighbours nn=new Neighbours(active_user);
        Random rn=new Random();
        for(int i=0;i<GlobalConstants.number_of_neighbours;i++)
        {
            User_Ratings neighbour=get_user_by_id(sorted_ids.get( rn.nextInt(sorted_ids.size())).intValue());
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
                System.out.println(GlobalConstants.names.get_names(i)+" "+d.get_specific_rating(i));
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
    
    // Predict ratings using the similarity
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
 //               System.out.println("Neighbours Id: "+ neighbour_id+ "User-User Id: "+users_correlation.get(j).get_user2().get_user_id());
                if((users_correlation.get(j).get_user1().get_user_id()==active_user_id) && (users_correlation.get(j).get_user2().get_user_id()==neighbour_id) )
                {
                    //System.out.println("Index: "+j);
                    sum_correlation_ratings+=(users_correlation.get(j).get_correlation())*(users_correlation.get(j).get_user2().get_specific_rating(index)-users_correlation.get(j).get_user2().get_mean());
                    
                  //  System.out.println("Correlation: "+ users_correlation.get(j).get_correlation());
                    sum_correlation+=Math.abs(users_correlation.get(j).get_correlation());
                    break;
                }
               //System.out.println("Out Index:"+j );
            }
        }
      //  System.out.println("Correlation: "+ sum_correlation_ratings+ ", Sum: "+sum_correlation);
        predicted_rating=active_user.get_mean()+(sum_correlation_ratings/sum_correlation);
        //Random rn=new Random();
       // predicted_rating=rn.nextInt(7)+1;
        return predicted_rating;
    }
    
    public static double find_the_minimum(User_Ratings active_user,double prediction, int index)
    {
        double min=100.0;
        Vector<Integer> child_ids=lattice.get(index).get_childs();
        
        if(child_ids.size()>0)
        {
            for(int i=0;i<child_ids.size();i++)
            {
                int cid=child_ids.get(i)-16;
            //    System.out.println("CID: "+ cid);
                double s_rating=active_user.get_specific_rating(cid);
                if(s_rating>0.0 && s_rating<min)
                    min=s_rating;
            }
        }
        else
        {
            return prediction;
        }
       // System.out.println("Minimum: "+min+", Prediction: "+prediction);
        if(prediction<min)
            min=prediction;
        
        return min;
    }
    
    public static double predict_ratings_based_on_similarity_random(User_Ratings active_user,Neighbours nn, int index)
    {
        
      //  System.out.println("Correlation: "+ sum_correlation_ratings+ ", Sum: "+sum_correlation);
       // predicted_rating=active_user.get_mean()+(sum_correlation_ratings/sum_correlation);
        Random rn=new Random();
        double predicted_rating=rn.nextInt(7)+1;
        return predicted_rating;
    }
    public static double get_item_item_correlation(int index1, int index2)
    {
        
        double corr=0.0;
     //   System.out.println("1: "+index1+ " 2: "+index2);
        for(int j=0;j<items_correlation.size();j++)
        {
                // System.out.println("Neighbours Id: "+ neighbour_id+ "User-User Id: "+users_correlation.get(j).get_user2().get_user_id());
            if((items_correlation.get(j).get_item1().get_id()==index1) && (items_correlation.get(j).get_item2().get_id()==index2))
            {
                corr=items_correlation.get(j).get_correlation();
             //   System.out.println("Corr"+corr);
            }
        }

        return corr;
    }
    
    public static double predict_ratings_item_based(User_Ratings active_user, int index)
    {
        double predicted_rating=0.0;
        double sum_correlation=0.0;
        double sum_correlation_ratings=0.0;
        int active_user_id=active_user.get_user_id();
        for(int i=0;i<GlobalConstants.number_of_items;i++)
        {
            if(active_user.get_specific_rating(i)>0.0)
            {
                double corr=get_item_item_correlation(16+index, 16+i);
                sum_correlation_ratings+=corr*active_user.get_specific_rating(i);
                sum_correlation+=Math.abs(corr);
            }
            
               //System.out.println("Out Index:"+j );
            
        }
      //  System.out.println("Correlation: "+ sum_correlation_ratings+ ", Sum: "+sum_correlation);
        predicted_rating=(sum_correlation_ratings/sum_correlation);
        return predicted_rating;
    }
    public static double leave_one_out_evaluation(int metric_type) throws IOException
    {
        double total_mae=0.0;
        double item_mae=0.0;
        double total_rmse=0.0;
        double item_rmse=0.0;
        
   //     double random_total_mae=0.0;
   //     double random_item_mae=0.0;
   ///     double random_total_rmse=0.0;
    //    double random_item_rmse=0.0;
        
        int tp=0;
        int tn=0;
        int fp=0;
        int fn=0;
        
        FileWriter fw = new FileWriter("PredictedRatings_5.csv");
        PrintWriter pw = new PrintWriter(fw);
        
        pw.print("User ID");
        pw.print(",");
        
        pw.print("Item ID");
        pw.print(",");
        
        pw.print("Item Name");
        pw.print(",");
        
        pw.print("Actual Rating");
        pw.print(",");
        
        pw.print("Predicted Rating");
        pw.print(",");
        
        pw.println("MAE");
        double p_level=4.3;
        double a_level=4.0;
        
        int true_v=0;
        int false_v=0;
               
        
        for(int i=0;i<allUserRatings.size();i++)
        {
            User_Ratings active_user=allUserRatings.get(i);
            item_mae=0.0;
            for(int j=0;j<active_user.get_total_used();j++)
            {
                int index=active_user.leave_nth_one(j);
             //   System.out.println("Index "+ index);
             //   System.out.println("Initial Mean"+active_user.get_mean());
           //     System.out.println(active_user.get_user_id());
                VariantClass var=new VariantClass(active_user.get_user_id(),index);
                pw.print(active_user.get_user_id());
                pw.print(",");
                double actual_value=active_user.get_specific_rating(index);
                active_user.set_specific_rating(0.0, index);
                System.out.println("Actual Rating "+actual_value);
                var.add_actual_rating(actual_value);
                pw.print(index+16);
                pw.print(",");
                
             //   System.out.println("Item"+(index+16));
                
                pw.print(GlobalConstants.names.get_names(index));
                pw.print(",");
        
                pw.print(actual_value);
                pw.print(",");
                users_correlation.clear();
                set_user_user(i,metric_type,index);
                Neighbours nearest_set=users_nearest_neighbours.get(0);
                //print_nearest_neighbours();
                double predicted_rating=nearest_set.predict_average_rating(index);
                predicted_rating=predict_ratings_based_on_similarity(active_user, nearest_set,index);
                
            //    predicted_rating=predict_ratings_based_on_similarity_random(active_user, nearest_set, index);
         //       double random_prediction=predict_ratings_based_on_similarity_random(active_user, nearest_set, index);
                if(predicted_rating<1)
                    predicted_rating=1;
                var.add_predicted_rating(predicted_rating);
               // System.out.println("Prediction:"+predicted_rating);
                pw.print(predicted_rating);
                pw.print(",");
                
                //double prediction_minimised=find_the_minimum(active_user,predicted_rating, index);
               // System.out.println("New Prediction: "+prediction_minimised);
                
              //  if(prediction_minimised!=predicted_rating)
               //     System.out.println("Minimized ");
                
                predicted_rating=find_the_minimum(active_user,predicted_rating, index);
                
               item_mae+=Math.abs(actual_value-predicted_rating);//predict_ratings_based_on_similarity(active_user, nearest_set,index));
               item_rmse+=(actual_value-predicted_rating)*(actual_value-predicted_rating);
               
        //       random_item_mae+=Math.abs(actual_value-random_prediction);//predict_ratings_based_on_similarity(active_user, nearest_set,index));
        //       random_item_rmse+=(actual_value-random_prediction)*(actual_value-random_prediction);
               
               pw.println(Math.abs(actual_value-predicted_rating));
        
                if(predicted_rating>=p_level && actual_value>=a_level )
                {
                    System.out.println("TP Actual: "+actual_value+" Prediction"+predicted_rating);
                    tp++;
                }
                if(predicted_rating<p_level  && actual_value<a_level )
                {
                    System.out.println("TN Actual: "+actual_value+" Prediction"+predicted_rating);
                    tn++;
                }
                if(predicted_rating>=p_level  && actual_value<a_level )
                {
                   System.out.println("FP Actual: "+actual_value+" Prediction"+predicted_rating);
                    fp++;
                }
                if(predicted_rating<p_level  && actual_value>=a_level )
                {
                    System.out.println("FN Actual: "+actual_value+" Prediction"+predicted_rating);
                    fn++;
                }
                
                
                
                
            //     print_nearest_neighbours();
           //      System.out.println("Cosine Similarity ");
           //     System.out.println("-------------------------------------------");
          //     System.out.println("Predicted Rating By Average: "+predicted_rating);
      //          System.out.println("Predicted rating By Correlation: "+predict_ratings_based_on_similarity(active_user, nearest_set,index));
        //        System.out.println("-------------------------------------------");
                users_nearest_neighbours.clear();
                generated_users.clear();
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
                variant.add(var);
               // System.out.println(active_user.get_specific_rating(index));
            }
            
         //   System.out.println("Id: "+active_user.get_user_id()+", Used Items "+active_user.get_total_used());
      //      System.out.println("MAE "+item_mae);
            item_mae=item_mae/(double)active_user.get_total_used();
            item_rmse=item_rmse/(double)active_user.get_total_used();
            
      //      random_item_mae=random_item_mae/(double)active_user.get_total_used();
      //      random_item_rmse=random_item_rmse/(double)active_user.get_total_used();
            
            total_mae+=item_mae;
            total_rmse+=item_rmse;
            
  //          random_total_mae+=random_item_mae;
    //        random_total_rmse+=random_item_rmse;
        //   System.out.println("Total MAE "+total_mae);
        //   System.out.println("Total RMSE "+total_rmse);
           
       //    System.out.println("Random Total MAE "+random_total_mae);
     //      System.out.println("Random Total RMSE "+random_total_rmse);
            
        }
        
        
       // total_mae=total_mae/(allUserRatings.size());
       // System.out.println(allUserRatings.size());
        System.out.println("Total RMSE "+Math.sqrt(total_rmse/(allUserRatings.size())));
       System.out.println("Total MAE "+(total_mae/(allUserRatings.size())));
       
     //  System.out.println("Total Random RMSE "+Math.sqrt(random_total_rmse/(allUserRatings.size())));
    //   System.out.println("Total Random MAE "+(random_total_mae/(allUserRatings.size())));
       
     //  return total_mae/(allUserRatings.size());
       System.out.println("TP: "+tp+", FP: "+fp+" TN: "+tn+", FN: "+fn);
       double tpr=(double)(tp/(double)(tp+fp));
       double tnr=(double)(tp/(double)(tp+fn));
       double accu=(double)((tp+tn)/(double)(tp+tn+fp+fn));
       System.out.println("TPR: "+tpr);
       System.out.println("TNR: "+tnr);
       System.out.println("Accuracy: "+accu);
       
       System.out.println("True: "+ true_v+ ", False: "+false_v);
       
       //Close the Print Writer
        pw.close();
        
        //Close the File Writer
        fw.close();      
      return Math.sqrt(total_rmse/(allUserRatings.size()));
    }
      public static double leave_one_out_evaluation_new(int metric_type) throws IOException
    {
        double total_mae=0.0;
        double item_mae=0.0;
        double total_rmse=0.0;
        double item_rmse=0.0;
        
   //     double random_total_mae=0.0;
   //     double random_item_mae=0.0;
   ///     double random_total_rmse=0.0;
    //    double random_item_rmse=0.0;
        
        int tp=0;
        int tn=0;
        int fp=0;
        int fn=0;
        
        FileWriter fw = new FileWriter("PredictedRatings_5.csv");
        PrintWriter pw = new PrintWriter(fw);
        
        pw.print("User ID");
        pw.print(",");
        
        pw.print("Item ID");
        pw.print(",");
        
        pw.print("Item Name");
        pw.print(",");
        
        pw.print("Actual Rating");
        pw.print(",");
        
        pw.print("Predicted Rating");
        pw.print(",");
        
        pw.println("MAE");
        double p_level=4.3;
        double a_level=4.0;
        
        double max_pred=0.0;
        int max_index=0;
        
        int true_v=0;
        int false_v=0;
               
        
        for(int i=0;i<allUserRatings.size();i++)
        {
            User_Ratings active_user=allUserRatings.get(i);
            item_mae=0.0;
            max_pred=0.0;
            max_index=-1;
            System.out.println("User ID: "+active_user.get_user_id());
            for(int j=0;j<active_user.get_total_used();j++)
            {
                int index=active_user.leave_nth_one(j);
            //    System.out.println("Index "+ index);
             //   System.out.println("Initial Mean"+active_user.get_mean());
              //  System.out.println(active_user.get_user_id());
                pw.print(active_user.get_user_id());
                pw.print(",");
                double actual_value=active_user.get_specific_rating(index);
                active_user.set_specific_rating(0.0, index);
            //    System.out.println("Actual Rating "+actual_value);
                pw.print(index+16);
                pw.print(",");
                
             //   System.out.println("Item"+(index+16));
                
                pw.print(GlobalConstants.names.get_names(index));
                pw.print(",");
        
                pw.print(actual_value);
                pw.print(",");
                users_correlation.clear();
                set_user_user(i,metric_type,index);
                Neighbours nearest_set=users_nearest_neighbours.get(0);
                //print_nearest_neighbours();
                double predicted_rating=nearest_set.predict_average_rating(index);
                predicted_rating=predict_ratings_based_on_similarity(active_user, nearest_set,index);
                if(predicted_rating<1)
                    predicted_rating=1;
                
                if(predicted_rating>max_pred)
                {
                    max_pred=predicted_rating;
                    max_index=index;
                }

                pw.print(predicted_rating);
                pw.print(",");
   
                
                predicted_rating=find_the_minimum(active_user,predicted_rating, index);
                
               item_mae+=Math.abs(actual_value-predicted_rating);//predict_ratings_based_on_similarity(active_user, nearest_set,index));
               item_rmse+=(actual_value-predicted_rating)*(actual_value-predicted_rating);
               
               pw.println(Math.abs(actual_value-predicted_rating));
  
                users_nearest_neighbours.clear();
                generated_users.clear();

                active_user.set_specific_rating(actual_value, index);
               // System.out.println(active_user.get_specific_rating(index));
            }
            
            System.out.println("Max_Predict:"+max_pred+ ", Actual: "+active_user.get_specific_rating(max_index)+ ", Item: "+GlobalConstants.names.get_names(max_index));
            if(active_user.get_specific_rating(max_index)>=4.0)
                true_v++;
            else
            {
                if(max_pred>=4.0)
                    false_v++;
                else
                    true_v++;
            }
            

            item_mae=item_mae/(double)active_user.get_total_used();
            item_rmse=item_rmse/(double)active_user.get_total_used();
            
      //      random_item_mae=random_item_mae/(double)active_user.get_total_used();
      //      random_item_rmse=random_item_rmse/(double)active_user.get_total_used();
            
            total_mae+=item_mae;
            total_rmse+=item_rmse;

        }
        System.out.println("Success: "+ true_v+ ",Failure: "+false_v);
        
      
        pw.close();
        
        //Close the File Writer
        fw.close();      
      return Math.sqrt(total_rmse/(allUserRatings.size()));
    }
    
     public static double leave_one_out_evaluation_item()
    {
        double total_mae=0.0;
        double item_mae=0.0;
        double total_rmse=0.0;
        double item_rmse=0.0;
        int tp=0;
        int tn=0;
        int fp=0;
        int fn=0;
        for(int i=0;i<allUserRatings.size();i++)
        {
            User_Ratings active_user=allUserRatings.get(i);
            item_mae=0.0;
            for(int j=0;j<active_user.get_total_used();j++)
            {
                int index=active_user.leave_nth_one(j);
          //      System.out.println("Index "+ index);
            //    System.out.println(active_user.get_user_id());
                double actual_value=active_user.get_specific_rating(index);
                active_user.set_specific_rating(0.0, index);
                addItem_ratings();
     //           System.out.println("Actual Rating "+actual_value);
                items_correlation.clear();
                set_item_item();
              //  Neighbours nearest_set=users_nearest_neighbours.get(0);
          //      print_nearest_neighbours();
                //double predicted_rating=nearest_set.predict_average_rating(index);
               double  predicted_rating=predict_ratings_item_based(active_user, index);
               item_mae+=Math.abs(actual_value-predicted_rating);//predict_ratings_based_on_similarity(active_user, nearest_set,index));
               item_rmse+=(actual_value-predicted_rating)*(actual_value-predicted_rating);
                if(predicted_rating>=4.0 && actual_value>=4.0)
                    tp++;
                if(predicted_rating<4.0 && actual_value<4.0)
                    tn++;
                if(predicted_rating>=4.0 && actual_value<4.0)
                    fp++;
                if(predicted_rating<4.0 && actual_value>=4.0)
                    fn++;
                
                
                
            //     print_nearest_neighbours();
           //      System.out.println("Cosine Similarity ");
           //     System.out.println("-------------------------------------------");
         //      System.out.println("Predicted Rating By Average: "+predicted_rating);
      //          System.out.println("Predicted rating By Correlation: "+predict_ratings_based_on_similarity(active_user, nearest_set,index));
        //        System.out.println("-------------------------------------------");
                users_nearest_neighbours.clear();
                generated_users.clear();
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
            System.out.println("MAE "+item_mae);
            item_mae=item_mae/(double)active_user.get_total_used();
            item_rmse=item_rmse/(double)active_user.get_total_used();
            
            total_mae+=item_mae;
            total_rmse+=item_rmse;
            
           System.out.println("Total MAE "+total_mae);
        }
        System.out.println("TP: "+tp+", FP: "+fp+" TN: "+tn+", FN: "+fn);
        
  //      total_mae=total_mae/(allUserRatings.size());
     //   System.out.println(allUserRatings.size());
        System.out.println("Total RMSE "+Math.sqrt(total_rmse/(allUserRatings.size())));
       System.out.println("Total MAE "+(total_mae/(allUserRatings.size())));
       
       return total_mae;
    }
    
     
    public static  void generate_lattice()
    {
        for(int i=0;i<GlobalConstants.number_of_items;i++)
        {
            Lattice temp_lattice=new Lattice(i+16);
            
            if(i==4)
            {
                temp_lattice.add_child(16);
                temp_lattice.add_child(17);
            }
            
            
            if(i==5)
            {
                temp_lattice.add_child(16);
                temp_lattice.add_child(18);
            }
            
            
            if(i==6)
            {
                temp_lattice.add_child(17);
                temp_lattice.add_child(18);
            }
            
            if(i==7)
            {
                temp_lattice.add_child(16);
                temp_lattice.add_child(19);
            }
            
            if(i==8)
            {
                temp_lattice.add_child(17);
                temp_lattice.add_child(19);
            }
            
            if(i==9)
            {
                temp_lattice.add_child(18);
                temp_lattice.add_child(19);
            }
            
            if(i==10)
            {
                temp_lattice.add_child(20);
                temp_lattice.add_child(21);
                temp_lattice.add_child(22);
            }
            
            if(i==11)
            {
                temp_lattice.add_child(20);
                temp_lattice.add_child(23);
                temp_lattice.add_child(24);
            }
            
            if(i==12)
            {
                temp_lattice.add_child(21);
                temp_lattice.add_child(23);
                temp_lattice.add_child(25);
            }
            
            if(i==13)
            {
                temp_lattice.add_child(22);
                temp_lattice.add_child(24);
                temp_lattice.add_child(25);
            }
            
            if(i==14)
            {
                temp_lattice.add_child(26);
                temp_lattice.add_child(27);
                temp_lattice.add_child(28);
                temp_lattice.add_child(29);
            }
            lattice.add(temp_lattice);
        }
        
     //   for(int i=0;i<lattice.size();i++)
   //     {
         //   lattice.get(i).print_childs();
      //  }
        
        
        
        
    }
    
    
    
    public static void calculate_avg_std()
    {
        
        double sum_std=0.0;
        for(int i=0;i<allUserRatings.size();i++)
        {
            sum_std+=allUserRatings.get(i).get_std();
            System.out.println("STD:"+allUserRatings.get(i).get_std());
        }
        
        System.out.println("Avg STD: "+sum_std/allUserRatings.size());
    }
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        set_item_name();
        readFile(true, "result_april_17.csv");
        generate_lattice();
        //print_data();
        //set_user_user(0,GlobalConstants.metric_pearson_corrlation);
      //  print_nearest_neighbours();
        
       
        double error=0;
        error=leave_one_out_evaluation(GlobalConstants.metric_cosine_similarity);
     //   error=leave_one_out_evaluation_new(GlobalConstants.metric_cosine_similarity);
        
        for(int l=0;l<variant.size();l++)
        {
            variant.get(l).print();
        }
        
       for(int k=0;k<10;k++)
        {
        //    
          // error+=leave_one_out_evaluation(GlobalConstants.metric_cosine_similarity);
        }
       
       
     //  System.out.println("K-fold result: "+error/10.0);
      //  initialize_items();
       // addItem_ratings();
        //set_item_item();
     //   leave_one_out_evaluation_item();
       //calculate_avg_std();
    }
}
