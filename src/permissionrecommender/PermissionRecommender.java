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
    public static Vector<Neighbours> users_nearest_neighbours=new Vector<Neighbours>();
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
  //          System.out.println("Size: "+sorted_map.get_nearest_ids().size());
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
            //sorted_map.printeSortedMap();
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
            //       double std=users_correlation.get(j).get_user2().get_std()==0.0?0:(1/users_correlation.get(j).get_user2().get_std());
            //       sum_correlation_ratings+=(users_correlation.get(j).get_correlation())*
             //              ((users_correlation.get(j).get_user2().get_specific_rating(index)-users_correlation.get(j).get_user2().get_mean())*std);
                    
                   sum_correlation_ratings+=(users_correlation.get(j).get_correlation())*
                           ((users_correlation.get(j).get_user2().get_specific_rating(index)-users_correlation.get(j).get_user2().get_mean()));
                    
                 //   System.out.println("STD: "+ users_correlation.get(j).get_user2().get_std());
                    sum_correlation+=Math.abs(users_correlation.get(j).get_correlation());
                    break;
                }
               //System.out.println("Out Index:"+j );
            }
        }
      //  System.out.println("Correlation: "+ sum_correlation_ratings+ ", Sum: "+sum_correlation);
        //predicted_rating=active_user.get_mean()+(active_user.get_std()*(sum_correlation_ratings/sum_correlation));
        
        predicted_rating=active_user.get_mean()+((sum_correlation_ratings/sum_correlation));
        
       // Random Prediction 
        Random rn=new Random();
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
            //   System.out.println("CID: "+ cid);
                double s_rating=active_user.get_specific_rating(cid);
                if(s_rating>0.0 && s_rating<min)
                    min=s_rating;
            }
        }
        else
        {
            return prediction;
        }
       //System.out.println("Minimum: "+min+", Prediction: "+prediction);
        if(prediction<min)
            min=prediction;
        else
        {
             System.out.println("Active User: "+active_user.get_user_id()+", index "+(index+16) +" Minimum: "+min+", Prediction: "+prediction);
        }
        
        return min;
    }
    

    //without clamping
    
    
    public static double leave_one_out_evaluation(int metric_type) throws IOException
    {
        double total_mae=0.0;
        double item_mae=0.0;
        double total_rmse=0.0;
        double item_rmse=0.0;
        
        double random_total_mae=0.0;
        double random_item_mae=0.0;
       double random_total_rmse=0.0;
       double random_item_rmse=0.0;
        
        int tp=0;
        int tn=0;
        int fp=0;
        int fn=0;
        
               
        
        for(int i=0;i<allUserRatings.size();i++)
        {
            User_Ratings active_user=allUserRatings.get(i);
            item_mae=0.0;
            if(active_user.get_user_id()==79)
            {
           for(int j=0;j<active_user.get_total_used();j++)
         //   for(int j=0;j<15;j++)
            {
                int index=active_user.leave_nth_one(j);
              //  int index=0;
            //    System.out.println("Index "+ index);
             //   System.out.println("Initial Mean"+active_user.get_mean());
            //    System.out.println(active_user.get_user_id());
                VariantClass var=new VariantClass(active_user.get_user_id(),index);
              
                double actual_value=active_user.get_specific_rating(index);
                active_user.set_specific_rating(0.0, index);
             //  System.out.println("Actual Rating "+actual_value);
                var.add_actual_rating(actual_value);
               
                users_correlation.clear();
                set_user_user(i,metric_type,index);
                Neighbours nearest_set=users_nearest_neighbours.get(0);
               print_nearest_neighbours();
                double predicted_rating=nearest_set.predict_average_rating(index);
                predicted_rating=predict_ratings_based_on_similarity(active_user, nearest_set,index);
                
            //    predicted_rating=predict_ratings_based_on_similarity_random(active_user, nearest_set, index);
              // double random_prediction=predict_ratings_based_on_similarity_random(active_user, nearest_set, index);
                if(predicted_rating<1)
                    predicted_rating=1;
                if(predicted_rating>7)
                    predicted_rating=7.0;
              
        //        System.out.println("Prediction:"+predicted_rating);
              
                if(GlobalConstants.clamping)
                {
                 predicted_rating=find_the_minimum(active_user,predicted_rating, index);
                }
                
                  var.add_predicted_rating(predicted_rating);
                
               item_mae+=Math.abs(actual_value-predicted_rating);//predict_ratings_based_on_similarity(active_user, nearest_set,index));
               item_rmse+=(actual_value-predicted_rating)*(actual_value-predicted_rating);
               
           //    random_item_mae+=Math.abs(actual_value-random_prediction);//predict_ratings_based_on_similarity(active_user, nearest_set,index));
            //   random_item_rmse+=(actual_value-random_prediction)*(actual_value-random_prediction);
               
               
   
      
                users_nearest_neighbours.clear();
//                generated_users.clear();
                var.print();
                
                active_user.set_specific_rating(actual_value, index);
                variant.add(var);
               // System.out.println(active_user.get_specific_rating(index));
            }
        }
            
         //   System.out.println("Id: "+active_user.get_user_id()+", Used Items "+active_user.get_total_used());
         //   System.out.println("MAE "+item_mae);
            item_mae=item_mae/(double)active_user.get_total_used();
            item_rmse=item_rmse/(double)active_user.get_total_used();
            
           // random_item_mae=random_item_mae/(double)active_user.get_total_used();
          //  random_item_rmse=random_item_rmse/(double)active_user.get_total_used();
            
            total_mae+=item_mae;
            total_rmse+=item_rmse;
            
          //  random_total_mae+=random_item_mae;
         //  random_total_rmse+=random_item_rmse;
            
        }
        
        
        total_mae=total_mae/(allUserRatings.size());
         System.out.printf(" MAE %.3f, RMSE %.3f ",(total_mae), Math.sqrt(total_rmse/(allUserRatings.size())));
         System.out.println();
         
   //      System.out.println("Total RMSE: "+Math.sqrt(total_rmse/(allUserRatings.size())));
    
       
     //  return total_mae/(allUserRatings.size());
     //  System.out.println("TP: "+tp+", FP: "+fp+" TN: "+tn+", FN: "+fn);
       double tpr=(double)(tp/(double)(tp+fp));
       double tnr=(double)(tp/(double)(tp+fn));
       double accu=(double)((tp+tn)/(double)(tp+tn+fp+fn));

       
   //    System.out.println("True: "+ true_v+ ", False: "+false_v);
       
       //Close the Print Writer   
       //return total_mae;
      return Math.sqrt(total_rmse/(allUserRatings.size()));
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
    
   

    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        set_item_name();
        readFile(true, "result_july_15.csv");
        generate_lattice();
       // print_data();
        //set_user_user(0,GlobalConstants.metric_pearson_corrlation);
      //  print_nearest_neighbours();
        
       
        double error=0;
        for(int i=2;i<3;i++)
        {
            GlobalConstants.number_of_neighbours=i;
            
           GlobalConstants.clamping=false;
          // System.out.print("NN: "+i+ " Without Clamping");
      //     error+=leave_one_out_evaluation(GlobalConstants.metric_cosine_similarity);
            
            GlobalConstants.clamping=true;
           // System.out.print("NN: "+i+ " With Clamping");
            error=leave_one_out_evaluation(GlobalConstants.metric_cosine_similarity);
            
            System.out.println("-------------------------------------------");
            
        }
       // correctness_variant2();
        //correctness_variant1();
     //   error=leave_one_out_evaluation_new(GlobalConstants.metric_cosine_similarity);
        int tot_ac=0;
        int tot_fault=0;
        for(int l=0;l<variant.size();l++)
        {
        //  System.out.println("Index "+ l);
          if(variant.get(l).user_id==61 || variant.get(l).user_id==77)
          {
       //     variant.get(l).print();
          }
            tot_ac=variant.get(l).predicted_rating>=4.0?tot_ac+1:tot_ac;
            tot_fault=variant.get(l).predicted_rating>=4.0 && variant.get(l).isAcceptable==1?tot_fault+1:tot_fault;
            
            
         
                
        }
        
      //  System.out.println("Total: "+tot_ac+ " Fault: "+tot_fault);

       for(int k=0;k<10;k++)
        {
        //    
          // error+=leave_one_out_evaluation(GlobalConstants.metric_cosine_similarity);
        }
       
       
      System.out.println("K-fold result: "+error/10.0);
      //  initialize_items();
       // addItem_ratings();
        //set_item_item();
     //   leave_one_out_evaluation_item();
      // calculate_avg_std();
    }
    
    
    
    
    
  
    
    // Item Based CF
   
    //Printing Stuffs
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
    
           
    
    
}
