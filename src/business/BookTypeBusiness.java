package business;

import entity.BookType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.ConnectionDB;

public class BookTypeBusiness {

    // Method to add a new book type
    public void addBookType(BookType bookType) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "INSERT INTO BookType (TypeName, IsDeleted) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bookType.getTypeName());
            preparedStatement.setBoolean(2, bookType.isDeleted());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book type added successfully!");
            } else {
                System.out.println("Failed to add the book type.");
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

    // Method to update an existing book type
    public void updateBookType(BookType bookType) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE BookType SET TypeName = ?, IsDeleted = ? WHERE TypeId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bookType.getTypeName());
            preparedStatement.setBoolean(2, bookType.isDeleted());
            preparedStatement.setInt(3, bookType.getTypeId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book type updated successfully!");
            } else {
                System.out.println("Failed to update the book type.");
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

    // Method to delete a book type (soft delete)
    public void deleteBookType(int typeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE BookType SET IsDeleted = 1 WHERE TypeId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, typeId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book type deleted successfully!");
            } else {
                System.out.println("Failed to delete the book type.");
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

    // Method to list all book types
    public List<BookType> listBookTypes() {
        List<BookType> bookTypes = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT * FROM BookType WHERE IsDeleted = 0";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                BookType bookType = new BookType(
                        resultSet.getInt("TypeId"),
                        resultSet.getString("TypeName"),
                        resultSet.getBoolean("IsDeleted")
                );
                bookTypes.add(bookType);
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

        return bookTypes;
    }

    // Method to get a book type by ID
    public BookType getBookTypeById(int typeId) {
        return BookType.getBookTypeById(typeId);
    }

    // Method to get the number of books by type
    public void getBookCountByType() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT bt.TypeName, COUNT(b.BookId) AS BookCount " +
                    "FROM BookType bt LEFT JOIN Book b ON bt.TypeId = b.TypeId " +
                    "WHERE bt.IsDeleted = 0 " +
                    "GROUP BY bt.TypeId";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String typeName = resultSet.getString("TypeName");
                int bookCount = resultSet.getInt("BookCount");
                System.out.println("Loại sách: " + typeName + ", Số lượng sách: " + bookCount);
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
    }
}
