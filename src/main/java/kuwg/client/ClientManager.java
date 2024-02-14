package kuwg.client;

import kuwg.client.command.ClientCommand;
import kuwg.client.config.ConfigManager;
import kuwg.client.mod.ForgeMod;
import kuwg.client.client.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientManager extends JavaPlugin {
    private static final Map<Player, String> playerClientMap = new HashMap<>();
    private static final Map<Player, List<ForgeMod>> playerModsMap = new HashMap<>();
    private static ClientManager instance;
    private ConfigManager configManager;
    @Override
    public void onEnable() {
        instance=this;
        configManager=new ConfigManager(instance);
        configManager.reloadAndSave();
        final MessageListener listener = new MessageListener();
        getServer().getMessenger().registerIncomingPluginChannel(this, "MC|Brand", listener);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "FML");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "FML|HS");

        getServer().getMessenger().registerIncomingPluginChannel(this, "FML", listener);
        getServer().getMessenger().registerIncomingPluginChannel(this, "FML|HS", listener);
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event){
                listener.onPlayerJoin(event.getPlayer());
                Bukkit.getOnlinePlayers().stream().filter((p) ->
                        !p.hasPermission(getConfigManager().broadcastPermission)).forEach((p) ->
                        p.sendMessage(event.getJoinMessage()));
                event.setJoinMessage("");
            }
            @EventHandler
            public void onQuit(PlayerQuitEvent event){
                remove(event.getPlayer());
            }
        }, this);

        final ClientCommand cmd = new ClientCommand();
        getCommand("clientmanager").setExecutor(cmd);
        getCommand("clientmanager").setTabCompleter(cmd);
    }

    public static void setClient(Player player, String client){
        playerClientMap.put(player, client);
    }
    public static void remove(Player player){
        playerClientMap.remove(player);
        playerModsMap.remove(player);
    }
    public static String getClient(Player player){
        return playerClientMap.getOrDefault(player, getInstance().getConfigManager().undefinedClient);
    }
    public static List<ForgeMod> getMods(Player player){
        return playerModsMap.getOrDefault(player, new ArrayList<>());
    }
    public static void setMods(Player player, List<ForgeMod> mods){
        playerModsMap.put(player, mods);
    }
    public static ClientManager getInstance(){
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
