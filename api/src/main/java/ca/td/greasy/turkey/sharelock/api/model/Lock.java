package ca.td.greasy.turkey.sharelock.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

@Entity
public class Lock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User owner;
    private String name;
    private String location;
    private String deviceId;
    private Status status = Status.LOCKED;
    
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastAcessed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastAcessed() {
        return lastAcessed;
    }

    public void setLastAcessed(Date lastAcessed) {
        this.lastAcessed = lastAcessed;
    }
    
    
}
