package com.Telgram.Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static com.Telgram.Main.users;

public class RequestHandler implements Runnable {

    private static Message processMessage(Message message) {
        Message response = new Message();
        HashMap<String, String> resVal = new HashMap<>();
        resVal.put("From", "Server");
        resVal.put("To", String.valueOf(message.value.get("From")));
        switch (message.action) {
            case MESSAGE:
                Iterator<User> mIterator = users.iterator();
                PM pmReciever = new PM(message.value.get("Message"), message.value.get("Date"), true);
                PM pmSender = new PM(message.value.get("Message"), message.value.get("Date"), false);
                while (mIterator.hasNext()) {
                    User u = mIterator.next();
                    if (u.Username.equals(message.value.get("To"))) {
                        HashSet<PM> pms = u.ChatLog.get(message.value.get("From"));
                        pms.add(pmReciever);
                        u.ChatLog.replace(message.value.get("From"), pms);
                    }
                    if (u.Username.equals(message.value.get("From"))) {
                        HashSet<PM> pms = u.ChatLog.get(message.value.get("To"));
                        pms.add(pmSender);
                        u.ChatLog.replace(message.value.get("To"), pms);
                    }
                }
                resVal.put("Status", "Sent");
                response.value = resVal;

                break;
            case FETCH_CONTACTS:
                Iterator<User> cIterator = users.iterator();
                while (cIterator.hasNext()) {
                    User u = cIterator.next();
                    if (u.Username.equals(message.value.get("From"))) {
                        String[] ctcs = u.Contacts.toArray(new String[u.Contacts.size()]);
                        for (int i = 0; i < ctcs.length; i++) {
                            resVal.put("Contact".concat(Integer.toString(i)), ctcs[i]);
                        }
                        response.value = resVal;
                        break;
                    }
                }
                break;
            case EDIT_USER:
                int i = 0;
                Iterator<User> eIterator = users.iterator();
                while (eIterator.hasNext()) {
                    User u = eIterator.next();
                    if (message.value.get("From").equals(u.Username)){
                        if (message.value.containsKey("Fetch")){
                            resVal.put("Name",u.Name);
                            resVal.put("Username",u.Username);
                            resVal.put("Bio",u.Bio);
                            resVal.put("Password",u.Password);
                        }
                        else if (message.value.containsKey("Name")){
                            u.Name= message.value.get("Name");
                            resVal.put("Change","Done");
                        }
                        else if (message.value.containsKey("Username")){
                            //^^^^^^^^^^^^^^^^^^^^^Check if its not repeated^^^^^^^^^^^^^^^^^^^^^
                            //>>>>>>>>>>>>>>>>>Change where ever username exists<<<<<<<<<<<<<<<<<
                            /*u.Username= message.value.get("Username");*/
                            resVal.put("Change","Done");
                        }
                        else if (message.value.containsKey("Bio")){
                            u.Bio = message.value.get("Bio");
                            resVal.put("Change","Done");
                        }
                        else if (message.value.containsKey("Password")){
                            u.Password = message.value.get("Password");
                            resVal.put("Change","Done");
                        }
                        else resVal.put("Change","Failed");
                    }
                }
                response.value = resVal;
                break;
            case ADD_CONTACT:
                boolean [] job = new boolean[2];
                job[0]=false;
                job[1]=false;

                Iterator<User> aIterator = users.iterator();
                while (aIterator.hasNext()) {
                    User u = aIterator.next();
                    if (u.Username.equals(message.value.get("From"))){
                        int size =u.Contacts.size();
                        u.addToContacts(message.value.get("Contact"));
                        if (u.Contacts.size() - size == 1)
                            job[0]=true;
                    }
                    else if (u.Username.equals(message.value.get("Contact"))) {
                        int size =u.Contacts.size();
                        u.addToContacts(message.value.get("From"));
                        if (u.Contacts.size() - size == 1)
                            job[1]=true;
                    }
                }
                if (job[0] && job[0])
                    resVal.put("AddContact","Done");
                else resVal.put("AddContact","Failed");

                response.value = resVal ;

                break;
            case SEARCH:
                int j = 0;
                Iterator<User> sIterator = users.iterator();
                while (sIterator.hasNext()) {
                    User u = sIterator.next();
                    if (u.Username.contains(message.value.get("SearchKey"))
                            || u.Name.contains(message.value.get("SearchKey"))) {
                        resVal.put("Contact".concat(String.valueOf(j)), u.Username);
                        j++;
                    }
                }
                /*resVal.remove("From");
                resVal.remove("To");*/
                response.value = resVal;
                break;
            case SIGN_IN:
                boolean fetched = false;
                Iterator<User> lIterator = users.iterator();
                while (lIterator.hasNext()) {
                    User u = lIterator.next();
                    if (u.Password.equals(message.value.get("Password"))
                            && u.Username.equals(message.value.get("Username"))) {
                        resVal.put("SignIn", "Done");
                        response.value = resVal;
                        fetched = true;
                        break;
                    }
                }
                if (!fetched) {
                    resVal.put("SignIn", "Failed");
                    response.value = resVal;
                }
                break;
            case SIGN_UP:
                boolean found = false;
                Iterator<User> repetitive = users.iterator();
                while (repetitive.hasNext()) {
                    User u = repetitive.next();
                    if (u.Username.equals(message.value.get("Username"))) {
                        resVal.put("SignUp", "Failed");
                        response.value = resVal;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    users.add(new User(message.value.get("Name"), message.value.get("Username"), message.value.get("Password")));
                    resVal.put("SignUp", "Done");
                    response.value = resVal;
                }
                break;
        }
        return response;

    }


    @Override
    public void run() {
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(8585);
                 Socket socket = serverSocket.accept();
                 ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());) {
                Message message = (Message) inputStream.readObject();
                System.out.println(message.value);

                Message Temp = processMessage(message);
                System.out.println(Temp.value);
                outputStream.writeObject(Temp);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
