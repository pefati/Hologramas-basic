package golden.friends.eventos;

import golden.friends.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import golden.friends.utils.friendmanager;

import java.util.List;
import java.util.UUID;

public class joinfriend implements Listener {

    private final friendmanager friendManager;
    private final String prefix;

    public joinfriend(friendmanager friendManager, String prefix) {
        this.friendManager = friendManager;
        this.prefix = prefix;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        List<String> friendsList = friendManager.getFriends(joinedPlayer.getUniqueId());

        boolean showMyEntry = friendManager.getShowMyEntry(joinedPlayer.getUniqueId());

        if (friendsList != null) {
            for (String friendUUID : friendsList) {
                if (friendUUID != null) {
                    UUID onlineFriendUUID = UUID.fromString(friendUUID);
                    if (friendManager.isPlayerOnline(onlineFriendUUID)) {
                        Player onlineFriend = Bukkit.getPlayer(onlineFriendUUID);

                        boolean showFriendEntry = friendManager.getShowFriendEntry(onlineFriendUUID);
                        if (showFriendEntry && showMyEntry) {
                            sendJoinMessage(joinedPlayer, onlineFriend);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void sendJoinMessage(Player joinedPlayer, Player onlineFriend) {
        boolean showMyEntry = friendManager.getShowMyEntry(joinedPlayer.getUniqueId());
        if (showMyEntry) {
            onlineFriend.sendMessage(Color.translate(prefix + " &bTu amigo " + joinedPlayer.getName() + " &bse unió al servidor."));
        }
    }
}
