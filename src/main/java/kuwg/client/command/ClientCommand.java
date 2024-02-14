package kuwg.client.command;

import kuwg.client.ClientManager;
import kuwg.client.config.ConfigManager;
import kuwg.client.mod.ForgeMod;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClientCommand implements CommandExecutor, TabCompleter {
    private static final ConfigManager configManager = ClientManager.getInstance().getConfigManager();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission(configManager.defaultCommandPerm)){
            sender.sendMessage(configManager.noPermissionMessage);
            return true;
        }
        if(!configManager.generalCommandEnabled){
            sender.sendMessage(configManager.commandNotEnabledMessage);
            return false;
        }
        if(args.length==0||args[0].equalsIgnoreCase("help")){

            if(!configManager.clientCommandEnabled&&!configManager.reloadCommandEnabled&&!configManager.modsCommandEnabled){
                sender.sendMessage(configManager.commandNotEnabledMessage);
                return false;
            }
            if(configManager.clientCommandEnabled&&configManager.reloadCommandEnabled||
                    configManager.clientCommandEnabled&&configManager.modsCommandEnabled||
                    configManager.reloadCommandEnabled&&configManager.modsCommandEnabled)
                sender.sendMessage("§aClientManager commands:");
            else
                sender.sendMessage("§aClientManager command:");
            if(configManager.clientCommandEnabled)
                sender.sendMessage("§4-§b/ClientManager client <player>§e: §7Gets the Client of a player.");
            if(configManager.modsCommandEnabled)
                sender.sendMessage("§4-§b/ClientManager mods <player>§e: §7Gets the Forge Mods of a player.");
            if(configManager.reloadCommandEnabled)
                sender.sendMessage("§4-§b/ClientManager reload§e: §7Reloads the config.");
            return true;
        }
        final Player target = Bukkit.getPlayer(args.length>1?args[1]:"");
        final String cmd = args[0].toLowerCase();
        switch (cmd){
            case "client":
                if(!sender.hasPermission(configManager.clientCommandPermission)){
                    sender.sendMessage(configManager.noPermissionMessage);
                    return true;
                }
                if(!configManager.clientCommandEnabled){
                    sender.sendMessage(configManager.commandNotEnabledMessage);
                    return false;
                }
                if(target==null){
                    sender.sendMessage("§4Player not found.");
                    return true;
                }
                sender.sendMessage("§a"+target.getName()+"'s client: \"§f"+ClientManager.getClient(target)+"§a\".");
                break;
            case "mods":
                if(!sender.hasPermission(configManager.modsCommandPermission)){
                    sender.sendMessage(configManager.noPermissionMessage);
                    return true;
                }
                if(!configManager.modsCommandEnabled){
                    sender.sendMessage(configManager.commandNotEnabledMessage);
                    return false;
                }
                if(target==null){
                    sender.sendMessage("§4Player not found.");
                    return true;
                }
                List<ForgeMod> mods = ClientManager.getMods(target);
                if(mods.isEmpty()){
                    sender.sendMessage("§4Player has no mods.");
                    return true;
                }
                sender.sendMessage(configManager.broadcastModsJoinMessageMain);
                for(ForgeMod mod : mods)
                    sender.sendMessage(configManager.broadcastModsJoinMessageMod.replace("%mod_name%", mod.getName()).replace("%mod_version%", mod.getVersion()));

                break;
            case "reload":
                if(!sender.hasPermission(configManager.reloadCommandPermission)){
                    sender.sendMessage(configManager.noPermissionMessage);
                    return true;
                }
                if(!configManager.reloadCommandEnabled){
                    sender.sendMessage(configManager.commandNotEnabledMessage);
                    return false;
                }
                ClientManager.getInstance().getConfigManager().reloadAndSave();
                sender.sendMessage("§aSuccessfully reloaded the plugin!");
                break;
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(Arrays.asList("client", "mods", "reload"));
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("client") || subCommand.equals("mods"))
                completions = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }
        completions.removeIf(option -> !option.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        return completions;
    }
}
