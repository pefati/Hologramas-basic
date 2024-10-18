package golden.friends.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;

import java.util.UUID;

public class friendmsg implements CommandExecutor {

    private final friendmanager friendManager;
    private final String prefix;

    public friendmsg(friendmanager friendManager, String prefix) {
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
        UUID playerUUID = player.getUniqueId();

        if (args.length < 3) {
            sender.sendMessage(Color.translate(prefix + " &cDebes proporcionar el nombre de tu amigo y el mensaje."));
            return true;
        }

        String friendName = args[1];
        Player friendPlayer = getFriendPlayer(player, friendName);

        if (friendPlayer == null) {
            sender.sendMessage(Color.translate(prefix + " &cEl jugador " + friendName + " no está en línea."));
            return true;
        }

        if (friendPlayer.getUniqueId().equals(playerUUID)) {
            sender.sendMessage(Color.translate(prefix + " &cNo puedes enviarte mensajes a ti mismo."));
            return true;
        }

        if (!friendManager.areFriends(playerUUID, friendPlayer.getUniqueId())) {
            sender.sendMessage(Color.translate(prefix + " &cEl jugador " + friendPlayer.getName() + " no está en tu lista de amigos."));
            return true;
        }

        String message = String.join(" ", args).substring(friendName.length() + 4).trim();

        sender.sendMessage(Color.translate(prefix + " &aMensaje enviado a " + friendPlayer.getName() + ": " + message));
        friendPlayer.sendMessage(Color.translate(prefix + " &eMensaje de " + player.getName() + ": " + message));

        return true;
    }

    private Player getFriendPlayer(Player player, String friendName) {
        UUID friendUUID = friendManager.getUUIDFromName(friendName);

        if (friendUUID != null) {
            return player.getServer().getPlayer(friendUUID);
        }

        return null;
    }
}
