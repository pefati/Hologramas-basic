package golden.holograms;

import golden.holograms.commands.HoloCommands;
import golden.holograms.events.HologramChecker;
import golden.holograms.utils.HologramStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class GoldenHolograms extends JavaPlugin {

    private HologramStorage hologramStorage;

    @Override
    public void onEnable() {
        hologramStorage = new HologramStorage(this);
        HoloCommands holoCommands = new HoloCommands(hologramStorage);

        getCommand("holo").setExecutor(holoCommands);

        new HologramChecker(hologramStorage).start(this);

        getLogger().info("El plugin de GoldenHolograms fue iniciado con éxito en Bukkit");
    }
}
