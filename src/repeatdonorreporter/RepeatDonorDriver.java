package repeatdonorreporter;

import java.io.*;
import java.util.ArrayList;

public class RepeatDonorDriver {
    public static void main(String[] args){
        String currentDirectory = new File("").getAbsolutePath();
        String outputFilePath = currentDirectory + "/output/repeat_donor.txt";
        int percentile = 30;
        String percentileString;
        String line;
        ArrayList<String> allInputs = new ArrayList<>();

//        try{
//            BufferedReader br = new BufferedReader(new FileReader(currentDirectory + "/input/percentile.txt"));
//            if((percentileString = br.readLine())!= null)
//                percentile = Integer.valueOf(percentileString);
//        }
//        catch (Exception exception){
//
//        }

        try {

            BufferedReader br = new BufferedReader(new FileReader(currentDirectory + "/input/itcont.txt"));

            while((line = br.readLine()) != null){
                allInputs.add(line);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("input text file NotFound");
        } catch (IOException ex) {
            System.out.println("error reading file");
        } catch (Exception exception) {
            System.out.println("Other exception");
        }

        RepeatDonorReporter repeatDonorReporter = new RepeatDonorReporter(percentile);

        // All higher abstraction which can be replaced for different needs. Just need implement new requirements
        InputValidator repeatDonationInputValidator = new RepeatDonationInputValidator();
        ReportStreamer fileReportStreamer = new FileReportStreamer(outputFilePath);

        while(allInputs.size()!=0){
            //********** Should be char[] for large inputs later can be detected if invalid
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

    }
}
