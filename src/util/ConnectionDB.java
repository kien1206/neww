package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/quanlythuvien";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "";

    // Mở kết nối với cơ sở dữ liệu
    public static Connection openConnection() {
        Connection connection;
        try {
            // Đăng ký driver
            Class.forName(DRIVER);
            // Kết nối tới cơ sở dữ liệu
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Kết nối không thành công: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return connection;
    }

    // Đóng kết nối
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Đóng kết nối thành công!");
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}