package golden.holograms.events;

import golden.holograms.utils.HologramStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class HologramChecker implements Runnable {

    private final HologramStorage hologramStorage;

    public HologramChecker(HologramStorage hologramStorage) {
        this.hologramStorage = hologramStorage;
    }

    @Override
    public void run() {
        List<String> toRemove = new ArrayList<>();

        for (Map.Entry<String, ArmorStand> entry : hologramStorage.getHolograms().entrySet()) {
            ArmorStand armorStand = entry.getValue();
            if (armorStand == null || !armorStand.isValid()) {
                toRemove.add(entry.getKey());
            }
        }

        for (String name : toRemove) {
            hologramStorage.loadHolograms();
        }
    }

    public void start(JavaPlugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 2L);
    }
}
