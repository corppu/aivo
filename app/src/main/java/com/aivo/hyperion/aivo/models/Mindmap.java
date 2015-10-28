package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;
import com.aivo.hyperion.aivo.models.pojos.MindmapPojo;

import java.io.IOException;

public class Mindmap {
    // The local pojo
    private MindmapPojo pojo;

    // The given local storage module
    private LocalStorageModule lsm;
    private void setLSM(LocalStorageModule lsm_) {
        if (lsm == null)
            throw new InternalError("Mindmap created without a valid LocalStorageModule reference!");
        lsm = lsm_;
    }

    /** Create a new Mindmap with no references.
     *
     * @param lsm_  LocalStorageModule reference. Required!
     */
    public Mindmap(LocalStorageModule lsm_) {
        setLSM(lsm_);
        pojo = new MindmapPojo();
    }

    /** Create a Mindmap from a existing file.
     *
     * @param lsm_          LocalStorageModule reference. Required!
     * @param userId        User identifier.
     * @param mindmapId     Mindmap identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    public Mindmap(LocalStorageModule lsm_, final int userId, final int mindmapId) throws IOException {
        setLSM(lsm_);
        pojo = lsm.loadMindmap(userId, mindmapId);
    }


}
