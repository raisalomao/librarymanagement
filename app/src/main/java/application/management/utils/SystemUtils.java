package application.management.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.management.interfaces.IBook;
import application.management.interfaces.ISystemUtils;
import application.management.interfaces.IUser;

public class SystemUtils implements ISystemUtils {

    @Override
    public List<String> loadTextFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public boolean isValidUserId(int userId) {
        return userId > 0;
    }

    @Override
    public boolean isValidLoanDate(LocalDate loanDate) {
        return loanDate != null && !loanDate.isAfter(LocalDate.now());
    }

    @Override
    public boolean isValidReturnDate(LocalDate loanDate, LocalDate returnDate) {
        return returnDate == null || !returnDate.isBefore(loanDate);
    }

    @Override
    public boolean isUserRegistered(int userId, IUser userController) {
        return userController.getListUserMap().values().stream().anyMatch(user -> user.getUserId() == userId);
    }

    @Override
    public boolean isValidBook(int bookId, IBook bookController) {
        return bookController.getListBookMap().values().stream().anyMatch(book -> book.getBookId() == bookId);
    }

    @Override
    public double calculateFine(long daysLate) {
        double finePerDay = 0.50;
        return daysLate * finePerDay;
    }

}