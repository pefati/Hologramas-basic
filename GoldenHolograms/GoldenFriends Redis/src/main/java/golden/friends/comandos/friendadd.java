package golden.friends.comandos;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;

import java.util.UUID;

public class friendadd {

    private final friendmanager friendManager;
    private final String prefix;

    public friendadd(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate("&cEste comando solo puede ser ejecutado por jugadores."));
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            sender.sendMessage(Color.translate(prefix + " &cDebes proporcionar el nombre del jugador al que quieres agregar como amigo."));
            return true;
        }

        Player targetPlayer = player.getServer().getPlayer(args[1]);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sender.sendMessage(Color.translate(prefix + " &cNo se puede encontrar al jugador " + args[1] + " en línea."));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        UUID friendUUID = targetPlayer.getUniqueId();

        if (playerUUID.equals(friendUUID)) {
            sender.sendMessage(Color.translate(prefix + " &cNo puedes agregarte a ti mismo como amigo."));
            return true;
        }

        if (friendManager.getFriends(playerUUID).contains(friendUUID.toString())) {
            sender.sendMessage(Color.translate(prefix + " &c" + targetPlayer.getName() + " ya es tu amigo."));
            return true;
        }

        friendManager.sendFriendRequest(player, targetPlayer);

        sender.sendMessage(Color.translate(prefix + " &aHas enviado una solicitud de amistad a " + targetPlayer.getName()));
        return true;
    }
}
