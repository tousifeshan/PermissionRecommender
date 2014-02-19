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
    Data user_1;
    Data user_2;
    double pearson_similarity;
    double cosine_similarity;

    public User_User() {
        user_1=new Data();
        user_2=new Data();
        pearson_similarity=0.0;
        cosine_similarity=0.0;
        
    }
    
    
    
}
