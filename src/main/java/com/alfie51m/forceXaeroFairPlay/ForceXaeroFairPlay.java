package com.alfie51m.forceXaeroFairPlay;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForceXaeroFairPlay extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        // Initialize default configuration file
        saveDefaultConfig();
        
        // Register events and commands
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("fxfp").setExecutor(this);
        getCommand("fxfp").setTabCompleter(this);
        
        getLogger().info("ForceXaeroFairPlay has been enabled with Folia support!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ForceXaeroFairPlay has been disabled!");
    }

    /**
     * Triggered when a player joins the server.
     * Checks and sets the Minimap mode for the world they logged into.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        handlePlayerMode(player, player.getWorld().getName(), null);
    }

    /**
     * Triggered when a player switches worlds.
     * Updates the Minimap mode based on the new world configuration.
     */
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String fromWorld = event.getFrom().getName();
        String toWorld = player.getWorld().getName();

        handlePlayerMode(player, toWorld, fromWorld);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if (sender.hasPermission("forcexaerofairplay.reload")) {
                if ("reload".startsWith(args[0].toLowerCase())) {
                    completions.add("reload");
                }
            }
            return completions;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("forcexaerofairplay.reload")) {
                sender.sendMessage("§cYou do not have permission to do this.");
                return true;
            }

            reloadConfig();
            sender.sendMessage("§aForceXaeroFairPlay config reloaded.");
            return true;
        }

        sender.sendMessage("§eUsage: §6/fxfp reload");
        return true;
    }

    /**
     * Handles logic for determining which Xaero's Minimap mode to force.
     * * @param player The player to apply the mode to.
     * @param toWorldName The name of the world the player is entering.
     * @param fromWorldName The name of the world the player left (null if joining).
     */
    private void handlePlayerMode(Player player, String toWorldName, String fromWorldName) {
        // Skip if player has bypass permission
        if (player.hasPermission("forcexaerofairplay.bypass")) {
            return;
        }

        FileConfiguration config = getConfig();
        String defaultMode = config.getString("defaultMode", "none").toLowerCase();
        
        // Determine mode for the current world, fallback to default if not set
        String toWorldMode = config.getString("worldModes." + toWorldName, defaultMode).toLowerCase();
        
        // Determine mode for the previous world to check if a reset packet is needed
        String fromWorldMode = fromWorldName != null
                ? config.getString("worldModes." + fromWorldName, defaultMode).toLowerCase()
                : "none";

        StringBuilder messageBuilder = new StringBuilder();

        // Send reset packet if the mode changes between worlds
        if (!fromWorldMode.equals(toWorldMode)) {
            messageBuilder.append("§r§e§s§e§t§x§a§e§r§o ");
        }

        // Append the specific mode packet based on Xaero's Minimap protocol
        switch (toWorldMode) {
            case "fairplay":
                messageBuilder.append("§f§a§i§r§x§a§e§r§o");
                break;

            case "fairplay_nether":
                messageBuilder.append("§f§a§i§r§x§a§e§r§o§x§a§e§r§o§w§m§n§e§t§h§e§r§i§s§f§a§i§r§x§a§e§r§o§m§m§n§e§t§h§e§r§i§s§f§a§i§r");
                break;

            case "disabled":
                messageBuilder.append("§n§o§m§i§n§i§m§a§p");
                break;

            case "none":
            default:
                break;
        }

        if (messageBuilder.length() > 0) {
            sendTellraw(player, messageBuilder.toString().trim());
        }
    }

    /**
     * Sends a raw JSON message to the player.
     * * [FOLIA FIX]: 
     * On Folia, events like PlayerJoinEvent run on a Region Thread. 
     * Bukkit.dispatchCommand(Console) must run on the Global Thread, which causes crashes if called here.
     * We use player.spigot().sendMessage() instead to safely send JSON directly to the player
     * without switching threads or using the command system.
     */
    private void sendTellraw(Player player, String message) {
        // Prepare JSON format
        String json = String.format("{\"text\":\"%s\"}", message.replace("\"", "\\\""));
        
        // Parse and send via Bungee/Spigot API for thread safety
        try {
            net.md_5.bungee.api.chat.BaseComponent[] components = 
                net.md_5.bungee.chat.ComponentSerializer.parse(json);
            player.spigot().sendMessage(components);
        } catch (Exception e) {
            getLogger().warning("Failed to send fairplay packet to " + player.getName());
        }
    }
}
