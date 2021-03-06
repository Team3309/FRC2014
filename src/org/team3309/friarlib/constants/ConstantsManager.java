/*
 * Copyright (c) 2014. FRC Team 3309 All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.team3309.friarlib.constants;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import org.team3309.friarlib.util.Util;

import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;

public class ConstantsManager {

    private static Hashtable constantsValues = new Hashtable();

    static {
        try {
            loadConstantsFromFile("/Constants.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add constant to the map
     *
     * @param name
     * @param o
     */
    protected static void addValue(String name, Object o) {
        constantsValues.put(name, o);
    }

    /**
     * Get a constant
     *
     * @param key
     * @return
     */
    protected static Object getValue(String key) {
        return constantsValues.get(key);
    }

    /**
     * Is there a constant with this name?
     *
     * @param key
     * @return
     */
    protected static boolean contains(String key) {
        return constantsValues.containsKey(key);
    }

    /**
     * Load constants from a txt file on the cRIO
     *
     * @param path
     * @throws java.io.IOException
     */
    public static void loadConstantsFromFile(String path) throws IOException {
        FileConnection fileConnection = (FileConnection) Connector.open("file:///" + path, Connector.READ);
        loadConstants(fileConnection.openInputStream());
    }

    public static void loadConstants(InputStream inputStream) {
        try {
            String line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                line = Util.remove(line, ' ');
                line = Util.remove(line, '\t');
                if (line.startsWith("#") || line.startsWith("//") || line.equals("")) {
                    continue;
                }
                if (!Util.contains(line, "=")) {
                    throw new IOException("Invalid format, line <" + line + "> does not contain equals sign");
                }
                String key = line.substring(0, line.indexOf("=")).trim();
                String value = line.substring(line.indexOf("=") + 1);

                addValue(key, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Returns the array of substrings obtained by dividing the given input
     * string at each occurrence of the given delimiter.
     */
    private static String[] split(String input, String delimiter) {
        Vector node = new Vector();
        int index = input.indexOf(delimiter);
        while (index >= 0) {
            node.addElement(input.substring(0, index));
            input = input.substring(index + delimiter.length());
            index = input.indexOf(delimiter);
        }
        node.addElement(input);

        String[] retString = new String[node.size()];
        for (int i = 0; i < node.size(); ++i) {
            retString[i] = (String) node.elementAt(i);
        }

        return retString;
    }
}
