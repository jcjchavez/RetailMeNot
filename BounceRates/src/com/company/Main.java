package com.company;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
* This program takes 2 arguments, a RetailMeNot HTTP ACCESS Log file for one hour and a string with the output csv file
* name, and calculates the bounce rate for a session time of 1 hour for each page in the log.
*
* @author  Joshua Chavez
* @version 1.0
* @since   2015-03-28
*/

public class Main {
    public static void main(String[] args) {
        findBounceRate(args[0], args[1]); //argument: input HTTP ACCESS Log file
    }

    private static void
        findBounceRate(String hourlyLog, String outputFile){
        File file = new File(hourlyLog);
        Map<String, ArrayList<String>> _pageToEntries = new HashMap<String, ArrayList<String>>(); //Maps RetailMeNot pages to lists on user
        Map<String, Boolean> _onlyOneVisitBoolean = new HashMap<String, Boolean>(); //Maps IP addresses to a Boolean indicating whether or not they only visited one page.

        try{
            Pattern _pattern = Pattern.compile("/view[0-9]*/[^?&%{\" ]+[?&%\" ]"); //regex search file to see only lines with page views
            Scanner _scanner = new Scanner(file);
            while (_scanner.hasNextLine()){
                String line = _scanner.nextLine();
                Matcher m = _pattern.matcher(line);
                if(m.find()) {
                    String _page = m.group();
                    if ((((_page.charAt(_page.length()-1))) == '?') || ((((_page.charAt(_page.length()-1))) == '"') || ((((_page.charAt(_page.length()-1))) == '&') || ((((_page.charAt(_page.length()-1))) == '"') || (_page.charAt(_page.length()-1)==' '))))){
                        _page = _page.substring(0, _page.length()-1); //truncate trailing [?&%" ]
                    }
                    _page = _page.split("/")[2]; //String page value ex:"xxxxxx.com", "xxxxx.it"

                    String[] fields = line.split(" ");
                    String _user = fields[0]; //user IP Address
                    if (!_pageToEntries.containsKey(_page)){
                        ArrayList<String> _entries = new ArrayList<String>(); //new list to contain all users who visited page
                        _entries.add(_user);
                        _pageToEntries.put(_page, _entries); //add list as value to dictionary
                    }else{
                        if(!_pageToEntries.get(_page).contains(_user)){
                            _pageToEntries.get(_page).add(_user); //adds each site maximum once per user
                        }
                    }
                    Boolean currVal2 = _onlyOneVisitBoolean.putIfAbsent(_user, Boolean.TRUE); //user's first appearance, set to TRUE
                    if (currVal2 != null)
                        _onlyOneVisitBoolean.replace(_user, Boolean.TRUE, Boolean.FALSE); //user's n appearance, set to FALSE
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try
        {
            FileWriter writer = new FileWriter(outputFile);
            writer.append("Page, BounceRate\n");

            for (String _page: _pageToEntries.keySet()){
                double tv = 0;//Total number of visitors viewing one page only
                double te; //Total entries to page
                ArrayList<String>_entries = _pageToEntries.get(_page);
                te = _entries.size();
                for(String _user: _entries){
                    if (_onlyOneVisitBoolean.get(_user) == Boolean.TRUE)
                        tv++;
                }
                double _bounceRate = tv/te;

                writer.append(_page);
                writer.append(',');
                writer.append(String.valueOf(tv/te));
                writer.append('\n');
                //System.out.println("TotalOnePAGE: " + tv + ",  Total for this page: " + te + " Bounce Rate: " + tv/te);
            }


            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


    }
}

