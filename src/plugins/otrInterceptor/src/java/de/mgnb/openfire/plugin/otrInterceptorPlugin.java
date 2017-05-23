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
    private MessageRouter messageRouter;

    public otrInterceptorPlugin() {
        interceptorManager = InterceptorManager.getInstance();
        messageRouter = XMPPServer.getInstance().getMessageRouter();
    }

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        System.out.println("Hallo Weltli");
        interceptorManager.addInterceptor(this);
    }

    @Override
    public void destroyPlugin() {
        System.out.println("Tschuess Welt");
    }

    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {

    }
}
