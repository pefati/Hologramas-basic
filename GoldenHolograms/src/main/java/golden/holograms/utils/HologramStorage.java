package golden.holograms.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HologramStorage {

    private final File file;
    private final FileConfiguration config;

    private final Map<String, ArmorStand> holograms = new HashMap<>();

    public HologramStorage(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "holograms.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadHolograms();
        resetHolograms();
    }

    public void resetHolograms() {
        for (String key : config.getKeys(false)) {
            String worldName = config.getString(key + ".world");
            String text = config.getString(key + ".text");
            double x = config.getDouble(key + ".x");
            double y = config.getDouble(key + ".y");
            double z = config.getDouble(key + ".z");

            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);

            location.getWorld().getEntitiesByClass(ArmorStand.class).forEach(armorStand -> {
                if (armorStand.getCustomName() != null && armorStand.getCustomName().equals(Color.translate(text))) {
                    armorStand.remove();
                }
            });
        }
    }

    public void loadHolograms() {
        holograms.clear();
        for (String key : config.getKeys(false)) {
            String worldName = config.getString(key + ".world");
            double x = config.getDouble(key + ".x");
            double y = config.getDouble(key + ".y");
            double z = config.getDouble(key + ".z");
            String text = config.getString(key + ".text");

            if (text != null) {
                Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
                ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
                armorStand.setCustomName(Color.translate(text));
                armorStand.setCustomNameVisible(true);
                armorStand.setGravity(false);
                armorStand.setVisible(false);
                armorStand.setMarker(true);

                holograms.put(key, armorStand);
            }
        }
    }

    public void saveHologram(String name, ArmorStand armorStand) {
        Location loc = armorStand.getLocation();
        config.set(name + ".world", loc.getWorld().getName());
        config.set(name + ".x", loc.getX());
        config.set(name + ".y", loc.getY());
        config.set(name + ".z", loc.getZ());
        config.set(name + ".text", armorStand.getCustomName());

        holograms.put(name, armorStand);
        saveConfig();
    }

    public void removeHologram(String name) {
        config.set(name, null);
        holograms.remove(name);
        saveConfig();
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, ArmorStand> getHolograms() {
        return holograms;
    }
}
