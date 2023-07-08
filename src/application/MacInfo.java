package application;

public class MacInfo {

	private String name;
	private String type;
	private String date;
	
	// Vendor Name,Private,Block Type,Last Update
	public MacInfo(String name, String type, String date) {
		this.name = name;
		this.type = type;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
