package de.cynfos.lpt.teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.Teleport;

public class TPAcceptCommand implements CommandExecutor {
	
	private Teleport plugin;
	
	public TPAcceptCommand(Teleport plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(this.plugin.getPrefix(true) + "§cConsole is not allowed to do this.");
		} else {
			Player p = (Player) cs;
			
			if (this.plugin.tpa.containsKey(p)) {
				TPA tpa = this.plugin.tpa.get(p);
				
				if (label.equalsIgnoreCase("tpaccept")) {				
					if (System.currentTimeMillis() - tpa.getCreationTime() >= 30000L) {
						p.sendMessage(this.plugin.getPrefix(true) + "§cThis TPA request was sent long time ago...");
					} else {
						if (tpa.getTo().isOnline() && tpa.getFrom().isOnline()) {
							if (tpa.getTo() == p) {
								tpa.getFrom().teleport(p);
							} else {
								p.teleport(tpa.getTo());
							}
						} else {
							if (tpa.getTo() == p) {
								p.sendMessage(this.plugin.getPrefix(true) + "§6" + tpa.getFrom().getName() + " §cis not online now.");
							} else {
								p.sendMessage(this.plugin.getPrefix(true) + "§6" + tpa.getTo().getName() + " §cis not online now.");
							}
						}
					}
				} else {
					if (tpa.getTo() == p) {
						p.sendMessage(this.plugin.getPrefix(true) + "You denied §6" + tpa.getFrom().getName() + "§7's §6TPA §7request.");
						tpa.getFrom().sendMessage(this.plugin.getPrefix(true) + "§6" + p.getName() + " §7denied your §6TPA §7request.");
					} else {
						p.sendMessage(this.plugin.getPrefix(true) + "You denied §6" + tpa.getTo().getName() + "§7's §6TPAHERE §7request.");
						tpa.getTo().sendMessage(this.plugin.getPrefix(true) + "§6" + p.getName() + " §7denied your §6TPAHERE §7request.");
					}
				}
					
				this.plugin.tpa.remove(p);
			} else {
					p.sendMessage(this.plugin.getPrefix(true) + "§cYou didn't get any TPA requests so far.");
			}
		}
		
		return true;
	}

}
