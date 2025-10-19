import java.util.ArrayList;

public class User extends Person {
    public String password;
    public String role;
    public ArrayList<String> borrowedBooks = new ArrayList<>();

    public User(String id, String name, String password, String role) {
        super(id, name);
        this.password = password;
        this.role = role;
    }

    public String getPassword() { return password; }
    public String getRole() { return role; }

    public ArrayList<String> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(String bookId) {
        borrowedBooks.add(bookId);
    }

    public void returnBook(String bookId) {
        borrowedBooks.remove(bookId);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Role: " + role + ", Borrowed: " + borrowedBooks);
    }
}