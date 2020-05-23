package org.coderdreams.locking;

import java.io.Serializable;

public final class LockPublishMsg implements Serializable {
	private static final long serialVersionUID = 1L;

	private final int objectId;
	private final int userId;
    private final long numLocks;

    public LockPublishMsg(int objectId, int userId, long numLocks) {
        this.objectId = objectId;
        this.userId = userId;
        this.numLocks = numLocks;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getUserId() {
        return userId;
    }

    public long getNumLocks() {
        return numLocks;
    }

    @Override
    public String toString() {
        return "LockPublishMsg{" +
                "objectId=" + objectId +
                ", userId=" + userId +
                ", numLocks=" + numLocks +
                '}';
    }
}
