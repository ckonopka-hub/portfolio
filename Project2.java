/*
 * Author:		Carson Konopka
 * Course:		COP3503
 * Project #: 	2
 * Title:		Working with Speed Data
 * Due Date: 	10/29/2023
 * 
 * Changes date format and adds new data columns to a created file.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Changes date format and adds data columns to created file.
 */
public class Project2 {
	
	// Array List declarations
	public static ArrayList<String> dates = new ArrayList<String>();
	public static ArrayList<String> times = new ArrayList<String>();
	public static ArrayList<Double> sensor2278 = new ArrayList<Double>();
	public static ArrayList<Double> sensor3276 = new ArrayList<Double>();
	public static ArrayList<Double> sensor4689 = new ArrayList<Double>();
	public static ArrayList<Double> sensor5032 = new ArrayList<Double>();
	public static ArrayList<Double> section1Diff = new ArrayList<Double>();
	public static ArrayList<Double> section2Diff = new ArrayList<Double>();
	public static ArrayList<Double> totalAvg = new ArrayList<Double>();


	public static void main(String[] args) //throws FileNotFoundException
	{
		
		System.out.println("Project 2 Data Preprocessing\n");
		Scanner console = new Scanner(System.in);
		boolean done = false;
		
		
		
		while(!done)
		{
			// clearing possible bad data
			dates.clear();
			times.clear();
			sensor2278.clear();
			sensor3276.clear();
			sensor4689.clear();
			sensor5032.clear();
			section1Diff.clear();
			section2Diff.clear();
			totalAvg.clear();
			
			System.out.println("Input file name & location.");
			String inputFileName = console.next();
			String outputFileName = "Speed_Data_Difference.csv";
			
			// make file object and scanner
			File inputFile = new File(inputFileName);
				
			try(Scanner in = new Scanner(inputFile);FileWriter out1 = new FileWriter(outputFileName); PrintWriter out = new PrintWriter(out1);)
			{
				String fieldNames = in.nextLine();
				fieldNames.trim();
				fieldNames = fieldNames + ",Section1_Diff,Section2_Diff,Total_Avg\n";
			
				// read in information from file
				System.out.println("Reading in Data from the file " + inputFileName);
				while (in.hasNextLine())
				{
					String line = in.nextLine();
					String[] fields = line.split(",");
					
					// put values into respective ArrayLists
					dates.add(fields[0]);
					times.add(fields[1]);
					sensor2278.add(Double.parseDouble(fields[2]));
					sensor3276.add(Double.parseDouble(fields[3]));
					sensor4689.add(Double.parseDouble(fields[4]));
					sensor5032.add(Double.parseDouble(fields[5]));
				}
				
				// change format of dates
				System.out.println("Converting Dates from MM/DD/YYYY to YYYY/MM/DD");
				Date date = new Date();
				SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
				for(int i = 0; i < dates.size(); i++)
				{
					date = formatter1.parse(dates.get(i));
					String str = formatter2.format(date);
					dates.set(i, str);
				}
				
				// Speed calculations
				System.out.println("Calculating Speed Difference");
				System.out.println("Calculating Speed Average");
				
				for(int i = 0; i < sensor2278.size(); i++)
				{
					section1Diff.add(sensor3276.get(i) - sensor2278.get(i));
					section2Diff.add(sensor5032.get(i) - sensor4689.get(i));
					totalAvg.add((sensor2278.get(i) + sensor3276.get(i) + sensor4689.get(i) + sensor5032.get(i)) / 4.0);
				}
				
				// write to output file
				System.out.println("Writing data to file " + outputFileName);
		
				for(int i = 0; i < dates.size(); i++)
				{
					
					out.println(dates.get(i) + "," + times.get(i) + "," + sensor2278.get(i) + ","
					+ sensor3276.get(i) + "," + sensor4689.get(i) + "," + sensor5032.get(i) + "," 
					+ section1Diff.get(i) + "," + section2Diff.get(i) + "," + totalAvg.get(i));
					
				}
				
				done = true;
			}
			
			// exception handling
			catch(FileNotFoundException fnf)
			{
				System.out.println("*File does not exist or path was entered incorrectly*");
				System.out.println("Please try again.");
			}
			catch(NumberFormatException nf)
			{
				System.out.println("*Bad number Data in CSV File.*");
				System.out.println("Check CSV file data and try again.");
			}
			catch(ParseException p)
			{
				System.out.println("*Bad Date Data in CSV File.*");
				System.out.println("Check CSV file data and try again.");
			}
			catch(IOException io)
			{
				System.out.println("New File could not be created");
			}
		} // end while loop
		System.out.println("Done! Exiting Program");
	} // end main
} // end class
