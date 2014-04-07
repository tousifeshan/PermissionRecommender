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
public class Neighbours {
    public User_Ratings active_user;
    Vector<User_Ratings> nearest_neighbours;
    
    Neighbours()
    {
        active_user=new User_Ratings();
        nearest_neighbours=new Vector<User_Ratings>();    
    }
    
    Neighbours(User_Ratings active)
    {
        active_user=active;
        nearest_neighbours=new Vector<User_Ratings>();   
    }
    
    public void add_nearest_neighbours(User_Ratings nn)
    {
        nearest_neighbours.add(nn);
    }
    
    public int get_active_user_id()
    {
        return active_user.get_user_id();
    }
    public int get_neighbours_id(int index)
    {
        return nearest_neighbours.get(index).get_user_id();
    }
    
    public double predict_average_rating(int index)
    {
        double predicted_rating=0.0;
        double rating_sum=0.0;
        int total_user=0;
        for(int i=0;i<nearest_neighbours.size();i++)
        {
            User_Ratings nn=nearest_neighbours.get(i);
            if(nn.get_specific_rating(index)>0.0)
            {
                rating_sum+=nn.get_specific_rating(index);
                total_user++;
            }
            
            
        }
        predicted_rating=rating_sum/(double)total_user;
        return predicted_rating;
    }
    
    
    
   
    
    
}
