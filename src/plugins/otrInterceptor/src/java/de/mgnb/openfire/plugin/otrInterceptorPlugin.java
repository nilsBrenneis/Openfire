package de.mgnb.openfire.plugin;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

import java.io.File;


public class otrInterceptorPlugin implements Plugin {

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        System.out.println("Hallo Welt");
    }

    @Override
    public void destroyPlugin() {
        System.out.println("Tschuess Welt");
    }
}
