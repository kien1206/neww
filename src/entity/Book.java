package entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import util.ConnectionDB;

public class Book implements IBookManagement {
    private int bookId;
    private String bookName;
    private String title;
    private String author;
    private int totalPages;
    private String publisher;
    private double price;
    private int typeId;
    private boolean isDeleted;

    // Constructor không tham số
    public Book() {
    }

    // Constructor có tham số
    public Book(int bookId, String bookName, String title, String author, int totalPages, String publisher, double price, int typeId, boolean isDeleted) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.publisher = publisher;
        this.price = price;
        this.typeId = typeId;
        this.isDeleted = isDeleted;
    }

    // Getter và Setter
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    // Triển khai phương thức inputData
    @Override
    public void inputData(Scanner scanner) {
        System.out.print("Nhập tên sách: ");
        this.bookName = scanner.nextLine();
        System.out.print("Nhập tiêu đề: ");
        this.title = scanner.nextLine();
        System.out.print("Nhập tác giả: ");
        this.author = scanner.nextLine();
        System.out.print("Nhập số trang: ");
        this.totalPages = scanner.nextInt();
        scanner.nextLine(); // Xóa bộ nhớ đệm
        System.out.print("Nhập nhà xuất bản: ");
        this.publisher = scanner.nextLine();
        System.out.print("Nhập giá: ");
        this.price = scanner.nextDouble();
        scanner.nextLine(); // Xóa bộ nhớ đệm
        System.out.print("Nhập mã loại sách: ");
        this.typeId = scanner.nextInt();
        this.isDeleted = false; // Mặc định là chưa bị xóa
    }

    // Triển khai phương thức displayData
    @Override
    public void displayData() {
        System.out.println("Mã sách: " + this.bookId);
        System.out.println("Tên sách: " + this.bookName);
        System.out.println("Tiêu đề: " + this.title);
        System.out.println("Tác giả: " + this.author);
        System.out.println("Số trang: " + this.totalPages);
        System.out.println("Nhà xuất bản: " + this.publisher);
        System.out.println("Giá: " + this.price);
        System.out.println("Mã loại sách: " + this.typeId);
        System.out.println("Trạng thái: " + (this.isDeleted ? "Đã xóa" : "Chưa xóa"));
    }

    // Method to save a book to the database
    public void save() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "INSERT INTO Book (BookName, Title, Author, TotalPages, Publisher, Price, TypeId, IsDeleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.bookName);
            preparedStatement.setString(2, this.title);
            preparedStatement.setString(3, this.author);
            preparedStatement.setInt(4, this.totalPages);
            preparedStatement.setString(5, this.publisher);
            preparedStatement.setDouble(6, this.price);
            preparedStatement.setInt(7, this.typeId);
            preparedStatement.setBoolean(8, this.isDeleted);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book saved successfully!");
            } else {
                System.out.println("Failed to save the book.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            ConnectionDB.closeConnection(connection);
        }
    }

    // Method to update a book in the database
    public void update() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE Book SET BookName = ?, Title = ?, Author = ?, TotalPages = ?, Publisher = ?, Price = ?, TypeId = ?, IsDeleted = ? WHERE BookId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.bookName);
            preparedStatement.setString(2, this.title);
            preparedStatement.setString(3, this.author);
            preparedStatement.setInt(4, this.totalPages);
            preparedStatement.setString(5, this.publisher);
            preparedStatement.setDouble(6, this.price);
            preparedStatement.setInt(7, this.typeId);
            preparedStatement.setBoolean(8, this.isDeleted);
            preparedStatement.setInt(9, this.bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book updated successfully!");
            } else {
                System.out.println("Failed to update the book.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            ConnectionDB.closeConnection(connection);
        }
    }

    // Method to delete a book (soft delete) in the database
    public void delete() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE Book SET IsDeleted = 1 WHERE BookId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Failed to delete the book.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            ConnectionDB.closeConnection(connection);
        }
    }

    // Method to fetch a book by its ID
    public static Book getBookById(int bookId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Book book = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT * FROM Book WHERE BookId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                book = new Book(
                        resultSet.getInt("BookId"),
                        resultSet.getString("BookName"),
                        resultSet.getString("Title"),
                        resultSet.getString("Author"),
                        resultSet.getInt("TotalPages"),
                        resultSet.getString("Publisher"),
                        resultSet.getDouble("Price"),
                        resultSet.getInt("TypeId"),
                        resultSet.getBoolean("IsDeleted")
                );
            } else {
                System.out.println("No book found with ID: " + bookId);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            ConnectionDB.closeConnection(connection);
        }

        return book;
    }
}
