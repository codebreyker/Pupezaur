package com.example.pupezaur.Connections;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ConnectionHandler {

    public Socket getSocket() {
        return socket;
    }


    private Socket socket;
    public void connectSocket()
    {
        try {
            socket = IO.socket("http://192.168.1.6:8080");
            socket.connect();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
