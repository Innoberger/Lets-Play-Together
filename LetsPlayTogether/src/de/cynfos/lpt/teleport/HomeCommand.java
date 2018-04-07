package de.cynfos.lpt.teleport;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.Teleport;

public class HomeCommand implements CommandExecutor {
	
	private Teleport plugin;
	
	public HomeCommand(Teleport plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(this.plugin.getPrefix(true) + "§cConsole is not allowed to do this.");
		} else {
			Player p = (Player) cs;
			
			String uuid = p.getUniqueId().toString();
			String path = "homes." + uuid;
			
			if (label.equalsIgnoreCase("sethome")) {				
				FileConfiguration cfg = this.plugin.getHomeConfig();
				
				String w = p.getLocation().getWorld().getName();
				
				double x = p.getLocation().getX();
				double y = p.getLocation().getY();
				double z = p.getLocation().getZ();
				
				float yaw = p.getLocation().getYaw();
				float pitch = p.getLocation().getPitch();
				
				this.setHome(cfg, p, path, w, x, y, z, yaw, pitch);
			} else {
				if (this.plugin.getHomeConfig().getConfigurationSection(path) == null) {
					p.sendMessage(this.plugin.getPrefix(true) + "§cYou haven't yet set your home.");
				} else {
					FileConfiguration cfg = this.plugin.getHomeConfig();
					
					String w = cfg.getString(path + ".world");
					
					if (Bukkit.getWorld(w) == null) {
						p.sendMessage(this.plugin.getPrefix(true) + "§cWorld §6" + w + " §cdoes not exist.");
					} else {
						World world = Bukkit.getWorld(w);
						
						double x = cfg.getDouble(path + ".x");
						double y = cfg.getDouble(path + ".y");
						double z = cfg.getDouble(path + ".z");

						float yaw = (float) cfg.getDouble(path + ".yaw");
						float pitch = (float) cfg.getDouble(path + ".pitch");
						
						Location loc = new Location(world, x, y, z, yaw, pitch);
							
						p.teleport(loc);
					}
				}
			}
		}
		
		return true;
	}
	
	private void setHome(FileConfiguration cfg, Player p, String path, String world, double x, double y, double z, float yaw, float pitch) {
		cfg.set(path + "." + ".world", world);
		cfg.set(path + "." + ".x", x);
		cfg.set(path + "." + ".y", y);
		cfg.set(path + "." + ".z", z);
		cfg.set(path + "." + ".yaw", yaw);
		cfg.set(path + "." + ".pitch", pitch);
	
		try {
			cfg.save(this.plugin.getConfigFile());
		} catch (IOException e) {
			p.sendMessage(this.plugin.getPrefix(true) + "§cYour home could not be saved.");
		} finally {
			p.sendMessage(this.plugin.getPrefix(true) + "Your home has been set.");
		}
	}

}
