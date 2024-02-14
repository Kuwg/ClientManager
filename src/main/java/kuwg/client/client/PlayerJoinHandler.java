package kuwg.client.client;

import kuwg.client.ClientManager;
import kuwg.client.mod.ForgeMod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PlayerJoinHandler {
    private static final ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void handlePlayer(final Player player, String client){
        if(ClientManager.getInstance().getConfigManager().broadcastAsync)
            pool.execute(() -> handle(player, client));
        else handle(player, client);
    }
    private static void alert(final String msg){
        Bukkit.getOnlinePlayers().stream().filter((p) -> p.hasPermission(ClientManager.getInstance().getConfigManager().broadcastPermission)).forEach((p) ->
            p.sendMessage(msg));
    }
    private static void kick(Player player, final String msg){
        Bukkit.getScheduler().runTask(ClientManager.getInstance(), ()->player.kickPlayer(msg));
    }
    private static void handle(final Player player, String client){
        final List<ForgeMod> mods = ClientManager.getMods(player);
        if (ClientManager.getInstance().getConfigManager().broadcastClient) {
            alert(
                    ClientManager.getInstance().getConfigManager().broadcastClientJoinMessage
                            .replace("%player%", player.getDisplayName())
                            .replace("%client%", client)
            );
        }
        final boolean flagClient = ClientManager.getInstance().getConfigManager().bannedClients.contains(client.toLowerCase());
        final boolean notBypassClient = !player.hasPermission(ClientManager.getInstance().getConfigManager().staffBypassClientPermission) || !ClientManager.getInstance().getConfigManager().canStaffBypassClient;
        if(ClientManager.getInstance().getConfigManager().alertStaffPreKickClient){
            if (notBypassClient && flagClient) {
                alert(ClientManager.getInstance().getConfigManager().alertBannedClientPreKickMessage
                        .replace("%player%", player.getDisplayName())
                        .replace("%client%", client));
            }
        }

        if (player.isOnline()&&ClientManager.getInstance().getConfigManager().kickBannedClient) {
            if (notBypassClient && flagClient) {
                kick(player, ClientManager.getInstance().getConfigManager().kickBannedClientMessage);
                alert(ClientManager.getInstance().getConfigManager().alertBannedClientStaffMessage
                        .replace("%player%", player.getDisplayName())
                        .replace("%client%", client));
                return;
            }
        }
        if (!mods.isEmpty()) {
            final boolean notBypassMod = !player.hasPermission(ClientManager.getInstance().getConfigManager().staffBypassModPermission)|| !ClientManager.getInstance().getConfigManager().canStaffBypassMod;
            final Optional<ForgeMod> bannedMod = mods.stream()
                    .filter(mod -> ClientManager.getInstance().getConfigManager().bannedMods.contains(mod.getName().toLowerCase()))
                    .findFirst();
            if(ClientManager.getInstance().getConfigManager().alertStaffPreKickMod)
                if(notBypassMod)
                    bannedMod.ifPresent(forgeMod -> alert(ClientManager.getInstance().getConfigManager().alertBannedModPreKickMessage.replace("%player%",
                                    player.getDisplayName())
                            .replace("%mod%", forgeMod.getName())));
            if(ClientManager.getInstance().getConfigManager().kickBannedMod)
                if (notBypassMod)
                    if (bannedMod.isPresent()) {
                        if (ClientManager.getInstance().getConfigManager().alertStaffClientKickMod) {
                            alert(ClientManager.getInstance().getConfigManager().alertBannedModStaffMessage.replace("%player%", player.getDisplayName())
                                    .replace("%mod%", bannedMod.get().getName()));
                        }
                        kick(player, ClientManager.getInstance().getConfigManager().kickBannedModMessage.replace("%mod%", bannedMod.get().getName()));
                        return;
                    }


            if (!client.contains("forge") && !client.equals(ClientManager.getInstance().getConfigManager().undefinedClient) && ClientManager.getInstance().getConfigManager().alertStrangeClientBrand) {
                alert(
                        ClientManager.getInstance().getConfigManager().strangeClientBrand
                                .replace("%player%", player.getDisplayName())
                                .replace("%client%", client)
                );
            }

            if(ClientManager.getInstance().getConfigManager().broadcastMods)
                alert(Optional.ofNullable(ClientManager.getInstance().getConfigManager().broadcastModsJoinMessageMain)
                        .map(mainMessage -> mods.stream()
                                .map(mod -> ClientManager.getInstance().getConfigManager().broadcastModsJoinMessageMod
                                        .replace("%mod_name%", mod.getName())
                                        .replace("%mod_version%", mod.getVersion()))
                                .collect(Collectors.joining("\n", mainMessage + "\n", "")))
                        .orElse("null"));


        }
    }
}
