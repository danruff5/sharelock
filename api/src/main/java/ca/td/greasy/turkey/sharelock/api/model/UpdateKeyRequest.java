package ca.td.greasy.turkey.sharelock.api.model;

public class UpdateKeyRequest {
    private String name;
    private Long newExpiry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNewExpiry() {
        return newExpiry;
    }

    public void setNewExpiry(Long newExpiry) {
        this.newExpiry = newExpiry;
    }
}
