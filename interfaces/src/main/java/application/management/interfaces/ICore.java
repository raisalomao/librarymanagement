package application.management.interfaces;

public abstract class ICore {

    public static ICore getInstance() {
        return instance;
    }

    public abstract IUIController getUIController();
    public abstract IUIUtils getUIUtilsController();
    public abstract ISystemUtils getSystemUtilsController();
    public abstract ILoan getLoanController();
    public abstract IUser getUserController();
    public abstract IBook getBookController();
    public abstract IPluginController getPluginController();
    public abstract void initializeLibraryData();
    protected static ICore instance = null;
    
}