package golden.friends;

import golden.friends.comandos.FriendCommands;
import golden.friends.eventos.joinfriend;
import golden.friends.eventos.leavefriend;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import golden.friends.utils.friendmanager;
import org.bukkit.configuration.file.FileConfiguration;



public class Friends extends JavaPlugin {

    private friendmanager friendManager;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        config = getConfig();

        String prefix = config.getString("prefix");

        this.friendManager = new friendmanager(getDataFolder(), prefix);
        getCommand("friend").setExecutor(new FriendCommands(friendManager, prefix));
        Bukkit.getPluginManager().registerEvents(new joinfriend(friendManager, prefix), this);
        Bukkit.getPluginManager().registerEvents(new leavefriend(friendManager, prefix), this);
        getLogger().info("El plugin de GoldenFriends fue iniciado con éxito");
    }
}
