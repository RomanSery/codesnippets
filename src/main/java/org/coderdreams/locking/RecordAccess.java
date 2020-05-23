package org.coderdreams.locking;


import java.io.Serializable;

public class RecordAccess implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean isCollaborativeMode;
    private final boolean isInitalViewer;
    private final boolean isPageLocked;
    private final int listenerId;
    private final int userLockId;


    public RecordAccess(boolean isCollaborativeMode, boolean isInitalViewer, boolean isPageLocked,
                        int listenerId, int userLockId) {
        this.isCollaborativeMode = isCollaborativeMode;
        this.isInitalViewer = isInitalViewer;
        this.isPageLocked = isPageLocked;
        this.listenerId = listenerId;
        this.userLockId = userLockId;
    }

    public boolean isCollaborativeMode() {
        return isCollaborativeMode;
    }
    public boolean isInitalViewer() {
        return isInitalViewer;
    }
    public boolean isPageLocked() {
        return isPageLocked;
    }
    public int getListenerId() { return listenerId; }
    public int getUserLockId() { return userLockId; }

}
