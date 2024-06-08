module tigerscan {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.base;
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.base;
	requires javafx.swing;
	
	opens application to javafx.base, javafx.graphics, javafx.fxml;
}
