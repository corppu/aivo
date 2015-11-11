package com.aivo.hyperion.aivo.models.pojos;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by MicroLoota on 21.10.2015.
 */
public class LocalStorageModule {

    private static String TAG = "LocalStorageModule";
    // We need gson to transform objects to/from json
    Gson gson = new Gson();

    public void clearMainFolder() {
        String path = Environment.getExternalStorageDirectory() + "/Documents/Aivo";
        File file = new File(path);
        try {
            deleteFolder(file, true);
        } catch (Exception e) {
            Log.d(path, e.getMessage());
        }
    }


    /** Saves the given mindMap in the proper folder.
     *
     * @param mindMap       MindmapPojo to be saved.
     * @throws IOException  If unable to write to the file.
     */
    public void saveMindmap(final MindmapPojo mindMap) throws IOException {

        // Get the file and save to it
        File file = getUserFile(mindMap.getUserId(), mindMap.getMindmapId(), -1, fileType.mindmap);
        saveToFile(file, gson.toJson(mindMap));
    }

    /**  Saves the given mindMaps in the proper folders.
     *
     * @param mindMaps      MindmapPojos to be saved.
     * @throws IOException  If unable to write to a file. Any Mindmaps succesfully saved before
     *                      this exception occurred will remain on the external storage.
     */
    public void saveMindmaps(final ArrayList<MindmapPojo> mindMaps) throws IOException {
        for (MindmapPojo mindMap : mindMaps) {
            saveMindmap(mindMap);
        }
    }

    /** Saves the given MagnetPojo in the proper folder.
     *
     * @param magnetPojo        MagnetPojo to be saved.
     * @throws IOException      If unable to write to the file.
     */
    public void saveMagnet(final MagnetPojo magnetPojo) throws IOException {

        // Get the file and save to it
        File file = getUserFile(magnetPojo.getUserId(), magnetPojo.getMindmapId(), magnetPojo.getMagnetId(), fileType.magnet);
        saveToFile(file, gson.toJson(magnetPojo));
    }

    /** Saves the given LinePojo in the proper folder.
     *
     * @param linePojo          LinePojo to be saved.
     * @throws IOException      If unable to write to the file.
     */
    public void saveLine(final LinePojo linePojo) throws IOException {

        // Get the file and save to it
        File file = getUserFile(linePojo.getUserId(), linePojo.getMindmapId(), linePojo.getLineId(), fileType.line);
        saveToFile(file, gson.toJson(linePojo));
    }

    /** Saves the given NotePojo in the proper folder.
     *
     * @param notePojo      NotePojo to be saved.
     * @throws IOException  If unable to write to the file.
     */
    public void saveNote(final NotePojo notePojo) throws IOException {

        // Get the file and save to it
        File file = getUserFile(notePojo.getUserId(), -1, notePojo.getNoteId(), fileType.note);
        saveToFile(file, gson.toJson(notePojo));
    }

    /** Saves the given Notes in the proper folders. NotePojo validity is checked before saving.
     *
     * @param notePojos     Notes to be saved.
     * @throws IOException  If unable to write to a file. Any Notes succesfully saved before
     *                      this exception occurred will remain on the external storage.
     */
    public void saveNotes(final ArrayList<NotePojo> notePojos) throws IOException {
        for (NotePojo notePojo : notePojos) {
            saveNote(notePojo);
        }
    }

    /** Saves the given UserPojo in the proper folder.
     *
     * @param userPojo      UserPojo to be saved.
     * @throws IOException  If unable to write to the file.
     */
    public void saveUser(final UserPojo userPojo) throws IOException {

        // Get the file and save to it
        File file = getUserFile(userPojo.getUserId(), -1, -1, fileType.user);
        saveToFile(file, gson.toJson(userPojo));
    }

    /** Loads the wanted MindmapPojo from the external storage.
     *
     * @param userId        Identifier
     * @param mindMapId     Identifier
     * @return              The wanted mindmap, or null if the file doesnt exist at all.
     * @throws IOException  If unable to read or close the file.
     */
    public MindmapPojo loadMindmap(final int userId, final int mindMapId) throws IOException {

        // Get the file
        File file = getUserFile(userId, mindMapId, -1, fileType.mindmap);
        if (file.isFile()) {
            // read the file, transform it to pojo and check its validity
            String data = readFromFile(file);
            MindmapPojo mindMap = gson.fromJson(data, MindmapPojo.class);
            return mindMap;
        }

        return null;
    }

