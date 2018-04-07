package de.cynfos.lpt.plugins;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.World;

import de.cynfos.lpt.Main;
import de.cynfos.lpt.autoweather.DayVoteCommand;
import de.cynfos.lpt.autoweather.Listeners;

public class AutoWeather {
	
	public Main main;
	
	public AutoWeather(Main main) {
		this.main = main;
	}
	
	public ArrayList<UUID> votes = new ArrayList<UUID>();
	
	
	public void setup() {
		this.main.getServer().getPluginManager().registerEvents(new Listeners(this), this.main);
		this.main.getServer().getPluginCommand("day").setExecutor(new DayVoteCommand(this));
	}
	
	public String getPrefix(boolean colored) {
		if (colored) { return "§7[§6Weather§7] "; }
		else { return "[Weather] "; }
	}
	
	public String getDayPrefix(boolean colored) {
		if (colored) { return "§7[§6DayVote§7] "; }
		else { return "[DayVote] "; }
	}
	
	public boolean night(World world) {
	    long time = world.getTime();
	 
	    return (time > 12300L);
	}
}