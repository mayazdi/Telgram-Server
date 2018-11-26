package com.Telgram.API;

import com.Telgram.Model.Message;
import com.Telgram.Model.PM;
import com.Telgram.Model.Tools;
import com.Telgram.Model.User;
import javafx.concurrent.Task;

import javax.tools.Tool;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import static com.Telgram.Main.users;

public class API extends Task {
    Message message;
    Socket socket;
    public API(Socket s){
        this.socket=s;

    }

    private Message processMessage(Message message){
       Message response = new Message();
        /*HashMap<String, String> resVal = new HashMap<>();
        resVal.put("From","Server");
        resVal.put("To", String.valueOf(message.value.get("From")));
        switch (message.action){
            case MESSAGE:
                *//*boolean Sent1 = false;
                boolean Sent1 = false;*//*
                *//*resVal.put("Status","Sent");*//*
                Iterator<User> mIterator = users.iterator();
                PM pmReciever = new PM(message.value.get("Message"),message.value.get("Date"),true);
                PM pmSender = new PM(message.value.get("Message"),message.value.get("Date"),false);
                while(mIterator.hasNext()) {
                    User u = mIterator.next();
                    if (u.Username.equals(message.value.get("To"))) {
                        u.ChatLog.put(message.value.get("From"), pmReciever);
                    }
                    if (u.Username.equals(message.value.get("From"))){
                        u.ChatLog.put(message.value.get("To"),pmSender);
                    }
                }
                resVal.put("Status","Sent");
                response.value = resVal;
                //Do ja save she pm! yeki to From yeki To,
                break;
            case FETCH_CONTACTS:
                Iterator<User> cIterator = users.iterator();
                while(cIterator.hasNext()) {
                    User u = cIterator.next();
                    if (u.Username.equals(message.value.get("From"))){
                        String [] ctcs = u.Contacts.toArray(new String[u.Contacts.size()]);
                        for (int i = 0; i < ctcs.length; i++) {
                            resVal.put("Contact".concat(Integer.toString(i)),ctcs[i]);
                        }
                        response.value = resVal;
                        break;
                    }
                }
                break;
            case SIGN_IN:
                boolean fetched=false;
                Iterator<User> lIterator = users.iterator();
                while(lIterator.hasNext()){
                    User u = lIterator.next();
                    if (u.Password.equals(message.value.get("Password"))
                            && u.Username.equals(message.value.get("Username")) ){
                        resVal.put("SignIn", "Done");
                        response.value = resVal;
                        fetched = true;
                        break;
                    }
                }
                if (!fetched){
                    resVal.put("SignIn", "Failed");
                    response.value = resVal;
                }
                break;
            case SIGN_UP:
                boolean found=false;
                Iterator<User> repetitive = users.iterator();
                while(repetitive.hasNext()){
                    User u = repetitive.next();
                    if ( u.Username.equals(message.value.get("Username")) ){
                        resVal.put("SignUp", "Failed");
                        response.value = resVal;
                        found = true;
                        break;
                    }
                }
                if (!found){
                    users.add(new User(message.value.get("Name"),message.value.get("Username"),message.value.get("Password")));
                    resVal.put("SignUp", "Done");
                    response.value = resVal;
                }
                *//*if (message.value.containsKey("Username")) {
                    if (!message.value.get("Username").equals("mayazdii")) {
                        resVal.put("SignUp", "Done");
                        response.value = resVal;
                    }
                }
                else resVal.put("SignUp", "Failed");*//*
                break;
        }*/
        return response;
    }
    @Override
    public void run() {

    }

    @Override
    protected Object call() throws Exception {
        System.out.println("RUN");
        try(
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());){
            message = (Message) inputStream.readObject();
            System.out.println(message.value);
            Message response = processMessage(message);
            System.out.println(response.value);
            outputStream.writeObject(response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}