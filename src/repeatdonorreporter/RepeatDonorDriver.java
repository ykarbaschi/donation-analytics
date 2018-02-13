package repeatdonorreporter;

import java.io.*;

public class RepeatDonorDriver {
    public static void main(String[] args){
        float percentile = 0;
        String percentileString;
        String comingLine;

        try{
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            if((percentileString = br.readLine())!= null)
                percentile = Float.valueOf(percentileString);

        } catch (Exception exception){
            System.out.println("Percentile input file cannot be found");
        }

        InputValidator repeatDonationInputValidator = new RepeatDonationInputValidator();
        RepeatDonorReporter repeatDonorReporter = new RepeatDonorReporter(percentile);
        ReportStreamer fileReportStreamer = new FileReportStreamer(args[2]);

        try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));

            while((comingLine = br.readLine()) != null){
                Record donationRecord = repeatDonationInputValidator.isValidInput(comingLine.toCharArray());
                if(donationRecord.isValid()){
                    if(repeatDonorReporter.addNewDonation(donationRecord)) {
                        String repeatedDonorReport = repeatDonorReporter.emitRepeatedDonorReport(donationRecord);
                        fileReportStreamer.emitReport(repeatedDonorReport);

                        fileReportStreamer.flushBuffer();
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("input text file NotFound");
        } catch (IOException ex) {
            System.out.println("error reading file");
        } catch (Exception exception) {
            System.out.println("Other exception");
        }

        fileReportStreamer.stopEmitting();
    }
}
