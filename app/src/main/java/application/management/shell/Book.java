package application.management.shell;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import application.management.interfaces.IBook;

public class Book implements IBook, Serializable {

    private static final long serialVersionUID = 1L;

    private static int countBookId = 0;
    private static HashMap<Integer, Book> bookMap = new HashMap<>();

    private int id;
    private long isbn;
    private String title;
    private String author;
    private String publishedDate;
    private String genre;
    private int quantityInTheLibrary;
    private boolean borrowed;

    public Book() {}

    public Book(String title, long isbn, String author, String publishedDate, String genre, int quantityInTheLibrary) {
        this.id = countBookId++;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.genre = genre;
        this.quantityInTheLibrary = quantityInTheLibrary;
        this.borrowed = false;
    }

    @Override
    public boolean isBorrowed() {
        return borrowed;
    }
    
    @Override
    public int getBookId() {
        return id;
    }

    public long getBookISBN() {
        return isbn;
    }

    @Override
    public String getBookTitle() {
        return title;
    }

    @Override
    public String getBookAuthor() {
        return author;
    }

    @Override
    public String getBookGenre() {
        return genre;
    }

    @Override
    public int getQuantityInTheLibrary() {
        return quantityInTheLibrary;
    }

    public IBook getBookById(int bookId) {
        Book book = bookMap.get(bookId);
        return book != null ? book : null; 
    }

    public static int getCountBookId() {
        return countBookId;
    }

    @Override
    public String getBookPublishedDate() {
        return LocalDate.parse(publishedDate).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static HashMap<Integer, Book> getBookMap() {
        return bookMap;
    }

    @Override
    public HashMap<Integer, IBook> getListBookMap() {
        return new HashMap<Integer, IBook>(bookMap);
    }

    public static void setCountBookId(int count) {
        countBookId = count;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public boolean setBookBorrowedStatus(int bookId, boolean status) {
        Book book = bookMap.get(bookId);
        if (book != null) {
            book.setBorrowed(status);
            return true;
        }
        return false;
    }

    public void setBookId(int bookId) {
        this.id = bookId;
    }

    @Override
    public boolean addBookToLibrary(String title, long isbn, String author, String publishedDate, String genre, int quantityInTheLibrary) {
        for (Book book : bookMap.values()) {
            if (book.getBookISBN() == isbn)
                return false;
        }
        Book book = new Book(title, isbn, author, publishedDate, genre, quantityInTheLibrary);
        bookMap.put(book.getBookId(), book);
        return true;
    }

    @Override
    public boolean removeBookFromLibrary(int bookId) {
        return bookMap.remove(bookId) != null;
    }

}