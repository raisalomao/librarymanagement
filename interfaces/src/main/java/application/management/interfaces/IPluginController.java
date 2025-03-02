package application.management.interfaces;

import javafx.stage.Stage;

public interface IPluginController {

    public abstract boolean init();
    public abstract boolean init(Stage stage);
    
}
