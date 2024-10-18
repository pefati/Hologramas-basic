package golden.friends.eventos;

import golden.friends.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;

import java.util.List;
import java.util.UUID;

public class leavefriend implements Listener {

    private final friendmanager friendManager;
    private final String prefix;

    public leavefriend(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        List<String> friendsList = friendManager.getFriends(event.getPlayer().getUniqueId());
        if (friendsList != null) {
            friendsList.forEach(friendUUID -> {
                if (friendUUID != null) {
                    UUID onlineFriendUUID = UUID.fromString(friendUUID);
                    if (friendManager.isPlayerOnline(onlineFriendUUID)) {
                        Player onlineFriend = Bukkit.getPlayer(onlineFriendUUID);

                        boolean showMyExit = friendManager.getShowMyExit(event.getPlayer().getUniqueId());
                        boolean showFriendExit = friendManager.getShowFriendExit(onlineFriendUUID);

                        if (showMyExit && showFriendExit) {
                            onlineFriend.sendMessage(Color.translate(prefix + " &cTu amigo " + event.getPlayer().getName() + " &cse fue del servidor."));
                        }
                    }
                }
            });
        }
    }
}
