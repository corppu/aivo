package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;
import com.aivo.hyperion.aivo.models.pojos.MediaPojo;

public class Video {
    // The local pojo
    private MediaPojo pojo;

    // The given local storage module
    private LocalStorageModule lsm;
    private void setLSM(LocalStorageModule lsm_) {
        if (lsm == null)
            throw new InternalError("Video created without a valid LocalStorageModule reference!");
        lsm = lsm_;
    }

    /** Create a new Video with no references.
     *
     * @param lsm_  LocalStorageModule reference. Required!
     */
    public Video(LocalStorageModule lsm_) {
        setLSM(lsm_);
        pojo = new MediaPojo();
    }
}
