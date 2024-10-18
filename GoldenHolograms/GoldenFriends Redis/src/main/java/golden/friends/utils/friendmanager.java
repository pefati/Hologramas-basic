package golden.friends.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class friendmanager {

    private final File profilesFolder;
    private final File userFolder;
    private final Map<UUID, Map<UUID, Boolean>> friendRequests;
    private final String prefix;

    public friendmanager(File profilesFolder, String prefix) {
        this.profilesFolder = profilesFolder;
        this.friendRequests = new HashMap<>();
        this.prefix = prefix;

        if (!this.profilesFolder.exists()) {
            this.profilesFolder.mkdirs();
        }

        this.userFolder = new File(this.profilesFolder, "players");
        if (!this.userFolder.exists()) {
            this.userFolder.mkdirs();
        }
    }

    private File getUserFile(UUID playerUUID) {
        return new File(userFolder, playerUUID.toString() + ".yml");
    }

    private FileConfiguration getUserConfig(UUID playerUUID) {
        File userFile = getUserFile(playerUUID);
        return YamlConfiguration.loadConfiguration(userFile);
    }


    private void saveUserConfig(UUID playerUUID, FileConfiguration config) {
        try {
            File userFile = getUserFile(playerUUID);
            config.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getShowFriendEntry(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        return config.getBoolean("showFriendEntry", true);
    }

    public boolean toggleShowFriendEntry(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        boolean currentSetting = config.getBoolean("showFriendEntry", true);
        config.set("showFriendEntry", !currentSetting);
        saveUserConfig(playerUUID, config);
        return !currentSetting;
    }

    public boolean getShowMyEntry(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        return config.getBoolean("showMyEntry", true);
    }

    public boolean toggleShowMyEntry(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        boolean currentSetting = config.getBoolean("showMyEntry", true);
        config.set("showMyEntry", !currentSetting);
        saveUserConfig(playerUUID, config);
        return !currentSetting;
    }

    public boolean getShowFriendExit(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        return config.getBoolean("showFriendExit", true);
    }

    public boolean toggleShowFriendExit(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        boolean currentSetting = config.getBoolean("showFriendExit", true);
        config.set("showFriendExit", !currentSetting);
        saveUserConfig(playerUUID, config);
        return !currentSetting;
    }

    public boolean getShowMyExit(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        return config.getBoolean("showMyExit", true);
    }

    public boolean toggleShowMyExit(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        boolean currentSetting = config.getBoolean("showMyExit", true);
        config.set("showMyExit", !currentSetting);
        saveUserConfig(playerUUID, config);
        return !currentSetting;
    }
    public List<String> getFriends(UUID playerUUID) {
        FileConfiguration config = getUserConfig(playerUUID);
        return config.getStringList("friends");
    }

    private void updateFriends(UUID playerUUID, List<String> friends) {
        FileConfiguration config = getUserConfig(playerUUID);
        config.set("friends", friends);
        saveUserConfig(playerUUID, config);
    }

    public void addFriend(UUID playerUUID, UUID friendUUID) {
        List<String> playerFriends = getFriends(playerUUID);
        List<String> friendFriends = getFriends(friendUUID);

        if (!playerFriends.contains(friendUUID.toString())) {
            playerFriends.add(friendUUID.toString());
            updateFriends(playerUUID, playerFriends);
        }

        if (!friendFriends.contains(playerUUID.toString())) {
            friendFriends.add(playerUUID.toString());
            updateFriends(friendUUID, friendFriends);
        }
    }

    public void removeFriend(UUID playerUUID, UUID friendUUID) {
        List<String> playerFriends = getFriends(playerUUID);
        List<String> friendFriends = getFriends(friendUUID);

        if (playerFriends.contains(friendUUID.toString()) && friendFriends.contains(playerUUID.toString())) {
            playerFriends.remove(friendUUID.toString());
            updateFriends(playerUUID, playerFriends);

            friendFriends.remove(playerUUID.toString());
            updateFriends(friendUUID, friendFriends);
        }
    }

    public void removeFriendRequest(UUID senderUUID, UUID targetUUID) {
        friendRequests.getOrDefault(targetUUID, new HashMap<>()).remove(senderUUID);
    }

    public void sendFriendRequest(Player sender, Player target) {
        UUID senderUUID = sender.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        friendRequests.computeIfAbsent(targetUUID, k -> new HashMap<>()).put(senderUUID, true);

        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', prefix + " &aHas enviado una solicitud de amistad a &a" + target.getName() + ". "));
        TextComponent acceptButton = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&a[Aceptar]"));
        TextComponent rejectButton = new TextComponent(ChatColor.translateAlternateColorCodes('&', " &c[Rechazar]"));

        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + sender.getName()));
        rejectButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + sender.getName()));

        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aAceptar la solicitud de amistad"))}));
        rejectButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cRechazar la solicitud de amistad"))}));

        message.addExtra(acceptButton);
        message.addExtra(rejectButton);

        target.spigot().sendMessage(message);
    }

    public boolean hasFriendRequest(UUID senderUUID, UUID targetUUID) {
        return friendRequests.getOrDefault(targetUUID, new HashMap<>()).containsKey(senderUUID);
    }

    public UUID getUUIDFromName(String playerName) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equalsIgnoreCase(playerName)) {
                return onlinePlayer.getUniqueId();
            }
        }

        File[] userFiles = userFolder.listFiles();
        if (userFiles != null) {
            for (File userFile : userFiles) {
                UUID userUUID;
                try {
                    userUUID = UUID.fromString(userFile.getName().replace(".yml", ""));
                } catch (IllegalArgumentException e) {
                    continue;
                }

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userUUID);
                if (offlinePlayer.hasPlayedBefore() && offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(playerName)) {
                    return userUUID;
                }
            }
        }

        return null;
    }

    public List<String> getFriendsList(UUID playerUUID) {
        return getFriends(playerUUID);
    }

    public boolean areFriends(UUID player1, UUID player2) {
        List<String> friendsList1 = getFriendsList(player1);
        List<String> friendsList2 = getFriendsList(player2);

        return friendsList1.contains(player2.toString()) && friendsList2.contains(player1.toString());
    }

    public boolean isPlayerOnline(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        return player != null && player.isOnline();
    }

    public String getFriendName(UUID friendUUID) {
        Player friendPlayer = Bukkit.getPlayer(friendUUID);
        if (friendPlayer != null && friendPlayer.isOnline()) {
            return ChatColor.GREEN + friendPlayer.getName();
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(friendUUID);
        if (offlinePlayer.hasPlayedBefore()) {
            return ChatColor.RED + offlinePlayer.getName();
        }

        return null;
    }
}