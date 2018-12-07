package com.Telgram;

import com.Telgram.Model.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    Tools tools = Tools.getTools();

    public static HashSet<User> users = new HashSet<>();

    public static void main(String[] args) throws Exception {

        /*User Amin = new User("Amin", "may", "1234");
        User Amir = new User("Amir", "ami", "123");
        User Ali = new User("Ali", "ali", "12345");
        User Reza = new User("Reza", "rez", "234");
        User Majid = new User("Majid", "maj", "456");
        User Hamid = new User("Hamid", "ham", "678");

        Amin.addToContacts("rez");
        Amin.addToContacts("ham");
        Amin.addToContacts("maj");
        Reza.addToContacts("may");
        Ali.addToContacts("ham");
        Hamid.addToContacts("may");
        Hamid.addToContacts("maj");
        Hamid.addToContacts("ali");
        Majid.addToContacts("ami");
        Majid.addToContacts("ham");

        users.add(Amin);
        users.add(Ali);
        users.add(Amir);
        users.add(Hamid);
        users.add(Majid);
        users.add(Reza);*/



        FileInputStream fout = new FileInputStream("src/com/Telgram/Data/info.ser");
        ObjectInputStream oos = new ObjectInputStream(fout);
        users = (HashSet<User>) oos.readObject();
        System.out.println(users.size());

        /*FileOutputStream fout = new FileOutputStream("src/com/Telgram/Data/info.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(users);*/

        RequestHandler requestHandler = new RequestHandler();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.execute(requestHandler);

        MessageHandler messageHandler = new MessageHandler();
        ScheduledExecutorService executorService2 = Executors.newSingleThreadScheduledExecutor();
        executorService2.execute(messageHandler);

        ServerOnlineHandler onlineHandler = new ServerOnlineHandler();
        ScheduledExecutorService executorService3 = Executors.newSingleThreadScheduledExecutor();
        executorService3.execute(onlineHandler);

    }



}