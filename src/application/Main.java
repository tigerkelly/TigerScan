package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
	private Pane mainPane = null;
	private TsGlobal tg = TsGlobal.getInstance();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			System.setProperty("java.net.preferIPv4Stack" , "true");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/CuteTiger.png")));
			primaryStage.setTitle("TigerScan by Kelly Wiles");
			primaryStage.setScene(createScene(loadMainPane()));
//			primaryStage.setMinWidth(750);
//			primaryStage.setMinHeight(700);
//			primaryStage.setOnCloseRequest((e) -> e.consume());		// disable Stage close button.
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() {
//		System.out.println("*** TigerScan is Ending. ***");

		SceneInfo si = tg.sceneNav.fxmls.get(tg.scenePeek());
		if (si != null && si.controller instanceof RefreshScene) {
			RefreshScene c = (RefreshScene) si.controller;
			c.leaveScene();
		}
		
		tg.scans.forEach((key, value) -> {
			ScanThread st = (ScanThread)value;
			st.setStopThread();
		});
	}

	/**
	 * Loads the main fxml layout.
	 *
	 * @return the loaded pane.
	 * @throws IOException if the pane could not be loaded.
	 */
//    @SuppressWarnings("resource")
	private Pane loadMainPane() throws IOException {
//		System.out.println("*** TigerScan is Starting. ***");

		FXMLLoader loader = new FXMLLoader();

		mainPane = (Pane) loader.load(getClass().getResourceAsStream(SceneNav.MAIN)); // SceneNav

		SceneNavController mainController = loader.getController();

		tg.sceneNav.setMainController(mainController);
		tg.sceneNav.loadScene(SceneNav.TIGERSCAN);

		return mainPane;
	}

	/**
	 * Creates the main application scene.
	 *
	 * @param mainPane the main application layout.
	 *
	 * @return the created scene.
	 */
	private Scene createScene(Pane mainPane) {
		Scene scene = new Scene(mainPane);

//		scene.getStylesheets().setAll(getClass().getResource("application.css").toExternalForm());

		return scene;
	}
}
