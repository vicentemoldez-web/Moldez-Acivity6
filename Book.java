public class Book {
    public String bookId;
    public String title;
    public String author;
    public boolean available;

    // Constructor
    public Book(String bookId, String title, String author, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    // ✅ Add these getter methods:
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Optional: Display method
    public void displayBookDetails() {
        System.out.println(bookId + " - " + title + " by " + author + " [" + (available ? "Available" : "Not Available") + "]");
    }
}