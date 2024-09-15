/*
 *  Holds employee information as well as certification information.
 */
public class Tier2Employee extends Employee
{

	private String certification;
	
	public Tier2Employee(String firstName,String lastName, String address,
			String phoneNumber, String email, String employeeId,
			String clockedIn, String hiredDate, String certification)
	{
		super(firstName, lastName, address, phoneNumber, email,
			  employeeId, clockedIn, hiredDate);
		this.certification = certification;
	}
	
	@Override
	public String getFileData()
	{
		String data = super.getFileData() + ",E-" + certification;
		return data;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}
}
