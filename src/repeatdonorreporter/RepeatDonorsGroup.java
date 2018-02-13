package repeatdonorreporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepeatDonorsGroup {
    List<Float> amounts;
    float total;

    public RepeatDonorsGroup(){
        amounts = new ArrayList<>();
        total = 0;
    }
    public void addOrPut(float newAmount) throws ArithmeticException{
        int indexOfInsertion = Collections.binarySearch(amounts, newAmount);
        if(indexOfInsertion >=0)
            amounts.add(indexOfInsertion, newAmount);
        else
            amounts.add(-(indexOfInsertion+1), newAmount);

        total += newAmount;
    }
}
