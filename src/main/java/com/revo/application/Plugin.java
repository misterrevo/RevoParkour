package com.revo.application;
import com.revo.application.commands.TestCommandExecutor;
import com.revo.application.database.file.AreaFileRepository;
import com.revo.application.database.file.UserFileRepository;
import com.revo.application.utils.MessageAspect;
import com.revo.application.utils.PlayerSupport;
import com.revo.domain.AreaService;
import com.revo.domain.port.AreaRepositoryPort;
import com.revo.domain.port.PlayerSupportPort;
import com.revo.domain.port.UserRepositoryPort;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("test").setExecutor(new TestCommandExecutor());
    }
}
