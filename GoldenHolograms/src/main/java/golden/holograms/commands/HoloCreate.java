package golden.holograms.commands;

import golden.holograms.utils.Color;
import golden.holograms.utils.HologramStorage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class HoloCreate {

    private final HoloCommands holoCommands;
    private final HologramStorage hologramStorage;

    public HoloCreate(HoloCommands holoCommands, HologramStorage hologramStorage) {
        this.holoCommands = holoCommands;
        this.hologramStorage = hologramStorage;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.translate("&cSolo los jugadores pueden usar este comando."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 3) {
            player.sendMessage(Color.translate("&cUso incorrecto: /holo create <nombre> <texto>"));
            return true;
        }

        String name = args[1];

        if (hologramStorage.getHolograms().containsKey(name)) {
            player.sendMessage(Color.translate("&cYa existe un holograma con el nombre '" + name + "'."));
            return true;
        }

        String text = String.join(" ", args).substring(args[0].length() + args[1].length() + 2);

        Location location = player.getLocation();
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCustomName(Color.translate(text));
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setMarker(true);

        hologramStorage.saveHologram(name, armorStand);

        player.sendMessage(Color.translate("&aHolograma '" + name + "' creado con éxito."));
        return true;
    }
}