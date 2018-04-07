package de.cynfos.lpt.privatewarp;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.PrivateWarp;

public class DelWarp implements CommandExecutor {
	
	private PrivateWarp plugin;
	
	public DelWarp(PrivateWarp plugin) {
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
				
				FileConfiguration cfg = this.plugin.getWarpConfig();
					
				if (args.length < 1) {
					p.sendMessage(this.plugin.getPrefix(true) + "§cPlease select a private warp.");
					return true;
				} else {	
					String name = args[0];
					
					if (this.plugin.getWarpConfig().getConfigurationSection(path + "." + name.toLowerCase()) == null) {
						p.sendMessage(this.plugin.getPrefix(true) + "§cYour private warp §6" + name + " §cdoes not exist.");
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
								this.delWarp(cfg, p, path, name);
								return true;
							}
						} else {
							this.plugin.used.put(p, System.currentTimeMillis());
							this.delWarp(cfg, p, path, name);
							return true;
						}
					}
				}
			}
		}
	}
	
	private void delWarp(FileConfiguration cfg, Player p, String path, String name) {
		cfg.set(path + "." + name.toLowerCase(), null);
		
		try {
			cfg.save(new File("plugins/LPT", "warps.yml"));
		} catch (IOException e) {
			p.sendMessage(this.plugin.getPrefix(true) + "§cYour private warp §6" + name + " §ccould not be removed.");
		} finally {
			p.sendMessage(this.plugin.getPrefix(true) + "Your private warp §6" + name + " §7has been removed.");
		}
	}

}
