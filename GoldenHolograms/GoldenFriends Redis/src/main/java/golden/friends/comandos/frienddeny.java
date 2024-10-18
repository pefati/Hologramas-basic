package golden.friends.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;

import java.util.UUID;

public class frienddeny implements CommandExecutor {

    private final friendmanager friendManager;
    private final String prefix;

    public frienddeny(friendmanager friendManager, String prefix) {
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
            sender.sendMessage(Color.translate(prefix + " &cDebes proporcionar el nombre del jugador cuya solicitud de amistad quieres rechazar."));
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
            friendManager.removeFriendRequest(senderUUID, playerUUID);

            senderPlayer.sendMessage(Color.translate(prefix + " &cTu solicitud de amistad a " + player.getName() + " fue rechazada."));

            sender.sendMessage(Color.translate(prefix + " &cHas rechazado la solicitud de amistad de " + senderPlayer.getName()));
        } else {
            sender.sendMessage(Color.translate(prefix + " &cNo hay ninguna solicitud de amistad pendiente de " + senderPlayer.getName()));
        }

        return true;
    }
}
