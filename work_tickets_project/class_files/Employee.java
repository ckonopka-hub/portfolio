/*
 *  Holds employee information, such as ID, time they clocked in, 
 *  and date they were hired.
 */
public class Employee extends Person
{

	private String employeeId;
	private String clockedIn;
	private String hiredDate;
	
	public Employee(String firstName,String lastName, String address,
					String phoneNumber, String email, String employeeId,
					String clockedIn, String hiredDate)
	{
		super(firstName, lastName, address, phoneNumber, email);
		this.employeeId = employeeId;
		this.clockedIn = clockedIn;
		this.hiredDate = hiredDate;
	}
	
	@Override
	public String getFileData()
	{
		String data = "E-" + employeeId + ",E-" + getFirstName() + ",E-" + getLastName() + ",E-" + clockedIn;
		return data;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getClockedIn() {
		return clockedIn;
	}

	public void setClockedIn(String clockedIn) {
		this.clockedIn = clockedIn;
	}

	public String getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(String hiredDate) {
		this.hiredDate = hiredDate;
	}
}
