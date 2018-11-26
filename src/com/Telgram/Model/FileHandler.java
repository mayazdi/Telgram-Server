package com.Telgram.Model;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileHandler implements Runnable{
    File file;
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8085);
             Socket socket = serverSocket.accept();
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());) {
            /*file = (File)inputStream.readObject();
            File saved = new File("/Data/ProfilePics/"+)*/

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
