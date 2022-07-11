package com.revo.application.command;

import com.revo.application.InstanceManager;
import com.revo.application.utils.PluginUtils;
import com.revo.domain.Area;
import com.revo.domain.AreaService;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ParkourCommandExecutor implements CommandExecutor {
    private static final String PARKOUR_COMMAND_NAME = "parkour";
    private static final String CREATE_ARGUMENT = "create";
    private static final Object LIST_ARGUMENT = "list";

    private final AreaService areaService;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(isParkourCommand(command)){
            if(args.length == 0){
                printHelp(sender);
                return true;
            }
            if(args.length == 1){
                if (isListArgument(args[0])){
                    printListOfAreas(sender);
                    return true;
                }
            }
            if(args.length == 2){
                if(isCreateArgument(args[0])){
                    createArea(sender, args);
                    return true;
                }
            }
        }
        return false;
    }

    private void printListOfAreas(CommandSender sender) {
        List<Area> areaList = areaService.getAllAreas();
        sendMessage(sender, "&2]-----[ AREAS ]-----[");
        if(areaList.size() == 0){
            sendMessage(sender, "&a No Areas!");
        }
        areaList.forEach(area -> sendMessage(sender, "&a - "+area.getName()));
    }

    private boolean isListArgument(String argument) {
        return Objects.equals(argument, LIST_ARGUMENT);
    }

    private boolean isCreateArgument(String argument) {
        return Objects.equals(argument, CREATE_ARGUMENT);
    }

    private boolean isParkourCommand(Command command) {
        return Objects.equals(command.getName(), PARKOUR_COMMAND_NAME);
    }

    private void createArea(CommandSender sender, String[] args) {
        if(isConsole(sender)){
            sendMessage(sender, "&4This command is only for players!");
            return;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        areaService.createArea(playerUuid.toString(), args[1]);
        sendMessage(player, "&aSuccessfull created area!");
    }

    private boolean isConsole(CommandSender sender) {
        return !(sender instanceof Player);
    }

    private void printHelp(CommandSender sender) {
        sendMessage(sender, "&2]-----[ "+ InstanceManager.plugin().getName() +" ]-----[");
        sendMessage(sender, "&a/parkour - list of commands");
        sendMessage(sender, "&a/parkour list - list of areas");
        sendMessage(sender, "&a/parkour create [name] - create new area");
    }

    private void sendMessage(CommandSender sender, String message){
        sender.sendMessage(PluginUtils.translateSpecialCode(message));
    }
}
