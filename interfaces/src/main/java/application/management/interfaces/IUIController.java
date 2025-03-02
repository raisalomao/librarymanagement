package application.management.interfaces;

import java.util.List;
import javafx.scene.control.MenuItem;

public interface IUIController {

    public abstract List<MenuItem> createMenuItem(String menuText, String... menuItemTexts);
    
}
