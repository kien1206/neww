package business;

import entity.Book;
import util.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookBusiness {

    // Thêm sách mới
    public void addBookWithTransaction(String bookName, String title, String author, int totalPages, String publisher, double price, int typeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            connection.setAutoCommit(false); // Start transaction

            String sql = "INSERT INTO Book (BookName, Title, Author, TotalPages, Publisher, Price, TypeId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bookName);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, author);
            preparedStatement.setInt(4, totalPages);
            preparedStatement.setString(5, publisher);
            preparedStatement.setDouble(6, price);
            preparedStatement.setInt(7, typeId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                connection.commit(); // Commit transaction
                System.out.println("Book added successfully!");
            } else {
                connection.rollback(); // Rollback transaction
                System.out.println("Failed to add the book.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction in case of error
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
        } finally {
            ConnectionDB.closeConnection(connection);
        }
    }

    // Cập nhật thông tin sách
    public void updateBook(Book book) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE Book SET BookName = ?, Title = ?, Author = ?, TotalPages = ?, Publisher = ?, Price = ?, TypeId = ?, IsDeleted = ? WHERE BookId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getBookName());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setInt(4, book.getTotalPages());
            preparedStatement.setString(5, book.getPublisher());
            preparedStatement.setDouble(6, book.getPrice());
            preparedStatement.setInt(7, book.getTypeId());
            preparedStatement.setBoolean(8, book.isDeleted());
            preparedStatement.setInt(9, book.getBookId());
            preparedStatement.executeUpdate();

            System.out.println("Book updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(connection);
        }
    }

    // Xóa sách
    public void deleteBook(int bookId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE Book SET IsDeleted = true WHERE BookId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Failed to delete the book.");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(connection);
        }
    }

    // Liệt kê tất cả sách
    public List<Book> listBooks() {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT * FROM Book WHERE IsDeleted = false";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("BookId"));
                book.setBookName(resultSet.getString("BookName"));
                book.setTitle(resultSet.getString("Title"));
                book.setAuthor(resultSet.getString("Author"));
                book.setTotalPages(resultSet.getInt("TotalPages"));
                book.setPublisher(resultSet.getString("Publisher"));
                book.setPrice(resultSet.getDouble("Price"));
                book.setTypeId(resultSet.getInt("TypeId"));
                book.setDeleted(resultSet.getBoolean("IsDeleted"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(connection);
        }

        return books;
    }

    // Liệt kê sách theo giá giảm dần
    public List<Book> listBooksByPriceDescending() {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT * FROM Book WHERE IsDeleted = false ORDER BY Price DESC";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("BookId"));
                book.setBookName(resultSet.getString("BookName"));
                book.setTitle(resultSet.getString("Title"));
                book.setAuthor(resultSet.getString("Author"));
                book.setTotalPages(resultSet.getInt("TotalPages"));
                book.setPublisher(resultSet.getString("Publisher"));
                book.setPrice(resultSet.getDouble("Price"));
                book.setTypeId(resultSet.getInt("TypeId"));
                book.setDeleted(resultSet.getBoolean("IsDeleted"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(connection);
        }

        return books;
    }

    // Tìm kiếm sách theo tên hoặc nội dung
    public List<Book> searchBooks(String query) {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT * FROM Book WHERE IsDeleted = false AND (BookName LIKE ? OR Title LIKE ?)";
            preparedStatement = connection.prepareStatement(sql);
            String searchQuery = "%" + query + "%";
            preparedStatement.setString(1, searchQuery);
            preparedStatement.setString(2, searchQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt("BookId"));
                book.setBookName(resultSet.getString("BookName"));
                book.setTitle(resultSet.getString("Title"));
                book.setAuthor(resultSet.getString("Author"));
                book.setTotalPages(resultSet.getInt("TotalPages"));
                book.setPublisher(resultSet.getString("Publisher"));
                book.setPrice(resultSet.getDouble("Price"));
                book.setTypeId(resultSet.getInt("TypeId"));
                book.setDeleted(resultSet.getBoolean("IsDeleted"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(connection);
        }

        return books;
    }

    // Đếm số lượng sách theo mã loại sách
    public void getBookCountByType() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT TypeId, COUNT(*) AS Count FROM Book WHERE IsDeleted = false GROUP BY TypeId";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int typeId = resultSet.getInt("TypeId");
                int count = resultSet.getInt("Count");
                System.out.println("Type ID " + typeId + ": " + count + " books");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(connection);
        }
    }

    // Lấy thông tin sách theo ID
    public Book getBookById(int bookId) {
        Book book = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT * FROM Book WHERE BookId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                book = new Book();
                book.setBookId(resultSet.getInt("BookId"));
                book.setBookName(resultSet.getString("BookName"));
                book.setTitle(resultSet.getString("Title"));
                book.setAuthor(resultSet.getString("Author"));
                book.setTotalPages(resultSet.getInt("TotalPages"));
                book.setPublisher(resultSet.getString("Publisher"));
                book.setPrice(resultSet.getDouble("Price"));
                book.setTypeId(resultSet.getInt("TypeId"));
                book.setDeleted(resultSet.getBoolean("IsDeleted"));
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(connection);
        }

        return book;
    }
}
