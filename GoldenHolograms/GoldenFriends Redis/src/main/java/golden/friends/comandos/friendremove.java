package golden.friends.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;

import java.util.UUID;

public class friendremove implements CommandExecutor {

    private final friendmanager friendManager;
    private final String prefix;

    public friendremove(friendmanager friendManager, String prefix) {
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
            sender.sendMessage(Color.translate(prefix + " &cDebes proporcionar el nombre o UUID del jugador que quieres eliminar de tu lista de amigos."));
            return true;
        }

        String targetNameOrUUID = args[1];
        UUID friendUUID = friendManager.getUUIDFromName(targetNameOrUUID);

        if (friendUUID == null) {
            sender.sendMessage(Color.translate(prefix + " &cEl jugador " + targetNameOrUUID + " no está en tu lista de amigos."));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        if (!friendManager.areFriends(playerUUID, friendUUID)) {
            sender.sendMessage(Color.translate(prefix + " &cEl jugador " + targetNameOrUUID + " no está en tu lista de amigos."));
            return true;
        }

        friendManager.removeFriend(playerUUID, friendUUID);

        sender.sendMessage(Color.translate(prefix + " &aHas eliminado a " + targetNameOrUUID + " de tu lista de amigos."));

        Player targetPlayer = player.getServer().getPlayer(friendUUID);
        if (targetPlayer != null && targetPlayer.isOnline()) {
            targetPlayer.sendMessage(Color.translate(prefix + " &c" + player.getName() + " te ha eliminado de su lista de amigos."));
        }
        return true;
    }
}
