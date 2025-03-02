package application.management.shell;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

import application.management.App;
import application.management.interfaces.IPluginController;
import application.management.interfaces.IPlugin;
import javafx.stage.Stage;

public class PluginController implements IPluginController {

    @Override
    public boolean init() {
        return loadPlugins(null);
    }

    @Override
    public boolean init(Stage stage) {
        return loadPlugins(stage);
    }

    @SuppressWarnings("deprecation")
    private boolean loadPlugins(Stage stage) {
        try {
            File currentDir = new File("./plugins");
            FilenameFilter jarFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar");
                }
            };

            String[] plugins = currentDir.list(jarFilter);
            int i;
            URL[] jars = new URL[plugins.length];
            for (i = 0; i < plugins.length; i++) {
                jars[i] = (new File("./plugins/" + plugins[i])).toURL();
            }
            URLClassLoader ulc = new URLClassLoader(jars, App.class.getClassLoader());
            for (i = 0; i < plugins.length; i++) {
                String pluginName = plugins[i].split("-")[0];
                pluginName = pluginName.split("\\.")[0];
                IPlugin plugin = (IPlugin) Class.forName("application.management.plugins." + pluginName, true, ulc).newInstance();
                if (stage != null)
                    plugin.init(stage);
                else
                    plugin.init();
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getClass().getName() + " - " + e.getMessage());
            return false;
        }
    }

}