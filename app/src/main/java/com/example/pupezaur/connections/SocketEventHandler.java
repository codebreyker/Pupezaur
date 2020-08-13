package com.example.pupezaur.connections;

import com.example.pupezaur.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketEventHandler {

    private String personName;
    private String message;

    private ConnectionHandler connectionHandler;
    private MainActivity mainActivity;
    public SocketEventHandler(ConnectionHandler connectionHandler, MainActivity mainActivity) {
        this.connectionHandler = connectionHandler;
        this.mainActivity = mainActivity;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void doSocketEvents(){
        connectionHandler.getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });
        connectionHandler.getSocket().on("newMessage", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data = (JSONObject) objects[0] ;
                try {
                    personName = data.getString("name");
                    message  = data.getString("message");
//                    MainActivity.updateMessage();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
