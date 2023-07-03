package application;

import java.util.ArrayList;
import java.util.List;

public class IfAddr {

	private String name;
	private String displayName;
	private int PrefixLen;
	private byte[] hardware;
	private boolean loopback;
	private boolean up;
	private boolean pointToPoint;
	private boolean multicast;
	private boolean virtual;
	private List<String> ips = new ArrayList<String>();
	private List<String> vifs = new ArrayList<String>();
	
	public IfAddr() {
		
	}
	
	public IfAddr(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getIps() {
		return ips;
	}
	
	public void addIp(String ip) {
		ips.add(ip);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getPrefixLen() {
		return PrefixLen;
	}

	public void setPrefixLen(int PrefixLen) {
		this.PrefixLen = PrefixLen;
	}

	public byte[] getHardware() {
		return hardware;
	}

	public void setHardware(byte[] hardware) {
		this.hardware = hardware;
	}

	public boolean isLoopback() {
		return loopback;
	}

	public void setLoopback(boolean loopback) {
		this.loopback = loopback;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isPointToPoint() {
		return pointToPoint;
	}

	public void setPointToPoint(boolean pointToPoint) {
		this.pointToPoint = pointToPoint;
	}

	public boolean isMulticast() {
		return multicast;
	}

	public void setMulticast(boolean multicast) {
		this.multicast = multicast;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public List<String> getVifs() {
		return vifs;
	}

	public void addVirtaul(String vif) {
		vifs.add(vif);
	}
}
