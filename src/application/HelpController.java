package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HelpController implements Initializable {

    @FXML
    private AnchorPane aPane;

    @FXML
    private Button btnClose;
    
    @FXML
    private TextArea taHelp;

    @FXML
    void doBtnClose(ActionEvent event) {
    	Stage stage = (Stage) aPane.getScene().getWindow();
        stage.close();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		taHelp.setText("");
		
		try (InputStream in = getClass().getResourceAsStream("/resources/help.txt");
			    BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			    
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isBlank() == true) {
					taHelp.appendText("\n");
					continue;
				}
				
				taHelp.appendText(line + "\n");
			}
			reader.close();
			in.close();
			
			taHelp.selectHome();
			taHelp.deselect();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
