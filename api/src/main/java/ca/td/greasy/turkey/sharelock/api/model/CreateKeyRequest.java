package ca.td.greasy.turkey.sharelock.api.model;

public class CreateKeyRequest {
    private String name;
    private Long userId;
    private Long lockId;
    private Long expiryTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLockId() {
        return lockId;
    }

    public void setLockId(Long lockId) {
        this.lockId = lockId;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }
    
    
}
