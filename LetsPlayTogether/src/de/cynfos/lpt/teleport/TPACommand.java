package de.cynfos.lpt.teleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.cynfos.lpt.plugins.Teleport;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class TPACommand implements CommandExecutor {
	
	private Teleport plugin;
	
	public TPACommand(Teleport plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(this.plugin.getPrefix(true) + "§cConsole is not allowed to do this.");
		} else {
			Player p = (Player) cs;
			
			if (args.length == 0) {
				p.sendMessage(this.plugin.getPrefix(true) + "§c/" + label + " <player>");
			} else {
				if (label.equalsIgnoreCase("tpa")) {
					final Player t = p.getServer().getPlayer(args[0]);
					
					if (t == null) {
						p.sendMessage(this.plugin.getPrefix(true) + "§6" + args[0] + " §cis not online now.");
					} else {
						p.sendMessage(this.plugin.getPrefix(true) + "You sent a §6TPA §7request to §6" + t.getName() + "§7.");
						t.sendMessage(this.plugin.getPrefix(true) + "§6" + p.getName() + " §7sent a §6TPA §7request to you.");
						
						String start1 = this.plugin.getPrefix(true);
						String linked1 = "§aACCEPT";
						String cmd1 = "tpaccept";
						String hover1 = "§8Click to §aACCEPT";
						
						String start2 = this.plugin.getPrefix(true);
						String linked2 = "§cDENY";
						String cmd2 = "tpdeny";
						String hover2 = "§8Click to §cDENY";
						
						PlayerConnection con = ((CraftPlayer)t).getHandle().playerConnection;
						
						IChatBaseComponent chat1 = ChatSerializer.a("{\"text\":\"" + start1 + "\",\"extra\":"
								+ "[{\"text\":\"" + linked1 + "\",\"clickEvent\":{\"action\":\"run_command\","
								+ "\"value\":\"/" + cmd1 + "\"},\"hoverEvent\":{\"action\":\"show_text\","
								+ "\"value\":\"" + hover1 + "\"}}]}").a(" §7(Click)");
						
						IChatBaseComponent chat2 = ChatSerializer.a("{\"text\":\"" + start2 + "\",\"extra\":"
								+ "[{\"text\":\"" + linked2 + "\",\"clickEvent\":{\"action\":\"run_command\","
								+ "\"value\":\"/" + cmd2 + "\"},\"hoverEvent\":{\"action\":\"show_text\","
								+ "\"value\":\"" + hover2 + "\"}}]}").a(" §7(Click)");
						
						PacketPlayOutChat packet1 = new PacketPlayOutChat(chat1);
						PacketPlayOutChat packet2 = new PacketPlayOutChat(chat2);
						
						con.sendPacket(packet1);
						con.sendPacket(packet2);
						
						TPA tpa = new TPA(p, t, System.currentTimeMillis());
						
						if (this.plugin.tpa.containsKey(t)) {
							this.plugin.tpa.remove(t);
							this.plugin.tpa.put(t, tpa);
						} else {
							this.plugin.tpa.put(t, tpa);
						}
					}
				} else {
					final Player t = p.getServer().getPlayer(args[0]);
					
					if (t == null) {
						p.sendMessage(this.plugin.getPrefix(true) + "§6" + args[0] + " §cis not online now.");
					} else {
						p.sendMessage(this.plugin.getPrefix(true) + "You sent a §6TPAHERE §7request to §6" + t.getName() + "§7.");
						t.sendMessage(this.plugin.getPrefix(true) + "§6" + p.getName() + " §7sent a §6TPAHERE §7request to you.");
						
						String start1 = this.plugin.getPrefix(true);
						String linked1 = "§aACCEPT";
						String cmd1 = "tpaccept";
						String hover1 = "§8Click to §aACCEPT";
						
						String start2 = this.plugin.getPrefix(true);
						String linked2 = "§cDENY";
						String cmd2 = "tpdeny";
						String hover2 = "§8Click to §cDENY";
						
						PlayerConnection con = ((CraftPlayer)t).getHandle().playerConnection;
						
						IChatBaseComponent chat1 = ChatSerializer.a("{\"text\":\"" + start1 + "\",\"extra\":"
								+ "[{\"text\":\"" + linked1 + "\",\"clickEvent\":{\"action\":\"run_command\","
								+ "\"value\":\"/" + cmd1 + "\"},\"hoverEvent\":{\"action\":\"show_text\","
								+ "\"value\":\"" + hover1 + "\"}}]}").a(" §7(Click)");
						
						IChatBaseComponent chat2 = ChatSerializer.a("{\"text\":\"" + start2 + "\",\"extra\":"
								+ "[{\"text\":\"" + linked2 + "\",\"clickEvent\":{\"action\":\"run_command\","
								+ "\"value\":\"/" + cmd2 + "\"},\"hoverEvent\":{\"action\":\"show_text\","
								+ "\"value\":\"" + hover2 + "\"}}]}").a(" §7(Click)");
						
						PacketPlayOutChat packet1 = new PacketPlayOutChat(chat1);
						PacketPlayOutChat packet2 = new PacketPlayOutChat(chat2);
						
						con.sendPacket(packet1);
						con.sendPacket(packet2);
						
						TPA tpa = new TPA(t, p, System.currentTimeMillis());
						
						if (this.plugin.tpa.containsKey(t)) {
							this.plugin.tpa.remove(t);
							this.plugin.tpa.put(t, tpa);
						} else {
							this.plugin.tpa.put(t, tpa);
						}
					}
				}
			}
		}
		
		return true;
	}

}
