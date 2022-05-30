package com.revo.application.commands;

import com.revo.application.InstanceManager;
import com.revo.application.Plugin;
import com.revo.application.utils.PlayerSupport;
import com.revo.domain.AreaService;
import com.revo.domain.port.PlayerSupportPort;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestCommandExecutor implements CommandExecutor {

    AreaService areaService = InstanceManager.areaService();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equals("test")){
            areaService.test();
            return true;
        }
        return false;
    }
}
