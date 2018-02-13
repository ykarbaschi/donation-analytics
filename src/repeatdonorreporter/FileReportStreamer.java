package repeatdonorreporter;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class FileReportStreamer implements ReportStreamer {
    String outputFilePath;
    BufferedWriter bufferedWriter;

    public FileReportStreamer(String absoluteFilePath){
        outputFilePath = absoluteFilePath;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(outputFilePath));
        }catch (Exception ex){
            System.out.println("The path to output file is not accessible");
        }
    }

    @Override
    public void emitReport(String theString) {
        try {
            bufferedWriter.write(theString);
        }catch (Exception ex){
            System.out.println("The output file is not accessible or storage is full");
        }
    }

    @Override
    public void flushBuffer(){
        try {
            bufferedWriter.flush();
        }catch (Exception ex){
            System.out.println("There is a problem flushing buffer writer");
        }
    }

    @Override
    public void stopEmitting(){
        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (Exception ex){
            System.out.println("There is a problem finalizing output file");
        }

    }
}
