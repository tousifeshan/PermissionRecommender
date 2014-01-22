/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

/**
 * This class is used to store the ratings for the users. This class will be the basic data set.
 * 
 * @author touahmed
 */
public class Data {
    private int user_id; // the user_id
    private double[] rating; // ratings for different items
    private int total_items; // For CrowdSec it refers to total no of apps
    
    /* Default Constructor */
    public Data() // constructor
    {
        int user_id=0;
        total_items=0;      
    }
    
    /* Constructor with total items */
    public Data(int total_items)
    {
        this.total_items=total_items;
        initialize_rating();
    }
    public Data(int user_id, double [] item_ratings)
    {
        this.user_id=user_id;
        this.total_items=item_ratings.length;
        initialize_rating();
        for(int i=0;i<total_items;i++)
        {
            rating[i]=item_ratings[i];
        }
    }
    /*Constructor with direct line, it is expected that the csv file only consists the user id and item ratings. If there is extra data, try using other functions*/
    public Data(String line,int total_items){
        
        this.total_items=total_items;
        String tokens[] = line.split("[,]");
        this.user_id=Integer.parseInt(tokens[0]);
        
                
        for(int i=0; i<total_items; i++){
            rating[i] = Double.parseDouble(tokens[i]);
        }
       
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
    
   
}