    /** Returns all UserPojos MindmapPojos.
     *
     * @param user          UserPojo identifier
     * @return              ArrayList of users Mindmaps.
     * @throws IOException  If unable to read from or close any file.
     */
    public ArrayList<MindmapPojo> loadMindmaps(final UserPojo user) throws IOException {

        ArrayList<MindmapPojo> mindmapPojos = new ArrayList<>();

        // Read the mindmaps magnetPojos
        for (int mindmapId : user.getMindmapIds()) {
            MindmapPojo temp = loadMindmap(user.getUserId(), mindmapId);
            if ( !(temp == null) )
                mindmapPojos.add( temp );
        }

        return mindmapPojos;
    }

    /** Loads the wanted MagnetPojo from the external storage.
     *
     * @param userId        Identifier
     * @param mindMapId     Identifier
     * @param magnetId      Identifier
     * @return              The wanted MagnetPojo, or null if it doesnt exist at all
     * @throws IOException  If unable to read from or close the file.
     */
    public MagnetPojo loadMagnet(final int userId, final int mindMapId, final int magnetId)
            throws IOException {

        // Get the file
        File file = getUserFile(userId, mindMapId, magnetId, fileType.magnet);
        if (file.isFile()) {
            // read the file, transform it to pojo
            String data = readFromFile(file);
            MagnetPojo magnetPojo = gson.fromJson(data, MagnetPojo.class);
            return magnetPojo;
        }

        return null;
    }

    public LinePojo loadLine(final int userId, final int mindMapId, final int lineId)
            throws IOException {

        // Get the file
        File file = getUserFile(userId, mindMapId, lineId, fileType.line);
        if (file.isFile()) {
            // read the file, transform it to pojo
            String data = readFromFile(file);
            LinePojo linePojo = gson.fromJson(data, LinePojo.class);
            return linePojo;
        }

        return null;
    }

    /** Loads a NotePojo from the external storage.
     *
     * @param userId        identifier
     * @param noteId        identifier
     * @return              The wanted note
     * @throws IOException  If unable to read or close the file.
     */
    public NotePojo loadNote(final int userId, final int noteId) throws IOException {

        // Get the file
        File file = getUserFile(userId, -1, noteId, fileType.note);
        if (file.isFile()) {
            // read the file, transform it to pojo and check its validity
            String data = readFromFile(file);
            NotePojo notePojo = gson.fromJson(data, NotePojo.class);
            return notePojo;
        }

        return null;
    }

    /** Returns all UserPojos NotePojos.
     *
     * @param user      UserPojo identifier
     * @return          ArrayList of users NotePojos.
     */
    public ArrayList<NotePojo> loadNotes(final UserPojo user) throws IOException {

        ArrayList<NotePojo> notePojos = new ArrayList<>();

        for (int noteId : user.getNoteIds()) {
            NotePojo temp = loadNote(user.getUserId(), noteId);
            if ( !(temp == null) )
                notePojos.add(temp);
        }

        return notePojos;
    }

    /** Loads a UserPojo from the external storage.
     *
     * @param userId        identifier
     * @return              the wanted user
     * @throws IOException  If unable to read or close any file.
     */
    public UserPojo loadUser(final int userId) throws IOException {

        // Get the file
        File file = getUserFile(userId, -1, -1, fileType.user);
        if (file.isFile()) {
            // read the file, transform it to pojo and check its validity
            String data = readFromFile(file);
            UserPojo userPojo = gson.fromJson(data, UserPojo.class);
            return userPojo;
        }

        return null;
    }

    /** Delete the MindmapPojo file or entire Mindmap folder.
     *
     * @param userId        MindmapPojo identifier
     * @param mindMapId     MindmapPojo identifier
     * @param Recursive     If recursive, entire MindmapPojo folder with its content is removed.
     *                      Normally this only means the magnets of the MindmapPojo.
     *                      Notes connected to magnets will NOT be updated!
     * @throws IOException  If unable to delete a existing file or a folder.
     */
    public void deleteMindmap(final int userId, final int mindMapId, final boolean Recursive)
            throws IOException {

        // Delete the mindmap file
        File file = getUserFile(userId, mindMapId, -1, fileType.mindmap);
        deleteFile(file);

        // Delete all folder content?
        if (Recursive) {
            deleteFolder(file.getParentFile(), true);
        }
    }
    // ^
    public void deleteMindmap(final MindmapPojo mindMap, final boolean Recursive) throws IOException {
        deleteMindmap(mindMap.getUserId(), mindMap.getMindmapId(), Recursive);
    }

