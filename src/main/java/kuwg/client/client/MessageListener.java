package kuwg.client.client;

import kuwg.client.ClientManager;
import kuwg.client.mod.ForgeMod;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel!=null&&channel.equals("MC|Brand")) {
            ClientManager.setClient(player, new String(message).trim());
            new BukkitRunnable() {
                public void run() {
                    PlayerJoinHandler.handlePlayer(player, ClientManager.getClient(player));
                }
            }.runTaskLater(inst, 20L);
        } else if (message[0] == 2) {
            ClientManager.setMods(player, convertToForgeMods(message));
        }
    }

    private List<ForgeMod> convertToForgeMods(byte[] message) {
        List<ForgeMod> mods = new ArrayList<>();
        int length = message.length;
        int index = 2;
        String modName = null;
        boolean isName = true;

        while (index < length) {
            int nameLength = message[index++];
            String entry = new String(message, index, nameLength);

            if (isName) {
                modName = entry;
            } else {
                mods.add(new ForgeMod(modName, entry));
            }

            index += nameLength;
            isName = !isName;
        }
        return mods;
    }

    private static final ClientManager inst = ClientManager.getInstance();
    public void onPlayerJoin(final Player player){
        new BukkitRunnable() {
            public void run() {
                player.getPlayer().sendPluginMessage(inst, "FML|HS", new byte[]{-2,0});
                player.getPlayer().sendPluginMessage(inst, "FML|HS", new byte[]{0,2,0,0,0,0});
                player.getPlayer().sendPluginMessage(inst, "FML|HS", new byte[]{2,0,0,0,0});
            }
        }.runTaskLater(inst, 15L);
    }
}