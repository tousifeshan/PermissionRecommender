/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permissionrecommender;

/**
 *
 * @author touahmed
 */
public class UserVariant {
    int user_id;
    int should_retreive;
    double max_prediction;
    double second_max;
    double actual_value_for_max;
    double actual_value_for_second_max;
    int should_count;
    
    
    int is_actual_acceptable;
    int is_second_acceptable;
    
    int top;
    
    int max_index;
    int second_max_index;
    
    int relevant;
    int retrieved;
    double threshold;

    public UserVariant() {
        user_id=0;
        should_retreive=0;
        max_prediction=0;
        actual_value_for_max=0;
        max_index=0;
        relevant=0;
        threshold=4.0;
        retrieved=0;
        is_actual_acceptable=0;
        is_second_acceptable=0;
        top=1;
        second_max=0;
        should_count=0;
    }
    
    public UserVariant(int uId)
    {
        user_id=uId;
        should_retreive=0;
        max_prediction=0;
        actual_value_for_max=0;
        max_index=0;
        relevant=0;
        threshold=4.0;
        retrieved=0;
        is_actual_acceptable=0;
        is_second_acceptable=0;
        top=1;
        second_max=0;
        should_count=0;
    }
    
    public void set_topN(int n)
    {
        top=n;
    }
    
    public void set_threshold(double threshold)
    {
         this.threshold=threshold;
    }
    public void set_should_retrieve(int isAcceptable)
    {
        if(isAcceptable==1)
            should_retreive=1;
    }
    
    public boolean set_max(double prediction,int index, double actual, int isAcceptable)
    {
        if(prediction>=max_prediction)
        {
            second_max=max_prediction;
            second_max_index=max_index;
            actual_value_for_second_max=actual;
            is_second_acceptable=is_actual_acceptable;
            
            max_prediction=prediction;
            max_index=index;
            set_actual(actual);
            set_is_actual_acceptable(isAcceptable);
            
            return true;
            
        }
        else if(prediction> second_max)
        {
            second_max=prediction;
            second_max_index=index;
            actual_value_for_second_max=actual;
            set_is_second_acceptable(isAcceptable);
            return true;
        }
        return false;
    }
    
    public void set_max_random(double prediction,int index, double actual, int isAcceptable)
    {
        max_prediction=prediction;
        max_index=index;
        set_actual(actual);
        set_is_actual_acceptable(isAcceptable);
    }
    
    public void set_actual(double actual)
    {
        actual_value_for_max=actual;
    }
    public void set_is_actual_acceptable(int isAcceptable)
    {
        set_is_second_acceptable(is_actual_acceptable);
        is_actual_acceptable=isAcceptable;
    }
    public void set_is_second_acceptable(int isAcceptable)
    {
        is_second_acceptable=isAcceptable;
    }
    
    public void check_relevance()
    {
        if(max_prediction>=threshold)
        {
            retrieved=1; // As max is not is not grater than threshold,none of them is greater than threshold
            if(top==1)
            {
                if(is_actual_acceptable==1)
                {
                relevant=1;
                }
            }
            
            if(top==2)
            {
                if(is_actual_acceptable==1 || is_second_acceptable==1)
                {
                  relevant=1;
                }
            }
            
        }
    }
    
    public void print()
    {
        System.out.println("-------------------------------------------");
        System.out.println("User ID: "+user_id);
        System.out.println("-------------------------------------------");
        System.out.println("Actual Rating: "+actual_value_for_max+", Acceptable: "+is_actual_acceptable);
        System.out.println("Should Retrieve: "+should_retreive);
        System.out.println("Retrived: "+ retrieved);
        System.out.println("Relevant: "+ relevant);
        System.out.println("Max_Predict:"+max_prediction+ ", Actual: "+actual_value_for_max+ ", Item: "+GlobalConstants.names.get_names(max_index));
        System.out.println("Second Max_Predict:"+second_max+ ", Actual: "+actual_value_for_second_max+ ", Item: "+GlobalConstants.names.get_names(second_max_index));
    }
    
}
