package ca.td.greasy.turkey.sharelock.api.model;

public class LockActionRequest {
    private Status status;
    private String token;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
