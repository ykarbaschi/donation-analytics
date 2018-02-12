package repeatdonorreporter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RepeatDonorReporterTest {
    RepeatDonorReporter repeatDonorReporter;

    @Before
    public void setup(){repeatDonorReporter = new RepeatDonorReporter(30);}

    @Test
    public void checkIfLaterDonationUpdateAmountAndDateCorrectly(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", "01012017", 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", "01312017", 50);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);

        assertEquals(50, repeatDonorReporter.donations.get("Kar,Yas12345").getTransactionAmount(), 0.009f);
        assertEquals("01312017", repeatDonorReporter.donations.get("Kar,Yas12345").getTransactionDate());
    }

    @Test
    public void checkIfLAfterDonationDoesNotUpdate(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", "01312017", 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", "01302017", 50);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);

        assertEquals(100, repeatDonorReporter.donations.get("Kar,Yas12345").getTransactionAmount(), 0.009f);
        assertEquals("01312017", repeatDonorReporter.donations.get("Kar,Yas12345").getTransactionDate());
    }

    @Test
    public void checkTheNumberOfRepeatedDonor(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", "01312017", 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", "02302017", 50);
        DonationRecord donationRecord3 = new DonationRecord("C", "12345", "Kari,Yas", "01312017", 200);
        DonationRecord donationRecord4 = new DonationRecord("C", "12345", "Kari,Yas", "05302017", 150);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);
        repeatDonorReporter.addNewDonation(donationRecord3);
        repeatDonorReporter.addNewDonation(donationRecord4);

        assertEquals(2, repeatDonorReporter.getRepeatedDonor(donationRecord4).size());
    }

    @Test
    public void checkTotalAmountRepeatDonors(){
        DonationRecord donationRecord = new DonationRecord("C", "12345", "Kar,Yas", "01312017", 100);
        DonationRecord donationRecord2 = new DonationRecord("C", "12345", "Kar,Yas", "02302017", 50);
        DonationRecord donationRecord3 = new DonationRecord("C", "12345", "Kari,Yas", "01312017", 200);
        DonationRecord donationRecord4 = new DonationRecord("C", "12345", "Kari,Yas", "05302017", 150);
        repeatDonorReporter.addNewDonation(donationRecord);
        repeatDonorReporter.addNewDonation(donationRecord2);
        repeatDonorReporter.addNewDonation(donationRecord3);
        repeatDonorReporter.addNewDonation(donationRecord4);

        double total = repeatDonorReporter.getTotalAmount(repeatDonorReporter.getRepeatedDonor(donationRecord4));

        assertEquals(200, total, 0.009f);
    }

    @Test
    public void checkNumber14digitWith2PrecisionHaveCorrectOutPutString() {
        Double number = 384222222222.22;
        assertEquals("384222222222.22", repeatDonorReporter.generateCorrectAmount(number));
    }

    @Test
    public void checkNumberSum1and101HaveCorrectOutPutString(){
        Double number = 384222222222.22;
        assertEquals("384222222222.22", repeatDonorReporter.generateCorrectAmount(number));
    }
}
