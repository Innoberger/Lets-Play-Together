package de.cynfos.lpt.privatewarp;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.PrivateWarp;

public class PWarp implements CommandExecutor {
	
	private PrivateWarp plugin;
	
	public PWarp(PrivateWarp plugin) {
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
				
				if (args.length == 0) {				
					int currentWarps = 0;
					int maxWarps = 0;
					String amount = "";
					
					if (this.plugin.getWarpConfig().getConfigurationSection(path) != null) currentWarps = this.plugin.getWarpConfig().getConfigurationSection(path).getKeys(false).size();
					
					if (p.hasPermission("privatewarp.member")) maxWarps = this.plugin.maxMemberWarps();
					if (p.hasPermission("privatewarp.vip")) maxWarps = this.plugin.maxVipWarps();
					if (p.hasPermission("privatewarp.admin")) maxWarps = 0;
					if (p.isOp()) maxWarps = 0;
					
					if (maxWarps == 0) {
						amount = "unlimited";
					} else {
						amount = maxWarps + "";
					}
					
					p.sendMessage("§7=================[ §6PrivateWarp stats §7]=================");
					p.sendMessage("§7Amount of your private warps: §6" + currentWarps);
					if (this.plugin.showAmountOfPrivateWarpsOnServer()) p.sendMessage("§7Amount of global private warps: §6" + this.getAmountOfPrivateWarpsOnServer(this.plugin.getWarpConfig()));
					p.sendMessage("§7Your maximum amount: §6" + amount);
					
					if (currentWarps != 0) {
						Set<String> list = this.plugin.getWarpConfig().getConfigurationSection(path).getKeys(false);
						String warps = "";
						
						for (int i = 0; i < list.size(); i++) {
							if (i != (list.size() - 1)) {
								warps += this.plugin.getWarpConfig().getString(path + "." + list.toArray()[i] + ".name") + ", ";
							} else {
								warps += this.plugin.getWarpConfig().getString(path + "." + list.toArray()[i] + ".name");
							}
						}
						
						p.sendMessage("§7Your private warps: §f" + warps);
					} else {
						p.sendMessage("§7Your private warps: §fnone");
					}
					
					p.sendMessage("§7====================================================");
					return true;
				} else {
					String name = args[0];
					
					if (this.plugin.getWarpConfig().getConfigurationSection(path + "." + name.toLowerCase()) == null) {
						p.sendMessage(this.plugin.getPrefix(true) + "§cYour private warp §6" + name + " §cdoes not exist.");
						return true;
					} else {
						FileConfiguration cfg = this.plugin.getWarpConfig();
						
						String w = cfg.getString(path + "." + name.toLowerCase() + ".world");
						
						if (Bukkit.getWorld(w) == null) {
							p.sendMessage(this.plugin.getPrefix(true) + "§cWorld §6" + w + " §cdoes not exist.");
							return true;
						} else {
							World world = Bukkit.getWorld(w);
							
							double x = cfg.getDouble(path + "." + name.toLowerCase() + ".x");
							double y = cfg.getDouble(path + "." + name.toLowerCase() + ".y");
							double z = cfg.getDouble(path + "." + name.toLowerCase() + ".z");

							float yaw = (float) cfg.getDouble(path + "." + name.toLowerCase() + ".yaw");
							float pitch = (float) cfg.getDouble(path + "." + name.toLowerCase() + ".pitch");
							
							Location loc = new Location(world, x, y, z, yaw, pitch);
								
							p.teleport(loc);
							return true;
						}
					}
				}
			}
		}
	}
	
	private int getAmountOfPrivateWarpsOnServer(FileConfiguration cfg) {
		int amount = 0;
		
		if (cfg.getConfigurationSection("warps") != null) {
			Object[] playersRegistered = cfg.getConfigurationSection("warps").getKeys(false).toArray();
			
			for (int i = 0; i < playersRegistered.length; i++) {
				amount += cfg.getConfigurationSection("warps." + playersRegistered[i]).getKeys(false).size();
			}
			
			return amount;
		} else {
			return 0;
		}
		
	}

}
