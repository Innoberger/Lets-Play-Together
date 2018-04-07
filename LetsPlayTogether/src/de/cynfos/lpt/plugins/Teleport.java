package de.cynfos.lpt.plugins;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.cynfos.lpt.Main;
import de.cynfos.lpt.teleport.HomeCommand;
import de.cynfos.lpt.teleport.PingCommand;
import de.cynfos.lpt.teleport.TPA;
import de.cynfos.lpt.teleport.TPACommand;
import de.cynfos.lpt.teleport.TPAcceptCommand;

public class Teleport {
	
	public Main main;
	
	public Teleport(Main main) {
		this.main = main;
	}
	
	public void setup() {
		this.main.getCommand("home").setExecutor(new HomeCommand(this));
		this.main.getCommand("tpa").setExecutor(new TPACommand(this));
		this.main.getCommand("tpaccept").setExecutor(new TPAcceptCommand(this));
		this.main.getCommand("ping").setExecutor(new PingCommand(this));
	}
	
	public HashMap<Player, TPA> tpa = new HashMap<Player, TPA>();
	
	
	public FileConfiguration getHomeConfig() {
		return YamlConfiguration.loadConfiguration(this.getConfigFile());
	}
	
	public File getConfigFile() {
		return new File("plugins/LPT", "homes.yml");
	}
	
	public String getPrefix(boolean colored) {
		if (colored) { return "§7[§6Teleport§7] "; }
		else { return "[Teleport] "; }
	}
}