import java.io.*;
import java.util.*;

public class LibrarySystem {
    public List<User> users = new ArrayList<>();
    public List<Book> books = new ArrayList<>();
    public List<Transaction> transactions = new ArrayList<>();
    public User loggedInUser = null;

    public void start() {
        loadUsers();
        loadBooks();
        loadTransactions();

        login();

        if (loggedInUser != null) {
            if (loggedInUser.getRole().equals("admin")) {
                adminMenu();
            } else {
                userMenu();
            }
        }
    }

    public void login() {
        Scanner sc = new Scanner(System.in);
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("Username: ");
            String name = sc.nextLine().trim();
            System.out.print("Password: ");
            String password = sc.nextLine().trim();

            for (User u : users) {
                if (u.name.equalsIgnoreCase(name) && u.getPassword().equals(password)) {
                    System.out.println("\nLogin successful! Welcome, " + u.name);
                    loggedInUser = u;
                    return;
                }
            }

            attempts--;
            System.out.println("Invalid credentials. Attempts left: " + attempts);
        }

        System.out.println("Too many failed attempts. Exiting...");
        System.exit(0);
    }

    public void userMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. View All Books\n2. Borrow Book\n3. Return Book\n4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: viewAllBooks(); break;
                case 2: borrowBook(); break;
                case 3: returnBook(); break;
                case 4: saveAll(); System.exit(0);
                default: System.out.println("Invalid choice.");
            }
        }
    }

    public void viewAllBooks() {
        for (Book b : books) {
            b.displayBookDetails();
        }
    }

    public void returnBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID to return: ");
        String id = sc.nextLine();

        if (!loggedInUser.getBorrowedBooks().contains(id)) {
            System.out.println("You haven't borrowed this book.");
            return;
        }

        for (Book b : books) {
            if (b.getBookId().equals(id)) {
                b.setAvailable(true);
                loggedInUser.returnBook(id);

                for (Transaction t : transactions) {
                    if (t.getUserId().equals(loggedInUser.id) && t.getBookId().equals(id) && t.dateReturned.equals("null")) {
                        t.dateReturned = getCurrentDate();
                        break;
                    }
                }

                System.out.println("Book returned successfully.");
                return;
            }
        }

        System.out.println("Book not found.");
    }

    // Utility Methods

    public void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                users.add(new User(data[0], data[1], data[2], data[3]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("users.txt not found.");
        } catch (IOException e) {
            System.out.println("Error reading users.txt");
        }
    }

    public void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                books.add(new Book(data[0], data[1], data[2], Boolean.parseBoolean(data[3])));
            }
        } catch (Exception e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    public void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                transactions.add(new Transaction(data[0], data[1], data[2], data[3], data[4]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("transactions.txt not found.");
        } catch (IOException e) {
            System.out.println("Error reading transactions.txt");
        }
    }

    public void saveAll() {
        saveUsers();
        saveBooks();
        saveTransactions();
    }

    public void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User u : users) {
                bw.write(u.id + "," + u.name + "," + u.getPassword() + "," + u.getRole());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    public void saveBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book b : books) {
                bw.write(b.getBookId() + "," + b.title + "," + b.author + "," + b.isAvailable());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books.");
        }
    }

    public void saveTransactions() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.txt"))) {
            for (Transaction t : transactions) {
                bw.write(t.transactionId + "," + t.userId + "," + t.bookId + "," + t.dateBorrowed + "," + t.dateReturned);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions.");
        }
    }

    public String generateTransactionId() {
        return String.format("T%03d", transactions.size() + 1);
    }

    public String getCurrentDate() {
        return java.time.LocalDate.now().toString(); // YYYY-MM-DD
    }

    public void adminMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Books");
            System.out.println("2. Add User");
            System.out.println("3. Add Book");
            System.out.println("4. View Transactions");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1: viewAllBooks(); break;
                case 2: addUser(); break;
                case 3: addBook(); break;
                case 4: viewTransactions(); break;
                case 5: saveAll(); System.exit(0);
                default: System.out.println("Invalid option.");
            }
        }
    }

    public void addUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        System.out.print("Enter Role (user/admin): ");
        String role = sc.nextLine();

        users.add(new User(id, name, password, role));
        System.out.println("User added successfully.");
    }

    public void addBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author: ");
        String author = sc.nextLine();

        books.add(new Book(id, title, author, true));
        System.out.println("Book added successfully.");
    }

    public void viewTransactions() {
        for (Transaction t : transactions) {
            t.displayTransaction();
        }
    }

    public void searchBooks() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Search by:\n1. Title\n2. Author");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine().toLowerCase();

        boolean found = false;
        for (Book b : books) {
            if ((choice == 1 && b.getTitle().toLowerCase().contains(keyword)) ||
                (choice == 2 && b.getAuthor().toLowerCase().contains(keyword))) {
                b.displayBookDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching books found.");
        }
    }

    public void borrowBook() {
    Scanner sc = new Scanner(System.in);
    if (loggedInUser.getBorrowedBooks().size() >= 3) {
        System.out.println("You cannot borrow more than 3 books.");
        return;
    }

    System.out.print("Enter Book ID: ");
    String id = sc.nextLine();

    for (Book b : books) {
        if (b.getBookId().equals(id)) {
            if (!b.isAvailable()) {
                System.out.println("Book is currently not available.");
                return;
            }

            b.setAvailable(false);
            loggedInUser.borrowBook(id);
            transactions.add(new Transaction(
                    generateTransactionId(),
                    loggedInUser.id,
                    id,
                    getCurrentDate(),
                    "null"
            ));
            System.out.println("Book borrowed successfully!");
            return;
        }
    }

    System.out.println("Book not found.");
}
}
