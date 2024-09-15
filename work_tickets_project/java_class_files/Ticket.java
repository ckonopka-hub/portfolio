/*
 *  Holds customer objects and information about work tickets. 
 */
public class Ticket implements Printable
{

	private Customer customer;
	private String createdAt;
	private String ticketId;
	
	public Ticket(Customer customer, String createdAt, String ticketId)
	{
		this.customer = customer;
		this.createdAt = createdAt;
		this.ticketId = ticketId;
	}
	
	@Override
	public String getFileData()
	{
		String data = customer.getFileData() + "," + ticketId + ",T-" + createdAt;
		return data;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	
}