    /** Removes the given magnet. This does NOT update owning MindmapPojo or connected notes!
     *
     * @param userId        identifier
     * @param mindMapId     identifier
     * @param magnetId      identifier
     * @throws IOException  If unable to delete the MagnetPojo, if it exists.
     */
    public void deleteMagnet(final int userId, final int mindMapId, final int magnetId)
            throws IOException {

        // Delete the magnet file
        File file = getUserFile(userId, mindMapId, magnetId, fileType.magnet);
        deleteFile(file);
    }
    // ^
    public void deleteMagnet(final MagnetPojo magnetPojo) throws IOException {
        deleteMagnet(magnetPojo.getUserId(), magnetPojo.getMindmapId(), magnetPojo.getMagnetId());
    }

    /** Deletes the given note. This does NOT update connected magnets!
     *
     * @param userId        identifier
     * @param noteId        identifier
     * @throws IOException  If unable to delete the NotePojo, if it exists.
     */
    public void deleteNote(final int userId, final int noteId) throws IOException {

        // Delete the note file
        File file = getUserFile(userId, -1, noteId, fileType.note);
        deleteFile(file);
    }
    // ^
    public void deleteNote(final NotePojo notePojo) throws IOException {
        deleteNote(notePojo.getUserId(), notePojo.getNoteId());
    }

    /** Delete the given user file or entire user folder.
     *
     * @param userId        identifier
     * @param Recursive     If recursive, entire user folder is removed with all its contents.
     *                      Normally this means the users mindmaps, magnets and notes.
     * @throws IOException  If unable to delete a existing file or folder.
     */
    public void deleteUser(final int userId, final boolean Recursive) throws IOException {

        // Delete the user file
        File file = getUserFile(userId, -1, -1, fileType.user);
        deleteFile(file);

        // Delete all folder content?
        if (Recursive) {
            deleteFolder(file.getParentFile(), true);
        }
    }
    // ^
    public void deleteUser(final UserPojo userPojo, final boolean Recursive) throws IOException {
        deleteUser(userPojo.getUserId(), Recursive);
    }

    enum fileType {user, mindmap, magnet, note, line}
    /** Returns a reference to a user file.
     *
     * @param userId            UserId. Must be >= 0.
     * @param mindmapId         MindmapId. Must be >=0, in case of magnet, mindmap or line.
     * @param typeId            TypeId. Must be >=0, in case of magnet, note or line.
     * @param filetype          Enum to define which filetype we are getting.
     * @return                  Wanted File reference.
     */
    private File getUserFile(final int userId, final int mindmapId, final int typeId, final fileType filetype) {
        if (userId < 0)
            throw new InternalError("getUserFile() called with invalid userId!");
        if (mindmapId < 0 && (
                filetype == fileType.magnet ||
                filetype == fileType.mindmap ||
                filetype == fileType.line))
            throw new InternalError("getUserFile() called with invalid mindmapId, when it was required!");
        if (typeId < 0 && (
                filetype == fileType.note ||
                filetype == fileType.magnet ||
                filetype == fileType.line))
            throw new InternalError("getUserFile() called with invalid typeId, when it was required!");

        // File is always in user file
        String filepath = "/Documents/Aivo/User_" + userId;
        String filename = "";

        // Append to filepath and set filename
        switch (filetype) {
            case user:
                // filepath already ready
                filename = "User_" + userId + ".json";
                break;
            case mindmap:
                filepath += "/Mindmap_" + typeId;
                filename = "Mindmap_" + typeId + ".json";
                break;
            case magnet:
                filepath += "/Mindmap_" + typeId;
                filename = "Magnet_" + typeId + ".json";
                break;
            case note:
                filepath += "/Notes";
                filename = "Note_" + typeId + ".json";
                break;
            case line:
                filepath += "/Mindmap_" + typeId;
                filename = "Line_" + typeId + ".json";
                break;
        }
        // append filename to path
        File file = new File(Environment.getExternalStorageDirectory() + filepath, filename);
        return file;
        //return (folderOnly) ? file.getParentFile() : file;
    }



