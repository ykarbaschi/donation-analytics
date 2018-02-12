package repeatdonorreporter;

public interface ReportStreamer {
    void emitReport(String theString);
    void stopEmitting();
}
