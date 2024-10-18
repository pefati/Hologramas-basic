package golden.holograms.commands;

import golden.holograms.utils.Color;
import golden.holograms.utils.HologramStorage;
import org.bukkit.command.CommandSender;

public class HoloList {

    private final HologramStorage hologramStorage;

    public HoloList(HologramStorage hologramStorage) {
        this.hologramStorage = hologramStorage;
    }

    public boolean execute(CommandSender sender) {
        if (hologramStorage.getHolograms().isEmpty()) {
            sender.sendMessage(Color.translate("&cNo hay hologramas creados."));
            return true;
        }

        sender.sendMessage(Color.translate("&aLista de hologramas:"));
        for (String name : hologramStorage.getHolograms().keySet()) {
            sender.sendMessage(Color.translate("&e- " + name));
        }

        return true;
    }
}
