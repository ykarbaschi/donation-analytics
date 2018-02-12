package repeatdonorreporter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RepeatDonationInputValidatorTest {
    RepeatDonationInputValidator inputValidator;
    @Before
    public void setup(){inputValidator = new RepeatDonationInputValidator();}

    @Test
    public void returnTrueForValidCMTE_IdField(){
        char[] arr = new String("C00629618|||||||PEREZ, JOHN A|").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidCMTE_IDField(fields[inputValidator.CMTE_ID_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals("C00629618", record.getReceiptID());
    }

    @Test
    public void returnFalseForEmptyCMTe_IDField(){
        char[] arr = new String("|C00629618|N").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidCMTE_IDField(fields[inputValidator.CMTE_ID_INDEX], record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void returnTrueForValidNameField(){
        char[] arr = new String("|||||||PEREZ, JOHN A|").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidNameField(fields[inputValidator.NAME_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals("PEREZ, JOHN A", record.getName());
    }

    @Test
    public void returnFalseForEmptyFieldName(){
        char[] arr = new String("||||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidNameField(fields[inputValidator.NAME_INDEX], record);
        assertEquals(false, record.isValid());
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
        assertEquals("01032017", record.getTransactionDate());
    }

    @Test
    public void invalidMonthDate(){
        char[] arr = new String("|N|TER|P||||||||||21032017|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
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
        char[] arr = new String("|N|TER|P|||||||7701234560|||21032017|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidZipCode(fields[inputValidator.ZIP_INDEX], record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void invalidZipCodeExactlyFive(){
        char[] arr = new String("|N|TER|P|||||||77012|||21032017|40||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidZipCode(fields[inputValidator.ZIP_INDEX], record);
        assertEquals("77012", record.getZipcode());
    }

    @Test
    public void validAmount350(){
        char[] arr = new String("|N|TER|P|||||||77012|||21032017|350||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals(350f, record.getTransactionAmount(),0.009f);
    }

    @Test
    public void validAmount35099(){
        char[] arr = new String("|N|TER|P|||||||77012|||21032017|350.99||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals(350.99f, record.getTransactionAmount(),0.009f);
    }

    @Test
    public void invalidAmount350991(){
        char[] arr = new String("|N|TER|P|||||||77012|||21032017|350.991||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record);
        assertEquals(false, record.isValid());
    }

    @Test
    public void validAmountUnderOneDollar099(){
        char[] arr = new String("|N|TER|P|||||||77012|||21032017|0.99||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record);
        assertEquals(true, record.isValid());
        assertEquals(0.99f, record.getTransactionAmount(),0.009f);
    }

    @Test
    public void invalidAmountMoreThanPrec(){
        char[] arr = new String("|N|TER|P|||||||77012|||21032017|111400000000000||||||").toCharArray();
        String[] fields = inputValidator.splitFields(arr);
        DonationRecord record = new DonationRecord();
        inputValidator.isValidAmount(fields[inputValidator.AMT_INDEX], record);
        assertEquals(false, record.isValid());
    }

}

