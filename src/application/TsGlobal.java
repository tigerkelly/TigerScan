package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TsGlobal {
private static TsGlobal singleton = null;
	
	private TsGlobal() {
		initGlobals();
	}
	
	private void initGlobals() {
		appVersion = "1.0.6";
		
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win") == true) {
			osType = 1;
		} else if (os.contains("nux") == true || 
				os.contains("nix") == true || 
				os.contains("aix") == true || 
				os.contains("sunos") == true) {
			osType = 2;
		} else if (os.contains("mac") == true) {
			osType = 3;
		}
		
		File base = new File(System.getProperty("user.home") + File.separator + "TigerScan");
		if (base.exists() == false)
			base.mkdirs();
		
		vendorFile = new File(base.getAbsolutePath() + File.separator + "vendors.txt");
		if (vendorFile.exists() == false) {
			try {
				vendorFile.createNewFile();
				
				FileWriter w = new FileWriter(vendorFile.getAbsolutePath());
				w.write("# Known MAC vendor IDs. Format 00:00:00,Vendor name\n");
				w.write("B8:27:EB,Raspberry Pi Foundation\n");
				w.write("DC:A6:32,Raspberry Pi Trading Ltd\n");
				w.write("3A:35:41,Raspberry Pi (Trading) Ltd\n");
				w.write("E4:5F:01,Raspberry Pi Trading Ltd\n");
				w.write("28:CD:C1,Raspberry Pi Trading Ltd\n");
				w.write("D8:3A:DD,Raspberry Pi Trading Ltd\n");
				
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		vendors = new HashMap<String, String>();
		
		loadVendors();
		
		macFile = new File(base.getAbsolutePath() + File.separator + "macs.txt");
		if (macFile.exists() == false) {
			try {
				macFile.createNewFile();
				
				FileWriter w = new FileWriter(macFile.getAbsolutePath());
				w.write("# Known MACs. Format 00:00:00:00:00:00,name\n");
				w.write("d8:9e:f3:14:e2:ff,DPDK\n");
				
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		macs = new HashMap<String, String>();
		
		loadMacs();
		
		sceneNav = new SceneNav();
		
		ifAddrs = new HashMap<String, IfAddr>();
		scans = new HashMap<String, ScanThread>();
		
		InputStream scanImg = getClass().getResourceAsStream("/images/scan.png");
		imgScan = new Image(scanImg, 24, 24, false, false);
		
		InputStream stopImg = getClass().getResourceAsStream("/images/stop_hand.png");
		imgStop = new Image(stopImg, 24, 24, false, false);
		
		InputStream resetImg = getClass().getResourceAsStream("/images/reset.png");
		imgReset = new Image(resetImg, 24, 24, false, false);
	}
	
	public String appVersion = null;
	public Map<String, IfAddr> ifAddrs = null;
	public Map<String, ScanThread> scans = null;
	
	public Map<String, String> macs = null;
	public Map<String, String> vendors = null;
	
	public MacLookup macLookup = null;
	
	public Image imgScan = null;
    public Image imgStop = null;
    public Image imgReset = null;
    
    public int osType;
    
    public File vendorFile = null;
    public File macFile = null;
	
	Alert alert = null;
	public int timeout = 150;
	
	public SceneNav sceneNav = null;
	
	public static TsGlobal getInstance() {
		// return SingletonHolder.singleton;
		if (singleton == null) {
			synchronized (TsGlobal.class) {
				singleton = new TsGlobal();
			}
		}
		return singleton;
	}
	
	public String scenePeek() {
		if (sceneNav.sceneQue == null || sceneNav.sceneQue.isEmpty())
			return SceneNav.TIGERSCAN;
		else
			return sceneNav.sceneQue.peek();
	}

	public void guiRestart(String msg) {
		String errMsg = String.format("A GUI error occurred.\r\nError loading %s\r\n\r\nRestarting GUI.", msg);
		showAlert("GUI Error", errMsg, AlertType.CONFIRMATION, false);
		System.exit(1);
	}

	public void loadSceneNav(String fxml) {
		if (sceneNav.loadScene(fxml) == true) {
			guiRestart(fxml);
		}
	}
	
	public void loadVendors() {
		
		try {
			List<String> allLines = Files.readAllLines(Paths.get(vendorFile.getAbsolutePath()));

			for (String line : allLines) {
				line = line.trim();
				if (line.length() <= 0)
					continue;
				if (line.charAt(0) == '#')
					continue;
				
				String[] arr = line.split(",");
				if (arr.length >= 2)
					vendors.put(arr[0].toLowerCase(), arr[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		for (String key : macs.keySet()) {
//			System.out.println(key + ", " + macs.get(key));
//		}
	}
	
	public void loadMacs() {
		
		try {
			List<String> allLines = Files.readAllLines(Paths.get(macFile.getAbsolutePath()));

			for (String line : allLines) {
				line = line.trim();
				if (line.length() <= 0)
					continue;
				if (line.charAt(0) == '#')
					continue;
				
				String[] arr = line.split(",");
				if (arr.length >= 2)
					macs.put(arr[0].toLowerCase(), arr[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		for (String key : macs.keySet()) {
//			System.out.println(key + ", " + macs.get(key));
//		}
	}
	
	public void closeAlert() {
		if (alert != null) {
			alert.close();
			alert = null;
		}
	}
	
	public ButtonType yesNoAlert(String title, String msg, AlertType alertType) {
		ButtonType yes = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType no = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(alertType, msg, yes, no);
		alert.getDialogPane().setPrefWidth(500.0);
		alert.setTitle(title);
		alert.setHeaderText(null);
		
		for (ButtonType bt : alert.getDialogPane().getButtonTypes()) {
			Button button = (Button) alert.getDialogPane().lookupButton(bt);
			button.setStyle("-fx-font-size: 16px;");
			button.setPrefWidth(100.0);
		}
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");

		Optional<ButtonType> result = alert.showAndWait();
		
		return result.get();
	}

	public ButtonType showAlert(String title, String msg, AlertType alertType, boolean yesNo) {
		alert = new Alert(alertType);
		alert.getDialogPane().setPrefWidth(500.0);
		for (ButtonType bt : alert.getDialogPane().getButtonTypes()) {
			Button button = (Button) alert.getDialogPane().lookupButton(bt);
			if (yesNo == true) {
				if (button.getText().equals("Cancel"))
					button.setText("No");
				else if (button.getText().equals("OK"))
					button.setText("Yes");
			}
			button.setStyle("-fx-font-size: 16px;");
			button.setPrefWidth(100.0);
		}
		alert.setTitle(title);
		alert.setHeaderText(null);

		alert.setContentText(msg);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");

		ButtonType bt = alert.showAndWait().get();

		alert = null;

		return bt;
	}
	
	public void showOutput(String title, String msg, AlertType alertType, boolean yesNo) {
		alert = new Alert(alertType);
		alert.getDialogPane().setPrefWidth(750.0);
		alert.getDialogPane().setPrefHeight(450.0);
		for (ButtonType bt : alert.getDialogPane().getButtonTypes()) {
			Button button = (Button) alert.getDialogPane().lookupButton(bt);
			if (yesNo == true) {
				if (button.getText().equals("Cancel"))
					button.setText("No");
				else if (button.getText().equals("OK"))
					button.setText("Yes");
			}
			button.setStyle("-fx-font-size: 16px;");
			button.setPrefWidth(100.0);
		}
		alert.setTitle(title);
		alert.setHeaderText(null);
	
		TextArea txt = new TextArea(msg);
		txt.setStyle("-fx-font-size: 16px;");
		txt.setWrapText(true);

		alert.getDialogPane().setContent(txt);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");

		alert.showAndWait().get();

		alert = null;
	}

	public void Msg(String msg) {
		System.out.println(msg);
	}
	
	public boolean copyFile(File in, File out) {
		
		try {
	        FileInputStream fis  = new FileInputStream(in);
	        FileOutputStream fos = new FileOutputStream(out);
	        byte[] buf = new byte[4096];
	        int i = 0;
	        while((i=fis.read(buf))!=-1) {
	            fos.write(buf, 0, i);
	        }
	        fis.close();
	        fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		
		return false;
    }
	
	public void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
	
	public boolean isDigit(String s) {
		boolean tf = true;
		
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i)) == false) {
				tf = false;
				break;
			}
		}
		
		return tf;
	}
	
	public void centerScene(Node node, String fxml, String title, String data) {
		FXMLLoader loader = null;
		try {
			Stage stage = new Stage();
			stage.setTitle(title);

			loader = new FXMLLoader(getClass().getResource(fxml));

			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setScene(new Scene(loader.load()));
			stage.hide();

			Stage ps = (Stage) node.getScene().getWindow();

			ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
				double stageWidth = newValue.doubleValue();
				stage.setX(ps.getX() + ps.getWidth() / 2 - stageWidth / 2);
			};
			ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
				double stageHeight = newValue.doubleValue();
				stage.setY(ps.getY() + ps.getHeight() / 2 - stageHeight / 2);
			};

			stage.widthProperty().addListener(widthListener);
			stage.heightProperty().addListener(heightListener);

			// Once the window is visible, remove the listeners
			stage.setOnShown(e2 -> {
				stage.widthProperty().removeListener(widthListener);
				stage.heightProperty().removeListener(heightListener);
			});

			stage.showAndWait();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public FXMLLoader loadScene(Node node, String fxml, String title, String data) {
		FXMLLoader loader = null;
		try {
			Stage stage = new Stage();
			stage.setTitle(title);

			loader = new FXMLLoader(getClass().getResource(fxml));

			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setScene(new Scene(loader.load()));
			stage.hide();

			Stage ps = (Stage) node.getScene().getWindow();

			ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
				double stageWidth = newValue.doubleValue();
				stage.setX(ps.getX() + ps.getWidth() / 2 - stageWidth / 2);
			};
			ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
				double stageHeight = newValue.doubleValue();
				stage.setY(ps.getY() + ps.getHeight() / 2 - stageHeight / 2);
			};

			stage.widthProperty().addListener(widthListener);
			stage.heightProperty().addListener(heightListener);

			// Once the window is visible, remove the listeners
			stage.setOnShown(e2 -> {
				stage.widthProperty().removeListener(widthListener);
				stage.heightProperty().removeListener(heightListener);
			});

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return loader;
	}
	
	public ButtonInfo centerDialog(Node node, String title, String msg, Image icon, ButtonInfo[] buttons) {
		FXMLLoader loader = null;
		YesNoOKController yno = null;
		try {
			Stage stage = new Stage();
			stage.setTitle(title);

			loader = new FXMLLoader(getClass().getResource("YesNoOK.fxml"));

			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UTILITY);
			stage.setAlwaysOnTop(true);

			stage.setScene(new Scene(loader.load()));
			stage.hide();

			Stage ps = (Stage) node.getScene().getWindow();
			
			yno = (YesNoOKController)loader.getController();
			if (title != null)
				yno.setTitle(title);
			if (msg != null)
				yno.setMessage(msg);
			if (icon != null)
				yno.setImage(icon);
			if (buttons != null)
				yno.addButtons(buttons);
			
			stage.setOnCloseRequest((e) -> e.consume());		// disable Stage close button.

			ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
				double stageWidth = newValue.doubleValue();
				stage.setX(ps.getX() + ps.getWidth() / 2 - stageWidth / 2);
			};
			ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
				double stageHeight = newValue.doubleValue();
				stage.setY(ps.getY() + ps.getHeight() / 2 - stageHeight / 2);
			};

			stage.widthProperty().addListener(widthListener);
			stage.heightProperty().addListener(heightListener);

			// Once the window is visible, remove the listeners
			stage.setOnShown(e2 -> {
				stage.widthProperty().removeListener(widthListener);
				stage.heightProperty().removeListener(heightListener);
			});

			stage.showAndWait();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return yno.getAction();
	}
	
	public ProcessRet runProcess(String[] args, Object obj) {
    	Process p = null;
    	ProcessBuilder pb = new ProcessBuilder();
		pb.redirectErrorStream(true);
		pb.command(args);

		try {
			p = pb.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

//		@SuppressWarnings("resource")
		StreamGobbler inGobbler = new StreamGobbler(p.getInputStream(), obj);
		inGobbler.start();

		int ev = 0;
		try {
			ev = p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new ProcessRet(ev, inGobbler.getOutput());
    }
}
