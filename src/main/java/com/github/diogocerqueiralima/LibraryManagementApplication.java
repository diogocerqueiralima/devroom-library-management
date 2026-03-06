package com.github.diogocerqueiralima;

import com.github.diogocerqueiralima.model.Book;
import com.github.diogocerqueiralima.model.Patron;
import com.github.diogocerqueiralima.repositories.BookRepository;
import com.github.diogocerqueiralima.repositories.PatronRepository;
import com.github.diogocerqueiralima.services.BookService;
import com.github.diogocerqueiralima.services.LibraryService;
import com.github.diogocerqueiralima.services.PatronService;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;

public class LibraryManagementApplication {

    private final static String BOOKS_FILE_PATH = "./books.csv";
    private final static String PATRONS_FILE_PATH = "./patrons.csv";

    public static void main(String[] args) {

        // 1. Setup files
        setupFiles();

        // 2. Initialize repositories
        BookRepository bookRepository = new BookRepository(BOOKS_FILE_PATH);
        PatronRepository patronRepository = new PatronRepository(PATRONS_FILE_PATH, bookRepository);

        // 3. Initialize services
        BookService bookService = new BookService(bookRepository);
        PatronService patronService = new PatronService(patronRepository);
        LibraryService libraryService = new LibraryService(bookRepository, patronRepository);

        // 4. Start the menu loop
        menu(bookService, patronService, libraryService);
    }

    private static void setupFiles() {
        // Create the books.csv and patrons.csv files if they don't exist
        try {
            new File(BOOKS_FILE_PATH).createNewFile();
            new File(PATRONS_FILE_PATH).createNewFile();
        } catch (java.io.IOException e) {
            System.err.println("Error creating files: " + e.getMessage());
        }
    }

    private static void menu(BookService bookService, PatronService patronService, LibraryService libraryService) {

        int option;

        do {

            try {
                printMenu();
                option = getOption();

                switch (option) {
                    case 1 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter book title:");
                        String title = scanner.nextLine();
                        System.out.println("Enter book author:");
                        String author = scanner.nextLine();
                        System.out.println("Enter book year:");
                        String year = scanner.nextLine();
                        bookService.add(title, author, year);
                    }
                    case 2 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter book ID to update:");
                        UUID id = UUID.fromString(scanner.nextLine());
                        System.out.println("Enter new book title:");
                        String title = scanner.nextLine();
                        System.out.println("Enter new book author:");
                        String author = scanner.nextLine();
                        System.out.println("Enter new book year:");
                        String year = scanner.nextLine();
                        bookService.update(id, title, author, year);
                    }
                    case 3 -> {
                        bookService.getAll().forEach(book -> {
                            System.out.println("ID: " + book.getId());
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Year: " + book.getYear());
                            System.out.println("Status: " + book.getStatus());
                            System.out.println();
                        });
                    }
                    case 4 -> {
                        bookService.getAvailableBooks().forEach(book -> {
                            System.out.println("ID: " + book.getId());
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Year: " + book.getYear());
                            System.out.println();
                        });
                    }
                    case 5 -> {
                        bookService.getCheckedOutBooks().forEach(book -> {
                            System.out.println("ID: " + book.getId());
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Year: " + book.getYear());
                            System.out.println();
                        });
                    }
                    case 6 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter author name:");
                        String author = scanner.nextLine();
                        bookService.getAllByAuthor(author).forEach(book -> {
                            System.out.println("ID: " + book.getId());
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Year: " + book.getYear());
                            System.out.println("Status: " + book.getStatus());
                            System.out.println();
                        });
                    }
                    case 7 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter book title:");
                        String title = scanner.nextLine();
                        bookService.getAllByTitle(title).forEach(book -> {
                            System.out.println("ID: " + book.getId());
                            System.out.println("Title: " + book.getTitle());
                            System.out.println("Author: " + book.getAuthor());
                            System.out.println("Year: " + book.getYear());
                            System.out.println("Status: " + book.getStatus());
                            System.out.println();
                        });
                    }
                    case 8 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter book ID to delete:");
                        UUID id = UUID.fromString(scanner.nextLine());
                        bookService.delete(id);
                    }
                    case 9 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter patron name:");
                        String name = scanner.nextLine();
                        Patron patron = patronService.add(name);
                        System.out.println("Patron added with ID: " + patron.getId());
                    }
                    case 10 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter patron ID to update:");
                        UUID id = UUID.fromString(scanner.nextLine());
                        System.out.println("Enter new patron name:");
                        String name = scanner.nextLine();
                        patronService.update(id, name);
                    }
                    case 11 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter patron ID to delete:");
                        UUID id = UUID.fromString(scanner.nextLine());
                        patronService.delete(id);
                    }
                    case 12 -> {
                        patronService.getAll().forEach(patron -> {
                            System.out.println("ID: " + patron.getId());
                            System.out.println("Name: " + patron.getName());
                            System.out.println("Checked out books: " + patron.getCheckedOutBooks().stream().map(Book::getTitle).toList());
                            System.out.println();
                        });
                    }
                    case 13 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter patron ID:");
                        UUID patronId = UUID.fromString(scanner.nextLine());
                        System.out.println("Enter book ID:");
                        UUID bookId = UUID.fromString(scanner.nextLine());
                        libraryService.borrow(patronId, bookId);
                    }
                    case 14 -> {
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Enter patron ID:");
                        UUID patronId = UUID.fromString(scanner.nextLine());
                        System.out.println("Enter book ID:");
                        UUID bookId = UUID.fromString(scanner.nextLine());
                        libraryService.returnBook(patronId, bookId);
                    }
                    case 99 -> System.exit(0);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        } while (true);

    }

    private static int getOption() {

        Scanner scanner = new Scanner(System.in);

        try {
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid option. Please try again.");
            return getOption();
        }

    }

    private static void printMenu() {

        System.out.println();
        System.out.println("Library Management System");
        System.out.println();
        System.out.println("a) Books");
        System.out.println("\t1 - Add a new book");
        System.out.println("\t2 - Update a book");
        System.out.println("\t3 - List all books");
        System.out.println("\t4 - List all available books");
        System.out.println("\t5 - List all checked out books");
        System.out.println("\t6 - List all books by a specific author");
        System.out.println("\t7 - List all books with a specific title");
        System.out.println("\t8 - Delete a book");
        System.out.println();
        System.out.println("b) Patrons");
        System.out.println("\t9 - Add a new patron");
        System.out.println("\t10 - Update a patron");
        System.out.println("\t11 - Delete a patron");
        System.out.println("\t12 - List all patrons");
        System.out.println();
        System.out.println("c) Library");
        System.out.println("\t13 - Check out a book");
        System.out.println("\t14 - Return a book");
        System.out.println();
        System.out.println("Exit - 99");

    }

}
