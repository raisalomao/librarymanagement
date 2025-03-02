package application.management.shell;

import java.util.List;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import application.management.interfaces.IUIController;

public class UIController extends Application implements IUIController {

    private MenuBar menuBar;
    private static UIController uiController;

    public UIController() {}

    @Override
    public void init() {
        uiController = this;
    }

    public static UIController getInstance() {
        return uiController;
    }

    @Override
    public void start(Stage primaryStage) {
        Image icon = new Image(getClass().getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(icon);
        showIntroScreen(primaryStage);
    }

    @Override
    public List<MenuItem> createMenuItem(String menuText, String... menuItemTexts) {
        Menu newMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText().equals(menuText)) {
                newMenu = menu;
                break;
            }
        }
        if (newMenu == null) {
            newMenu = new Menu(menuText);
            menuBar.getMenus().add(newMenu);
        }
        List<MenuItem> menuItems = new ArrayList<>();
        for (String menuItemText : menuItemTexts) {
            MenuItem menuItem = new MenuItem(menuItemText);
            newMenu.getItems().add(menuItem);
            menuItems.add(menuItem);
        }

        return menuItems;
    }

    private void showIntroScreen(Stage stage) {
        Label title = new Label("Library Management");
        title.setFont(new Font(24));
        title.setTextAlignment(TextAlignment.CENTER);

        Image image = new Image(getClass().getResource("/icon.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);

        Button managementButton = new Button("Click to start");
        managementButton.setStyle("-fx-font-size: 12px;");
        managementButton.setOnAction(e -> showManagementScreen(stage));
        
        VBox layout = new VBox(15, title, imageView, managementButton);
        layout.setSpacing(25);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30;");

        StackPane stackPane = new StackPane(layout);
        stackPane.setStyle("-fx-background-color: white;");
        Scene scene = new Scene(stackPane, 900, 450);
        stage.setScene(scene);
        stage.show();
    }

    public void showManagementScreen(Stage stage) {
        stage.setTitle("Library Management");
        
        menuBar = new MenuBar();
        VBox vBox = new VBox(menuBar);
        Scene scene = new Scene(vBox, 900, 450);
        stage.setScene(scene);
        stage.show();
        Core.getInstance().getPluginController().init(stage);

        Label principalsLabel = new Label("Use the options in the menu to manage the library.");   
        principalsLabel.setFont(new Font(24));

        vBox.getChildren().add(principalsLabel);
        principalsLabel.setTranslateX(180);
        principalsLabel.setTranslateY(150);
    
        Menu backMenu = new Menu("Back");
        backMenu.setStyle("-fx-font-size: 12px;");
        MenuItem backAction = new MenuItem("Home page");
        backAction.setOnAction(e -> showIntroScreen(stage));
        backMenu.getItems().add(backAction);
    
        menuBar.getMenus().add(backMenu);
    }
    
}