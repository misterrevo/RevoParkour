package com.revo.application;

import com.revo.application.listener.PlayerMoveEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private static final String PARKOUR_COMMAND_NAME = "parkour";

    @Override
    public void onEnable() {
        InstanceManager.loadPluginInstance(this);
        getCommand(PARKOUR_COMMAND_NAME).setExecutor(InstanceManager.parkourCommandExecutor());
        getServer().getPluginManager().registerEvents(new PlayerMoveEventListener(), this);
    }
}
