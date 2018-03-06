package repeatdonorreporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepeatDonorsGroup {
    private List<Float> amounts;
    private float total;

    public RepeatDonorsGroup(){
        amounts = new ArrayList<>();
        total = 0;
    }

    public float getTotal() {
        return total;
    }

    public int getSize(){
        return amounts.size();
    }

    public float getPercentile(int index){
        return amounts.get(index);
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
