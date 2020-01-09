package com.example.ex4;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientCommunication {

    private boolean isConnect;
    private String ip;
    private int port;
    private PrintWriter output;
    private Socket socket;

    private static ClientCommunication client_instance = null;
    public static ClientCommunication getInstance()
    {
        if (client_instance == null)
            client_instance = new ClientCommunication();

        return client_instance;
    }

    private ClientCommunication(){
        this.isConnect = false;
    }

    public void connect(String tempIp, String tempPort){
        this.ip = tempIp;
        this.port  = Integer.parseInt(tempPort);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    isConnect = true;
                    output = new PrintWriter(socket.getOutputStream());
                } catch (Exception e){
                }
            }
        };
        t.start();

    }

    public void writeToServer(final String obj, final double value){
        Thread t = new Thread() {
            @Override
            public void run() {
                if ((isConnect)){
                    try{
                        String command = getCommand(obj, value);
                        if (!command.isEmpty()){
                            output.println(getCommand(obj, value));
                            output.flush();
                        }
                    } catch (Exception e){
                    }
                }
            }
        };
        t.start();
    }

    private String getCommand(String obj, Double value){
        String path =  "";
        if (obj.compareTo("aileron") == 0){
            path = "/controls/flight/aileron";
        } else if (obj.compareTo("elevator") == 0){
            path = "/controls/flight/elevator";
        }
        if (path.isEmpty()){
            return "";
        }
        return "set " + path + ' ' + value.toString() + "\r\n";
    }

    public void close(){
        this.output.close();
        try {
            this.socket.close();
        } catch (Exception e){

        }
        this.isConnect = false;
    }
}
