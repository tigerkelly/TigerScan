package application;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
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
    void doImageCuteTiger(ActionEvent event) {
    	tg.centerScene(aPane, "About.fxml", "About TigerScan", null);
    }
    
    @FXML
    void doBtnHelp(ActionEvent event) {
    	tg.centerScene(aPane, "Help.fxml", "Help for TigerScan", null);
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
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
	        	buildTab(ifAddr);
        }
     }
    
    void buildTab(IfAddr ifAddr) {
    	
    	Tab tab = new Tab();
    	tab.setClosable(false);
    	tab.setText(ifAddr.getName());
    	tab.setStyle("-fx-pref-width: 80; -fx-font-size: 16px;");
    	
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
			if (ip.charAt(0) == 'f' || ip.charAt(1) == ':' || ifAddr.isLoopback() == true)
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
        	
//        	ProgressBar pb = new ProgressBar();
//        	pb.setProgress(0.0);
//        	pb.setPrefWidth(600.0);
        	
        	HBox hb2 = new HBox();
        	hb2.setSpacing(4.0);
        	HBox hb3 = new HBox();
        	HBox hb4 = new HBox();
        	
        	hb2.setPadding(new Insets(4));
        	hb2.getChildren().add(btnScan);
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
        		lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #ffff88; -fx-border-color: black;");
        		fp.getChildren().add(lbl);
    		}
    		
    		btnScan.setUserData(new TabInfo(ip, fp, ifAddr.getPrefixLen()));
    		
    		hb3.getChildren().add(fp);
    		
//    		hb3.setStyle("-fx-padding: 4;" + 
//                    "-fx-border-style: solid inside;" + 
//                    "-fx-border-width: 2;" +
//                    "-fx-border-insets: 2;" + 
//                    "-fx-border-color: green;");
    		
    		HBox.setHgrow(fp, Priority.ALWAYS);
    		
    		VBox.setVgrow(hb3, Priority.ALWAYS);
    		
    		Label lblPending = new Label("Pending");
    		lblPending.setPrefWidth(100.0);
    		lblPending.setAlignment(Pos.CENTER);
    		lblPending.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #ffff88; -fx-border-color: black;");
    		Label lblScanned = new Label("Found");
    		lblScanned.setPrefWidth(100.0);
    		lblScanned.setAlignment(Pos.CENTER);
    		lblScanned.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: lightgreen; -fx-border-color: black;");
    		Label lblFound   = new Label("Not Found");
    		lblFound.setPrefWidth(100.0);
    		lblFound.setAlignment(Pos.CENTER);
    		lblFound.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-border-color: black;");
    		
    		hb4.getChildren().addAll(lblPending, lblScanned, lblFound);
    		
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
