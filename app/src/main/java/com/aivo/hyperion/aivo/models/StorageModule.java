package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.main.MainActivity;

import org.json.JSONException;
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

    private File appDir;

    protected StorageModule() {
        appDir = MainActivity.getContext().getFilesDir();
    }

    public boolean saveMindmap(Mindmap mindmap) {
        try {
            File dir = new File(appDir + "/mindmaps");
            File file = new File(dir, mindmap.getTitle() + ".json");
            JSONObject json = mindmap.getJSON();
            saveToFile(file, json.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Mindmap loadMindmap(ModelMediator mediator, String title) {
        try {
            File dir = new File(appDir + "/mindmaps");
            File file = new File(dir, title + ".json");
            String jsonString = readFromFile(file);
            JSONObject jsonObject = new JSONObject(jsonString);
            return new Mindmap(mediator, jsonObject);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

    public boolean saveNote(Note note) {
        try {
            File dir = new File(appDir + "/notes");
            File file = new File(dir, note.getTitle() + ".json");
            JSONObject json = note.getJSON();
            saveToFile(file, json.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Note loadNote(ModelMediator mediator, String title) {
        try {
            File dir = new File(appDir + "/notes");
            File file = new File(dir, title + ".json");
            JSONObject json = new JSONObject(readFromFile(file));
            return new Note(mediator, json);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }


/*
    public boolean saveUser(User user) {
        JSONObject json = user.getJSON();
        File file = new File(user.getEmail() + ".json");
        try {
            saveToFile(file, json.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
*/

    /** Reads contents of given file to a string.
     *
     * @param file          file to read, must not be null
     * @return              Content of file as string or null, if the file does not exist.
     * @throws IOException  If unable to read from or close the file.
     */
    private String readFromFile(File file) throws IOException {
        if (!file.isFile())
            throw new IOException("File \"" + file.toString() + "\" not found!");

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
