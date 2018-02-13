package repeatdonorreporter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RepeatDonorReporterTest {
    RepeatDonorReporter repeatDonorReporter;

    @Before
    public void setup(){repeatDonorReporter = new RepeatDonorReporter(30, 0.01f);}

    @Test
    public void checkIfLaterDonationUpdateYearCorrectly(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", 2017, 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", 2018, 50);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);

        assertEquals(2018, (int)repeatDonorReporter.maxYearDonated.get("Kar,Yas12345"));
    }

    @Test
    public void checkIfLAfterDonationDoesNotUpdate(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", 2017, 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", 2017, 50);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);
        String key = donationRecord.getReceiptID() +
                donationRecord.getZipcode() +
                String.valueOf(donationRecord.getTransactionYear());

        assertEquals(false, repeatDonorReporter.repeatedDonorsGroups.containsKey(key));
    }

    @Test
    public void checkTheNumberOfRepeatedDonorWithSameReceiptID(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", 2017, 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", 2018, 50);
        DonationRecord donationRecord3 = new DonationRecord("C", "12345", "Kari,Yas", 2017, 200);
        DonationRecord donationRecord4 = new DonationRecord("C", "12345", "Kari,Yas", 2018, 150);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);
        repeatDonorReporter.addNewDonation(donationRecord3);
        repeatDonorReporter.addNewDonation(donationRecord4);

        String key = donationRecord4.getReceiptID() +
                donationRecord4.getZipcode() +
                String.valueOf(donationRecord4.getTransactionYear());

        assertEquals(true, repeatDonorReporter.repeatedDonorsGroups.containsKey(key));
        assertEquals(2, repeatDonorReporter.repeatedDonorsGroups.get(key).amounts.size());
    }

    @Test
    public void DonorwithDiffZipcodeSameReceiptID(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", 2017, 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12349", "Kar,Yas", 2018, 50);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);

        String key = donationRecord2.getReceiptID() +
                donationRecord2.getZipcode() +
                String.valueOf(donationRecord2.getTransactionYear());

        assertEquals(false, repeatDonorReporter.repeatedDonorsGroups.containsKey(key));
    }

    @Test
    public void DonorWithSameZipcodeDiffReceipt(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", 2017, 100);
        DonationRecord donationRecord2 = new DonationRecord("B", "12345", "Kar,Yas", 2018, 50);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);

        String key = donationRecord2.getReceiptID() +
                donationRecord2.getZipcode() +
                String.valueOf(donationRecord2.getTransactionYear());

        assertEquals(true, repeatDonorReporter.repeatedDonorsGroups.containsKey(key));
        assertEquals(50, repeatDonorReporter.repeatedDonorsGroups.get(key).total, 0.009);
    }

    @Test
    public void checkTotalAmountRepeatDonors(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", 2017, 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", 2018, 50);
        DonationRecord donationRecord3 = new DonationRecord("C", "12345", "Kari,Yas", 2017, 200);
        DonationRecord donationRecord4 = new DonationRecord("C", "12345", "Kari,Yas", 2018, 150);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);
        repeatDonorReporter.addNewDonation(donationRecord3);
        repeatDonorReporter.addNewDonation(donationRecord4);

        String key = donationRecord2.getReceiptID() +
                donationRecord2.getZipcode() +
                String.valueOf(donationRecord2.getTransactionYear());

        float total = repeatDonorReporter.repeatedDonorsGroups.get(key).total;

        assertEquals(200, total, 0.009f);
    }

    @Test
    public void getExceptionOverflowWhenCalculatingTotal() throws ArithmeticException{
        RepeatDonorReporter mockRepeatDonorReporter = mock(RepeatDonorReporter.class);
        when(mockRepeatDonorReporter.addNewDonation(any())).thenThrow(new ArithmeticException());
    }
}
