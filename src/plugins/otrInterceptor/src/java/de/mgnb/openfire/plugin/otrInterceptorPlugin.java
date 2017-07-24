package de.mgnb.openfire.plugin;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.session.ClientSession;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import net.java.otr4j.OtrException;
import net.java.otr4j.session.SessionStatus;
import net.java.otr4j.test.dummyclient.DummyClient;

import java.io.File;

public class otrInterceptorPlugin implements Plugin, PacketInterceptor, Component {

	private InterceptorManager interceptorManager;
	private DummyClient[] convo = DummyClient.getConversation();
    private DummyClient server = convo[0];
    private DummyClient client = convo[1];
	private ComponentManager componentManager;

	public otrInterceptorPlugin() {
		interceptorManager = InterceptorManager.getInstance();
	}

	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		System.out.println("Hallo Welt");
		interceptorManager.addInterceptor(this);
		componentManager = ComponentManagerFactory.getComponentManager();
        try {
            componentManager.addComponent("otrInterceptor", this);
        }
        catch (Exception e) {
            e.getMessage();
        }
	}

	@Override
	public void destroyPlugin() {
		interceptorManager.removeInterceptor(this);
	}

	@Override
	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
			throws PacketRejectedException {
			
		if (!processed && ((Message) packet).getBody() != null 
				&& !((Message) packet).getBody().contains("liest") && incoming) {
			
			Message newMessage = (Message) packet.createCopy();
			newMessage.setTo(packet.getFrom());
			newMessage.setFrom(packet.getTo());
			newMessage.setBody("Hallo, wenn du das liest, bist du einen großen Schritt weiter!");
			try {
				componentManager.sendPacket(this, newMessage);
			} catch (ComponentException e) {
				e.printStackTrace();
			}
//			System.out.println("###Session: " + session.hashCode());
//			System.out.println("###GetBody: " + ((Message)packet).getBody());
//			((Message)packet).setBody("huhu");
			
			String bodyToSendBack;
			Packet packetToSendBack = packet;
			packetToSendBack.setTo(packet.getFrom());
			
			sendMessageTo(packet);
			

			bodyToSendBack = initOTRSession(((Message)packet).getBody());

			
			((Message)packetToSendBack).setBody(bodyToSendBack);
			XMPPServer.getInstance().getPacketRouter().route(packetToSendBack);
		}
				
	}
	
	private void sendMessageTo(Packet packet) {
		SessionManager sessionManager = SessionManager.getInstance();
        ClientSession clientSession = sessionManager.getSession(packet.getFrom());

            Message in = (Message) packet.createCopy();

            in.setFrom(packet.getFrom());
            in.setBody("test");
            in.setType(Message.Type.normal);
            in.setTo(packet.getFrom());
            clientSession.process(in);

            
	}
	
	private String initOTRSession(String messageFromClient) {
        try {
        	client.secureSession(server.getAccount());
			client.send(server.getAccount(), messageFromClient);
		} catch (OtrException e) {
			e.printStackTrace();
		}
        
        return server.getConnection().getSentMessage();
	}
	
	private String doOtr(String messageFromClient) {
		
        
        try {
        	
        	
        	
        	if (client.getSession().getSessionStatus() != SessionStatus.ENCRYPTED 
        			&& server.getSession().getSessionStatus() != SessionStatus.ENCRYPTED) {
        		server.send(client.getAccount(), "alice an bob");
        	}
        		
			
			// noch verschlüsselte Nachricht empfangen
			//System.out.println("###1" + alice.getConnection().getSentMessage());
			//System.out.println("###2" + bob.pollReceivedMessage().getContent());
		} catch (OtrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return server.getConnection().getSentMessage();
	}
	
	private String doOtrOLD(String messageFromClient) {
		DummyClient[] convo = DummyClient.getConversation();
        DummyClient alice = convo[0];
        DummyClient bob = convo[1];
        
        try {
			DummyClient.forceStartOtr(alice, bob);
			alice.send(bob.getAccount(), "alice an bob");
			// noch verschlüsselte Nachricht empfangen
			System.out.println("###1OLD" + alice.getConnection().getSentMessage());
			System.out.println("###2OLD" + bob.pollReceivedMessage().getContent());
		} catch (OtrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messageFromClient;
	}
}
