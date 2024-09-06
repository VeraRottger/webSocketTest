package com.example.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebSocketHandler extends TextWebSocketHandler {

    // Store all active WebSocket sessions
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Add the new session to the set of active sessions
        sessions.add(session);
    }

    //Handles the incoming WebSocket messages. The example provided simply echoes the message back to the client.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Echo the received message back to the client
//        String payload = message.getPayload();
//        session.sendMessage(new TextMessage("Server received: " + payload));
        broadcastMessage(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Remove the session from the set of active sessions when it is closed
        sessions.remove(session);
    }

    // Method to broadcast a message to all connected clients
    private void broadcastMessage(String message) throws IOException {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage("Broadcast: " + message));
                }
            }
        }
    }

}
