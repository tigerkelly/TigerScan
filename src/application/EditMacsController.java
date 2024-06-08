package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditMacsController implements Initializable, RefreshScene {

    @FXML
    private AnchorPane aPane;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextArea taVendors;
    
    @FXML
    private TextArea taMacs;
    
    private TsGlobal tg = TsGlobal.getInstance();
    private File macFile = null;
    private File vendorFile = null;

    @FXML
    void doBtnCancel(ActionEvent event) {
    	Stage stage = (Stage)aPane.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void doBtnSave(ActionEvent event) {
    	
		try {
			FileWriter w = new FileWriter(vendorFile.getAbsolutePath(), false);
			
			w.write(taVendors.getText());
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter w = new FileWriter(macFile.getAbsolutePath(), false);
			
			w.write(taMacs.getText());
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Stage stage = (Stage)aPane.getScene().getWindow();
    	stage.close();
    	
    	tg.loadVendors();
    	tg.loadMacs();
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	vendorFile = new File(System.getProperty("user.home") + File.separator + "TigerScan" + File.separator + "vendors.txt");
    	
    	if (vendorFile.exists() == true) {
	    	try {
				FileInputStream fis = new FileInputStream(vendorFile);
				byte[] data = new byte[(int) vendorFile.length()];
		    	fis.read(data);
		    	fis.close();
		    	String str = new String(data, "UTF-8");
		    	
		    	taVendors.setText(str);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	macFile = new File(System.getProperty("user.home") + File.separator + "TigerScan" + File.separator + "macs.txt");
    	
    	if (macFile.exists() == true) {
	    	try {
				FileInputStream fis = new FileInputStream(macFile);
				byte[] data = new byte[(int) macFile.length()];
		    	fis.read(data);
		    	fis.close();
		    	String str = new String(data, "UTF-8");
		    	
		    	taMacs.setText(str);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}
    
    public Stage getStage() {
    	return (Stage)aPane.getScene().getWindow();
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
