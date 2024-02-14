package kuwg.client.config;

import kuwg.client.ClientManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    private final ClientManager plugin;

    // Lists
    public List<String> bannedClients;
    public List<String> bannedMods;

    // Booleans
    public boolean broadcastMods;
    public boolean broadcastClient;
    public boolean broadcastAsync;
    public boolean kickBannedMod;
    public boolean kickBannedClient;
    public boolean generalCommandEnabled;
    public boolean clientCommandEnabled;
    public boolean modsCommandEnabled;
    public boolean reloadCommandEnabled;
    public boolean alertStrangeClientBrand;
    public boolean removeJoinGameMessage;
    public boolean alertStaffClientKickClient;
    public boolean alertStaffClientKickMod;
    public boolean canStaffBypassClient;
    public boolean canStaffBypassMod;
    public boolean alertStaffPreKickMod;
    public boolean alertStaffPreKickClient;

    // Strings
    public String kickBannedClientMessage;
    public String kickBannedModMessage;
    public String broadcastModsJoinMessageMod;
    public String broadcastModsJoinMessageMain;
    public String broadcastClientJoinMessage;
    public String broadcastPermission;
    public String undefinedClient;
    public String commandNotEnabledMessage;
    public String defaultCommandPerm;
    public String noPermissionMessage;
    public String clientCommandPermission;
    public String modsCommandPermission;
    public String reloadCommandPermission;
    public String strangeClientBrand;
    public String alertBannedClientStaffMessage;
    public String alertBannedModStaffMessage;
    public String staffBypassModPermission;
    public String staffBypassClientPermission;
    public String alertBannedModPreKickMessage;
    public String alertBannedClientPreKickMessage;

    public ConfigManager(ClientManager plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
        reloadAndSave();
    }

    public void reloadAndSave() {
        this.plugin.reloadConfig();
        FileConfiguration config = this.plugin.getConfig();
        this.generalCommandEnabled = config.getBoolean("commands.general-command-enabled", true);
        this.clientCommandEnabled = config.getBoolean("commands.enable-client-command", true);
        this.reloadCommandEnabled = config.getBoolean("commands.enable-reload-command", true);
        this.modsCommandEnabled = config.getBoolean("commands.enable-mods-command", true);
        this.commandNotEnabledMessage = config.getString("commands.command-not-enabled-message", "§4This command is not enabled!");
        this.defaultCommandPerm = config.getString("commands.default-perm", "cmanager.command.default");
        this.noPermissionMessage = config.getString("commands.no-permission-message", "§4You cannot execute this command!");
        this.clientCommandPermission = config.getString("commands.command-client-permission", "cmanager.command.client");
        this.modsCommandPermission = config.getString("commands.command-mods-permission", "cmanager.command.mods");
        this.reloadCommandPermission = config.getString("commands.command-reload-permission", "cmanager.command.reload");

        this.undefinedClient = config.getString("undefined-client", "undefined");
        this.kickBannedClient = config.getBoolean("kick-banned-client", true);
        this.kickBannedMod = config.getBoolean("kick-banned-mod", true);
        this.bannedClients = config.getStringList("banned-clients");
        this.bannedMods = config.getStringList("banned-mods");

        this.removeJoinGameMessage = config.getBoolean("broadcast-on-join.remove-default-join-message", true);
        this.broadcastClient = config.getBoolean("broadcast-on-join.broadcast-client", true);
        this.broadcastMods = config.getBoolean("broadcast-on-join.broadcast-mods", true);
        this.broadcastPermission = config.getString("broadcast-on-join.permission", "cmanager.join");
        this.broadcastClientJoinMessage = config.getString("broadcast-on-join.client-join-message", "§e%player% has joined with a powerful §b§l%client%§r§e client.");
        this.broadcastModsJoinMessageMain = config.getString("broadcast-on-join.mods-join-message-main", "§aLoaded Mods: ");
        this.broadcastModsJoinMessageMod = config.getString("broadcast-on-join.mods-join-message-mod", "    §4- §dMod: \"§b%mod_name%§d\" Version: \"§b%mod_version%§d\".");
        this.broadcastAsync = config.getBoolean("broadcast-on-join.async", true);
        this.strangeClientBrand = config.getString("broadcast-on-join.strange-client-brand-message", "§cPlayer §e%player%§c might have altered their client brand. Client: §b%client%§c, expected forge.");
        this.alertStrangeClientBrand = config.getBoolean("broadcast-on-join.alert-strange-client-brand", true);

        this.kickBannedClientMessage = config.getString("kick-banned-client-message", "§4You have been expelled for wielding forbidden power.");
        this.kickBannedModMessage = config.getString("kick-banned-mod-message", "§4The forbidden mod %mod% has been detected on your device.");
        this.alertStaffClientKickClient = config.getBoolean("alert-staff-on-client-kick", true);
        this.alertStaffClientKickMod = config.getBoolean("alert-staff-on-mod-kick", true);
        this.alertBannedClientStaffMessage = config.getString("kick-banned-client-staff-message", "§cPlayer §e%player%§c was cast out for wielding a forbidden client: §b%client%§c.");
        this.alertBannedModStaffMessage = config.getString("kick-banned-mod-staff-message", "§cPlayer §e%player%§c was banished for harboring a forbidden mod: §b%mod%§c.");
        this.canStaffBypassClient = config.getBoolean("staff-bypass-client-ban", true);
        this.canStaffBypassMod = config.getBoolean("staff-bypass-mod-ban", true);
        this.staffBypassClientPermission = config.getString("banned-client-bypass-permission", "cmanager.bypass.client");
        this.staffBypassModPermission = config.getString("banned-mod-bypass-permission", "cmanager.bypass.mod");
        this.alertStaffPreKickClient = config.getBoolean("alert-staff-banned-client", true);
        this.alertStaffPreKickMod = config.getBoolean("alert-staff-banned-mod", true);
        this.alertBannedClientPreKickMessage = config.getString("alert-banned-client-message", "§cPlayer §e%player%§c joined with a forbidden client: §b%client%§c.");
        this.alertBannedModPreKickMessage = config.getString("alert-banned-mod-message", "§cPlayer §e%player%§c joined with a forbidden mod: §b%mod%§c.");

        // Setting lists to lower case
        bannedClients.replaceAll(String::toLowerCase);
        bannedMods.replaceAll(String::toLowerCase);
    }
}
