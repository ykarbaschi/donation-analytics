package repeatdonorreporter;

import java.util.*;

public class RepeatDonorReporter {
    Map<String, Integer> minYearDonated; // key would be name+zipcode
    Map<String, RepeatDonorsGroup> repeatedDonorsGroups; // key would be receiptID+ZipCode+Year
    PercentileCalculator percentileCalculator;
    float desiredPercentile;

    public RepeatDonorReporter(float thePercentile){
        minYearDonated = new HashMap<>();
        repeatedDonorsGroups = new HashMap<>();
        percentileCalculator = new NearestRankPercentile();
        desiredPercentile = thePercentile;
    }

    public boolean addNewDonation(Record theRecord) throws ArithmeticException{
        // can be improved, casting is smelling to me~~
        DonationRecord theDonationRecord = (DonationRecord)theRecord;

        String nameAndZip = theDonationRecord.getName() + theDonationRecord.getZipcode();

        // new donors is repeated and new record is after his prior donation
        if(minYearDonated.containsKey(nameAndZip) &&
                minYearDonated.get(nameAndZip) < theDonationRecord.getTransactionYear()){

            //make a key for repeated Donor Map
            String receiptZipYear = theDonationRecord.getReceiptID() +
                    theDonationRecord.getZipcode() +
                    String.valueOf(theDonationRecord.getTransactionYear());

            if(repeatedDonorsGroups.containsKey(receiptZipYear)){

                repeatedDonorsGroups.get(receiptZipYear).addOrPut(theDonationRecord.getTransactionAmount());
            }else{

                RepeatDonorsGroup newGroup = new RepeatDonorsGroup();
                newGroup.addOrPut(theDonationRecord.getTransactionAmount());
                repeatedDonorsGroups.put(receiptZipYear, newGroup);
            }

            return true;

        }else{
            // put if not exist before or update with least year so far
            minYearDonated.put(nameAndZip, theDonationRecord.getTransactionYear());
            return false;
        }
    }

    public String emitRepeatedDonorReport(Record theRecord) {

        DonationRecord donationRecord = (DonationRecord)theRecord;
        String receiptZipYear = donationRecord.getReceiptID() +
                donationRecord.getZipcode() +
                String.valueOf(donationRecord.getTransactionYear());

        RepeatDonorsGroup repeatedList = repeatedDonorsGroups.get(receiptZipYear);

        int indexForSorted = percentileCalculator.calcPercentile(desiredPercentile, repeatedList.amounts.size());
        long thePercentile = generateRoundedAmount(repeatedList.amounts.get(indexForSorted-1));

        return donationRecord.getReceiptID() + "|" +
                donationRecord.getZipcode() + "|" +
                donationRecord.getTransactionYear() + "|" +
                thePercentile + "|" +
                generateRoundedAmount(repeatedList.total) + "|" +
                repeatedList.amounts.size() + "\n";
    }

    public long generateRoundedAmount(Float amount){

        return (long)(amount / 1 + (amount%1 > 0.5f ? 1:0));
    }
}
