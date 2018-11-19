package bookmarks;

import java.util.List;
import java.util.Scanner;

import bookmarks.dao.EntryDao;
import bookmarks.dao.Database;
import bookmarks.domain.Entry;

public class Main {
	public static Database base;
	public static Scanner sc;
	public static EntryDao entryDao;

	public static void init() {
		base = new Database("jdbc:sqlite:vinkit.db");
		base.getConnection();
		base.createNewTables();
		
		sc = new Scanner(System.in);
		
		entryDao = new EntryDao(base);
	}

	public static void addCommand() {
		String title, type, author, description, comment;
		
		System.out.print("title: ");
		title = sc.nextLine();

		System.out.print("type: ");
		type = sc.nextLine();
		
		System.out.print("author: ");
		author = sc.nextLine();

		System.out.print("description: ");
		description = sc.nextLine();

		System.out.print("comment: ");
		comment = sc.nextLine();

		try {
			entryDao.saveOrUpdate(new Entry(title, type, author, description, comment, null, null));
		} catch(Exception e) {
			System.out.println("Exception :(");
		}
	}
	
	public static void listCommand() {
		try {
			List<Entry> entries = entryDao.findAll();
			entries = entryDao.findAll();
			for (Entry entry : entries) System.out.println(entry);
		} catch (Exception e) {
			System.out.println("Exception :(");
		}
	}

	public static void helpCommand() {
		System.out.println("add: add a new entry");
		System.out.println("list: list all entries");
		System.out.println("quit: exits the program");
		System.out.println("help: print this screen");
	}

	public static void main(String[] args) {
		init();
		
		System.out.println("program started");
		helpCommand();

		while(true) {
			String comm = sc.nextLine();
			if (comm.equals("add")) {
				addCommand();
			} else if (comm.equals("list")) {
				listCommand();
			} else if (comm.equals("quit")) {
				break;
			} else if (comm.equals("help")) {
				helpCommand();
			} else {
				System.out.println("unrecognized command");
			}
		}
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
