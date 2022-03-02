package me.saywut.uuidfix;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerJoin implements Listener {

    private UUIDFix plugin;
    public PlayerJoin(UUIDFix plugin) {
        this.plugin = plugin;
    }

    String wrongName = ChatColor.translateAlternateColorCodes('&', UUIDFix.getInstance().getConfig().getString("wrong_name"));
    String webMsg = ChatColor.translateAlternateColorCodes('&', UUIDFix.getInstance().getConfig().getString("web_register_msg"));

    @EventHandler(
            priority = EventPriority.LOW
    )
    public void onPlayerJoin(PreLoginEvent event) throws SQLException {
        if (event.isCancelled()) {
            return;
        }

        String playerName = event.getConnection().getName();

        PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT realname FROM authme WHERE username=?");
        ps.setString(1, playerName.toLowerCase());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String realName = rs.getString("realname");
            if (!realName.equals(playerName)) {
                event.setCancelled(true);
                event.setCancelReason(TextComponent.fromLegacyText(wrongName.replace("%realname%", realName)));
                this.plugin.getLogger().info("Kicked " + playerName + ", real name: " + realName);
                return;
            }
            return;
        }
        if (this.plugin.getConfig().getBoolean("web_register")) {
            event.setCancelled(true);
            event.setCancelReason(TextComponent.fromLegacyText(webMsg));
        }

    }

}
