package repeatdonorreporter;

import java.io.*;

public class RepeatDonorDriver {
    public static void main(String[] args){
        /*String currentDirectory = new File("").getAbsolutePath();
        String outputFilePath = currentDirectory + "/output/repeat_donor.txt";*/
        int percentile = 15;
        String percentileString;
        String comingLine;

        try{
            //BufferedReader br = new BufferedReader(new FileReader(currentDirectory + "/input/percentile.txt"));
            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            if((percentileString = br.readLine())!= null)
                percentile = Integer.valueOf(percentileString);

        } catch (Exception exception){
            System.out.println("Percentile input file cannot be found");
        }

        InputValidator repeatDonationInputValidator = new RepeatDonationInputValidator();
        RepeatDonorReporter repeatDonorReporter = new RepeatDonorReporter(percentile, RepeatDonationInputValidator.PRECISION);
        //ReportStreamer fileReportStreamer = new FileReportStreamer(outputFilePath);
        ReportStreamer fileReportStreamer = new FileReportStreamer(args[2]);

        try {
            //BufferedReader br = new BufferedReader(new FileReader(currentDirectory + "/input/itcont.txt"));
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

/*
        InputValidator repeatDonationInputValidator = new RepeatDonationInputValidator();

        RepeatDonorReporter repeatDonorReporter = new RepeatDonorReporter(percentile, RepeatDonationInputValidator.PRECISION);

        // All higher abstraction which can be replaced for different needs. Just need implement new requirements

        ReportStreamer fileReportStreamer = new FileReportStreamer(outputFilePath);

        while(allInputs.size()!=0){
            char[] input = allInputs.remove(0).toCharArray();
            Record donationRecord = repeatDonationInputValidator.isValidInput(input);
            if(donationRecord.isValid()){
                if(repeatDonorReporter.addNewDonation(donationRecord)) {
                    String repeatedDonorReport = repeatDonorReporter.emitRepeatedDonorReport(donationRecord);
                    fileReportStreamer.emitReport(repeatedDonorReport);
                }
            }
        }

        fileReportStreamer.stopEmitting();
*/

    }
}
