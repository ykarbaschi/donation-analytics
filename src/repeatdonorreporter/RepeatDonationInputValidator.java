package repeatdonorreporter;

import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.org.apache.bcel.internal.generic.FLOAD;

public class RepeatDonationInputValidator implements InputValidator {
    //according to the assignment explanation and this address:
    // https://classic.fec.gov/finance/disclosure/metadata/DataDictionaryContributionsbyIndividuals.shtml
    static final int CMTE_ID_SIZE = 9;
    static final int Name_SIZE = 200;
    static final int DATE_SIZE = 8;
    static final int ZipCode_SIZE = 9;
    static final int ZipCode_MIN_SIZE = 5;
    static final String AMT_PREC = "14";
    static final char AMT_SCALE = '2';
    static final float PRECISION = 0.01f;
    static final int MAX_NUM_INCOMING_CHARS = 577;// according to the FEC table

    //from here (AmazingJob): https://stackoverflow.com/questions/36465131/how-do-i-create-a-regex-that-matches-p-s-precision-and-scale-like-the-sql-deci?rq=1
    static final String AMOUNT_REGEX = "(?=(\\D*\\d\\D*){1,PP}$)^-?[0-9]+(\\.[0-9]{1,S})?$";

    static final int NUMBER_SEPARATOR = 20;
    static final String alphaNumericREGEX = "^[a-zA-Z0-9]*$";
    static final String alphaNumericREGEXWithSpace = "^[a-zA-Z0-9,'\\.\\s]*$";
    static final int LEAST_POSSIBLE_YEAR_AT_ALL = 1975;
    static final int LAST_POSSIBLE_YEAR_AMT_500 = 1988;
    static final int CUR_YEAR = 2018;
    static final int CUR_Month = 2;
    static final int CMTE_ID_INDEX = 0;
    static final int NAME_INDEX = 7;
    static final int DATE_INDEX = 13;
    static final int AMT_INDEX = 14;
    static final int OTHER_ID_INDEX = 15;
    static final int ZIP_INDEX = 10;

    @Override
    public Record isValidInput(char[] incomingInput) {
        DonationRecord newRecord = new DonationRecord();
        if(incomingInput.length > MAX_NUM_INCOMING_CHARS)
            return newRecord;

        String[] fields = splitFields(incomingInput);

        if(!isValidOtherIDField(fields[OTHER_ID_INDEX]))
            return newRecord;

        if(!isValidCMTE_IDField(fields[CMTE_ID_INDEX], newRecord))
            return newRecord;

        if(!isValidNameField(fields[NAME_INDEX], newRecord))
            return newRecord;

        if(!isValidDateField(fields[DATE_INDEX], newRecord))
            return newRecord;

        if(!isValidAmount(fields[AMT_INDEX], newRecord.getTransactionYear(), newRecord))
            return newRecord;

        if(!isValidZipCode(fields[ZIP_INDEX], newRecord))
            return newRecord;

        return newRecord;
    }

    public String[] splitFields(char[] arr){
        String[] res = new String[21];
        StringBuilder sb =new StringBuilder();

        int index = 0;
        int numSeperator = 0;
        while (index < arr.length && numSeperator <= NUMBER_SEPARATOR){
            if (arr[index]!='|') {
                sb.append(arr[index]);
                index++;
            }else{
                res[numSeperator] = sb.toString();
                sb = new StringBuilder();
                index++;
                numSeperator++;
            }
        }

        if(index == arr.length)
            res[numSeperator] = sb.toString();

        return res;
    }

    public boolean isValidCMTE_IDField(String str, DonationRecord theRecord){
        if(str.length()!= CMTE_ID_SIZE || str.indexOf('|') > 0 ||
                !str.matches(alphaNumericREGEX)) {
            theRecord.setValid(false);
        }
        else{
            theRecord.setValid(true);
            theRecord.setReceiptID(str);
        }

        return theRecord.isValid();
    }

    public boolean isValidNameField(String str, DonationRecord theRecord){
        str = str.toUpperCase();
        if(str.equals("") || !str.matches(alphaNumericREGEXWithSpace) || str.length() >= Name_SIZE){
            theRecord.setValid(false);
        }else{
            theRecord.setName(str);
            theRecord.setValid(true);
        }

        return theRecord.isValid();
    }

    public boolean isValidOtherIDField(String field) {
        return field.length()==0;
    }

    public boolean isValidDateField(String field, DonationRecord theRecord) {
        // we only care about year
        field = field.trim();
        if(field.length() != DATE_SIZE)
            theRecord.setValid(false);
        else{
            int month = Integer.valueOf(field.substring(0,2));
            int day = Integer.valueOf(field.substring(2,4));
            int year = Short.valueOf(field.substring(4));

            if(month < 0 || month > 12 || (year == CUR_YEAR && month > CUR_Month)|| day < 0 || day > 31 || year < LEAST_POSSIBLE_YEAR_AT_ALL || year > CUR_YEAR)
                theRecord.setValid(false);
            else{
                theRecord.setValid(true);
                theRecord.setTransactionYear(year);
            }
        }

        return theRecord.isValid();
    }

    public boolean isValidZipCode(String field, DonationRecord theRecord) {
        field = field.trim();
        if(field.length() > ZipCode_SIZE || field.length() < ZipCode_MIN_SIZE)
            theRecord.setValid(false);
        else{
            theRecord.setValid(true);
            theRecord.setZipcode(field.substring(0,5));
            }

        return theRecord.isValid();
    }

    public boolean isValidAmount(String amountField,int year, DonationRecord record) {
        amountField = amountField.trim();
        StringBuilder sb = new StringBuilder(AMOUNT_REGEX);

        sb.replace(sb.indexOf("PP"), sb.indexOf("PP")+2, AMT_PREC);
        sb.setCharAt(sb.indexOf("S"), AMT_SCALE);

        String regex = sb.toString();
        //check if more than allowed digits
        if(amountField.length() > Integer.valueOf(AMT_PREC)+1 || amountField.matches("") || !amountField.matches(regex))
            record.setValid(false);
        /*else if(Float.valueOf(amountField) < 200 || year >= LEAST_POSSIBLE_YEAR_AT_ALL && year <= LAST_POSSIBLE_YEAR_AMT_500 && Float.valueOf(amountField) < 500){

            record.setValid(false);
        }*/
        else{
            record.setTransactionAmount(Float.valueOf(amountField));
            record.setValid(true);
        }

        return record.isValid();
    }
}
