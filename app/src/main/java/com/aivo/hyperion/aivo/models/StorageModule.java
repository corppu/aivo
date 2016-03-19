package com.aivo.hyperion.aivo.models;

import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

public class StorageModule {

    static final String defaultUserFolder = "/localuser";
    static final String mindmapFolder = "/mindmaps";
    static final String noteFolder = "/notes";
    static final String fileExtension = ".json";

    private File appDir;

    protected StorageModule() {
        appDir = MainActivity.getContext().getFilesDir();
    }

    // ---------------
    // Private Helpers
    private String getUserFolder(String userEmail) {
        if (userEmail != null && userEmail != "")
            return "/" + userEmail;
        else
            return defaultUserFolder;
    }

    private File getMindmapFile(User user, String mindmapTitle) {
        File dir = new File(appDir + getUserFolder(user.getEmail()) + mindmapFolder);
        if (mindmapTitle != null)
            return new File(dir, mindmapTitle + fileExtension);
        return dir;
    }

    private File getNoteFile(User user, String noteTitle) {
        File dir = new File(appDir + getUserFolder(user.getEmail()) + noteFolder);
        if (noteTitle != null)
            return new File(dir, noteTitle + fileExtension);
        return dir;
    }

    private File getUserFile(String userEmail) {
        File dir = new File(appDir + getUserFolder(userEmail));
        return new File(dir, "user" + fileExtension);
    }


    // ---------------

    protected List<String> readMindmapTitles(User user) {
        List<String> namelist = new ArrayList<>();

        File dir = getMindmapFile(user, null);
        String[] filenames = dir.list();

        for (String filename : filenames) {
            namelist.add(filename.substring(0, filename.length()-fileExtension.length()));
        }
        return namelist;
    }

    protected List<String> readNoteTitles(User user) {
        List<String> namelist = new ArrayList<>();

        File dir = getNoteFile(user, null);
        String[] filenames = dir.list();

        for (String filename : filenames) {
            namelist.add(filename.substring(0, filename.length()-fileExtension.length()));
        }
        return namelist;
    }

    protected boolean saveUser(User user) {
        try {
            File file = getUserFile(user.getEmail());
            JSONObject json = user.getJSON();
            saveToFile(file, json.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean saveMindmap(User user, Mindmap mindmap) {
        try {
            File file = getMindmapFile(user, mindmap.getTitle());
            JSONObject json = mindmap.getJSON();
            saveToFile(file, json.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean saveNote(User user, Note note) {
        try {
            File file = getNoteFile(user, note.getTitle());
            JSONObject json = note.getJSON();
            saveToFile(file, json.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected Note loadUser(ModelMediator mediator, String email) {
        try {
            File file = getUserFile(email);
            String jsonString = readFromFile(file);
            JSONObject jsonObject = new JSONObject(jsonString);
            return new Note(mediator, jsonObject);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

    protected Mindmap loadMindmap(ModelMediator mediator, User user, String title) {
        try {
            File file = getMindmapFile(user, title);
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

    protected Note loadNote(ModelMediator mediator, User user, String title) {
        try {
            File file = getNoteFile(user, title);
            String jsonString = readFromFile(file);
            JSONObject jsonObject = new JSONObject(jsonString);
            return new Note(mediator, jsonObject);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

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
