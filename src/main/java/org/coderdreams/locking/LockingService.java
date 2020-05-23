package org.coderdreams.locking;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.coderdreams.dao.UserLockRepository;
import org.coderdreams.dom.UserLock;
import org.coderdreams.service.UserService;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class LockingService {
    private static final Logger logger = LoggerFactory.getLogger(LockingService.class);

    private final UserLockRepository userLockRepository;
    private final UserService userService;

    private final Object lockObj = new Object();

    private static final RedissonClient client;

    static {
        try {
            client = Redisson.create();
        } catch (Exception e) {
            logger.error("Error in create RedissonLockingClient RedisClient", e);
            throw new RuntimeException(e);
        }
    }

    public LockingService(UserLockRepository userLockRepository, UserService userService) {
        this.userLockRepository = userLockRepository;
        this.userService = userService;
    }


    public RecordAccess getAccessToRecord(int lockObjId, String ipAddress, String clientInfo,
                                          MessageListener<LockPublishMsg> listener) {


        boolean isPageLocked = false;

        boolean isCollaborativeMode = false;
        boolean isInitalViewer = false;
        int listenerId = 0;
        int userLockId = 0;

        LockResult lockResult = initUserLock(lockObjId, userService.getCurrUserId(), ipAddress, clientInfo, listener);
        isInitalViewer = lockResult.isInitalViewer();
        listenerId = lockResult.getListenerId();
        userLockId = lockResult.getUserLockId();
        if(!isInitalViewer) {
            isCollaborativeMode = true;
            isPageLocked = true;
        }

        return new RecordAccess(isCollaborativeMode, isInitalViewer, isPageLocked, listenerId, userLockId);
    }

    
    public LockResult initUserLock(int recordId, int userId, String ipAddress, String clientInfo, MessageListener<LockPublishMsg> listener) {
        if (recordId <= 0 || userId <= 0) {
            return new LockResult(0,true, 0);
        }

        int listenerId = 0;
        int userLockId = 0;
        try {
            RTopic topic = client.getTopic(getChannelName(recordId));

            synchronized (lockObj) {
                listenerId = topic != null ? topic.addListener(LockPublishMsg.class, listener) : 0;
                UserLock ul = userLockRepository.save(new UserLock(userId, recordId, ipAddress, clientInfo, listenerId));
                userLockId = ul.getId();
            }
        } catch (DataIntegrityViolationException e) {
            logger.warn("Trying to lock already locked record: userId={}, recordId={}", userId, recordId);
        } catch (Exception e) {
            logger.error("failed initUserLock", e);
        }

        long numLocks = getNumLocks(recordId);
        boolean isInitalViewer = numLocks < 1;
        return new LockResult(userLockId, isInitalViewer, listenerId);
    }

    public void publishToChannel(int recordId, int userId) {
        long numLocks = getNumLocks(recordId);
        RTopic topic = client.getTopic(getChannelName(recordId));
        if(topic != null) {
            topic.publish(new LockPublishMsg(recordId, userId, numLocks));
        }
    }

    
    public List<UserLock> findUserLocks(int recordId) {
        if (recordId <= 0) {
            return Collections.emptyList();
        }
        return userLockRepository.findByRecordId(recordId);
    }

    


    
    public void removeMyLock(int userLockId, int recordId, int userId, int listenerId) {
        if (recordId <= 0 || userId <= 0 || userLockId <= 0) {
            return;
        }

        userLockRepository.deleteById(userLockId);

        long numLocks = getNumLocks(recordId);
        List<Integer> listenersToRemove = clearLocks(recordId, userId);

        RTopic topic = client.getTopic(getChannelName(recordId));
        if(topic != null) {
            listenersToRemove.add(listenerId);
            listenersToRemove.forEach(listener -> {
                        if (listener > 0) {
                            topic.removeListener(listener);
                        }
                    });
            topic.publish(new LockPublishMsg(recordId, userId, numLocks));
        }
    }

    private List<Integer> clearLocks(int recordId, int userId) {
        List<UserLock> locks = userLockRepository.findByRecordIdAndUserId(recordId, userId);
        List<Integer> listenersToDelete = locks.stream().map(UserLock::getListenerId).collect(Collectors.toList());

        userLockRepository.deleteAll(locks);
        return listenersToDelete;
    }


    public boolean isRecordLocked(int recordId) {
        return getNumLocks(recordId) > 0;
    }

    private long getNumLocks(int recordId) {
        if (recordId <= 0) {
            return 0;
        }
        return userLockRepository.countByRecordId(recordId);
    }


    private String getChannelName(int recordId) {
        return "Locks_" + recordId;
    }

}
