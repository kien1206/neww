package presentation;

import business.BookBusiness;
import business.BookTypeBusiness;
import entity.Book;
import entity.BookType;

import java.util.List;
import java.util.Scanner;

public class BookManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookBusiness bookBusiness = new BookBusiness();
    private static final BookTypeBusiness bookTypeBusiness = new BookTypeBusiness();

    public static void main(String[] args) {
        while (true) {
            System.out.println("******************BOOK-MANAGEMENT******************");
            System.out.println("1. Quản lý loại sách");
            System.out.println("2. Quản lý sách");
            System.out.println("3. Thoát");

            int choice = getIntInput("Chọn lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    manageBookTypes();
                    break;
                case 2:
                    manageBooks();
                    break;
                case 3:
                    System.out.println("Thoát ứng dụng.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private static void manageBookTypes() {
        while (true) {
            System.out.println("**********************BOOKTYPE-MENU********************");
            System.out.println("1. Danh sách loại sách");
            System.out.println("2. Tạo mới loại sách");
            System.out.println("3. Cập nhật thông tin loại sách");
            System.out.println("4. Xóa loại sách");
            System.out.println("5. Thống kê số lượng sách theo mã loại sách");
            System.out.println("6. Thoát");

            int choice = getIntInput("Chọn lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    listBookTypes();
                    break;
                case 2:
                    addBookType();
                    break;
                case 3:
                    updateBookType();
                    break;
                case 4:
                    deleteBookType();
                    break;
                case 5:
                    countBooksByType();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private static void manageBooks() {
        while (true) {
            System.out.println("***********************BOOK-MENU***********************");
            System.out.println("1. Danh sách sách");
            System.out.println("2. Tạo mới sách");
            System.out.println("3. Cập nhật thông tin sách");
            System.out.println("4. Xóa sách");
            System.out.println("5. Hiển thị danh sách các cuốn sách theo giá giảm dần");
            System.out.println("6. Tìm kiếm sách theo tên hoặc nội dung");
            System.out.println("7. Thống kê số lượng sách theo nhóm");
            System.out.println("8. Thoát");

            int choice = getIntInput("Chọn lựa chọn của bạn: ");
            switch (choice) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                    listBooksByPriceDescending();
                    break;
                case 6:
                    searchBooks();
                    break;
                case 7:
                    categorizeBooks();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private static void listBookTypes() {
        List<BookType> bookTypes = bookTypeBusiness.listBookTypes();
        if (bookTypes.isEmpty()) {
            System.out.println("Không có loại sách nào.");
        } else {
            for (BookType bookType : bookTypes) {
                bookType.displayData();
            }
        }
    }

    private static void addBookType() {
        BookType bookType = new BookType();
        bookType.inputData(scanner);
        bookTypeBusiness.addBookType(bookType);
    }

    private static void updateBookType() {
        int typeId = getIntInput("Nhập mã loại sách cần cập nhật: ");
        BookType bookType = bookTypeBusiness.getBookTypeById(typeId);
        if (bookType == null) {
            System.out.println("Loại sách không tồn tại.");
            return;
        }
        bookType.displayData();
        System.out.println("Nhập thông tin mới:");
        bookType.inputData(scanner);
        bookTypeBusiness.updateBookType(bookType);
    }

    private static void deleteBookType() {
        int typeId = getIntInput("Nhập mã loại sách cần xóa: ");
        bookTypeBusiness.deleteBookType(typeId);
    }

    private static void countBooksByType() {
        bookTypeBusiness.getBookCountByType();
    }

    private static void listBooks() {
        List<Book> books = bookBusiness.listBooks();
        if (books.isEmpty()) {
            System.out.println("Không có sách nào.");
        } else {
            for (Book book : books) {
                book.displayData();
            }
        }
    }

    private static void addBook() {
        Book book = new Book();
        book.inputData(scanner);
        bookBusiness.addBookWithTransaction(
                book.getBookName(), book.getTitle(), book.getAuthor(), book.getTotalPages(),
                book.getPublisher(), book.getPrice(), book.getTypeId()
        );
    }

    private static void updateBook() {
        int bookId = getIntInput("Nhập mã sách cần cập nhật: ");
        Book book = bookBusiness.getBookById(bookId);
        if (book == null) {
            System.out.println("Sách không tồn tại.");
            return;
        }
        book.displayData();
        System.out.println("Nhập thông tin mới:");
        book.inputData(scanner);
        bookBusiness.updateBook(book);
    }

    private static void deleteBook() {
        int bookId = getIntInput("Nhập mã sách cần xóa: ");
        bookBusiness.deleteBook(bookId);
    }

    private static void listBooksByPriceDescending() {
        List<Book> books = bookBusiness.listBooksByPriceDescending();
        if (books.isEmpty()) {
            System.out.println("Không có sách nào.");
        } else {
            for (Book book : books) {
                book.displayData();
            }
        }
    }

    private static void searchBooks() {
        String searchQuery = getStringInput("Nhập tên hoặc nội dung sách để tìm kiếm: ");
        List<Book> books = bookBusiness.searchBooks(searchQuery);
        if (books.isEmpty()) {
            System.out.println("Không tìm thấy sách phù hợp.");
        } else {
            for (Book book : books) {
                book.displayData();
            }
        }
    }

    private static void categorizeBooks() {
        List<Book> books = bookBusiness.listBooks();
        if (books.isEmpty()) {
            System.out.println("Không có sách nào.");
            return;
        }

        int group1Count = 0;
        int group2Count = 0;
        int group3Count = 0;

        for (Book book : books) {
            if (book.getTotalPages() < 50) {
                group1Count++;
            } else if (book.getTotalPages() < 300) {
                group2Count++;
            } else {
                group3Count++;
            }
        }

        System.out.println("Nhóm 1 (Số trang < 50): " + group1Count);
        System.out.println("Nhóm 2 (50 <= Số trang < 300): " + group2Count);
        System.out.println("Nhóm 3 (Số trang >= 300): " + group3Count);
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Lựa chọn không hợp lệ. " + prompt);
            scanner.next(); // Clear invalid input
        }
        return scanner.nextInt();
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        scanner.nextLine(); // Consume newline left-over
        return scanner.nextLine();
    }
}
