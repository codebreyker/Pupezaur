package com.example.pupezaur.connections;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
