package application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TigerScanController implements Initializable, RefreshScene {

    @FXML
    private AnchorPane aPane;

    @FXML
    private TabPane tabPane;
    
    @FXML
    private Label lblVersion;
    
    @FXML
    private TextField tfTimeout;

    @FXML
    private Button btnQuit;
    
    @FXML
    private Button btnMacs;
    
    @FXML
    private ImageView imageCuteTiger;
    
    @FXML
    private ChoiceBox<String> cbThreads;
    
    private TsGlobal tg = TsGlobal.getInstance();
    private ObservableList<String> thrds = null;
    
    @FXML
    void doBtnQuit(ActionEvent event) {
    	Stage stage = (Stage) aPane.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void doBtnMacs(ActionEvent event) {
    	tg.centerScene(aPane, "EditMacs.fxml", "Edit MAC Addresses", null);
    	
    	tg.loadMacs();
    }
    
    @FXML
    void doImageCuteTiger(ActionEvent event) {
    	tg.centerScene(aPane, "About.fxml", "About TigerScan", null);
    }
    
    @FXML
    void doBtnHelp(ActionEvent event) {
    	tg.centerScene(aPane, "Help.fxml", "Help for TigerScan", null);
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	tg.macLookup = new MacLookup();
    	
		lblVersion.setText(tg.appVersion);
		tfTimeout.setText(tg.timeout + "");
		
		thrds = FXCollections.observableArrayList();
		
		thrds.addAll("1", "2", "4", "8", "16");
		
		int cores = Runtime.getRuntime().availableProcessors();
		
		cbThreads.setItems(thrds);
		
		cbThreads.setValue(cores + "");
    	
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)) {
	            displayInterfaceInformation(netint);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
    
    void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        List<InterfaceAddress> ifAddresses = netint.getInterfaceAddresses();
        Enumeration<NetworkInterface> subInterfaces = netint.getSubInterfaces();
        
        ArrayList<NetworkInterface> subs = Collections.list(subInterfaces);
        
        ArrayList<InetAddress> addrs = Collections.list(inetAddresses);
        if (addrs.size() > 0) {
        	
//        	byte[] hw = netint.getHardwareAddress();
//            if (hw != null)
//            	System.out.printf("%s - %02x:%02x:%02x:%02x:%02x:%02x\n", netint.getName(), hw[0], hw[1], hw[2], hw[3], hw[4], hw[5]);
        
	        IfAddr ifAddr = new IfAddr();
	        ifAddr.setName(netint.getName());
	        ifAddr.setDisplayName(netint.getDisplayName());
	        ifAddr.setHardware(netint.getHardwareAddress());
	        ifAddr.setLoopback(netint.isLoopback());
	        ifAddr.setUp(netint.isUp());
	        ifAddr.setPointToPoint(netint.isPointToPoint());
	        ifAddr.setMulticast(netint.supportsMulticast());
	        ifAddr.setVirtual(netint.isVirtual());
	        
	        int idx = 0;
	        for (InetAddress addr : addrs) {
	        	ifAddr.addIp(addr.toString().substring(1));
	        	ifAddr.setPrefixLen(ifAddresses.get(idx++).getNetworkPrefixLength());
	        }
	        
	        for (NetworkInterface nif : subs) {
	        	ifAddr.addVirtaul(nif.toString().substring(1));
	        }
	        
	        tg.ifAddrs.put(ifAddr.getName(), ifAddr);
	        
	        if (ifAddr.isLoopback() == false)
	        	buildTab(netint, ifAddr);
        }
     }
    
    void buildTab(NetworkInterface iface, IfAddr ifAddr) {
    	
    	Tab tab = new Tab();
    	tab.setClosable(false);
    	tab.setText(ifAddr.getName());
    	tab.setStyle("-fx-pref-width: 100; -fx-font-size: 16px;");
    	
    	tabPane.getTabs().add(tab);
    	
    	AnchorPane ap1 = new AnchorPane();
    	VBox vb1 = new VBox();
    	HBox hb1 = new HBox();
    	
    	hb1.setAlignment(Pos.CENTER_LEFT);
    	hb1.setPadding(new Insets(4));
    	
    	Label lblDisplayName = new Label(ifAddr.getDisplayName());
    	lblDisplayName.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #8a2be2;");
    	
    	hb1.getChildren().add(lblDisplayName);
    	
    	ap1.setStyle("-fx-padding: 4;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 2;" + 
                "-fx-border-color: grey;");
    	
    	AnchorPane.setBottomAnchor(vb1, 0.0);
		AnchorPane.setTopAnchor(vb1, 0.0);
		AnchorPane.setLeftAnchor(vb1, 0.0);
		AnchorPane.setRightAnchor(vb1, 0.0);
    	
    	ap1.getChildren().add(vb1);
    	
    	tab.setContent(ap1);
    	
		TabPane tabPane2 = new TabPane();
		
//		tabPane2.setStyle("-fx-padding: 4;" + 
//                "-fx-border-style: solid inside;" + 
//                "-fx-border-width: 2;" +
//                "-fx-border-insets: 2;" + 
//                "-fx-border-color: yellow;");
		
		VBox.setVgrow(tabPane2, Priority.ALWAYS);
		
		vb1.getChildren().addAll(hb1, tabPane2);
		
		for (String ip : ifAddr.getIps()) {
			if (ip.charAt(0) == 'f' || ip.indexOf(':') != -1 || ifAddr.isLoopback() == true)
    			continue;
			
			Tab tab2 = new Tab(ip);
			tab2.setClosable(false);
			tab2.setStyle("-fx-pref-width: 125px; -fx-font-size: 15px; -fx-text-base-color: blue;");
			
			AnchorPane ap2 = new AnchorPane();
			VBox vb2 = new VBox();
			
//			ap2.setStyle("-fx-padding: 4;" + 
//                      "-fx-border-style: solid inside;" + 
//                      "-fx-border-width: 2;" +
//                      "-fx-border-insets: 2;" + 
//                      "-fx-border-color: blue;");
			
			VBox.setVgrow(ap2,  Priority.ALWAYS);
			
			AnchorPane.setBottomAnchor(vb2, 0.0);
    		AnchorPane.setTopAnchor(vb2, 0.0);
    		AnchorPane.setLeftAnchor(vb2, 0.0);
    		AnchorPane.setRightAnchor(vb2, 0.0);
    		
    		Button btnScan = new Button("Scan");
    		btnScan.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
    		btnScan.setGraphic(new ImageView(tg.imgScan));
        	Tooltip tip2 = new Tooltip("Click to scan this subnet.");
        	tip2.setStyle("-fx-font-size: 16px;");
        	btnScan.setTooltip(tip2);
        	btnScan.setOnAction(e -> {
        		scanIP((Button)e.getSource());
        	});
        	
        	Button btnReset = new Button("Reset");
        	btnReset.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
    		btnReset.setGraphic(new ImageView(tg.imgReset));
        	Tooltip tipReset = new Tooltip("Click to reset all addresses to pending.");
        	tipReset.setStyle("-fx-font-size: 16px;");
        	btnReset.setTooltip(tipReset);
        	
        	Label lblTxt = new Label("Click on # to test single address.");
        	lblTxt.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: blue;");
        	
//        	ProgressBar pb = new ProgressBar();
//        	pb.setProgress(0.0);
//        	pb.setPrefWidth(600.0);
        	
        	HBox hb2 = new HBox();
        	hb2.setSpacing(4.0);
        	HBox hb3 = new HBox();
        	HBox hb4 = new HBox();
        	
        	hb2.setPadding(new Insets(4));
        	hb2.setSpacing(8.0);
        	hb2.getChildren().addAll(lblTxt, btnReset, btnScan);
        	hb2.setAlignment(Pos.CENTER_RIGHT);
        	
        	vb2.getChildren().add(hb2);
        	
        	FlowPane fp = new FlowPane();
        	fp.setHgap(5.0);
        	fp.setVgap(5.0);
        	fp.setPrefWrapLength(300.0);
        	
        	for (int i = 1; i <= 254; i++) {
    			Label lbl = new Label(i + "");
        		lbl.setAlignment(Pos.CENTER);
        		lbl.setPrefWidth(40.0);
        		if (tg.osType != 1)
        			lbl.setPrefHeight(23.5);
        		lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #ffff88; -fx-border-color: black;");
        		lbl.setUserData(new TabInfo(ip, fp, ifAddr.getPrefixLen()));
        		lbl.setOnMouseClicked((e) -> {
        			Label l = (Label)e.getSource();
        			getIpInfo(iface, l);
        		});
        		fp.getChildren().add(lbl);
    		}
        	
        	btnReset.setOnAction(e -> {
        		for (Node n : fp.getChildren()) {
        			Label lbl = (Label)n;
            		lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #ffff88; -fx-border-color: black;");
            		
        		}
        	});
    		
    		btnScan.setUserData(new TabInfo(ip, fp, ifAddr.getPrefixLen()));
    		
    		hb3.getChildren().add(fp);
    		
//    		hb3.setStyle("-fx-padding: 4;" + 
//                    "-fx-border-style: solid inside;" + 
//                    "-fx-border-width: 2;" +
//                    "-fx-border-insets: 2;" + 
//                    "-fx-border-color: green;");
    		
    		HBox.setHgrow(fp, Priority.ALWAYS);
    		
    		VBox.setVgrow(hb3, Priority.ALWAYS);
    		
    		Label lblLedger = new Label("Color Ledger:");
    		lblLedger.setStyle("-fx-font-size: 16px;");
    		
    		Label lblPending = new Label("Pending");
    		lblPending.setPrefWidth(100.0);
    		lblPending.setAlignment(Pos.CENTER);
    		lblPending.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #ffff88; -fx-border-color: black;");
    		Tooltip tipPending = new Tooltip("Address has not been scanned yet.");
    		tipPending.setStyle("-fx-font-size: 16px;");
    		lblPending.setTooltip(tipPending);
    		Label lblScanned = new Label("Found");
    		lblScanned.setPrefWidth(100.0);
    		lblScanned.setAlignment(Pos.CENTER);
    		lblScanned.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black;");
    		Tooltip tipScanned = new Tooltip("Address has been scanned and was found.");
    		tipScanned.setStyle("-fx-font-size: 16px;");
    		lblScanned.setTooltip(tipScanned);
    		Label lblFound   = new Label("Not Found");
    		lblFound.setPrefWidth(100.0);
    		lblFound.setAlignment(Pos.CENTER);
    		lblFound.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-border-color: black;");
    		Tooltip tipNotFound = new Tooltip("Address has been scanned but did NOT responded.");
    		tipNotFound.setStyle("-fx-font-size: 16px;");
    		lblFound.setTooltip(tipNotFound);
    		
    		hb4.getChildren().addAll(lblLedger, lblPending, lblScanned, lblFound);
    		
    		hb4.setSpacing(4.0);
    		hb4.setAlignment(Pos.CENTER_LEFT);
    		
    		vb2.getChildren().addAll(hb3, hb4);
			
			ap2.getChildren().add(vb2);
			
			tab2.setContent(ap2);
			
			tabPane2.getTabs().add(tab2);
		}

		AnchorPane.setBottomAnchor(vb1, 0.0);
		AnchorPane.setTopAnchor(vb1, 0.0);
		AnchorPane.setLeftAnchor(vb1, 0.0);
		AnchorPane.setRightAnchor(vb1, 0.0);
    }
    
    private void getIpInfo(NetworkInterface iface, Label lbl) {
    	String octal = lbl.getText();
    	
    	TabInfo ti = (TabInfo)lbl.getUserData();
    	String ip = ti.getIp();
    	
    	String[] a = ip.split("\\.");
    	
    	String addr = a[0] + "." + a[1] + "." + a[2] + "." + octal;
    	
    	Stage stage = (Stage)lbl.getScene().getWindow();
    	stage.getScene().setCursor(Cursor.WAIT);
    	
		new Thread(() -> {
			try {
				int timeout = 0;
		    	try {
		    		timeout = Integer.parseInt(tfTimeout.getText());
		    	}  catch (NumberFormatException nfe) {
		    		tg.showAlert("User Error", "Timeout is invalid.", AlertType.ERROR, false);
		    		return;
		    	}
		    	
				InetAddress inet = InetAddress.getByName(addr);
				if (inet.isReachable(timeout) == true) {
					String vendor = null;
					try {
						String mac = tg.macLookup.getMacAddrHost(addr).replaceAll("-", ":");
						vendor = tg.macs.get(mac.substring(0, 8));
						
//						System.out.println("mac = " + mac.substring(0, 8));
//						System.out.println("Vendor = " + vendor);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if (vendor != null) {
						lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black; -fx-text-fill: yellow;");
						if (lbl.getTooltip() == null) {
							Tooltip tt = new Tooltip(vendor);
							lbl.setTooltip(tt);
						} else {
							lbl.getTooltip().setText(vendor);
						}
					} else {
						lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black;");
					}
				} else {
	    			lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-border-color: black;");
				}
				
				stage.getScene().setCursor(Cursor.DEFAULT);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
    }
    
    private void scanIP(Button scan) {
    	if (scan == null)
    		return;
    	
    	TabInfo ti = (TabInfo)scan.getUserData();
    	String ip = ti.getIp();
    	
    	// For now only do the last octal 1 - 254
    	
    	scan.setText("Stop");
    	scan.setGraphic(new ImageView(tg.imgStop));
    	
    	FlowPane fp = ti.getFp();
    	
    	int numThreads = Integer.parseInt(cbThreads.getValue());
    	
    	boolean retFlag = false;
    	for (int k = 1; k <= numThreads; k++) {
	    	if (tg.scans.containsKey(ip + "-" + k) == true) {
	    		retFlag = true;
	    		ScanThread st = (ScanThread)tg.scans.get(ip + "-" + k);
	    		st.setStopThread();
//	    		tg.scans.remove(ip + "-" + k);
	    	}
    	}
    	
    	if (retFlag == true) {
	    	scan.setText("Scan");
	    	scan.setGraphic(new ImageView(tg.imgScan));
	    	return;
    	}
    	
    	for (int i = 1; i <= 254; i++) {
    		Label lbl = (Label)fp.getChildren().get(i-1);
    		lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: yellow; -fx-border-color: black;");
    	}
    	
    	scan.setText("Stop");
    	scan.setGraphic(new ImageView(tg.imgStop));
    	
    	int timeout = tg.timeout;
    	
    	try {
    		timeout = Integer.parseInt(tfTimeout.getText());
    	} catch (NumberFormatException nfe) {
    		
    	}
    	
    	int num = (256 / numThreads);
//    	int rm = (256 % numThreads);
    	
//    	System.out.println(numThreads + " = " + num + "," + rm);
    	
    	int start = 1;
    	int end = num;
    	for (int k = 1; k <= numThreads; k++) {
    		
    		if (k == numThreads) {
    			end = 254;
		    	ScanThread st1 = new ScanThread(ti, k, timeout, scan, start, 254);
		    	st1.start();
		    	tg.scans.put(ip + "-" + k,  st1);
    		} else {
    			ScanThread st1 = new ScanThread(ti, k, timeout, scan, start, end);
    	    	st1.start();
    	    	tg.scans.put(ip + "-" + k,  st1);
    		}
    		
//    		System.out.println("Start " + start + ", End " + end);
    		
    		start += num;
    		end += num;
    	}
    }

	@Override
	public void refreshScene() {
		
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text) {
		
	}
}
