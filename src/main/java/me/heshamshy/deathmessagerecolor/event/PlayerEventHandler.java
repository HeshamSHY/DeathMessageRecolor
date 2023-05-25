/*
 *     Death Message Recolor - Simple plugin that allows user to recolor death messages
 *     Copyright (C) 2023  HeshamSHY
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.heshamshy.deathmessagerecolor.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class PlayerEventHandler implements Listener {

    private final Plugin plugin;
    private final ChatColor NAME_COLOR;
    private final ChatColor MESSAGE_COLOR;

    public PlayerEventHandler(Plugin plugin) {
        this.plugin = plugin;

        NAME_COLOR = ChatColor.getByChar(
                plugin.getConfig().getString("message.nameColor").substring(1)
        );
        MESSAGE_COLOR = ChatColor.getByChar(
                plugin.getConfig().getString("message.color").substring(1)
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final String deathMessage = event.getDeathMessage();
        event.setDeathMessage(
                colorDeathMessage(
                        deathMessage,
                        event.getEntity(),
                        plugin.getConfig().getBoolean("message.colorDeadOnly")
                )
        );
    }

    private String colorDeathMessage(String message, Player player, boolean colorDeadOnly) {
        StringBuilder messageToSend = new StringBuilder();

        if (colorDeadOnly) {

            for (String messageArg : message.split("\\s")) {
                if (messageArg.equals(player.getName())) messageToSend.append(ChatColor.RESET).append(NAME_COLOR).append(messageArg);
                else messageToSend.append(ChatColor.RESET).append(MESSAGE_COLOR).append(messageArg);
            }

        } else {

            for (String messageArg : message.split("\\s")) {

                boolean isPlayerName = false;
                for (Player serverPlayer : plugin.getServer().getOnlinePlayers()) {
                    isPlayerName = messageArg.equals(serverPlayer.getName());
                }

                if (isPlayerName) messageToSend.append(ChatColor.RESET).append(NAME_COLOR).append(messageArg);
                else messageToSend.append(ChatColor.RESET).append(MESSAGE_COLOR).append(messageArg);
            }

        }

        return messageToSend.toString();
    }
}
