package application.management.shell;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import application.management.interfaces.IUser;

public class User implements IUser, Serializable {

    private static final long serialVersionUID = 1L;

    private static int countUserId = 0;
    private List<Loan> loanBooksHistory;
    private static HashMap<Integer, User> userMap = new HashMap<>();
    private int id;
    private String name;

    public User() {}

    public User(String name) {
        this.id = countUserId++;
        this.name = name;
        this.loanBooksHistory = new ArrayList<>();
    }

    @Override
    public int getUserId() {
        return id;
    }

    @Override
    public String getUserName() {
        return name;
    }

    public static HashMap<Integer, User> getUserMap() {
        return userMap;
    }

    @Override
    public HashMap<Integer, IUser> getListUserMap() {
        HashMap<Integer, IUser> userIMap = new HashMap<>();
        for (Integer key : userMap.keySet()) {
            userIMap.put(key, userMap.get(key));
        }
        return userIMap;
    }

    public List<Loan> getUserLoanBooksHistory() {
        return loanBooksHistory;
    }

    @Override
    public String getUserName(int userId) {
        User user = userMap.get(userId);
        return (user != null) ? user.getUserName() : "Unknown User";
    }

    public static int getCountUserId() {
        return countUserId;
    }

    public static void setCountUserId(int count) {
        countUserId = count;
    }

    @Override
    public void addUserToLibrary(String name) {
        User user = new User(name);
        userMap.put(user.getUserId(), user);
    }

    @Override
    public boolean removeUserFromLibrary(int userId) {
        if (userMap.containsKey(userId)) {
            userMap.remove(userId);
            return true;
        }
        return false;
    }
    
}