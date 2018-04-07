package de.cynfos.lpt.autoweather;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.cynfos.lpt.plugins.AutoWeather;

public class Listeners implements Listener {
	
	private AutoWeather plugin;
	
	public Listeners(AutoWeather plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWeatherChange(WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			e.setCancelled(true);
			Bukkit.broadcastMessage(this.plugin.getPrefix(true) + "A thunderstorm has been prevented.");
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onKick(PlayerKickEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		
		if (this.plugin.votes.contains(uuid)) {
			this.plugin.votes.remove(uuid);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		
		if (this.plugin.votes.contains(uuid)) {
			this.plugin.votes.remove(uuid);
		}
	}

}
