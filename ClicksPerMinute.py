#!/bin/usr/env python3
__author__ = 'Joshua Chavez'
__version__ = '1.0'
__since__ = '2015-03-28'
import sys
import re
import csv
import math

# This program, written with python3 takes two arguments, an hour RetailMeNot HTTP ACCESS Log file
# and a string outputFile name. The program outputs statistics for clicks per minute for every couponId in
# log in the form of a csv file

def statistics(oneHourDataLog, outputFile):
    file = open(oneHourDataLog)
    couponId_to_reqs = {};
    outrequestsonly = re.findall(r'\[.+\] "GET /out/[sS0-9]+', file.read())#regex search for GET requests
    file.close()

    for match in outrequestsonly:
        fields = match.split()
        time = fields[0][1:] #DD/Month/YYYY:HR:MN:SC
        minute = time[len(time)-5:len(time)-3] #MN (0-59)
        couponId = fields[3][5:]
        hour = time[len(time)-8:len(time)-6]
        list_of_minutes = couponId_to_reqs.get(couponId, [])
        list_of_minutes.append(int(minute))
        couponId_to_reqs[couponId] = list_of_minutes
    #fields => header for csv file
    fields = ['couponId', 'Hour', 'Minimum Clicks/Minute', 'Minimum Clicks Minute', 'Maximum C/Min',
              'Maximum Clicks Minute', 'Mean C/min', 'Median C/min ', 'Sample Standard Deviation']
    with open(outputFile, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(fields)

        for couponId in couponId_to_reqs.keys():
            outRequestMinutes = couponId_to_reqs[couponId]
            mean = len(outRequestMinutes)/60
            minimum_min = 0
            minimum = sys.maxsize
            maximum_min = 0
            maximum = 0
            minute_to_count = [0]*60 # key: [0-59] value: int number of out requests for that minute

            for minute in outRequestMinutes:
                minute_to_count[minute] += 1 #count the number of clicks per minute

            standard_deviation = 0
            for minute in range(0,60):
                count = minute_to_count[minute]
                if count > maximum: #if count is bigger than the max, count is new max
                    maximum = count 
                    maximum_min = minute
                if count < minimum: #if count is less than the min, count is the new min
                    minimum = count
                    minimum_min = minute
                standard_deviation += ((count-mean) ** 2)
            standard_deviation = math.sqrt(standard_deviation/59) #Sample Standard Deviation - divide by N-1
            minute_to_count.sort()
            median = (minute_to_count[29]+minute_to_count[30])/2
            writer.writerow([couponId, hour, minimum, minimum_min, maximum, maximum_min, mean, median,
                             standard_deviation]) # write values to csv file


def main(argv):
    statistics(argv[1], argv[2])




if __name__ == '__main__':
    sys.exit(main(sys.argv))