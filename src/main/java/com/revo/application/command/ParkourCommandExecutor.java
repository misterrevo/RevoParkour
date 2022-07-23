package com.revo.application.command;

import com.revo.application.InstanceManager;
import com.revo.application.utils.PluginUtils;
import com.revo.domain.Area;
import com.revo.domain.Point;
import com.revo.domain.exception.AreaConfigurationException;
import com.revo.domain.exception.AreaNameInUseException;
import com.revo.domain.exception.AreaNotFoundException;
import com.revo.domain.exception.UserHasNotAreaException;
import com.revo.domain.port.AreaService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ParkourCommandExecutor implements CommandExecutor {
    private static final String PARKOUR_COMMAND_NAME = "parkour";
    private static final String CREATE_ARGUMENT = "create";
    private static final String LIST_ARGUMENT = "list";
    private static final String START_ARGUMENT = "start";
    private static final String END_ARGUMENT = "end";
    private static final String DELETE_ARGUMENT = "delete";
    private static final String CHECKPOINT_ARGUMENT = "checkpoint";
    private static final String SET_ARGUMENT = "set";
    private static final Object REMOVE_ARGUMENT = "remove";
    private static final String JOIN_ARGUMENT = "join";
    private static final String LEAVE_ARGUMENT = "leave";

    private static final String AREA_COMMAND_TOP_TAG = "&2]-----[ %s ]-----[";
    private static final String AREA_COMMAND_PARKOUR_MESSAGE = "&a/parkour - list of commands";
    private static final String AREA_COMMAND_PARKOUR_LIST_MESSAGE = "&a/parkour list - list of areas";
    private static final String AREA_COMMAND_PARKOUR_CREATE_MESSAGE = "&a/parkour create [name] - create new area";
    private static final String AREA_COMMAND_PARKOUR_SET_START_MESSAGE = "&a/parkour start [name] - sets area start point";
    private static final String AREA_COMMAND_PARKOUR_SET_END_MESSAGE = "&a/parkour end [name] - sets area end point";
    private static final String AREA_COMMAND_PARKOUR_DELETE_MESSAGE = "&a/parkour delete [name] - deletes area";
    private static final String AREA_COMMAND_PARKOUR_SET_CHECKPOINT_MESSAGE = "&a/parkour checkpoint set [name] - sets checkpoint in area";
    private static final String AREA_COMMAND_PARKOUR_REMOVE_CHECKPOINT_MESSAGE = "&a/parkour checkpoint remove [name] - removes checkpoint in area";
    private static final String AREA_COMMAND_PARKOUR_JOIN_MESSAGE = "&a/parkour join [name] - join to area";
    private static final String AREA_COMMAND_PARKOUR_LEAVE_MESSAGE = "&a/parkour leave - leave from area";

    private static final String SET_CHECKPOINT_MESSAGE = "Successfully set checkpoint in area!";
    private static final String DELETE_MESSAGE = "&aSuccessfully deleted area!";
    private static final String SET_ENDPOINT_MESSAGE = "&aSuccessfully updated area end point!";
    private static final String CAN_NOT_FOUND_AREA_MESSAGE = "&4Can not found area with name %s!";
    private static final String SET_START_MESSAGE = "&aSuccessfully updated area start point!";
    private static final String AREA_LIST_TOP_TAG = "&2]-----[ AREAS ]-----[";
    private static final String NO_AREAS_MESSAGE = "&a No Areas!";
    private static final String SINGLE_AREA_MESSAGE = "&a - %s";
    private static final String CREATE_MESSAGE = "&aSuccessfully created area!";
    private static final String AREA_NAME_IN_USE_MESSAGE = "&4Area name is in use!";
    private static final String ONLY_FOR_PLAYER_MESSAGE = "&4This command is only for players!";
    private static final String REMOVE_CHECKPOINT_MESSAGE = "&aSuccessfully removed checkpoint from area!";
    private static final String IS_NOT_CHECKPOINT_MESSAGE = "&4Can not found checkpoint here!";
    private static final String JOIN_MESSAGE = "&aSuccessfully join to area!";
    private static final String NOT_CONFIGURED_AREA_MESSAGE = "&4Area are not configured yet!";
    private static final String NOT_IN_AREA_MESSAGE = "&4You are not in area!";
    private static final String LEAVE_MESSAGE = "&aSuccessfully leaved area!";

    private final AreaService areaService;

    public ParkourCommandExecutor(AreaService areaService) {
        this.areaService = areaService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (isParkourCommand(command)) {
            if (args.length == 0) {
                printHelp(sender);
                return true;
            }
            if (args.length == 1) {
                if (isListArgument(args[0])) {
                    printListOfAreas(sender);
                    return true;
                }
                if (isLeaveArgument(args[0])){
                    leaveArea(sender);
                    return true;
                }
            }
            if (args.length == 2) {
                if (isCreateArgument(args[0])) {
                    createArea(sender, args[1]);
                    return true;
                }
                if (isStartArgument(args[0])) {
                    setStartInArea(sender, args[1]);
                    return true;
                }
                if (isEndArgument(args[0])) {
                    setEndInArea(sender, args[1]);
                    return true;
                }
                if (isDeleteArgument(args[0])) {
                    deleteArea(sender, args[1]);
                    return true;
                }
                if (isJoinArgument(args[0])){
                    joinToArea(sender, args[1]);
                    return true;
                }
            }
            if (args.length == 3) {
                if (isCheckPointSetArgument(args[0], args[1])) {
                    setCheckPoint(sender, args[2]);
                    return true;
                }
                if(isCheckPointRemoveArgument(args[0], args[1])){
                    removeCheckPoint(sender, args[2]);
                    return true;
                }
            }
        }
        return false;
    }

    private void leaveArea(CommandSender sender) {
        if (isConsole(sender)) {
            sendOnlyForPlayerMessage(sender);
            return;
        }
        try {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            areaService.leaveArea(uuid.toString());
            sendMessage(player, LEAVE_MESSAGE);
        } catch (UserHasNotAreaException exception){
            sendMessage(sender, NOT_IN_AREA_MESSAGE);
        }
    }

    private boolean isLeaveArgument(String argument) {
        return Objects.equals(argument, LEAVE_ARGUMENT);
    }

    private void joinToArea(CommandSender sender, String name) {
        if (isConsole(sender)) {
            sendOnlyForPlayerMessage(sender);
            return;
        }
        try {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            areaService.joinToArea(uuid.toString(), name);
            sendMessage(player, JOIN_MESSAGE);
        } catch (AreaNotFoundException exception) {
            sendNotFoundAreaMessage(name, sender);
        } catch (AreaConfigurationException exception){
            sendMessage(sender, NOT_CONFIGURED_AREA_MESSAGE);
        }
    }

    private boolean isJoinArgument(String argument) {
        return Objects.equals(argument, JOIN_ARGUMENT);
    }

    private void removeCheckPoint(CommandSender sender, String name) {
        if (isConsole(sender)) {
            sendOnlyForPlayerMessage(sender);
            return;
        }
        try {
            Player player = (Player) sender;
            Point point = PluginUtils.mapPointFromLocation(player.getLocation());
            areaService.removeCheckPoint(name, point);
            sendMessage(player, REMOVE_CHECKPOINT_MESSAGE);
        } catch (AreaNotFoundException exception) {
            sendNotFoundAreaMessage(name, sender);
        } catch (NullPointerException exception) {
            sendMessage(sender, IS_NOT_CHECKPOINT_MESSAGE);
        }
    }

    private boolean isCheckPointRemoveArgument(String firstArgument, String secondArgument) {
        return Objects.equals(firstArgument, CHECKPOINT_ARGUMENT) && Objects.equals(secondArgument, REMOVE_ARGUMENT);
    }

    private void setCheckPoint(CommandSender sender, String name) {
        if (isConsole(sender)) {
            sendOnlyForPlayerMessage(sender);
            return;
        }
        try {
            Player player = (Player) sender;
            Point point = PluginUtils.mapPointFromLocation(player.getLocation());
            areaService.setCheckPoint(name, point);
            sendMessage(player, SET_CHECKPOINT_MESSAGE);
        } catch (AreaNotFoundException exception) {
            sendNotFoundAreaMessage(name, sender);
        }
    }

    private boolean isCheckPointSetArgument(String firstArgument, String secondArgument) {
        return Objects.equals(firstArgument, CHECKPOINT_ARGUMENT) && Objects.equals(secondArgument, SET_ARGUMENT);
    }

    private void deleteArea(CommandSender sender, String name) {
        try {
            areaService.deleteArea(name);
            sendMessage(sender, DELETE_MESSAGE);
        } catch (AreaNotFoundException exception) {
            sendNotFoundAreaMessage(name, sender);
        }
    }

    private boolean isDeleteArgument(String argument) {
        return Objects.equals(argument, DELETE_ARGUMENT);
    }

    private void setEndInArea(CommandSender sender, String name) {
        if (isConsole(sender)) {
            sendOnlyForPlayerMessage(sender);
            return;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        try {
            areaService.setEnd(playerUuid.toString(), name, PluginUtils.mapPointFromLocation(player.getLocation()));
            sendMessage(player, SET_ENDPOINT_MESSAGE);
        } catch (AreaNotFoundException exception) {
            sendNotFoundAreaMessage(name, player);
        }
    }

    private void sendNotFoundAreaMessage(String name, CommandSender sender) {
        sendMessage(sender, CAN_NOT_FOUND_AREA_MESSAGE.formatted(name));
    }

    private boolean isEndArgument(String argument) {
        return Objects.equals(argument, END_ARGUMENT);
    }

    private void setStartInArea(CommandSender sender, String name) {
        if (isConsole(sender)) {
            sendOnlyForPlayerMessage(sender);
            return;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        try {
            areaService.setStart(playerUuid.toString(), name, PluginUtils.mapPointFromLocation(player.getLocation()));
            sendMessage(player, SET_START_MESSAGE);
        } catch (AreaNotFoundException exception) {
            sendNotFoundAreaMessage(name, player);
        }
    }

    private boolean isStartArgument(String argument) {
        return Objects.equals(argument, START_ARGUMENT);
    }

    private void printListOfAreas(CommandSender sender) {
        List<Area> areaList = areaService.getAllAreas();
        sendMessage(sender, AREA_LIST_TOP_TAG);
        if (areaList.size() == 0) {
            sendMessage(sender, NO_AREAS_MESSAGE);
        }
        areaList.forEach(area -> sendMessage(sender, SINGLE_AREA_MESSAGE.formatted(area.getName())));
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
        if (isConsole(sender)) {
            sendOnlyForPlayerMessage(sender);
            return;
        }
        Player player = (Player) sender;
        UUID playerUuid = player.getUniqueId();
        try {
            areaService.createArea(playerUuid.toString(), name);
            sendMessage(player, CREATE_MESSAGE);
        } catch (AreaNameInUseException exception) {
            sendMessage(player, AREA_NAME_IN_USE_MESSAGE);
        }
    }

    private void sendOnlyForPlayerMessage(CommandSender sender) {
        sendMessage(sender, ONLY_FOR_PLAYER_MESSAGE);
    }

    private boolean isConsole(CommandSender sender) {
        return !(sender instanceof Player);
    }

    private void printHelp(CommandSender sender) {
        sendMessage(sender, AREA_COMMAND_TOP_TAG.formatted(InstanceManager.plugin().getName()));
        sendMessage(sender, AREA_COMMAND_PARKOUR_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_LIST_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_CREATE_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_SET_START_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_SET_END_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_DELETE_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_SET_CHECKPOINT_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_REMOVE_CHECKPOINT_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_JOIN_MESSAGE);
        sendMessage(sender, AREA_COMMAND_PARKOUR_LEAVE_MESSAGE);
    }

    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(PluginUtils.translateSpecialCode(message));
    }
}
