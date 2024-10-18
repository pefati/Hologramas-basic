package golden.holograms.commands;

import golden.holograms.utils.Color;
import golden.holograms.utils.HologramStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class HoloRemove {

    private final HoloCommands holoCommands;
    private final HologramStorage hologramStorage;

    public HoloRemove(HoloCommands holoCommands, HologramStorage hologramStorage) {
        this.holoCommands = holoCommands;
        this.hologramStorage = hologramStorage;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate("&cSolo los jugadores pueden usar este comando."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(Color.translate("&cUso incorrecto: /holo remove <nombre>"));
            return true;
        }

        String name = args[1];

        ArmorStand armorStand = hologramStorage.getHolograms().get(name);
        if (armorStand != null) {
            armorStand.remove();
            hologramStorage.removeHologram(name);
            player.sendMessage(Color.translate("&aHolograma '" + name + "' eliminado con éxito."));
        } else {
            player.sendMessage(Color.translate("&cNo se encontró ningún holograma con el nombre '" + name + "'."));
        }
        return true;
    }
}
