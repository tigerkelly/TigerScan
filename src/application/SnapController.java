package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SnapController implements Initializable, RefreshScene {

    @FXML
    private AnchorPane aPane;
    
    @FXML
    private Label lblInterface;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnSave;

    @FXML
    private ImageView ivSnapshot;

    @FXML
    private TextArea textArea;
    
    private Image img = null;
    private String iface = null;
    private String txt = null;

    @FXML
    void doBtnClose(ActionEvent event) {
    	Stage stage = (Stage)aPane.getScene().getWindow();
    	stage.close();
    }

    @FXML
    void doBtnSave(ActionEvent event) {
    	Stage stage = (Stage)aPane.getScene().getWindow();
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setInitialFileName("TigerScan-" + iface + ".txt");
    	fileChooser.setTitle("TigerScan Save");
    	fileChooser.getExtensionFilters().addAll(
    		     new FileChooser.ExtensionFilter("Text Files", "*.txt")
    		    ,new FileChooser.ExtensionFilter("All Files", "*.*")
    		);
    	fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    	
    	
    	File selectedFile = fileChooser.showSaveDialog(stage);
    	
    	if (selectedFile == null)
    		return;
        
		try {
			PrintWriter writer = new PrintWriter(selectedFile, "UTF-8");
			writer.print("Scan Snapshot from TigerScan program.\n");
			writer.print("\n");
	    	writer.print(txt);
	    	writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
    	stage.close();
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void setImage(Image img, String iface) {
		this.img = img;
		this.iface = iface;
		ivSnapshot.setFitWidth(img.getWidth());
		ivSnapshot.setFitHeight(img.getHeight());
		ivSnapshot.setImage(this.img);
		lblInterface.setText("Interface: " + this.iface);
	}
	
	public void setTextArea(String txt) {
		this.txt = txt;
		textArea.setText(this.txt);
	}
	
	public Stage getStage() {
		return (Stage)aPane.getScene().getWindow();
	}

}