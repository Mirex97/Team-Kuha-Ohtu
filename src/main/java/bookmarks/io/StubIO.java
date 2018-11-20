/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookmarks.io;

import bookmarks.domain.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Harri
 */
public class StubIO implements IO {

    private List<String> lines;
    private int i;
    private ArrayList<String> prints;

    
    public StubIO(List<String> values) {
        this.lines = values;
        i = 0;
        prints = new ArrayList<>();
    }

    public void print(String toPrint) {
        prints.add(toPrint);
    }

    public int readInt(String prompt) {
        print(prompt);
        return Integer.parseInt(lines.get(i++));
    }

    public ArrayList<String> getPrints() {
        return prints;
    }

    public String readLine(String prompt) {
        
            print(prompt);
            System.out.println(prompt);
        

            
        
        if (i < lines.size()) {
            String palautus = lines.get(i);
            System.out.println(palautus);
            System.out.println(lines.size());
            i++;
            return palautus;

        }
        return "";
    }


}
