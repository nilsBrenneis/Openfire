package de.mgnb.openfire.plugin;

import org.xmpp.packet.Packet;

public class SessionManager {

	private Packet packet;
	
	public SessionManager(Packet packet) {
		this.packet = packet;
	}

}
