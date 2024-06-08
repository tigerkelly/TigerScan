package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;

public class ScanThread extends Thread {

	private TabInfo ti;
	private int timeout;
	private int startPort;
	private int endPort;
	private int numThread;
	private boolean stopLooping = false;
	private TsGlobal tg = TsGlobal.getInstance();
	
	public ScanThread(TabInfo ti, int numThread, int timeout, int startPort, int endPort) {
		this.ti = ti;
		this.numThread = numThread;
		this.timeout = timeout;
		this.startPort = startPort;
		this.endPort = endPort;
	}
	
	@Override
	public void run() {
		
		String[] a = ti.getIp().split("\\.");
		
		FlowPane fp = ti.getFp();
		ObservableList<Node> lbls = fp.getChildren();
    	
    	for (int octal = startPort; octal <= endPort; octal++) {
    		if (stopLooping == true)
    			break;
    		
    		Label lbl = (Label)lbls.get(octal - 1);
    		
    		String addr = a[0] + "." + a[1] + "." + a[2] + "." + octal;
    		
			try {
				InetAddress inet = InetAddress.getByName(addr);
				boolean status = inet.isReachable(timeout);
				
				String vendor = null;
				String name = null;
				String ttStr = null;
				
				if (status == true) {
					try {
						String mac = tg.macLookup.getMacAddrHost(addr);
						if (mac != null) {
							mac = mac.replaceAll("-", ":");
							vendor = tg.vendors.get(mac.substring(0, 8));
							name = tg.macs.get(mac.trim().toLowerCase());
						}
						
						if (name != null)
							ttStr = "Name: " + name + "\nMAC: " + mac;
						
						if (vendor != null) {
							if (ttStr == null)
								ttStr = "Vendor: " + vendor + "\nMAC: " + mac;
							else
								ttStr += "\nVendor: " + vendor;
						} else {
							if (ttStr == null)
								ttStr = "MAC: " + mac;
						}
						
	//					System.out.println("mac = " + mac.substring(0, 8));
	//					System.out.println("Vendor = " + vendor);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				final String ven = ttStr;

				Platform.runLater(new Runnable() {
                    @Override
					public void run() {
                		if (status == true) {
                			if (ven != null) {
//                				System.out.println(ven + "\n-----");
                				if (ven.startsWith("Name:") == true)
                					lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #aaaaff; -fx-border-color: black; -fx-text-fill: yellow;");
                				else {
                					if (ven.startsWith("MAC:") == true)
                						lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black;");
                					else
                						lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black; -fx-text-fill: yellow;");
                				}
                				
                				if (lbl.getTooltip() == null) {
            						Tooltip tt = new Tooltip(ven);
            						lbl.setTooltip(tt);
            					} else {
            						lbl.getTooltip().setText(ven);
            					}
            				} else {
            					lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black;");
            				}
                		} else {
                			lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-border-color: black;");
                		}
                    }
                });
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	if (tg.scans.containsKey(ti.getIp() + "-" + numThread) == true) {
    		tg.scans.remove(ti.getIp() + "-" + numThread);
    	}
    	
    	tg.safeCounter.increment();
	}
	
	public void setStopThread() {
		stopLooping = true;
	}
}
