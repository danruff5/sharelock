package ca.td.greasy.turkey.sharelock.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Temporal;

public class CreateKeyRequest {
    private Long userId;
    private Long lockId;
    /*@JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)*/
    private Long expiryTime;

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
