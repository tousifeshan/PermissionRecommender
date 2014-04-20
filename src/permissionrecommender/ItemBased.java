/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

/**
 *
 * @author touahmed
 */
public class ItemBased {
    private ItemClass item_1;
    private ItemClass item_2;
    private double cosine_similarity;
    private double correlation;

    public ItemBased() {
        item_1=new ItemClass();
        item_2=new ItemClass();
        correlation=0.0;
   /*     pearson_similarity=0.0;
        cosine_similarity=0.0;
        constrained_pearson_similarity=0.0;*/
        
    }
    
    public void add_item1(ItemClass item1)
    {
        item_1=item1;
        
        
    }
    public void add_item2(ItemClass item2)
    {
        item_2=item2;
       
        
    }
    
       public void add_items(ItemClass item1, ItemClass item2)
    {
        item_1=item1;
        item_2=item2;
        
    }
    
    public ItemClass get_item1()
    {
        return item_1;
    }
    public ItemClass get_item2()
    {
        return item_2;
    }
    
    public void calculate_cosine_similarity()
    {
        double numerator=0.0;
        double item1_denominator=0.0;
        double item2_denominator=0.0;
        
        for(int i=0;i<item_1.get_size();i++)
        {
          //  if(item_2.getRatings(index)>0.0)
         //   {
                double item_1_rating=item_1.getRatings(i);

                double item_2_rating=item_2.getRatings(i);


                numerator+=((item_1_rating)*(item_2_rating));
                item1_denominator+=((item_1_rating)*(item_1_rating));
                item2_denominator+=((item_2_rating)*(item_2_rating));
          //  }
                
            
        }
        if(item1_denominator==0.0)
            item1_denominator+=0.0001;
        if(item2_denominator==0.0)
            item2_denominator+=0.0001;
        cosine_similarity=numerator/(Math.sqrt(item1_denominator)*Math.sqrt(item2_denominator));
        correlation=numerator/(Math.sqrt(item1_denominator)*Math.sqrt(item2_denominator));
        
    }
    
    public double get_correlation()
    {
        return correlation;
    }
    
}


