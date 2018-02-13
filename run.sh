#!/bin/bash
#
# Use this shell script to compile (if necessary) your code and then execute it. Below is an example of what might be found in this file if your program was written in Python

javac ./src/repeatdonorreporter/*.java
java -cp "./src" repeatdonorreporter.RepeatDonorDriver ./input/itcont.txt ./input/percentile.txt ./output/repeat_donor.txt

