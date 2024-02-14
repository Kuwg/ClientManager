package kuwg.client;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private FileConfiguration config;
    private final ClientManager plugin;
    public ConfigManager(ClientManager plugin){
        this.plugin=plugin;
        this.plugin.saveDefaultConfig();
        config=this.plugin.getConfig();
    }
    public FileConfiguration getConfig(){
        return config;
    }
    public void reloadAndSave(){
        this.plugin.reloadConfig();
    }
}
