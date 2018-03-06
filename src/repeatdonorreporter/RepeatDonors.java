package repeatdonorreporter;

import java.util.Comparator;
import java.util.PriorityQueue;

public class RepeatDonors {
    private float percentile;
    private float total;
    PriorityQueue<Float> maxHeap;
    PriorityQueue<Float> minHeap;
    private int size;

    public RepeatDonors(float thePercentile){
        percentile = thePercentile;
        maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        minHeap = new PriorityQueue<>();
        size = 0;
    }

    public float getTotal() {
        return total;
    }

    public int getSize() {
        return size;
    }

    public float getPercentile(){
        return maxHeap.peek();
    }

    public void addAmount(float amount) throws ArithmeticException{
        size++;
        int index = getPercentileIndex();
        if(!maxHeap.isEmpty() && amount < maxHeap.peek()){
            maxHeap.add(amount);
            if(maxHeap.size() > index + 1)
                minHeap.add(maxHeap.poll());
        }else{
            minHeap.add(amount);
            if(minHeap.size() >= size - index)
                maxHeap.add(minHeap.poll());
        }

        total += amount;
    }

    private int getPercentileIndex(){
        return (int)(Math.ceil(percentile * size)/100)-1;
    }

}
