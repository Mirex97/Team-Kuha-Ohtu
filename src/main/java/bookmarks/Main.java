package bookmarks;

import java.util.List;
import java.util.Scanner;

import bookmarks.dao.EntryDao;
import bookmarks.dao.Database;
import bookmarks.domain.Entry;
import bookmarks.io.ConsoleIO;
import bookmarks.io.IO;

public class Main {

    public static Database base;
//    public static Scanner sc;
    public static EntryDao entryDao;

    private IO io;

    public Main(IO io) {
        this.io = io;
    }

    public static void init() {
        base = new Database("jdbc:sqlite:vinkit.db");
        base.getConnection();
        base.createNewTables();

//        sc = new Scanner(System.in);

        entryDao = new EntryDao(base);
    }

    public void addCommand() {
        String title, type, author, description, comment;

//        System.out.print("title: ");
        title = io.readLine("title: ");

//        System.out.print("type: ");
        type = io.readLine("type: ");

//        System.out.print("author: ");
        author = io.readLine("author: ");

//        System.out.print("description: ");
        description = io.readLine("description: ");

//        System.out.print("comment: ");
        comment = io.readLine("comment: ");

        try {
            entryDao.saveOrUpdate(new Entry(title, type, author, description, comment, null, null));
        } catch (Exception e) {
            io.print("Exception :(");
        }
        io.print("Added");
    }

    public void listCommand() {
        try {
            List<Entry> entries = entryDao.findAll();
            entries = entryDao.findAll();
            for (Entry entry : entries) {
                io.print(entry.toString());
            }
        } catch (Exception e) {
            io.print("Exception :(");
        }
    }

    public void helpCommand() {
        io.print("add: add a new entry");
        io.print("list: list all entries");
        io.print("quit: exits the program");
        io.print("help: print this screen");
    }

    public void run() {

        init();

        io.print("program started");
        
        helpCommand();
        

        
        
        while (true) {
            
            String comm = io.readLine("Anna komento:");
            
//            String comm = sc.nextLine();

            if (comm.equals("add")) {
                addCommand();
            } else if (comm.equals("list")) {
                listCommand();
            } else if (comm.equals("quit")) {
                break;
            } else if (comm.equals("help")) {
                helpCommand();
            } else {
                io.print("unrecognized command: " + comm);
                
            }
        }

    }

    public static void main(String[] args) {

        IO io = new ConsoleIO();
        new Main(io).run();

//        init();
//        System.out.println("program started");
//        helpCommand();
//
//        while (true) {
//            String comm = sc.nextLine();
//            if (comm.equals("add")) {
//                addCommand();
//            } else if (comm.equals("list")) {
//                listCommand();
//            } else if (comm.equals("quit")) {
//                break;
//            } else if (comm.equals("help")) {
//                helpCommand();
//            } else {
//                System.out.println("unrecognized command");
//            }
//        }
    }

    public static void tempTestDatabase() {
        boolean errored = false;
        try {
            entryDao.delete(4);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DID NOT SAVE!!!");
        }

        List<Entry> entries = null;
        Entry entry = null;
        try {
            entries = entryDao.findAll();
            entry = entryDao.findOne(1);
        } catch (Exception e) {
            errored = true;
        }
        if (!errored) {
            for (Entry e : entries) {
                System.out.println(e);
            }
        }
    }
}
