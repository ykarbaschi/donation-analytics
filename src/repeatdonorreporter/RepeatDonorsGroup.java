package repeatdonorreporter;

import java.util.ArrayList;
import java.util.List;

public class RepeatDonorsGroup {
    List<Float> amounts;
    float total;
    float precision;

    public RepeatDonorsGroup(float thePrecision){
        amounts = new ArrayList<>();
        total = 0;
        precision = thePrecision;
    }
    public void addOrPut(float newAmount) throws ArithmeticException{
        if(amounts.size()==0)
            amounts.add(newAmount);
        else
            for (int i = 0; i <= amounts.size(); i++) {
                //add to list if greater than all
                if(i == amounts.size()) {
                    amounts.add(newAmount);
                    break;
                }
                else if(Math.abs(newAmount - amounts.get(i)) < precision) {
                    amounts.add(i, newAmount);
                    break;
                }
            }

        total += newAmount;

    }
}
