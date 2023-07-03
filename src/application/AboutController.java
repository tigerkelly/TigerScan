package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutController implements Initializable {

    @FXML
    private AnchorPane aPane;

    @FXML
    private Button btnClose;

    @FXML
    private Label lblVersion;
    
    private TsGlobal tg = TsGlobal.getInstance();

    @FXML
    void doBtnClose(ActionEvent event) {
    	Stage stage = (Stage) aPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    void doIconLink(ActionEvent event) {
    	 try {
			Desktop.getDesktop().browse(new URI("https://iconarchive.com"));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		lblVersion.setText(tg.appVersion);
	}

}