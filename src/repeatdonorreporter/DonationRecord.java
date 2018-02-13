package repeatdonorreporter;

public class DonationRecord extends Record{
    private String receiptID,
            zipcode,
            name;
    private int transactionYear;
    private float transactionAmount;

    private boolean valid;

    public DonationRecord(){}
    public DonationRecord(String theReceiptId, String theZipcode, String theName,
                          int theYear, float theAmount){
        receiptID = theReceiptId;
        zipcode = theZipcode;
        name = theName;
        transactionYear = theYear;
        transactionAmount = theAmount;
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getReceiptID(){ return receiptID;}

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public String getZipcode(){return zipcode;}

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getName(){return name;}

    public void setName(String name) {
        this.name = name;
    }

    public int getTransactionYear(){return transactionYear;}

    public void setTransactionYear(int transactionYear) {
        this.transactionYear = transactionYear;
    }

    public float getTransactionAmount(){return transactionAmount;}

    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
