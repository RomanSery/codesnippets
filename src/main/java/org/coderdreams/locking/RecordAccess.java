package org.coderdreams.locking;


import java.io.Serializable;

public class RecordAccess implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean isRecordLocked;
    private final boolean isInitialViewer;
    private final int listenerId;
    private final int userLockId;


    public RecordAccess(boolean isRecordLocked, boolean isInitialViewer, int listenerId, int userLockId) {
        this.isRecordLocked = isRecordLocked;
        this.isInitialViewer = isInitialViewer;
        this.listenerId = listenerId;
        this.userLockId = userLockId;
    }

    public boolean isRecordLocked() { return isRecordLocked; }
    public boolean isInitialViewer() { return isInitialViewer; }
    public int getListenerId() { return listenerId; }
    public int getUserLockId() { return userLockId; }
}
