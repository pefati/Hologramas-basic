package golden.friends.comandos;

import golden.friends.Friends;
import golden.friends.utils.Color;
import golden.friends.utils.friendmanager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class friendsettings implements CommandExecutor, Listener {
    private final friendmanager friendManager;
    private final String prefix;
    public friendsettings(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
        Bukkit.getPluginManager().registerEvents(this, Friends.getProvidingPlugin(Friends.class));
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

        openSettingsMenu(player);
        return true;
    }

    private void openSettingsMenu(Player player) {
        Inventory settingsMenu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Configuración de Amigos");

        boolean showFriendEntry = friendManager.getShowFriendEntry(player.getUniqueId());
        boolean showMyEntry = friendManager.getShowMyEntry(player.getUniqueId());
        boolean showFriendExit = friendManager.getShowFriendExit(player.getUniqueId());
        boolean showMyExit = friendManager.getShowMyExit(player.getUniqueId());

        ItemStack showFriendEntryItem = createToggleItem(showFriendEntry, "Mostrar Entrada de Amigos");
        ItemStack showMyEntryItem = createToggleItem(showMyEntry, "Mostrar Mi Entrada");
        ItemStack showFriendExitItem = createToggleItem(showFriendExit, "Mostrar Salida de Amigos");
        ItemStack showMyExitItem = createToggleItem(showMyExit, "Mostrar Mi Salida");

        settingsMenu.setItem(0, showFriendEntryItem);
        settingsMenu.setItem(1, showMyEntryItem);
        settingsMenu.setItem(2, showFriendExitItem);
        settingsMenu.setItem(3, showMyExitItem);

        player.openInventory(settingsMenu);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), Friends.getProvidingPlugin(Friends.class));
    }

    private ItemStack createToggleItem(boolean status, String displayName) {
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH_ON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + displayName);
        meta.setLore(getToggleStatusLore(status));
        item.setItemMeta(meta);
        return item;
    }

    private List<String> getToggleStatusLore(boolean status) {
        List<String> lore = new ArrayList<>();
        lore.add(status ? ChatColor.GREEN + "Habilitado" : ChatColor.RED + "Deshabilitado");
        return lore;
    }

    private class ClickListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getInventory().getTitle().equals(ChatColor.BLUE + "Configuración de Amigos")) {
                event.setCancelled(true);

                if (event.getCurrentItem() != null) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack clickedItem = event.getCurrentItem();

                    if (clickedItem.getType() == Material.REDSTONE_TORCH_ON) {
                        toggleSetting(player, clickedItem);
                        player.closeInventory();
                        HandlerList.unregisterAll(this);
                    }
                }
            }
        }
    }

    private void toggleSetting(Player player, ItemStack clickedItem) {
        ItemMeta itemMeta = clickedItem.getItemMeta();

        if (itemMeta != null) {
            String displayName = ChatColor.stripColor(itemMeta.getDisplayName());

            if (displayName.equals("Mostrar Entrada de Amigos")) {
                boolean showFriendEntry = friendManager.toggleShowFriendEntry(player.getUniqueId());
                itemMeta.setLore(getToggleStatusLore(showFriendEntry));
            } else if (displayName.equals("Mostrar Mi Entrada")) {
                boolean showMyEntry = friendManager.toggleShowMyEntry(player.getUniqueId());
                itemMeta.setLore(getToggleStatusLore(showMyEntry));
            } else if (displayName.equals("Mostrar Salida de Amigos")) {
                boolean showFriendExit = friendManager.toggleShowFriendExit(player.getUniqueId());
                itemMeta.setLore(getToggleStatusLore(showFriendExit));
            } else if (displayName.equals("Mostrar Mi Salida")) {
                boolean showMyExit = friendManager.toggleShowMyExit(player.getUniqueId());
                itemMeta.setLore(getToggleStatusLore(showMyExit));
            }

            clickedItem.setItemMeta(itemMeta);
            player.sendMessage(Color.translate(prefix + " &aConfiguración actualizada."));
        }
    }
}
