package com.revo.application.command;

import com.revo.application.InstanceManager;
import com.revo.application.utils.PluginUtils;
import com.revo.domain.AreaService;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

@RequiredArgsConstructor
public class ParkourExecutor implements CommandExecutor {
    private static final String PARKOUR_COMMAND_NAME = "parkour";
    private static final String CREATE_ARGUMENT = "create";

    private final AreaService areaService;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Objects.equals(command.getName(), PARKOUR_COMMAND_NAME)){
            if(args.length == 0){
                printHelp(sender);
            }
            if(args.length == 2){
                if(Objects.equals(args[0], CREATE_ARGUMENT)){
                    createArea(sender, args);
                }
            }
            return true;
        }
        return false;
    }

    private void createArea(CommandSender sender, String[] args) {
        
    }

    private void printHelp(CommandSender sender) {
        sendMessage(sender, "&2]-----["+ InstanceManager.plugin().getName() +"]-----[");
        sendMessage(sender, "/parkour - list of commands");
    }

    private void sendMessage(CommandSender sender, String message){
        sender.sendMessage(PluginUtils.translateSpecialCode(message));
    }
}
