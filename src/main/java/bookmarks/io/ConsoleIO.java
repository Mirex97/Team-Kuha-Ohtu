/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookmarks.io;

import bookmarks.domain.Entry;
import java.util.Scanner;

/**
 *
 * @author Harri
 */
public class ConsoleIO implements IO {
    private Scanner scanner = new Scanner(System.in);
    
    public void print(String toPrint) {
        System.out.println(toPrint);
    }

    public int readInt(String prompt) {
        System.out.println(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    public String readLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

//    @Override
//    public void printEntry(Entry toPrint) {
//        System.out.println(toPrint);
//    }
    
}