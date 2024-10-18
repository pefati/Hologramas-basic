package golden.friends.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;

import java.util.List;
import java.util.UUID;

public class friendglobalmsg implements CommandExecutor {

    private final friendmanager friendManager;
    private final String prefix;

    public friendglobalmsg(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, args);
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate(prefix + " &cEste comando solo puede ser ejecutado por jugadores."));
            return false;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (args.length < 2) {
            sender.sendMessage(Color.translate(prefix + " &cDebes proporcionar el mensaje que deseas enviar a tus amigos."));
            return true;
        }

        String message = String.join(" ", args).substring(10);

        List<String> friends = friendManager.getFriends(playerUUID);

        if (friends.isEmpty()) {
            sender.sendMessage(Color.translate(prefix + " &cNo tienes amigos en línea."));
            return true;
        }

        sender.sendMessage(Color.translate(prefix + " &aMensaje enviado a tus amigos: " + message));

        for (String friend : friends) {
            UUID friendUUID;
            try {
                friendUUID = UUID.fromString(friend);
            } catch (IllegalArgumentException e) {
                continue;
            }

            Player friendPlayer = player.getServer().getPlayer(friendUUID);

            if (friendPlayer != null && friendPlayer.isOnline()) {
                friendPlayer.sendMessage(Color.translate(prefix + " &eMensaje de " + player.getName() + ": " + message));
            }
        }

        return true;
    }
}
