package application;

public class ScanInfo {

	private String ipAddr;
	private String time;
	private int mask;
	private boolean up;
	
	public ScanInfo() {
		super();
		this.ipAddr = "";
		this.time = "";
	}
	
	public ScanInfo(String ipAddr, String time, boolean up) {
		super();
		this.ipAddr = ipAddr;
		this.time = time;
		this.up = up;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}
	
	
}
