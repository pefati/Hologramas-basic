package golden.friends.comandos;

import golden.friends.Friends;
import golden.friends.utils.friendmanager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import java.util.UUID;

public class friendmenu implements Listener {
    private final friendmanager friendManager;
    private boolean menuOpen;

    public friendmenu(friendmanager friendManager) {
        this.friendManager = friendManager;
        Bukkit.getPluginManager().registerEvents(this, Friends.getProvidingPlugin(Friends.class));
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!menuOpen) {
                openCustomMenu(player);
                menuOpen = true;
            } else {
                closeCustomMenu(player);
                menuOpen = false;
            }
        }
        return true;
    }

    private void openCustomMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Menú");

        int onlineFriendsCount = getOnlineFriendsCount(player);

        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta skullMeta = skullItem.getItemMeta();
        skullMeta.setDisplayName(ChatColor.GREEN + "Amigos Online (" + onlineFriendsCount + ")");
        skullMeta.setLore(getOnlineFriendsLore(player));
        skullItem.setItemMeta(skullMeta);
        menu.setItem(4, skullItem);

        ItemStack ayudaItem = new ItemStack(Material.SIGN);
        ItemMeta ayudaMeta = ayudaItem.getItemMeta();
        ayudaMeta.setDisplayName(ChatColor.YELLOW + "¿Necesitas ayuda?");
        ayudaItem.setItemMeta(ayudaMeta);
        menu.setItem(0, ayudaItem);

        ItemStack settingsItem = new ItemStack(Material.DIAMOND);
        ItemMeta settingsMeta = settingsItem.getItemMeta();
        settingsMeta.setDisplayName(ChatColor.AQUA + "Settings");
        settingsItem.setItemMeta(settingsMeta);
        menu.setItem(8, settingsItem);

        player.openInventory(menu);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), Friends.getProvidingPlugin(Friends.class));
    }

    private void closeCustomMenu(Player player) {
        player.closeInventory();
    }
    private int getOnlineFriendsCount(Player player) {
        List<String> friendsList = friendManager.getFriendsList(player.getUniqueId());
        int onlineFriendsCount = 0;

        for (String friendUUID : friendsList) {
            UUID uuid = UUID.fromString(friendUUID);
            if (friendManager.isPlayerOnline(uuid)) {
                onlineFriendsCount++;
            }
        }

        return onlineFriendsCount;
    }
    private List<String> getOnlineFriendsLore(Player player) {
        List<String> lore = new ArrayList<>();

        List<String> friendsList = friendManager.getFriendsList(player.getUniqueId());

        for (String friendUUID : friendsList) {
            UUID uuid = UUID.fromString(friendUUID);
            if (friendManager.isPlayerOnline(uuid)) {
                lore.add(ChatColor.GREEN + friendManager.getFriendName(uuid) + ChatColor.RESET);
            }
        }
        if (lore.isEmpty()) {
            lore.add(ChatColor.GRAY + "Ningún amigo en línea");
        }
        return lore;
    }

    private class ClickListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getInventory().getTitle().equals(ChatColor.BLUE + "Menú") && menuOpen) {
                event.setCancelled(true);

                if (event.getCurrentItem() != null) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack clickedItem = event.getCurrentItem();

                    if (clickedItem.getType() == Material.SKULL_ITEM && clickedItem.getItemMeta() != null &&
                            ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Amigos Online")) {
                        closeCustomMenu(player);
                        player.performCommand("friend list");
                    } else if (clickedItem.getType() == Material.SIGN && clickedItem.getItemMeta() != null &&
                            ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("¿Necesitas ayuda?")) {
                        closeCustomMenu(player);
                        player.performCommand("friend help");
                    } else if (clickedItem.getType() == Material.DIAMOND && clickedItem.getItemMeta() != null &&
                            ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Settings")) {
                        closeCustomMenu(player);
                        player.performCommand("friend settings");
                    }
                }
            }
            HandlerList.unregisterAll(this);
        }
    }
}