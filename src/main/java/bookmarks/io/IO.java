/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookmarks.io;

import bookmarks.domain.Entry;

/**
 *
 * @author Harri
 */
public interface IO {
    void print(String toPrint);
//    void printEntry(Entry toPrint);
    int readInt(String prompt);
    String readLine(String prompt);
}
