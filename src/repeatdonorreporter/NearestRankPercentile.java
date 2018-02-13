package repeatdonorreporter;

public class NearestRankPercentile implements PercentileCalculator {
    @Override
    public int calcPercentile(float percentile, int listSize) {
        //for rounding up without overflow worry
        return (int)(percentile*listSize) / 100 + ((percentile*listSize) % 100 == 0 ? 0:1);
    }
}