    /** Returns a reference to a user file.
     *
     * @param userId            UserId. Must be >= 0.
     * @param mindMapId         MindmapId.
     * @param magnetIdOrNoteId  MagnetId or NoteId. NoteId if mindMapId < 0.
     * @param folderOnly        If true, returns reference to the folder the wanted file resides in.
     * @return                  Wanted File reference.
     */
/*    private File getUserFile(final int userId, final int mindMapId,
                             final int magnetIdOrNoteId, final boolean folderOnly) {

        if (userId < 0)
            throw new InternalError("getUserFile() called with invalid userId!");

        // UserPojo/UserPojo.json
        String filepath = "/Documents/Aivo/User_" + userId;
        String filename = "User_" + userId + ".json";

        if (mindMapId >= 0) {
            // UserPojo/MindmapPojo/MindmapPojo.json
            filepath += "/Mindmap_" + mindMapId;
            filename = "Mindmap_" + mindMapId + ".json";
            if (magnetIdOrNoteId >= 0) {
                // UserPojo/MindmapPojo/MagnetPojo.json
                filename = "Magnet_" + magnetIdOrNoteId + ".json";
            }
        } else if (magnetIdOrNoteId >= 0) {
            // UserPojo/Notes/NotePojo.json
            filepath += "/Notes";
            filename = "Note_" + magnetIdOrNoteId + ".json";
        }

        // append filename to path
        File file = new File(Environment.getExternalStorageDirectory() + filepath, filename);
        return (folderOnly) ? file.getParentFile() : file;
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

    /** Delete a file.
     *
     * @param file          File to be deleted. If not a file, does nothing.
     * @throws IOException  If file existed and delete failed.
     */
    private void deleteFile(File file) throws IOException {
        if (file.isFile() && !file.delete())
            throw new IOException("Could not delete file \"" + file.toString() + "\"!");
    }

    /** Delete a folder, optionally recursively.
     *
     * @param dir           Directory to be deleted.
     * @param Recursive     If recursive, all content is deleted.
     * @throws IOException  If unable to delete a existing file or folder.
     */
    private void deleteFolder(File dir, boolean Recursive) throws IOException {
        if (dir.isDirectory()) {

            // Clear folder contents
            if (Recursive) {
                for (File f : dir.listFiles()) {
                    // Clear subfolders, delete files
                    if (f.isDirectory())
                        deleteFolder(f, true);
                    else
                        deleteFile(f);
                }
            }

            // Delete the folder itself
            if (!dir.delete())
                throw new IOException("Could not delete folder \"" + dir.toString() + "\"!");
        }
    }

    /** Returns a filtered list of users notes.
     *
     * @param userPojo      UserPojo identifier, must be defined.
     * @param nameFilter    NotePojo title must contain this. Pass null to ignore.
     * @param favouriteOnly NotePojo must be a userPojo favourite. Pass false to ignore.
     * @param recentOnly    NotePojo must be in users recent note list. Pass false to ignore.
     * @param mindMapId     NotePojo must belong to this mindMap. Pass negative to ignore.
     * @return              Filtered ArrayList of users Notes.
     * @throws IOException  If unable to read from or close any file.
     */
    public ArrayList<NotePojo> loadNotes(final UserPojo userPojo, @Nullable final String nameFilter,
                                     final boolean favouriteOnly, final boolean recentOnly,
                                     final int mindMapId ) throws IOException {
        if (userPojo == null)
            throw new InternalError("loadNotes() called with an invalid userPojo!");

        ArrayList<NotePojo> list = loadNotes(userPojo);

        for (Iterator<NotePojo> it = list.iterator(); it.hasNext(); ) {
            NotePojo notePojo = it.next();

            if  (   (mindMapId >= 0 && notePojo.getMindmapId() != mindMapId) ||
                    (favouriteOnly && !userPojo.getFavouriteNoteIds().contains(notePojo.getNoteId())) ||
                    (recentOnly && !userPojo.getRecentNoteIds().contains(notePojo.getNoteId())) ||
                    (!(nameFilter == null) && !notePojo.getTitle().contains(nameFilter))    )
                it.remove();

        }

        return list;
    }

    /** Returns a filtered list of users mindmaps.
     *
     * @param userPojo      UserPojo identifier, must be defined.
     * @param nameFilter    MindmapPojo title must contain this. Pass null to ignore.
     * @param favouriteOnly MindmapPojo must be a userPojo favourite. Pass false to ignore.
     * @param recentOnly    MindmapPojo must be in recent mindmap list. Pass false to ignore.
     * @return              Filtered ArrayList of users Mindmaps.
     * @throws IOException  If unable to read from or close any file.
     */
    public ArrayList<MindmapPojo> loadMindmaps(final UserPojo userPojo, @Nullable final String nameFilter,
                                           final boolean favouriteOnly, final boolean recentOnly) throws IOException {
        if (userPojo == null)
            throw new InternalError("loadMindmaps() called with an invalid userPojo!");

        ArrayList<MindmapPojo> list = loadMindmaps(userPojo);

        for (Iterator<MindmapPojo> it = list.iterator(); it.hasNext(); ) {
            MindmapPojo mindMap = it.next();

            if  (   (favouriteOnly && !userPojo.getFavouriteMindmapIds().contains(mindMap.getMindmapId())) ||
                    (recentOnly && !userPojo.getRecentMindmapIds().contains(mindMap.getMindmapId())) ||
                    (!(nameFilter == null) && !mindMap.getTitle().contains(nameFilter))    )
                it.remove();

        }

        return list;
    }
}
