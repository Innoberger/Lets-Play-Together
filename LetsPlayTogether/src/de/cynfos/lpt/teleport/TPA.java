package de.cynfos.lpt.teleport;

import org.bukkit.entity.Player;

public class TPA {
	
	private Player from;
	private Player to;
	private long created;
	
	public TPA(Player from, Player to, long created) {
		this.from = from;
		this.to = to;
		this.created = created;
	}
	
	public Player getFrom() {
		return this.from;
	}
	
	public Player getTo() {
		return this.to;
	}
	
	public long getCreationTime() {
		return this.created;
	}

}
