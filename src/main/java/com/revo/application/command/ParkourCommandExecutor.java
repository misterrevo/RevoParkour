package com.revo.application.command;

import com.revo.application.InstanceManager;
import com.revo.application.utils.PluginUtils;
import com.revo.domain.Area;
import com.revo.domain.exception.AreaNameInUseException;
import com.revo.domain.exception.AreaNotFoundException;
import com.revo.domain.port.AreaService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class ParkourCommandExecutor implements CommandExecutor {
    private static final String PARKOUR_COMMAND_NAME = "parkour";
    private static final String CREATE_ARGUMENT = "create";
    private static final String LIST_ARGUMENT = "list";
    private static final String START_ARGUMENT = "start";
    private static final String END_ARGUMENT = "end";
    private static final String DELETE_ARGUMENT = "delete";

    private final AreaService areaService;

    public ParkourCommandExecutor(AreaService areaService) {
        this.areaService = areaService;
    }

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
                    createArea(sender, args[1]);
                    return true;
                }
                if(isStartArgument(args[0])){
                    setStartInArea(sender, args[1]);
                    return true;
                }
                if(isEndArgument(args[0])){
                    setEndInArea(sender, args[1]);
                    return true;
                }
                if(isDeleteArgument(args[0])){
                    deleteArea(sender, args[1]);
                    return true;
                }
            }
        }
        return false;
    }

    private void deleteArea(CommandSender sender, String name) {
        try {
            areaService.deleteArea(name);
            sendMessage(sender, "&aSuccessfully deleted area!");
        }catch (AreaNotFoundException exception){
            sendNotFoundAreaMessage(name, sender);
        }
    }

    private boolean isDeleteArgument(String argument) {
        return Objects.equals(argument, DELETE_ARGUMENT);
    }

    private void setEndInArea(CommandSender sender, String name) {
        if(isConsole(sender)){
            sendOnlyForPlayerMessage(sender);
            return;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        try{
            areaService.setEnd(playerUuid.toString(), name, PluginUtils.mapPointFromLocation(player.getLocation()));
            sendMessage(player, "&aSuccessfully updated area end point!");
        } catch (AreaNotFoundException exception){
            sendNotFoundAreaMessage(name, player);
        }
    }

    private void sendNotFoundAreaMessage(String name, CommandSender sender) {
        sendMessage(sender, "&4Can not found area with name " + name);
    }

    private boolean isEndArgument(String argument) {
        return Objects.equals(argument, END_ARGUMENT);
    }

    private void setStartInArea(CommandSender sender, String name) {
        if(isConsole(sender)){
            sendOnlyForPlayerMessage(sender);
            return;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        try{
            areaService.setStart(playerUuid.toString(), name, PluginUtils.mapPointFromLocation(player.getLocation()));
            sendMessage(player, "&aSuccessfully updated area start point!");
        } catch (AreaNotFoundException exception){
            sendMessage(player, "&4Can not found area with name " + name);
        }
    }

    private boolean isStartArgument(String argument) {
        return Objects.equals(argument, START_ARGUMENT);
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

    private void createArea(CommandSender sender, String name) {
        if(isConsole(sender)){
            sendOnlyForPlayerMessage(sender);
            return;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        try{
            areaService.createArea(playerUuid.toString(), name);
            sendMessage(player, "&aSuccessfully created area!");
        } catch (AreaNameInUseException exception){
            sendMessage(player, "&4Area name is in use!");
        }
    }

    private void sendOnlyForPlayerMessage(CommandSender sender) {
        sendMessage(sender, "&4This command is only for players!");
    }

    private boolean isConsole(CommandSender sender) {
        return !(sender instanceof Player);
    }

    private void printHelp(CommandSender sender) {
        sendMessage(sender, "&2]-----[ "+ InstanceManager.plugin().getName() +" ]-----[");
        sendMessage(sender, "&a/parkour - list of commands");
        sendMessage(sender, "&a/parkour list - list of areas");
        sendMessage(sender, "&a/parkour create [name] - create new area");
        sendMessage(sender, "&a/parkour start [name] - sets area start point");
        sendMessage(sender, "&a/parkour end [name] - sets area end point");
        sendMessage(sender, "&a/parkour delete [name] - deletes area");
    }

    private void sendMessage(CommandSender sender, String message){
        sender.sendMessage(PluginUtils.translateSpecialCode(message));
    }
}
