package golden.friends.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;

import java.util.UUID;

public class friendaccept implements CommandExecutor {

    private final friendmanager friendManager;
    private final String prefix;

    public friendaccept(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, args);
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate("&cEste comando solo puede ser ejecutado por jugadores."));
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            sender.sendMessage(Color.translate(prefix + " &cDebes proporcionar el nombre del jugador cuya solicitud de amistad quieres aceptar."));
            return true;
        }

        Player senderPlayer = player.getServer().getPlayer(args[1]);

        if (senderPlayer == null || !senderPlayer.isOnline()) {
            sender.sendMessage(Color.translate(prefix + " &cNo se puede encontrar al jugador " + args[1] + " en línea."));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        UUID senderUUID = senderPlayer.getUniqueId();

        if (friendManager.hasFriendRequest(senderUUID, playerUUID)) {
            if (friendManager.getFriends(playerUUID).contains(senderUUID.toString())) {
                sender.sendMessage(Color.translate(prefix + " &c" + senderPlayer.getName() + " ya es tu amigo."));
            } else {
                friendManager.addFriend(senderUUID, playerUUID);

                senderPlayer.sendMessage(Color.translate(prefix + " &aTu solicitud de amistad a " + player.getName() + " fue aceptada."));
                friendManager.removeFriendRequest(senderUUID, playerUUID);

                sender.sendMessage(Color.translate(prefix + " &aHas aceptado la solicitud de amistad de " + senderPlayer.getName()));
            }
        } else {
            sender.sendMessage(Color.translate(prefix + " &cNo hay ninguna solicitud de amistad pendiente de " + senderPlayer.getName()));
        }

        return true;
    }
}
