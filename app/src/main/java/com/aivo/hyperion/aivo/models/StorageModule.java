package com.aivo.hyperion.aivo.models;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StorageModule {

    public void saveMindmap(Mindmap mindmap) {
        JSONObject json = mindmap.getJSON();
        File file = new File("/Mindmap", mindmap.getTitle() + ".json");
        try {
            saveToFile(file, json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** Reads contents of given file to a string.
     *
     * @param file          file to read, must not be null
     * @return              Content of file as string or null, if the file does not exist.
     * @throws IOException  If unable to read from or close the file.
     */
    private String readFromFile(File file) throws IOException {
        if (!file.isFile())
            return null;

        StringBuilder content = new StringBuilder();
        BufferedReader br = null;

        // Try to open a BufferedReader to read the file  efficiently through from
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            // Read file, line by line, into the StringBuilder
            String line;
            while (!((line = br.readLine()) == null))
                content.append(line);

        } catch (IOException e) {
            throw new IOException("Could not read file \"" + file.toString() + "\"!", e);

        } finally {
            try {
                if (!(br == null))
                    br.close();
            } catch (IOException e) {
                throw new IOException("Could not close file \""
                        + file.toString() + "\" after reading from it!", e);
            }
        }

        return content.toString();
    }

    /** Saves given string to the given File.
     *
     * @param file          File to save to, must not be null
     * @param content       String to save, must not be null
     * @throws IOException  If unable to write to or close the file.
     */
    private void saveToFile(File file, String content) throws IOException {

        if (!file.getParentFile().mkdirs() && !file.getParentFile().isDirectory())
            throw new IOException("Could not find or create folder for file " + file.toString());

        BufferedWriter bw = null;
        // Try to open a BufferedWriter to save our content through
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            bw.write(content);
            bw.flush();

        } catch (IOException e) {
            throw new IOException("Could not write to file \"" + file.toString() + "\"!", e);

        } finally {
            try {
                if (!(bw == null))
                    bw.close();
            } catch (IOException e) {
                throw new IOException("Could not close file \""
                        + file.toString() + "\" after writing to it!", e);
            }
        }
    }
}
