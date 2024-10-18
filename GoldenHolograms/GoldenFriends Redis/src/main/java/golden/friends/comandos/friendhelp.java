package golden.friends.comandos;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import golden.friends.utils.Color;

public class friendhelp {

    private final String prefix;

    public friendhelp(String prefix) {
        this.prefix = prefix;
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            TextComponent textComponent = new TextComponent(Color.translate("&dBienvenido! Si quieres utilizar el sistema de amigos de GoldenFriends y no sabes cómo, aquí te &ddejo unos comandos que te ayudarán\n"));

            String[] comandos = {"/friend list", "/friend add", "/friend remove", "/friend msg", "/friend deny", "/friend accept", "/friend globalmsg"};

            for (String comando : comandos) {

                String ColorComando = Color.translate("-" + "&7" + comando);

                TextComponent comandoComponent = new TextComponent(ColorComando + "\n");

                switch (comando.toLowerCase()) {
                    case "/friend list":
                        comandoComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Color.translate("&aMuestra tu lista de amigos y a todos tus amigos online!"))}));
                        break;
                    case "/friend add":
                        comandoComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Color.translate("&aAñade a un jugador a tu lista de amigos!"))}));
                        break;
                    case"/friend remove":
                        comandoComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Color.translate("&aBorra de tu lista de amigos a un amigo en especial!"))}));
                        break;
                    case"/friend msg":
                        comandoComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Color.translate("&aManda un mensaje personal para un amigo en especial!"))}));
                        break;
                    case"/friend globalmsg":
                        comandoComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Color.translate("&aManda un mensaje global para todos tus amigos conectados!"))}));
                        break;
                    case"/friend accept":
                        comandoComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Color.translate("&aAcepta una solicitud de amistad"))}));
                        break;
                    case"/friend deny":
                        comandoComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(Color.translate("&aDeniega una solicitud de amistad"))}));
                        break;
                }
                comandoComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, comando));

                textComponent.addExtra(comandoComponent);
            }
            Player player = (Player) sender;
            player.spigot().sendMessage(textComponent);
        } else {
            sender.sendMessage(prefix + " Solo los jugadores pueden usar este comando en el juego.");
        }
        return true;
    }
}
