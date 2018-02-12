package repeatdonorreporter;

import java.math.BigInteger;
import java.util.*;

public class RepeatDonorReporter {
    Map<String, DonationRecord> donations;
    Map<Integer, Map<String, Integer>> yearlyRepeatedDonations;
    PercentileCalculator percentileCalculator;
    int theDesiredPercentile;

    public RepeatDonorReporter(int percentile){
        donations = new HashMap<>();
        yearlyRepeatedDonations = new HashMap<>();
        percentileCalculator = new NearestRankPercentile();
        theDesiredPercentile = percentile;
    }

    public boolean addNewDonation(Record theRecord){
        // can be improved, casting is smelling to me~~
        DonationRecord theDonationRecord = (DonationRecord)theRecord;

        String nameAndZip = theDonationRecord.getName() + theDonationRecord.getZipcode();
        if(donations.containsKey(nameAndZip)){
            DonationRecord record = donations.get(nameAndZip);
            if(Integer.valueOf(theDonationRecord.getTransactionDate()) >=
                     Integer.valueOf(record.getTransactionDate())){
                record.setTransactionDate(theDonationRecord.getTransactionDate());
                record.setTransactionAmount(theDonationRecord.getTransactionAmount());
                record.setRepeated(true);
                return true;
            }else
                return false;

        }else{
            donations.put(nameAndZip, theDonationRecord);
            return false;
        }
    }

    public List<DonationRecord> getRepeatedDonor(DonationRecord theRecord) {
        //in repeatedDonor fin all receipt in this year. then emit number and total and percentile.
        List<DonationRecord> res = new ArrayList<>();
        //can be replaced with stream call
        for(DonationRecord record:donations.values()){
            if(extractYear(record.getTransactionDate()) == extractYear(theRecord.getTransactionDate()) &&
                record.getZipcode().equals(theRecord.getZipcode()) &&
                record.isRepeated())

                res.add(record);
        }

        return res;
    }

    private int extractYear(String date){
        return Integer.valueOf(date.substring(date.length()-4));
    }

    public Double getPercentileOfRepeatedDonor(int year, List<DonationRecord> repeatedDonors){

        //****** Room for improvement, faster sorting map entries

        int indexForSorted = percentileCalculator.getPercentile(theDesiredPercentile, repeatedDonors.size());
        List<Double> list = new ArrayList<>();
        for(DonationRecord record:repeatedDonors)
            list.add(record.getTransactionAmount());

        Collections.sort(list);
        return list.get(indexForSorted-1);
    }

    public double getTotalAmount(List<DonationRecord> repeatedDonor) {
        double res = 0;
        for(DonationRecord record:repeatedDonor)
            res += record.getTransactionAmount();

        return res;
    }

    public String emitRepeatedDonorReport(Record theRecord) {
        //******* DEFENITLY SHOULD BE IMPROVED

        DonationRecord donationRecord = (DonationRecord)theRecord;
        List<DonationRecord> repeated = getRepeatedDonor(donationRecord);

        return donationRecord.getReceiptID() + "|" +
                donationRecord.getZipcode() + "|" +
                extractYear(donationRecord.getTransactionDate()) + "|" +

                generateCorrectAmount(getPercentileOfRepeatedDonor(
                extractYear(donationRecord.getTransactionDate()), repeated)) + "|" +

                generateCorrectAmount(getTotalAmount(repeated)) + "|" +
                repeated.size() + "\n";
    }

    public String generateCorrectAmount(Double amount){
        double fraction = amount % 1;
        if(fraction >= 0.009d)
            return String.format("%.2f", amount);
        else
            return String.format("%.0f", amount);
    }

//    public long roundPercentile(Double amount){
//        Double num = amount % 1;
//        if(num > 0.5)
//            return Long.+1;
//
//    }
}
