package de.cynfos.lpt.plugins;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.cynfos.lpt.Main;
import de.cynfos.lpt.privatewarp.DelWarp;
import de.cynfos.lpt.privatewarp.PWarp;
import de.cynfos.lpt.privatewarp.SetWarp;

public class PrivateWarp {
	
	public Main main;
	
	public PrivateWarp(Main main) {
		this.main = main;
	}
	
	public void setup() {
		this.loadConfigs();
		this.loadCommands();
	}
	
	private void loadCommands() {
		this.main.getCommand("setpwarp").setExecutor(new SetWarp(this));
		this.main.getCommand("pwarp").setExecutor(new PWarp(this));
		this.main.getCommand("delpwarp").setExecutor(new DelWarp(this));
	}
	
	
	public HashMap<Player, Long> used = new HashMap<Player, Long>();
	
	
	public FileConfiguration getWarpConfig() {
		return YamlConfiguration.loadConfiguration(new File("plugins/LPT", "warps.yml"));
	}
	
	public void loadConfigs() {
		File file = new File("plugins/LPT", "config.yml");
		
		FileConfiguration pc = this.main.getConfig();
		FileConfiguration wc = this.getWarpConfig();
		
		pc.addDefault("privatewarp.showTotalPrivateWarpsOnServer", true);
		pc.addDefault("privatewarp.memberWarpAmount", 3);
		pc.addDefault("privatewarp.vipWarpAmount", 5);
		pc.addDefault("privatewarp.cooldownForMemberAndVip", 30);
		pc.options().copyDefaults(true);
		
		try {
			pc.save(file);
			wc.save(new File("plugins/LPT", "warps.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int maxMemberWarps() {
		return this.main.getConfig().getInt("privatewarp.memberWarpAmount");
	}
	
	public int maxVipWarps() {
		return this.main.getConfig().getInt("privatewarp.vipWarpAmount");
	}
	
	public int cooldownSeconds() {
		return this.main.getConfig().getInt("privatewarp.cooldownForMemberAndVip");
	}
	
	public boolean showAmountOfPrivateWarpsOnServer() {
		return this.main.getConfig().getBoolean("privatewarp.showTotalPrivateWarpsOnServer");
	}
	
	public String getPrefix(boolean colored) {
		if (colored) { return "§7[§6PrivateWarp§7] "; }
		else { return "[PrivateWarp] "; }
	}
}
