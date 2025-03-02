package application.management.interfaces;

import java.util.HashMap;

public interface IUser {

    public abstract void addUserToLibrary(String name);
    public abstract int getUserId();
    public abstract String getUserName();
    public abstract String getUserName(int id);
    public abstract boolean removeUserFromLibrary(int userId);
    public abstract HashMap<Integer, IUser> getListUserMap();
    
}
