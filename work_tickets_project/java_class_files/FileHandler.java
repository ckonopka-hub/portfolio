import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/*
 *  Handles all file operations.
 */
public class FileHandler {
	private File logFile = new File("log.txt");

	/*
	 *  Creates Work Order file and loads data from Work Order
	 *  ArrayList to it.
	 *  @param workOrderFileName The name of the file being created.
	 */
	public void writeData(String workOrderFileName)
	{
		try
		{
			File file = new File(workOrderFileName);
			FileWriter writer = new FileWriter(file);
			PrintWriter pw = new PrintWriter(writer);
			
			logger("Writing Work Order Data to File");
			pw.println("customer_id,customer_first_name,customer_last_name,ticket_id,ticket_createdAt,workorder_createdAt,employee_id,employee_first_name,employee_last_name,clocked_in,certification");
			
			for(int i = 0; i < Project3.workOrderList.size();i++)
			{
				pw.println(Project3.workOrderList.get(i).getFileData());
				logger(Project3.workOrderList.get(i).getFileData());
			}
			pw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		logger("Work Orders created. Program Exiting");

	} // end writeData method
	
	/*
	 *  Loads employee data from file and creates necessary
	 *  objects from data.
	 *  @param employeeFileName The name of the employee file being read.
	 */
	public void readEmployeeData(String employeeFileName)
	{
		logger("Loading Employee Data");
		FileReader fr;
		
		try 
		{
			fr = new FileReader(employeeFileName);
			Scanner in = new Scanner(fr);
			
			String header = in.nextLine();
			
			while(in.hasNextLine())
			{
				String line = in.nextLine();
				
				String[] data = line.split(",");
				// could create comments for what each index is
				
				if(data[8].equals("tier1"))
				{
					// creating employee objects
					Employee e = new Employee(data[0],data[1],data[2],
						data[3],data[4],data[5],data[6],data[7]);
					Project3.employeeList.add(e);
				}
				else
				{
					// creating Tier 2 employee objects
					Tier2Employee e = new Tier2Employee(data[0],data[1],
							data[2],data[3],data[4],data[5],data[6],data[7],
							data[9]);
					Project3.employeeList.add(e);
				}
			}
			in.close();
			
		} // end try
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	} // end readEmployeeData method
	
	/*
	 *  Loads ticket data from file and creates Ticket objects
	 *  from data.
	 *  @param ticketFileName The name of the ticket file being read.
	 */
	public LinkedList<Ticket> readTicketData(String ticketFileName)
	{
		logger("Loading Ticket Data");
		FileReader fr;
		
		try 
		{
			fr = new FileReader(ticketFileName);
			Scanner in = new Scanner(fr);
			
			String header = in.nextLine();
			
			while(in.hasNextLine())
			{
				String line = in.nextLine();
				String[] data = line.split(",");
				
				if(ticketFileName.equals("tier1_ticket_data.csv"))
				{
					Customer c = new Customer(data[1],data[2],data[4],data[5],data[3],data[0],data[6]);
					Ticket t = new Ticket(c,data[8],data[7]);
					Project3.tier1TicketFile.add(t);
				}
				else if(ticketFileName.equals("tier2_ticket_data.csv"))
				{
					Customer c = new Customer(data[1],data[2],data[4],data[5],data[3],data[0],data[6]);
					Ticket t = new Ticket(c,data[8],data[7]);
					Project3.tier2TicketFile.add(t);
				}
			}
			in.close();
			
		} // end try 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	} // end readTicketData method
	
	/*
	 *  Records all operations in log file.
	 *  @param log The operation to be logged.
	 */
	private void logger(String log)
	{
		
		try
		{
			// creating Date Object
			Date date = new Date();
			SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			String strDate = formatter1.format(date);
			
			// creating file-writing objects
			PrintWriter pw = new PrintWriter(new FileWriter(logFile, true), true);
			pw.println(strDate + " : " + log);
			pw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	} // end log method
}
