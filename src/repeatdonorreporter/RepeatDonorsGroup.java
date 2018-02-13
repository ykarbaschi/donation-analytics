package repeatdonorreporter;

import java.util.ArrayList;
import java.util.List;

public class RepeatDonorsGroup {
    List<Float> amounts;
    float total;

    public RepeatDonorsGroup(){
        amounts = new ArrayList<>();
        total = 0;
    }
    public void addOrPut(float newAmount) throws ArithmeticException{
        if(amounts.size()==0)
            amounts.add(newAmount);
        else
        // insertion sort, O(n)
            for (int i = 0; i <= amounts.size(); i++) {
                //add to list if greater than all
                if(i == amounts.size()) {
                    amounts.add(newAmount);
                    break;
                }
                else if(newAmount < amounts.get(i)) {
                    amounts.add(i, newAmount);
                    break;
                }
            }

        total += newAmount;

    }
}
