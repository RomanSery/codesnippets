package org.coderdreams.locking;


import java.io.Serializable;

public class LockResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int userLockId;
    private final boolean isInitalViewer;
    private final int listenerId;

    public LockResult(int userLockId, boolean isInitalViewer, int listenerId) {
        this.userLockId = userLockId;
        this.isInitalViewer = isInitalViewer;
        this.listenerId = listenerId;
    }

    public int getUserLockId() { return userLockId; }
    public boolean isInitalViewer() {
        return isInitalViewer;
    }
    public int getListenerId() { return listenerId; }
}
