package application.management.interfaces;

import java.util.HashMap;

public interface IBook {

    public abstract boolean addBookToLibrary(String title, long isbn, String author, String publishedDate, String genre, int quantityInTheLibrary);
    public abstract boolean removeBookFromLibrary(int bookId);
    public abstract boolean isBorrowed();
    public abstract String getBookTitle();
    public abstract int getBookId();
    public abstract int getQuantityInTheLibrary();
    public abstract String getBookAuthor();
    public abstract String getBookPublishedDate();
    public abstract String getBookGenre();
    public abstract boolean setBookBorrowedStatus(int bookId, boolean status);
    public abstract HashMap<Integer, IBook> getListBookMap();
    public abstract IBook getBookById(int bookId);
    
}