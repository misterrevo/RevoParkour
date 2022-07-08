package com.revo.application.command;

import com.revo.application.Plugin;
import com.revo.application.utils.PluginUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

class ParkourExecutor implements CommandExecutor {
    private static final String PARKOUR_COMMAND_NAME = "/parkour";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals(PARKOUR_COMMAND_NAME)){
            if(args.length == 0){
                printHelp(sender);
            }
            return true;
        }
        return false;
    }

    private void printHelp(CommandSender sender) {
        sendMessage(sender, "&2]-----["+ Plugin.get().getName() +"]-----[");
        sendMessage(sender, "/parkour - list of commands");
    }

    private void sendMessage(CommandSender sender, String message){
        sender.sendMessage(PluginUtils.translateSpecialCode(message));
    }
}
