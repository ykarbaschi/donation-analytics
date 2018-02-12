package repeatdonorreporter;

public class DonationRecord extends Record{
    private String receiptID,
            zipcode,
            name,
            transactionDate;
    private double transactionAmount;
    private boolean repeated;

    private boolean valid;

    public DonationRecord(){}
    public DonationRecord(String theReceiptId, String theZipcode, String theName,
                          String theYear, int theAmount){
        receiptID = theReceiptId;
        zipcode = theZipcode;
        name = theName;
        transactionDate = theYear;
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

    public String getTransactionDate(){return transactionDate;}

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getTransactionAmount(){return transactionAmount;}

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }
}
