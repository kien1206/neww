package entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import util.ConnectionDB;

public class BookType {
    private int typeId;
    private String typeName;
    private boolean isDeleted;

    // Constructor không tham số
    public BookType() {
    }

    // Constructor có tham số
    public BookType(int typeId, String typeName, boolean isDeleted) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.isDeleted = isDeleted;
    }

    // Getter và Setter
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    // Triển khai phương thức inputData
    public void inputData(Scanner scanner) {
        System.out.print("Nhập tên loại sách: ");
        this.typeName = scanner.nextLine();
        this.isDeleted = false; // Mặc định là chưa bị xóa
    }

    // Triển khai phương thức displayData
    public void displayData() {
        System.out.println("Mã loại sách: " + this.typeId);
        System.out.println("Tên loại sách: " + this.typeName);
        System.out.println("Trạng thái: " + (this.isDeleted ? "Đã xóa" : "Chưa xóa"));
    }

    // Method to save a book type to the database
    public void save() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "INSERT INTO BookType (TypeName, IsDeleted) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.typeName);
            preparedStatement.setBoolean(2, this.isDeleted);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book type saved successfully!");
            } else {
                System.out.println("Failed to save the book type.");
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

    // Method to update a book type in the database
    public void update() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE BookType SET TypeName = ?, IsDeleted = ? WHERE TypeId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.typeName);
            preparedStatement.setBoolean(2, this.isDeleted);
            preparedStatement.setInt(3, this.typeId);

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

    // Method to delete a book type (soft delete) in the database
    public void delete() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "UPDATE BookType SET IsDeleted = 1 WHERE TypeId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.typeId);

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

    // Method to fetch a book type by its ID
    public static BookType getBookTypeById(int typeId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        BookType bookType = null;

        try {
            connection = ConnectionDB.openConnection();
            String sql = "SELECT * FROM BookType WHERE TypeId = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, typeId);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bookType = new BookType(
                        resultSet.getInt("TypeId"),
                        resultSet.getString("TypeName"),
                        resultSet.getBoolean("IsDeleted")
                );
            } else {
                System.out.println("No book type found with ID: " + typeId);
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

        return bookType;
    }
}
