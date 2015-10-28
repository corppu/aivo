package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;
import com.aivo.hyperion.aivo.models.pojos.MagnetPojo;

import java.io.IOException;

public class Magnet {
    // The local pojo
    private MagnetPojo pojo;

    // The given local storage module
    private LocalStorageModule lsm;
    private void setLSM(LocalStorageModule lsm_) {
        if (lsm == null)
            throw new InternalError("Magnet created without a valid LocalStorageModule reference!");
        lsm = lsm_;
    }

    /** Create a new Magnet with no references.
     *
     * @param lsm_  LocalStorageModule reference. Required!
     */
    public Magnet(LocalStorageModule lsm_) {
        setLSM(lsm_);
        pojo = new MagnetPojo();
    }

    /** Create a Magnet from a existing file.
     *
     * @param lsm_          LocalStorageModule reference. Required!
     * @param userId        User identifier.
     * @param mindmapId     Mindmap identifier.
     * @param magnetId      Magnet identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    public Magnet(LocalStorageModule lsm_, final int userId, final int mindmapId,
                  final int magnetId) throws IOException {
        setLSM(lsm_);
        pojo = lsm.loadMagnet(userId, mindmapId, magnetId);
    }
}
