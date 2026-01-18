
# ForceXaeroFairPlay

**ForceXaeroFairPlay** is a simple Spigot plugin for Minecraft servers running version 1.21 or later. The plugin sends a custom `/tellraw` message to players when they join the server, ensuring compliance with fair play rules. Players with the appropriate permission are exempt from receiving this message.

## Features
- Sends a custom formatted message to players upon joining.
- Customisable per-world settings.

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
1. Download the latest release from the [Releases](https://github.com/Alfie51m/ForceXaeroFairPlay/releases) section.
2. Place the `.jar` file in your server's `plugins` folder.
3. Restart your server.

## Usage
- By default the plugin will set all players Xaero's Minimap to Fair Play.
- Set per-world configuration settings or leave worldModes blank to use the servers default config.
- Grant the `forcexaerofairplay.bypass` permission to exempt specific players or groups.

## Contributing
Feel free to submit issues or contribute via pull requests on the [GitHub repository](https://github.com/Alfie51m/ForceXaeroFairPlay).

## License
This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).

--- 

### **Thanks for using ForceXaeroFairPlay**
You can also find this plugin on [Modrinth](https://modrinth.com/plugin/forcexaerofairplay), [Paper Hangar](https://hangar.papermc.io/Alfie51m/ForceXaeroFairPlay), [SpigotMC](https://www.spigotmc.org/resources/forcexaerofairplay.121907/)
