package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class ScanThread extends Thread {

	private TabInfo ti;
	private int timeout;
	private int startPort;
	private int endPort;
	private int numThread;
	private boolean stopLooping = false;
	private Button scan;
	private TsGlobal tg = TsGlobal.getInstance();
	
	public ScanThread(TabInfo ti, int numThread, int timeout, Button scan, int startPort, int endPort) {
		this.ti = ti;
		this.numThread = numThread;
		this.timeout = timeout;
		this.scan = scan;
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
				try {
					String mac = tg.macLookup.getMacAddrHost(addr);
					if (mac != null) {
						mac = mac.replaceAll("-", ":");
						vendor = tg.macs.get(mac.substring(0, 8));
					}
					
//					System.out.println("mac = " + mac.substring(0, 8));
//					System.out.println("Vendor = " + vendor);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				final String ven = vendor;

				Platform.runLater(new Runnable() {
                    @Override
					public void run() {
                		if (status == true) {
                			if (ven != null) {
            					lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black; -fx-text-fill: yellow;");
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
//    		System.out.println("remove " + ti.getIp() + "-" + numThread);
    		tg.scans.remove(ti.getIp() + "-" + numThread);
    	}
    	
    	if (endPort == 254) {
    		if (scan != null) {
    			Platform.runLater(new Runnable() {
                    @Override
					public void run() {
                    	scan.setText("Scan");
                    	scan.setGraphic(new ImageView(tg.imgScan));
                    }
                });
    		}
    			
    	}
	}
	
	public void setStopThread() {
		stopLooping = true;
	}
}
