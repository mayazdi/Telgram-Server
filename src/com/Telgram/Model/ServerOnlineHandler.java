package com.Telgram.Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerOnlineHandler implements Runnable {
    @Override
    public void run() {
        while (true){
            try (ServerSocket serverSocket = new ServerSocket(8080);
                 Socket socket = serverSocket.accept();
                 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());) {
                String s = (String) inputStream.readObject();

                /*System.out.println(s);*/
                outputStream.writeObject(s);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
