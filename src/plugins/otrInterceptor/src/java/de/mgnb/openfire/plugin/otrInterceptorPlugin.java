package de.mgnb.openfire.plugin;

import org.jivesoftware.openfire.MessageRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.Packet;

import java.io.File;

public class otrInterceptorPlugin implements Plugin, PacketInterceptor {

	private InterceptorManager interceptorManager;

	public otrInterceptorPlugin() {
		interceptorManager = InterceptorManager.getInstance();
	}

	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		System.out.println("Hallo Welt");
		interceptorManager.addInterceptor(this);
	}

	@Override
	public void destroyPlugin() {
		interceptorManager.removeInterceptor(this);
	}

	@Override
	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
			throws PacketRejectedException {
		if (!processed) {
			System.out.println(packet.getFrom());
			System.out.println(packet.getTo());
			System.out.println(packet.getElement().getText());
		}
	}
}
