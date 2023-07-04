package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class IpInfoController implements Initializable {

    @FXML
    private AnchorPane aPane;

    @FXML
    private Button btnClose;

    @FXML
    private Label lblIpv4Address;

    @FXML
    private Label lblIpv4Broadcast;

    @FXML
    private Label lblIpv4Mask;

    @FXML
    private Label lblMacAddress;

    @FXML
    private Label lblVendorId;

    @FXML
    void doBtnClose(ActionEvent event) {
    	Stage stage = (Stage) aPane.getScene().getWindow();
        stage.close();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public Stage getStage() {
    	return (Stage)aPane.getScene().getWindow();
    }

	public void setLblIpv4Address(String txt) {
		this.lblIpv4Address.setText(txt);
	}

	public void setLblIpv4Broadcast(String txt) {
		this.lblIpv4Broadcast.setText(txt);
	}

	public void setLblIpv4Mask(String txt) {
		this.lblIpv4Mask.setText(txt);
	}

	public void setLblMacAddress(String txt) {
		this.lblMacAddress.setText(txt);
	}

	public void setLblVendorId(String txt) {
		this.lblVendorId.setText(txt);
	}

}
