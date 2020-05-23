package org.coderdreams.dom;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_lock")
public class UserLock extends BaseEntity  {
	private static final long serialVersionUID=1L;

	@Column(name = "user_id", nullable = false)
	private int userId;

	@Column(name = "record_id", nullable = false)
	private int recordId;

	@Column(name = "creation_date", nullable = false)
	private LocalDateTime creationDate = LocalDateTime.now();

	@Column(name = "ip_addr")
	private String ipAddress;

	@Column(name = "client_info")
	private String clientInfo;

	@Column(name = "listener_id")
	private int listenerId;

	public UserLock() {
		
	}
	
	public UserLock(int userId, int recordId, String ipAddress, String clientInfo, int listenerId) {
		super();		
		this.userId = userId;
		this.recordId = recordId;
		this.ipAddress = ipAddress;
		this.clientInfo = clientInfo;
		this.listenerId = listenerId;
	}

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(String clientInfo) {
		this.clientInfo = clientInfo;
	}

	public int getListenerId() {
		return listenerId;
	}

	public void setListenerId(int listenerId) {
		this.listenerId = listenerId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		UserLock userLock = (UserLock) o;

		if (userId != userLock.userId) return false;
		if (recordId != userLock.recordId) return false;
		if (listenerId != userLock.listenerId) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + userId;
		result = 31 * result + recordId;
		result = 31 * result + listenerId;
		return result;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
}