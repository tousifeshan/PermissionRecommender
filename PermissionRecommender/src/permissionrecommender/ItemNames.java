/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

/**
 *
 * @author eshan
 */
public class ItemNames {
    private String[] item_names;
    private int total_items;
    
    public ItemNames()
    {
        total_items=0;
    }
    public ItemNames(int total_items)
    {
        this.total_items=total_items;
        initialize_item_names();
    }
    public void setSize(int total_items)
    {
        this.total_items=total_items;
        initialize_item_names();
        
    }
    public void initialize_item_names()
    {
        item_names=new String[this.total_items];
    }
    
    public void set_item_names(String [] names)
    {
        for(int i=0;i<names.length;i++)
        {
            item_names[i]=names[i];
        }
    }
    public String get_names(int index)
    {
        return item_names[index];
    }
   
}
