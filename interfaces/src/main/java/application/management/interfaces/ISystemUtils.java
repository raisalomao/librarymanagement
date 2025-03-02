package application.management.interfaces;

import java.time.LocalDate;
import java.util.List;

public interface ISystemUtils {
    
    public abstract List<String> loadTextFile(String filePath);
    public abstract boolean isValidUserId(int userId);
    public abstract boolean isValidLoanDate(LocalDate loanDate);
    public abstract boolean isValidReturnDate(LocalDate loanDate, LocalDate returnDate);
    public abstract boolean isUserRegistered(int userId, IUser userController);
    public abstract boolean isValidBook(int bookId, IBook bookController);
    public abstract double calculateFine(long daysLate);

}
