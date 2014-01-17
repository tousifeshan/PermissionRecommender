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
    private int[] rating; // ratings for different items
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
    
    /* Function for setting total items */
    public void set_totalItems(int total_items)
    {
        this.total_items=total_items;
        initialize_rating();
    }
    
    /* This function is needed to initialize the rating array*/
    public void initialize_rating()
    {
        rating= new int[total_items];
    }
    
}
