/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

import java.util.ArrayList;

/**
 * This class is used to store the ratings for the users. This class will be the basic data set.
 * 
 * @author touahmed
 */
public class User_Ratings {
    private int user_id; // the user_id
    private double[] rating; // ratings for different items
    ArrayList<Integer> used_item_index=new ArrayList<Integer>();
    private double mean_rating;
    private int total_items; // For CrowdSec it refers to total no of apps
    private int total_used;
    
    
    /* Default Constructor */
    public User_Ratings() // constructor
    {
        int user_id=0;
        total_items=0;   
        total_used=0;
    }
    
    /* Constructor with total items */
    public User_Ratings(int total_items)
    {
        this.total_items=total_items;
        initialize_rating();
    }
    public User_Ratings(int user_id, double [] item_ratings)
    {
        this.user_id=user_id;
        this.total_items=item_ratings.length;
        initialize_rating();
        for(int i=0;i<total_items;i++)
        {
            rating[i]=item_ratings[i];
            used_item_index.add(i);
        }
    }
    /*Constructor with direct line, it is expected that the csv file only consists the user id and item ratings. If there is extra data, try using other functions*/
    //unknown rating will be counted as 0
    public User_Ratings(String line,int total_items){
        
        this.total_items=total_items;
        String tokens[] = line.split("[,]");
        int sum_rating=0;
        int total_rating=0;
        this.user_id=Integer.parseInt(tokens[0]); 
        initialize_rating();
        for(int i=0; i<total_items; i++){
            if(tokens[i+1].compareTo("?")!=0)
            {
                rating[i] = Double.parseDouble(tokens[i+1]);
                used_item_index.add(i);
                sum_rating+=rating[i];
                total_rating++;
                
            }
            else
            {
                rating[i]=0;
            }
        }
       mean_rating=(double)sum_rating/(double)total_rating;
       total_used=total_rating;
    }
    
  
   

     /* Function for Intitalize */
    public void initialize()
    {
        this.total_items=0;
     
    }
    
    /* Function for initializing the aray */
    public void initialize(int total_items)
    {
        this.total_items=total_items;
        initialize_rating();
    }
    
    /* This function is needed to initialize the rating array*/
    public void initialize_rating()
    {
        rating= new double[total_items];
    }
    
    /* Only for set ratings */
    public void set_ratings(double [] item_ratings)
    {
        this.total_items=item_ratings.length;
        initialize_rating();
        for(int i=0;i<total_items;i++)
        {
            rating[i]=item_ratings[i];
        }
        
    }
    
    public void set_user_id(int user_id)
    {
        this.user_id=user_id;
    }
    
    public void set_specific_rating(double rating, int index)
    {
        this.rating[index]=rating;
    }
    
    public double get_specific_rating(int index)
    {
        return rating[index];
    }
    public int get_user_id()
    {
        return user_id;
    }
    public double get_mean()
    {
        return mean_rating;
    }
    
    public int leave_nth_one(int n)
    {
        int index=used_item_index.get(n).intValue();
        return index;
    }
    
    public int get_total_used()
    {
        return total_used;   
    }
}
