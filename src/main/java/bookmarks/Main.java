package bookmarks;

import java.util.List;
import java.util.Scanner;

import bookmarks.dao.EntryDao;
import bookmarks.dao.Database;
import bookmarks.domain.Entry;

public class Main {
	public static Database base;

	public static void init() {
		base = new Database("jdbc:sqlite:vinkit.db");
		base.getConnection();
		base.createNewTables();
	}

	public static void printHelp() {
		System.out.println("add: add a new entry");
		System.out.println("list: list all entries");
		System.out.println("quit: exits the program");
		System.out.println("help: print this screen");
	}

	public static void main(String[] args) {
		init();
		
		Scanner sc = new Scanner(System.in);

		System.out.println("program started");
		printHelp();

		while(true) {
			String comm = sc.nextLine();
			if (comm.equals("add")) {
				System.out.println("TODO");
			} else if (comm.equals("list")) {
				System.out.println("TODO");
			} else if (comm.equals("quit")) {
				break;
			} else if (comm.equals("help")) {
				printHelp();
			} else {
				System.out.println("unrecognized command");
			}
		}
	}

	public static void tempTestDatabase() {
		boolean errored = false;
		EntryDao entryDao = new EntryDao(base);
		try {
			entryDao.saveOrUpdate(new Entry(0, "Ei Otsikkoa", "book", "Minä", "12345", "Ei kommenttia", null, null));
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
