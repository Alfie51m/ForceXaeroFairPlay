# ForceXaeroFairPlay

**ForceXaeroFairPlay** is a simple Spigot plugin for Minecraft servers running version 1.21 or later. The plugin sends a custom `/tellraw` message to players when they join the server, ensuring compliance with fair play rules. Players with the appropriate permission are exempt from receiving this message.

---

### 🌿 Fork & Modifications
This repository is a modified fork of the original [ForceXaeroFairPlay by Alfie51m](https://github.com/Alfie51m/ForceXaeroFairPlay). Massive thanks and full credit to **Alfie51m** for creating this lightweight and essential plugin.

**What's changed in this version:**
- **Folia Support:** Added `folia-supported: true` to the `plugin.yml` to allow the plugin to load natively on Folia servers.
- **Thread-Safe Dispatching:** Replaced the console command dispatch (`Bukkit.dispatchCommand`) with the Spigot Chat API (`player.spigot().sendMessage()`). This ensures 100% thread safety for Folia's regionized threading (preventing `IllegalStateException` on player join) and improves overall performance across all server software by bypassing command parsing overhead.

---

## Features
- Sends a custom formatted message to players upon joining.
- Customisable per-world settings.
- **100% Folia compatible.**

## Example Config

```yaml
# Default mode for all players.
# Options: none, fairplay, fairplay_nether, disabled
# fairplay_nether enables cave mode in the nether to prevent it showing the bedrock nether roof.
defaultMode: fairplay

# World-specific modes
# Only add worlds if you want to override default setting.
worldModes:
  world_nether: fairplay_nether
  custom_world: none

```

## Permissions
- **`forcexaerofairplay.bypass`**: Players with this permission will not have their map set to Fair Play mode. Default: OP
- **`forcexaerofairplay.reload`**: Allows users to reload the plugin with /fxfp reload. Default: OP

## Commands
- **`/fxfp reload`** - Reloads plugin config file.

## Installation
1. Download the latest release from the [Releases](https://github.com/kungfu5554/ForceXaeroFairPlay-Folia-Support/releases) section.
2. Place the `.jar` file in your server's `plugins` folder.
3. Restart your server.
