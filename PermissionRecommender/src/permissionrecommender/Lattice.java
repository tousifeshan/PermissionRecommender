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
public class Lattice {
    int parent_id;
    Vector<Integer> child_id;
    
    Lattice()
    {
        parent_id=0;
        child_id=new Vector<Integer>();
    }
    Lattice(int pId)
    {
        parent_id=pId;
        child_id=new Vector<Integer>();
    }
    
    public void add_child(int cId)
    {
        child_id.add(cId);
    }
    
    public Vector<Integer> get_childs()
    {
        return child_id;
    }
    public void print_childs()
    {
        System.out.println("Parent: "+ parent_id+ "Name: "+GlobalConstants.names.get_names(parent_id-16));
        for(int i=0;i<child_id.size();i++)
        {
            System.out.println("Child- "+i+" : " +child_id.get(i)+", Name: "+GlobalConstants.names.get_names(child_id.get(i)-16));
        }
    }
    
}
