/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

/**
 *
 * @author eshan
 */
public class GlobalConstants {
    public static final int number_of_items=15;
    public static final int neutral_rating=4;
    public static  int number_of_neighbours=6;
    public static final int metric_pearson_corrlation=0;
    public static final int metric_cosine_similarity=1;
    public static final int metric_contsrained_pearson_corrlation=2;
    public static  boolean clamping=true;
    public static ItemNames names= new ItemNames(GlobalConstants.number_of_items);
    
}
