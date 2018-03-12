package com.application;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.application.db.util.HibernateUtil;
import com.application.db.util.Service;
import com.application.util.ApplicationProperties;
import com.application.util.Constants;
import com.application.view.ArtikelOverviewController;
import com.application.view.RootLayoutController;
import com.application.view.SettingsController;
import com.application.view.lagerort.LagerortOverviewController;
import com.application.view.mailsettings.MailSettingsEditController;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private static final Logger logger = Logger.getLogger(Main.class);
	private ResourceBundle resources = ResourceBundle.getBundle("language");

	// Wird automatisch von SVN beschrieben
	public final static String BUILD = "$Rev: 91 $";
	public final static String DATE = "$Date: 2018-01-31 11:05:15 +0100 (Mi, 31 Jan 2018) $";

	// Java Entwicklungsversion
	public final static String JDK = "1.8.0_152";

	private Stage primaryStage;
	private BorderPane rootLayout;

	public static void main(String[] args) {

		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;

		HibernateUtil.getSessionFactory();

		String userHome = System.getProperty("user.home");

		PropertyConfigurator.configure(getClass().getClassLoader().getResource("log4j.properties"));
		ApplicationProperties.configure("application.properties",
				userHome + File.separator + resources.getString("appname"), "application.properties");
		ApplicationProperties.getInstance().setup();

		this.primaryStage.setTitle(resources.getString("appname") + " " + BUILD.replace("$", " ") + "@"
				+ ApplicationProperties.getInstance().getProperty("db_host"));
		this.primaryStage
				.setMaximized(ApplicationProperties.getInstance().getProperty("start_maximized").contains("true"));
		this.primaryStage.getIcons()
				.add(new Image(getClass().getClassLoader().getResourceAsStream(Constants.APP_ICON)));

		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {

				HibernateUtil.getSessionFactory().close();

			}
		});

		initRootLayout();
	}

	public void initRootLayout() {
		try {

			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());

			// primaryStage.setMaximized(true);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMain(this);

			showArtikelOverview();

			primaryStage.show();

		} catch (IOException e) {

			if (logger.isInfoEnabled()) {
				logger.error(e);
			}

			e.printStackTrace();
		}

	}

	public void showArtikelOverview() {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ArtikelOverview.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());

			ArtikelOverviewController controller = loader.getController();
			controller.setDialogStage(primaryStage);
			controller.setData();

			rootLayout.setCenter(pane);

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showLagerortOverviewDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/lagerort/LagerortOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Lagerorte");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			LagerortOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData();

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public boolean showSettingsDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Settings.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.setTitle(resources.getString("settings"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());
			dialogStage.setScene(scene);

			SettingsController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData();

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean showMailSettingsDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/mailsettings/MailSettingsEdit.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.setTitle(resources.getString("settings"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());
			dialogStage.setScene(scene);

			MailSettingsEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(Service.getInstance().getMailSettings().get(0));

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

}
