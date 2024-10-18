package golden.friends.comandos;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;

import java.util.List;
import java.util.UUID;

public class friendlist implements CommandExecutor {

    private final friendmanager friendManager;
    private final String prefix;

    public friendlist(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender);
    }

    public boolean execute(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate("&cEste comando solo puede ser ejecutado por jugadores."));
            return false;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        List<String> friends = friendManager.getFriends(playerUUID);

        if (friends.isEmpty()) {
            sender.sendMessage(Color.translate(prefix + " &cNo tienes amigos, pero puedes hacer nuevos!"));
            return true;
        }

        sender.sendMessage(Color.translate("&aTus amigos:"));

        for (String friend : friends) {
            UUID friendUUID;
            try {
                friendUUID = UUID.fromString(friend);
            } catch (IllegalArgumentException e) {
                continue;
            }

            String friendName = friendManager.getFriendName(friendUUID);

            if (friendName != null && !friendName.isEmpty()) {
                sender.sendMessage(ChatColor.GREEN + friendName);
            } else {
                sender.sendMessage(ChatColor.RED + friend);
            }
        }
        sender.sendMessage(ChatColor.GREEN + "[Online] " + ChatColor.RESET + "| " + ChatColor.RED + "[Offline]");

        return true;
    }
}
