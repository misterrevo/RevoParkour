package com.revo.application;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Plugin extends JavaPlugin {
    private static final String PARKOUR_COMMAND_NAME = "parkour";

    @Override
    public void onEnable() {
        InstanceManager.loadPluginInstance(this);
        getCommand(PARKOUR_COMMAND_NAME).setExecutor(InstanceManager.parkourExecutor());
    }
}
