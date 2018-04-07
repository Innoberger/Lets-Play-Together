package de.cynfos.lpt.privatewarp;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.PrivateWarp;

public class SetWarp implements CommandExecutor {
	
	private PrivateWarp plugin;
	
	public SetWarp(PrivateWarp plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(this.plugin.getPrefix(true) + "§cConsole is not allowed to do this.");
			return true;
		} else {
			Player p = (Player) cs;
			
			if (!p.isOp() && !p.hasPermission("privatewarp.admin") && !p.hasPermission("privatewarp.vip") && !p.hasPermission("privatewarp.member")) {
				p.sendMessage(this.plugin.getPrefix(true) + "§cYou don't have permission.");
				return true;
			} else {
				String uuid = p.getUniqueId().toString();
				String path = "warps." + uuid;
				
				int maxWarps = 0;
				int currentWarps = 0;
				
				if (this.plugin.getWarpConfig().getConfigurationSection(path) != null) currentWarps = this.plugin.getWarpConfig().getConfigurationSection(path).getKeys(false).size();
				
				if (p.hasPermission("privatewarp.member")) maxWarps = this.plugin.maxMemberWarps();
				if (p.hasPermission("privatewarp.vip")) maxWarps = this.plugin.maxVipWarps();
				if (p.hasPermission("privatewarp.admin")) maxWarps = 0;
				if (p.isOp()) maxWarps = 0;
				
				if ((currentWarps >= maxWarps) && !p.isOp() && !p.hasPermission("privatewarp.admin")) {
					p.sendMessage(this.plugin.getPrefix(true) + "§cYou exceeded the maximum amount of private warps.");
					p.sendMessage(this.plugin.getPrefix(true) + "§cType /delpwarp <name> to delete one.");
					return true;
				} else {
					FileConfiguration cfg = this.plugin.getWarpConfig();
					
					if (args.length < 1) {
						p.sendMessage(this.plugin.getPrefix(true) + "§cPlease give a name.");
						return true;
					} else {
						String name = args[0];
						String world = p.getWorld().getName();
						double x = p.getLocation().getX();
						double y = p.getLocation().getY();
						double z = p.getLocation().getZ();
						
						float yaw = p.getLocation().getYaw();
						float pitch = p.getLocation().getPitch();
						
						if (this.plugin.getWarpConfig().getConfigurationSection(path + "." + name.toLowerCase()) != null) {
							p.sendMessage(this.plugin.getPrefix(true) + "§cYour private warp §6" + name + " §calready exists.");
							return true;
						} else {
							if (this.plugin.used.containsKey(p)) {
								int cooldown = this.plugin.cooldownSeconds();
								
								if ((this.plugin.used.get(p) + cooldown * 1000L) >= (System.currentTimeMillis()) && !p.isOp() && !p.hasPermission("privatewarp.admin")) {
									p.sendMessage(this.plugin.getPrefix(true) + "§cWait §6" + cooldown + " §cseconds to interact with private warps again.");
									return true;
								} else {
									this.plugin.used.remove(p);
									this.plugin.used.put(p, System.currentTimeMillis());
									
									if (args.length == 2 && args[1].equalsIgnoreCase("true")) {
										this.setWarpYawPitch(cfg, p, path, name, world, x, y, z, yaw, pitch);
										return true;
									} else {
										this.setWarp(cfg, p, path, name, world, x, y, z);
										return true;
									}
								}
							} else {
								this.plugin.used.put(p, System.currentTimeMillis());
								
								if (args.length == 2 && args[1].equalsIgnoreCase("true")) {
									this.setWarpYawPitch(cfg, p, path, name, world, x, y, z, yaw, pitch);
									return true;
								} else {
									this.setWarp(cfg, p, path, name, world, x, y, z);
									return true;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void setWarpYawPitch(FileConfiguration cfg, Player p, String path, String name, String world, double x, double y, double z, float yaw, float pitch) {
		cfg.set(path + "." + name.toLowerCase() + ".name", name);
		cfg.set(path + "." + name.toLowerCase() + ".world", world);
		cfg.set(path + "." + name.toLowerCase() + ".x", x);
		cfg.set(path + "." + name.toLowerCase() + ".y", y);
		cfg.set(path + "." + name.toLowerCase() + ".z", z);
		cfg.set(path + "." + name.toLowerCase() + ".yaw", yaw);
		cfg.set(path + "." + name.toLowerCase() + ".pitch", pitch);
	
		try {
			cfg.save(new File("plugins/LPT", "warps.yml"));
		} catch (IOException e) {
			p.sendMessage(this.plugin.getPrefix(true) + "§cYour private warp §6" + name + " §ccould not be saved.");
		} finally {
			p.sendMessage(this.plugin.getPrefix(true) + "Your private warp §6" + name + " §7has been saved.");
		}
	}
	
	private void setWarp(FileConfiguration cfg, Player p, String path, String name, String world, double x, double y, double z) {
		cfg.set(path + "." + name.toLowerCase() + ".name", name);
		cfg.set(path + "." + name.toLowerCase() + ".world", world);
		cfg.set(path + "." + name.toLowerCase() + ".x", x);
		cfg.set(path + "." + name.toLowerCase() + ".y", y);
		cfg.set(path + "." + name.toLowerCase() + ".z", z);
		
		try {
			cfg.save(new File("plugins/LPT", "warps.yml"));
		} catch (IOException e) {
			p.sendMessage(this.plugin.getPrefix(true) + "§cYour private warp §6" + name + " §ccould not be saved.");
		} finally {
			p.sendMessage(this.plugin.getPrefix(true) + "Your private warp §6" + name + " §7has been saved.");
		}
	}

}
