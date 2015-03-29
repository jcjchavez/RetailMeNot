
## Synopsis

This directory contains my solution to the mindsumo contest posted by RetailMeNot, Inc. at https://www.mindsumo.com/contests/analyze-clicks-and-bounce-rates-from-online-couponers. 

Part 1: "Calculate the minimum, maximum, mean, median and standard deviation of clicks out per minute." My solution for this part of the challenge is contained in the file ClicksPerMinute.py for python3 and does not make use of 3rd party libraries. This program takes two command line arguments, 1. RetailMeNot HTTP ACCESS Log file for one hour, 2. String for the output .csv file. The output file contains data for the fields: 'couponId', 'Hour', 'Minimum Clicks/Minute', 'Minimum Clicks Minute', 'Maximum C/Min', 'Maximum Clicks Minute', 'Mean C/min', 'Median C/min ', and 'Sample Standard Deviation'. I approached this task by first sifting through the file with a regular expression search to extract the appropriate lines from the log then using basic data structures to calculate the statistics for clicks per minute per couponId. 

Part 2: "Calculate the Bounce Rate for each store page." My solution for this part of the challenge is contained in the directory BounceRates. It was written in java and does not make use of 3rd part libraries. This program takes two command line arguments, 1. RetailMeNot HTTP ACCESS Log file for one hour, 2. String for the output .csv file. The output file contains the bounce rate for every page in the log. I approached this task by finding all the requests for storepages using a regular expression search then used basic data structures to calculate each page's bounce rate

