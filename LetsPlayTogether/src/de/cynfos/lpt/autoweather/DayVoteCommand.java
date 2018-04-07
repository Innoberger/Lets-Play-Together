package de.cynfos.lpt.autoweather;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.AutoWeather;

public class DayVoteCommand implements CommandExecutor {
	
	private AutoWeather plugin;
	
	public DayVoteCommand(AutoWeather plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(this.plugin.getDayPrefix(true) + "§cConsole is not allowed to do this.");
			return true;
		}
		
		Player p = (Player) cs;
		UUID uuid = p.getUniqueId();
		World w = p.getWorld();
		
		int online = Bukkit.getOnlinePlayers().size();
		
		if (!this.plugin.night(w)) {
			p.sendMessage(this.plugin.getDayPrefix(true) + "§cThe sun has not yet come down.");
			this.plugin.votes.clear();
			return true;
		}
		
		if (this.plugin.votes.contains(uuid)) {
			p.sendMessage(this.plugin.getDayPrefix(true) + "§cYou already voted.");
			return true;
		}
		
		if (this.plugin.votes.isEmpty()) {
			this.plugin.votes.add(uuid);
			Bukkit.broadcastMessage(this.plugin.getDayPrefix(true) + "§6" + p.getName() + " §7started a voting to skip the night (§61§7/§6" + online + "§7) (§6" + Math.floor((1 / (double) online) * 100D * 100D) / 100D + "%§7) §e§l=> /day");
		}
		
		if (online > 1 && !this.plugin.votes.contains(uuid)) {
			this.plugin.votes.add(uuid);
			Bukkit.broadcastMessage(this.plugin.getDayPrefix(true) + "§6" + p.getName() + " §7voted for the day (§6" + this.plugin.votes.size() + "§7/§6" + online + "§7) (§6" + Math.floor((this.plugin.votes.size() / (double) online) * 100D * 100D) / 100D + "%§7) §e§l=> /day");
		}
		
		if ((double) this.plugin.votes.size() / (double) online >= 0.5D) {
			Bukkit.broadcastMessage(this.plugin.getDayPrefix(true) + "Enough players have voted to skip the night (§6" + this.plugin.votes.size()  + "§7/§6" + online + "§7) (§6" + Math.floor((this.plugin.votes.size() / (double) online) * 100D * 100D) / 100D + "%§7)");
			Bukkit.broadcastMessage(this.plugin.getDayPrefix(true) + "Good Morning!");
			this.plugin.votes.clear();
			w.setTime(0);
			return true;
		}
		
		return true;
	}

}
