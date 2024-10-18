package golden.holograms.commands;

import golden.holograms.utils.HologramStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import golden.holograms.utils.Color;

import java.util.HashMap;
import java.util.Map;

public class HoloCommands implements CommandExecutor {
    private final Map<String, ArmorStand> holograms = new HashMap<>();
    private HologramStorage HologramStorage;
    public Map<String, ArmorStand> getHolograms() {
        return holograms;
    }
    public HoloCommands(HologramStorage HologramStorage) {
        this.HologramStorage = HologramStorage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("holo")) {
            if (args.length == 0) {
                sender.sendMessage(Color.translate("&aUsa /holo help para conseguir ayuda"));
                return true;
            } else if (args[0].equalsIgnoreCase("create")) {
                return new HoloCreate(this, HologramStorage).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("remove")) {
                return new HoloRemove(this, HologramStorage).execute(sender, args);
            } else if (args[0].equalsIgnoreCase("list")) {
                return new HoloList(HologramStorage).execute(sender);
            } else {
                sender.sendMessage(Color.translate("&aUsa /holo help para ver la lista de comandos disponibles"));
                return true;
            }
        }
        return false;
    }
}
