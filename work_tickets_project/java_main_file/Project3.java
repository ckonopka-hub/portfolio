/*
 *  Author:    Carson Konopka
 *  Course:    COP3503
 *  Project #: 3
 *  Title:     Work Order Generator
 *  Due Date:  11/27/2023
 *  
 *  Processes csv files and manipulates data to create new
 *  work order file.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Project3 {
	
	public static String employeeFileName;
	public static String tier1TicketFileName;
	public static String tier2TicketFileName;
	public static String workOrderFileName;
	public static ArrayList<Employee> employeeList = new ArrayList<Employee>();;
	public static Queue<Ticket> tier1TicketFile = new LinkedList<Ticket>();
	public static Queue<Ticket> tier2TicketFile = new LinkedList<Ticket>();
	public static ArrayList<WorkOrder> workOrderList = new ArrayList<WorkOrder>();
	
	
	
	public static void main(String[] args) {
		// create log file
		try 
		{
			PrintWriter log = new PrintWriter("log.txt");
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		FileHandler fh = new FileHandler();
		employeeFileName = "employee_data.csv";
		tier1TicketFileName = "tier1_ticket_data.csv";
		tier2TicketFileName = "tier2_ticket_data.csv";
		System.out.println("Project 3 Work Order Generator");
		
		System.out.println("Loading Employee Data");
		fh.readEmployeeData(employeeFileName);
		
		System.out.println("Loading Ticket Data");
		fh.readTicketData(tier1TicketFileName);
		fh.readTicketData(tier2TicketFileName);
		
		System.out.println("Creating Work Orders");
		createWorkOrders();
		
		System.out.println("Writing Work Order Data to File");
		fh.writeData("workorder_data.csv");
		
		System.out.println("Work Orders created. Program Exiting");
		
	} // end main method
	
	/*
	 *  Adds Work Order data to Work Order ArrayList.
	 */
	public static void createWorkOrders()
	{
		int i = 0;
		while((!tier1TicketFile.isEmpty() || !tier2TicketFile.isEmpty()) && i < employeeList.size())
		{
			
			Date date = new Date();
			SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			String strDate = formatter1.format(date);
			
			Class a = employeeList.get(i).getClass();
			
			if(a.getName().equals("Employee"))
			{
				WorkOrder w = new WorkOrder(employeeList.get(i),
						tier1TicketFile.remove(), strDate);
				workOrderList.add(w);
			}
			else
			{
				WorkOrder w = new WorkOrder(employeeList.get(i),
						tier2TicketFile.remove(), strDate);
				workOrderList.add(w);
			}
			i++;
		} //end while loop
	} // end class


} // end class
