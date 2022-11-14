package tools;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import model.Chat;
import model.users.Driver;
import model.users.Passenger;
import model.users.User;

public class Mokap {

    public static List<User> getUsers(){
        ArrayList<User> users = new ArrayList<User>();
        User u1 = new Driver(1, "Milos", "Obradovic", "test@gmail.com", "0691231234", "Maksima Gorkog 57", "1234", null, false, null, null, false);
        User u2 = new Passenger(2, "Danica", "Gazdic", "test2@gmail.com", "0691852001", "JNA 12", "danica", null, false, null);

        users.add(u1);
        users.add(u2);

        return users;
    }


//    public static List<Chat> getChats(){
//        ArrayList<Chat> chats = new ArrayList<Chat>();
//    }
}
