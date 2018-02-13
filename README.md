This is an assignment for insight data engineering program. I've tried to use Test Driven Development for most components. Also I decreased dependency by using abstract classes and interfaces to have less rigid design for future changes.

## Dependencies
* Java 8
* JUnit for Unit testing
* Mockito (I've used it barely, but for running unit tests, it is required)

## How to Run
After cloning the repository, you might need change the permission of bash files(run.sh and run_test.sh) by this command:
```shell
chmod 700 fileName
```

I've edited run.sh file and it has all required command to compile and run the program with needed arguments:
```
javac ./src/repeatdonorreporter/*.java
java -cp "./src" repeatdonorreporter.RepeatDonorDriver ./input/itcont.txt ./input/percentile.txt ./output/repeat_donors.txt
```
## Data Structure
I have used a Hashmap with key as name + zipcode and value as smallest year seen so far to detect repeated donors. Then I used another hashmap with key as receiptId + zipCode + Year and value a list of amounts.

To calculate running percentile, I have the idea of using a minHeap and maxHeap given from famous problem of median of a stream. The next percentile always would be at the root of minHeap. The runtime woulde be O(2*logn) for insertion and O(1) for getting the percentile. However, I couldn't get a chance to implement it carefully. Instead I implmented a sorted list by adding new elements in correct position using binary search. It has O(logn + n) insertion time and O(1) to get percentile.

## Software Design
I had a minimalistic view to only store what is really needed for this special report. I rigorously validate incoming data to make sure for better performance and security. I followed exactly the input standards from FEC table. For example, I check length of incoming input(as a  char[] not more than 577 and filter any invalid field using different accurate regex strings. However, if any new report needed differnt validator can be written and plugged in whitout touching the existing code (Open Closed Principle).

(main class) ```RepeatDonorDriver``` needs (interface)```InputValidator``` and ```RepeatDonorReporter``` and (interface)```ReportStreamer```.
```RepeatDonationInputValidator``` implmented ```inputValidator``` with details from assignment defenition and FEC tables which validates incoming data and extract needed field with proper formatting.
```RpeatDonorReporter``` is the class does all the calcualtion for this assignment.
```FileReportStreamer``` implemented ```ReportStreamer``` which write the result for each line if needed.
```NearestRankPercentile``` implemented ```PercentileCalculator```.
```Record``` is an abstract class and ```DonationRecord``` extended that for this assignment.


