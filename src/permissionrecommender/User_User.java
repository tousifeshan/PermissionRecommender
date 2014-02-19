/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

/**
 *
 * @author touahmed
 */
public class User_User {
    private User_Ratings user_1;
    private User_Ratings user_2;
    private double pearson_similarity;
    private double constrained_pearson_similarity;
    private double cosine_similarity;

    public User_User() {
        user_1=new User_Ratings();
        user_2=new User_Ratings();
        pearson_similarity=0.0;
        cosine_similarity=0.0;
        constrained_pearson_similarity=0.0;
        
    }
    public void add_user1(User_Ratings user1)
    {
        user_1=user1;
        
        
    }
    public void add_user2( User_Ratings user2)
    {
        user_2=user2;
       
        
    }
    public void add_users(User_Ratings user1, User_Ratings user2)
    {
        user_1=user1;
        user_2=user2;
        
    }
    
    public User_Ratings get_user1()
    {
        return user_1;
    }
    public User_Ratings get_user2()
    {
        return user_2;
    }
    
    public void calculate_pearson_distance()
    {
        double numerator=0.0;
        double denominator=0.0;
        double user1_denominator=0.0;
        double user2_denominator=0.0;
        for(int i=0;i<GlobalConstants.number_of_items;i++)
        {
            double user_1_rating=user_1.get_specific_rating(i);
            double user_1_mean=user_1.get_mean();
            double user_2_rating=user_2.get_specific_rating(i);
            double user_2_mean=user_2.get_mean();
            if((user_1_rating>0.0) && (user_2_rating>0.0))
            {
                numerator+=((user_1_rating-user_1_mean)*(user_2_rating-user_2_mean));
                user1_denominator+=((user_1_rating-user_1_mean)*(user_1_rating-user_1_mean));
                user2_denominator+=((user_2_rating-user_1_mean)*(user_2_rating-user_1_mean));
                
            }
        }
        pearson_similarity=numerator/(Math.sqrt(user1_denominator)*Math.sqrt(user2_denominator));
        
    }
    
    public void calculate_constraint_pearson_distance()
    {
        double numerator=0.0;
        double user1_denominator=0.0;
        double user2_denominator=0.0;
        double neutral_value=GlobalConstants.neutral_rating;
        for(int i=0;i<GlobalConstants.number_of_items;i++)
        {
            double user_1_rating=user_1.get_specific_rating(i);
           
            double user_2_rating=user_2.get_specific_rating(i);
            
            if((user_1_rating>0.0) && (user_2_rating>0.0))
            {
                numerator+=((user_1_rating-neutral_value)*(user_2_rating-neutral_value));
                user1_denominator+=((user_1_rating-neutral_value)*(user_1_rating-neutral_value));
                user2_denominator+=((user_2_rating-neutral_value)*(user_2_rating-neutral_value));
                
            }
        }
        constrained_pearson_similarity=numerator/(Math.sqrt(user1_denominator)*Math.sqrt(user2_denominator));
        
    }
    public void calculate_cosine_similarity()
    {
        double numerator=0.0;
        double user1_denominator=0.0;
        double user2_denominator=0.0;
        
        for(int i=0;i<GlobalConstants.number_of_items;i++)
        {
            double user_1_rating=user_1.get_specific_rating(i);
           
            double user_2_rating=user_2.get_specific_rating(i);
            
            if((user_1_rating>0.0) && (user_2_rating>0.0))
            {
                numerator+=((user_1_rating)*(user_2_rating));
                user1_denominator+=((user_1_rating)*(user_1_rating));
                user2_denominator+=((user_2_rating)*(user_2_rating));
                
            }
        }
        cosine_similarity=numerator/(Math.sqrt(user1_denominator)*Math.sqrt(user2_denominator));
        
    }
    public void calculate_similarity()
    {
        calculate_pearson_distance();
        calculate_constraint_pearson_distance();
        calculate_cosine_similarity();
    }
    
}
