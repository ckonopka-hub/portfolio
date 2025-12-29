import java.io.FileWriter;
import java.io.PrintWriter;

public class FileHandler 
{
	private String surveyFile;
	private FileWriter fileOutput;
	private PrintWriter printWriter;
	
	/*
	 * Creates csv file "survey_results_csv" and creates header for csv file
	 */
	public FileHandler()
	{
		try
		{
		surveyFile = "survey_results.csv";
		fileOutput = new FileWriter(surveyFile);
		printWriter = new PrintWriter(fileOutput);
		printWriter.println("DateTime,FirstName,LastName,PhoneNumber,Email,Sex,Water,Meals,Wheat,Sugar,Dairy,Miles,Weight");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/*
	 * Appends survey data string to survey_results.csv
	 */
	public void writeResults(String surveyData)
	{
		printWriter.println(surveyData);
	}
}
