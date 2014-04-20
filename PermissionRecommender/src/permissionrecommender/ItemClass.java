/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

import java.util.Vector;

/**
 *
 * @author touahmed
 */
public class ItemClass {
   private int item_id;
   private String item_name;
   private double[] ratings; 
   private int total_users;
   
   ItemClass()
   {
       item_id=0;
       total_users=0;
       initialize_rating();
   }
   ItemClass(int no_of_users)
   {
       total_users=no_of_users;
       initialize_rating();
   }
   
   ItemClass(int item_id, String item_name,int no_of_users)
   {
       this.item_id=item_id;
       this.item_name=item_name;
       this.total_users=no_of_users;
       initialize_rating();
       
   }
   
     /* This function is needed to initialize the rating array*/
    public void initialize_rating()
    {
        ratings= new double[total_users];
    }
    
   
   public void setRatiings(double rating,int index)
   {
       ratings[index]=rating;
   }
   
   public double getRatings(int index)
   {
       return ratings[index];
   }
   
   public int get_id()
   {
       return item_id;
       
   }
   
   public int get_size()
   {
       return total_users;
   }
           
   public void print_info()
   {
       System.out.println("Id: "+item_id+"Name "+item_name+", Total:"+total_users);
       for(int i=0;i<total_users;i++)
       {
           System.out.println("User "+ i+ ", Rating: "+ratings[i]);
       }
   }
    
}
