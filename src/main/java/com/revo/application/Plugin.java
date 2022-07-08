package com.revo.application;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Plugin extends JavaPlugin {
    //Zrob code review na trzezwo!!

    private static Plugin instance;

    @Override
    public void onEnable() {
    }

    public static Plugin get(){
        if(Objects.isNull(instance)){
            instance = new Plugin();
        }
        return instance;
    }
}
