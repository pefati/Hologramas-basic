package golden.friends.comandos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import golden.friends.utils.friendmanager;
import golden.friends.utils.Color;


public class FriendCommands implements CommandExecutor {

    private final friendmanager friendManager;
    private final String prefix;

    public FriendCommands(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("friend")) {
            if (args.length == 0) {
                sender.sendMessage(Color.translate("&aUsa /friend help para obtener ayuda"));
                return true;
            } else if (args[0].equalsIgnoreCase("add")) {
                return new friendadd(friendManager, prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("help")) {
                return new friendhelp(prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("accept")) {
                return new friendaccept(friendManager, prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("deny")) {
                return new frienddeny(friendManager, prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("remove")) {
                return new friendremove(friendManager, prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("list")) {
                return new friendlist(friendManager, prefix).execute(sender);
            } else if (args[0].equalsIgnoreCase("msg")) {
                return new friendmsg(friendManager, prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("globalmsg")) {
                return new friendglobalmsg(friendManager, prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("settings")) {
                return new friendsettings(friendManager, prefix).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("menu")) {
                return new friendmenu(friendManager).execute(sender, args);
            } else {
                sender.sendMessage(Color.translate(prefix + " Usa /friend help para ver la lista de comandos disponibles"));
                return true;
            }
        }
        return false;
    }
}
