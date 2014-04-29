/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

/**
 *
 * @author touahmed
 */
public class VariantClass {
    int user_id;
    int item_id;
    int no_of_permissions_removed;
    double actual_rating;
    int isAcceptable;
    double predicted_rating;
    
    public VariantClass()
    {
        user_id=0;
        item_id=0;
        no_of_permissions_removed=0;
        actual_rating=0;
        predicted_rating=0;
        isAcceptable=0;
    }
    
    public VariantClass(int user_id,int item_id)
    {
        this.user_id=user_id;
        this.item_id=item_id+16;
        if(this.item_id<20)
            no_of_permissions_removed=1;
        else if(this.item_id<26)
            no_of_permissions_removed=2;
        else if(this.item_id<30)
            no_of_permissions_removed=3;
        else
            no_of_permissions_removed=4;
            
    }
    public void add_actual_rating(double actual)
    {
        actual_rating=actual;
        if(actual_rating>=4.0)
            isAcceptable=1;
        else
            isAcceptable=0;
    }
    
    public void add_predicted_rating(double predicted)
    {
        predicted_rating=predicted;
    }
    
    public void print()
    {
        System.out.println("User_id: "+user_id);
        System.out.println("Item_id: "+item_id+", Item Name: "+GlobalConstants.names.get_names(item_id-16));
        System.out.println("No of Permission Removed: "+no_of_permissions_removed);
        System.out.println("Actual Rating: "+actual_rating+", Acceptable: "+isAcceptable);
        System.out.println("Predicted Rating: "+ predicted_rating);
        
    }
}
