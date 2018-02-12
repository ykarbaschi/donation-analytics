package repeatdonorreporter;

public abstract class Record {
    private boolean valid;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }
}
