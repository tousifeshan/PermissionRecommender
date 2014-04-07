
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

import java.util.*;

/**
 *
 * @author touahmed
 */
public class SortByMetric {
    private int active_user_id;
    Map<Integer,Double> unsortMap= new HashMap<Integer, Double>();
    Map<Integer,Double> sortedMap;
    private ArrayList<Integer> nearest_ids=new ArrayList<Integer>();
    
    public SortByMetric()
    {
        active_user_id=0;
    }

    public SortByMetric(int active_id) {
        active_user_id=active_id;
    }
    
    
    public void addValue(int second_user_id, double metric_value)
    {
        unsortMap.put(second_user_id, metric_value);
    }
    public void printUnsortedMap()
    {
        printMap(unsortMap);
    }
    
    public void sortMap()
    {
        sortedMap=sortByComparator(unsortMap);
        add_nearest_users(sortedMap);
    }
    public void printeSortedMap()
    {
        printMap(sortedMap);
    }
    
    public void add_nearest_users(Map<Integer,Double> map)
    {
        for(Map.Entry entry:map.entrySet())
        {
            nearest_ids.add(Integer.parseInt(entry.getKey().toString()));
            
        }
    }
    public ArrayList<Integer> get_nearest_ids()
    {
        return nearest_ids;
    }
    public int get_active_user_id()
    {
        return active_user_id;
    }
    public void printMap(Map<Integer,Double> map)
    {
        for(Map.Entry entry:map.entrySet())
        {
            System.out.println("user_id "+entry.getKey()+ " Metric: "+ entry.getValue());
            
        }
    }
    public Map sortByComparator(Map unsortMap) {
 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
 
}
