package com.Telgram.Model;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static com.Telgram.Main.users;

public class MessageHandler implements Runnable {

    private static HashMap<String, HashSet<PM>> processLog(Message message) {
        HashMap<String, HashSet<PM>> resVal = new HashMap<>();
        Iterator<User> uIterator = users.iterator();
        while (uIterator.hasNext()) {
            User u = uIterator.next();
            if (u.Username.equals(message.value.get("From"))) {
                resVal = u.ChatLog;
            }
        }
        return resVal;
    }

    @Override
    public void run() {
        while (true) {

            try (ServerSocket serverSocket = new ServerSocket(8580);
                 Socket socket = serverSocket.accept();
                 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());) {
                Message message = (Message) inputStream.readObject();
                outputStream.writeObject(processLog(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fout = new FileOutputStream("D:\\info.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(users);
            }
            catch (Exception e){
                System.out.println("writing Failed");
            }

        }
    }
}
