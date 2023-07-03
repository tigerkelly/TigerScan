package application;

import javafx.scene.layout.FlowPane;

public class TabInfo {

	private String ip;
	private int prefixLen;
	private FlowPane fp;
	
	public TabInfo(String ip, FlowPane fp, int prefixLen) {
		super();
		this.ip = ip;
		this.fp = fp;
		this.prefixLen = prefixLen;
	}

	public TabInfo() {
		super();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPrefixLen() {
		return prefixLen;
	}

	public void setPrefixLen(int prefixLen) {
		this.prefixLen = prefixLen;
	}

	public FlowPane getFp() {
		return fp;
	}

	public void setFp(FlowPane fp) {
		this.fp = fp;
	}
}
