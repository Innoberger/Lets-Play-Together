package de.cynfos.lpt.teleport;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.Teleport;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public class PingCommand implements CommandExecutor {
	
	private Teleport plugin;
	
	public PingCommand(Teleport plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(this.plugin.main.getPrefix(true) + "§cConsole should get a ping of §60 ms §c...");
			return true;
		} else {
			Player p = (Player) cs;
			
			p.sendMessage(this.plugin.main.getPrefix(true) + "Your ping: §6" + this.getPing(p) + " ms");
			return true;
		}
	}
	
	public int getPing(Player p) {
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
				
		return ep.ping;
	}

}
