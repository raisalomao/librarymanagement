package application.management.shell;

import application.management.interfaces.ICore;
import application.management.interfaces.IUIController;
import application.management.interfaces.IUIUtils;
import application.management.interfaces.ISystemUtils;
import application.management.interfaces.IUser;
import application.management.utils.SystemUtils;
import application.management.utils.UIUtils;
import application.management.interfaces.ILoan;
import application.management.interfaces.IBook;
import application.management.interfaces.IPluginController;

public class Core extends ICore {

    private IUIUtils uiUtilsController = new UIUtils();
    private ISystemUtils systemUtilsController = new SystemUtils();
    private ILoan loanController = new Loan();
    private IUser userController = new User("");
    private IBook bookController = new Book("", 0, "", "", "", 0);
    private IPluginController pluginController = new PluginController();

    private Core() {}

    public static boolean init() {
        if (instance != null) {
            System.out.println("Fatal error: core is already initialized!");
            System.exit(-1);
        }
        instance = new Core();

        instance.initializeLibraryData();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Relax, i'm saving the data...");
            IOController.serializeLibraryData(
                User.getUserMap(),
                Book.getBookMap(),
                Loan.getLoanHistory()
            );
        }));

        UIController.launch(UIController.class);

        return true;
    }

    public void initializeLibraryData() {
        System.out.println("Loading the data...");
        
        IOController.deserializeLibraryData(
            User.getUserMap(),
            Book.getBookMap(),
            Loan.getLoanHistory()
        );
    }

    public IUIController getUIController() {
        return UIController.getInstance();
    }

    public IUIUtils getUIUtilsController() {
        return uiUtilsController;
    }

    public ISystemUtils getSystemUtilsController() {
        return systemUtilsController;
    }

    public IUser getUserController() {
        return userController;
    }

    public ILoan getLoanController() {
        return loanController;
    }

    public IBook getBookController() {
        return bookController;
    }

    public IPluginController getPluginController() {
        return pluginController;
    }

}