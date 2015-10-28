package com.aivo.hyperion.aivo.models;

import com.aivo.hyperion.aivo.models.pojos.LocalStorageModule;
import com.aivo.hyperion.aivo.models.pojos.UserPojo;

import java.io.IOException;

/**
 * Created by corpp on 20.10.2015.
 */
public class User {
    // The local pojo
    private UserPojo pojo;

    // The given local storage module
    private LocalStorageModule lsm;
    private void setLSM(LocalStorageModule lsm_) {
        if (lsm == null)
            throw new InternalError("User created without a valid LocalStorageModule reference!");
        lsm = lsm_;
    }

    /** Create a new User with no references.
     *
     * @param lsm_  LocalStorageModule reference. Required!
     */
    public User(LocalStorageModule lsm_) {
        setLSM(lsm_);
        pojo = new UserPojo();
    }

    /** Create a User from a existing file.
     *
     * @param lsm_          LocalStorageModule reference. Required!
     * @param userId        User identifier.
     * @throws IOException  If unable to read from or close the file.
     */
    public User(LocalStorageModule lsm_, final int userId) throws IOException {
        setLSM(lsm_);
        pojo = lsm.loadUser(userId);
    }
}
