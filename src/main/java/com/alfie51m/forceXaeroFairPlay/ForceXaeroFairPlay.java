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

public class ForceXaeroFairPlay extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("fxfp").setExecutor(this);
        getCommand("fxfp").setTabCompleter(this);
        getLogger().info("ForceXaeroFairPlay has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ForceXaeroFairPlay has been disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        handlePlayerMode(player, player.getWorld().getName(), null);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String fromWorld = event.getFrom().getName();
        String toWorld = player.getWorld().getName();

        handlePlayerMode(player, toWorld, fromWorld);
    }

    @Override
    public java.util.List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args) {

        if (args.length == 1) {
            java.util.List<String> completions = new java.util.ArrayList<>();

            if (sender.hasPermission("forcexaerofairplay.reload")) {
                if ("reload".startsWith(args[0].toLowerCase())) {
                    completions.add("reload");
                }
            }

            return completions;
        }

        return java.util.Collections.emptyList();
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

    private void handlePlayerMode(Player player, String toWorldName, String fromWorldName) {
        if (player.hasPermission("forcexaerofairplay.bypass")) {
            return;
        }

        FileConfiguration config = getConfig();
        String defaultMode = config.getString("defaultMode", "none").toLowerCase();
        String toWorldMode = config.getString("worldModes." + toWorldName, defaultMode).toLowerCase();
        String fromWorldMode = fromWorldName != null
                ? config.getString("worldModes." + fromWorldName, defaultMode).toLowerCase()
                : "none";

        StringBuilder messageBuilder = new StringBuilder();

        if (!fromWorldMode.equals(toWorldMode)) {
            messageBuilder.append("§r§e§s§e§t§x§a§e§r§o ");
        }

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

    private void sendTellraw(Player player, String message) {
        String json = String.format("{\"text\":\"%s\"}", message.replace("\"", "\\\""));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "tellraw " + player.getName() + " " + json);
    }
}
