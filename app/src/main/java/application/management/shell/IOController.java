package application.management.shell;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import application.management.interfaces.ILoan;

public class IOController {

    public static void serializeLibraryData(
        HashMap<Integer, User> userMap,
        HashMap<Integer, Book> bookMap,
        HashMap<Integer, ArrayList<ILoan>> loanHistory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("LibraryData.dat"))) {
            oos.writeObject(userMap);
            oos.writeInt(User.getCountUserId());
            oos.writeObject(bookMap);
            oos.writeInt(Book.getCountBookId());
            oos.writeObject(loanHistory);
            System.out.println("[Data has been saved successfully]");
        } catch (IOException e) {
            System.out.println("[Error: " + e.getMessage() + "]");
        }
    }

    @SuppressWarnings("unchecked")
    public static void deserializeLibraryData(
        HashMap<Integer, User> userMap, 
        HashMap<Integer, Book> bookMap,
        HashMap<Integer, ArrayList<ILoan>> loanHistory) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("LibraryData.dat"))) {
            userMap.putAll((HashMap<Integer, User>) ois.readObject());
            User.setCountUserId(ois.readInt());
            bookMap.putAll((HashMap<Integer, Book>) ois.readObject());
            Book.setCountBookId(ois.readInt());
            loanHistory.putAll((HashMap<Integer, ArrayList<ILoan>>) ois.readObject());
        } catch (FileNotFoundException e) {} 
          catch (IOException | ClassNotFoundException e) {
            System.out.println("[Error: " + e.getMessage() + "]");
        }
    }

}