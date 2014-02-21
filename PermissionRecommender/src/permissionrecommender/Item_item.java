import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
*
* @author Qatrunnada
*/

public class Item_item
{
	
	private double Jaccard_similarity;
	private double cosine_similarity;
	public static ItemNames names= new ItemNames(GlobalConstants.number_of_items);
	private String item1;
	private String item2;
	private int item_1;
	private int item_2;
	
	//constructor
    public Item_item() 
    {
    	item1=new String();
    	item2=new String();
        //pearson_similarity=0.0;
        //cosine_similarity=0.0;
    	Jaccard_similarity=0.0;    
    }//end of constructor
    
    public static void set_item_name()
    {
        String[] name={"PS","RA","C", "Co","PS_RA",
            "RA_C","C_Co","PS_C","PS_Co","RA_Co","PS_RA_C","PS_RA_Co","PS_C_Co","RA_C_Co","PS_RA_C_Co"};
        names.set_item_names(name);
    }
    
	
	 private void  calculate_Jaccard_similarity() 
	 {
		 double numerator=0.0;   //>>> item1 intersection item2
		 double denominator=0.0; //>>> item1 union item2
		 int i=0,j=0,k=0;
	     
		 //item1= names.get_names(0);
		 //item2=names.get_names(1);
		
		 
		 //loop through the items//we have 15 of them
		 for(i=0;i<GlobalConstants.number_of_items;i++) // for(i=0;i<5;i++)
		 { numerator=0.0;
		   denominator=0.0;
		   for(j=i+1;j<GlobalConstants.number_of_items;j++) // for(j=i+1;j<5;j++)
			 {
				 item1=names.get_names(i);
				 item2=names.get_names(j);
				 System.out.println("item1:"+item1+" item2:"+item2);
			   
				 String[] parts_item1 = item1.split("_");
				 String[] parts_item2 = item2.split("_");
				 
				 /*for(k=0;k<parts_item1.length;k++)
					 System.out.print(" parts_item1:"+parts_item1[k]);
				
				 System.out.println("");
		 
				 for(k=0;k<parts_item2.length;k++)
					 System.out.print(" parts_item2:"+parts_item2[k]);
				 System.out.println("");*/
				
				 
				 //loop stops with the shorter list
				//calculate intersection
				if(parts_item1.length<parts_item2.length)
				{
					for(k=0;k<parts_item1.length;k++)
					{
						if(Arrays.asList(parts_item2).contains(parts_item1[k]))
							numerator++;					
					}
				}
				else
				{
					for(k=0;k<parts_item2.length;k++)
					{
						if(Arrays.asList(parts_item1).contains(parts_item2[k]))
							numerator++;					
					}
				}
				 
				//System.out.print("numerator:"+numerator);
					
				//just to save ourselves calculations when intersection=0
				if(numerator!=0)
				{
				//calculate union
				//combine the two items in one set without duplications (their union)
				Set<String> item1_item2_combine = new TreeSet<String>();
				for(k=0;k<parts_item1.length;k++)
					item1_item2_combine.add(parts_item1[k]);
				
				for(k=0;k<parts_item2.length;k++)
					item1_item2_combine.add(parts_item2[k]);
				
				//System.out.println(item1_item2_combine);
				//System.out.println(" denominator:"+item1_item2_combine.size());
				denominator=item1_item2_combine.size();
				
				Jaccard_similarity=numerator/denominator;
				}
				else
					Jaccard_similarity=0;
				
				System.out.println("Jaccard_similarity="+Jaccard_similarity);
				System.out.println("");
				 
			 }
		 }
			 
		 
	    }//end of calculate_Jaccard_similarity
	 
	 
	 public void calculate_cosine_similarity()
	    {
	        double numerator=0.0;
	        double item1_denominator=0.0;
	        double item2_denominator=0.0;
	        
	        for(int k=0;k<GlobalConstants.number_of_items;k++)
			 { 
			    for(int m=k+1;m<GlobalConstants.number_of_items;m++)
				 {
			    	numerator=0.0;
			        item1_denominator=0.0;
				    item2_denominator=0.0;
				    
				   // System.out.println("PermissionRecommender.allData.size():"+PermissionRecommender.allData.size());
			    	//loop through all user ratings for item1, item2
			        for(int i=0;i<PermissionRecommender.allData.size();i++)
			        {
			            double item_1_rating=PermissionRecommender.allData.get(i).get_specific_rating(k);  
			            double item_2_rating=PermissionRecommender.allData.get(i).get_specific_rating(m);
			          //  System.out.println("item_1_rating#"+item_1_rating+" and item_2_rating#"+item_2_rating);
			            numerator+=((item_1_rating)*(item_2_rating));
			            item1_denominator+=((item_1_rating)*(item_1_rating));
			            item2_denominator+=((item_2_rating)*(item_2_rating));
			        }
			        cosine_similarity=numerator/(Math.sqrt(item1_denominator)*Math.sqrt(item2_denominator));
			        System.out.println("item#"+k+" and item#"+m);
			        System.out.println("cosine_similarity="+cosine_similarity);
					System.out.println("");
				 }
			 }
	        
	    }//end of calculate_cosine_similarity
	 
	 public void calculate_similarity()
	    {
		 set_item_name();
	        //calculate_pearson_distance();
	        //calculate_constraint_pearson_distance();
	       // calculate_cosine_similarity();
		 calculate_Jaccard_similarity();
		 calculate_cosine_similarity();
	    }

}
