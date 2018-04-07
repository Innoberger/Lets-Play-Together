package de.cynfos.lpt;

import org.bukkit.plugin.java.JavaPlugin;

import de.cynfos.lpt.plugins.AutoWeather;
import de.cynfos.lpt.plugins.Teleport;
import de.cynfos.lpt.plugins.PrivateWarp;

public class Main extends JavaPlugin {
	
	public AutoWeather autoweather;
	public Teleport lpteleport;
	public PrivateWarp privatewarp;
	
	private String compiledMinecraftVersion = "1.11.2";
	
	@Override
	public void onEnable() {
		System.out.println(this.getPrefix(false) + "This build was compiled with spigot for minecraft " + this.compiledMinecraftVersion);
		System.out.println(this.getPrefix(false) + "Loading LetsPlayTogether " + this.getVersion() + "...");
		
		this.autoweather = new AutoWeather(this);
		this.lpteleport = new Teleport(this);
		this.privatewarp = new PrivateWarp(this);
		
		this.autoweather.setup();
		this.lpteleport.setup();
		this.privatewarp.setup();
		
		System.out.println(this.getPrefix(false) + "Successfully loaded LetsPlayTogether " + this.getVersion());
	}
	
	@Override
	public void onDisable() {
		System.out.println("[LPT] Successfully unloaded LetsPlayTogether v" + this.getVersion());
	}
	
	public String getVersion() {
		return this.getDescription().getVersion();
	}
	
	public String getPrefix(boolean colored) {
		if (colored) { return "§7[§6LPT§7] "; }
		else { return "[LPT] "; }
	}
	
}
