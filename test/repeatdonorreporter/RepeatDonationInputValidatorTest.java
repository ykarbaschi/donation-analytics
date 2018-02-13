package repeatdonorreporter;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RepeatDonationInputValidatorTest {
    RepeatDonationInputValidator inputValidator;
    @Before
    public void setup(){inputValidator = new RepeatDonationInputValidator();}

    @Test
    public void returnTrueForValidCMTE_IdField(){
        char[] arr = "C00629618|||||||PEREZ, JOHN A|".toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidCMTE_IDField(fields[inputValidator.CMTE_ID_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals("C00629618", record.getReceiptID());
    }

    @Test
    public void returnFalseForEmptyCMTe_IDField(){
        char[] arr = "|C00629618|N".toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidCMTE_IDField(fields[inputValidator.CMTE_ID_INDEX], record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void returnTrueForValidNameField(){
        char[] arr = "|||||||PEREZ, JOHN A|".toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidNameField(fields[inputValidator.NAME_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals("PEREZ, JOHN A", record.getName());
    }

    @Test
    public void returnFalseForEmptyFieldName(){
        char[] arr = "||||||||".toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidNameField(fields[inputValidator.NAME_INDEX], record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void checkTwoNameYAsERyAserIsSame(){
        char[] arr = "|||||||YAsER|".toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidNameField(fields[inputValidator.NAME_INDEX], record);
        String name = record.getName();
        arr = "|||||||yAser|".toCharArray();
        fields = inputValidator.splitFields(arr);
        inputValidator.isValidNameField(fields[inputValidator.NAME_INDEX], record);

        assertEquals(name, record.getName());
    }

    @Test
    public void returnTrueForEmptyOther_IDFiled(){
        char[] arr = new String("|N|M2|P||" +
                "15|IND|||GA||UNUM|SVP, SALES, CL||" +
                "384||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        assertEquals(true, inputValidator.isValidOtherIDField(fields[inputValidator.OTHER_ID_INDEX]));
    }

    @Test
    public void returnFalseForOther_IDFiledHasValue(){
        char[] arr = new String("|N|M2|P||" +
                "15|IND|||GA||UNUM|SVP, SALES, CL||" +
                "384|OtherFieldValue|||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        assertEquals(false, inputValidator.isValidOtherIDField(fields[inputValidator.OTHER_ID_INDEX]));
    }

    @Test
    public void checkForValidDate(){
        char[] arr = new String("|N|TER|P||||||||||01032017|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals(2017, (int)record.getTransactionYear());
    }

    @Test
    public void invalidYearBefore1975orAfter2018(){
        char[] arr = new String("|N|TER|P||||||||||21031974|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();

        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        assertEquals(false, record.isValid());

        arr = new String("|N|TER|P||||||||||21032019|40||||||").toCharArray();
        fields = inputValidator.splitFields(arr);
        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void validZipcode(){
        char[] arr = new String("|N|TER|P|||||||770123456|||21032017|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidZipCode(fields[inputValidator.ZIP_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals("77012", record.getZipcode());
    }

    @Test
    public void invalidZipCodeMoreThan9(){
        char[] arr = new String("|N|TER|P|||||||7701234560|||01032017|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidZipCode(fields[inputValidator.ZIP_INDEX], record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void invalidZipCodeExactlyFive(){
        char[] arr = new String("|N|TER|P|||||||77012|||01032017|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidZipCode(fields[inputValidator.ZIP_INDEX], record);
        assertEquals("77012", record.getZipcode());
    }

    @Test
    public void validAmount350(){
        char[] arr = new String("|N|TER|P|||||||77012|||01032017|350||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record.getTransactionYear(), record);
        assertEquals(true, record.isValid());
        assertEquals(350f, record.getTransactionAmount(),0.009f);
    }

    @Test
    public void validAmount35099(){
        char[] arr = new String("|N|TER|P|||||||77012|||01032017|350.99||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record.getTransactionYear(), record);
        assertEquals(true, record.isValid());
        assertEquals(350.99f, record.getTransactionAmount(),0.009f);
    }

    @Test
    public void invalidAmount350991(){
        char[] arr = new String("|N|TER|P|||||||77012|||01032017|350.991||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record.getTransactionYear(), record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void invalidAmountMoreThanPrec(){
        char[] arr = new String("|N|TER|P|||||||77012|||01032017|111400000000000||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record.getTransactionYear(), record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void checkForPossibleAmountOfDonation999999999999Dot99(){
        char[] arr = new String("|N|TER|P|||||||77012|||01032017|999999999999.99||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidDateField(fields[inputValidator.DATE_INDEX], record);
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record.getTransactionYear(), record);

        assertEquals(true, record.isValid());
    }

    @Test
    public void hugeInputData(){
        char[] input = new char[600];
        Arrays.fill(input, 'b');
        Record record = inputValidator.isValidInput(input);

        assertEquals(false, record.isValid());
    }
}

