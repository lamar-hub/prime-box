package com.lamar.primebox.web.shared.entity;

import com.lamar.primebox.web.auth.entity.User;
import com.lamar.primebox.web.file.entity.File;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "shared")
public class Shared implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "shared_user_id")
    private User sharedUser;
    @Id
    @ManyToOne
    @JoinColumn(name = "shared_file_id")
    private File sharedFile;
    @Column(name = "message")
    private String message;
    @Column(name = "date")
    private long date;

    public Shared() {
    }

    public Shared(String message, long date) {
        this.message = message;
        this.date = date;
    }

    @PrePersist
    protected void onCreate() {
        this.date = new Date().getTime();
    }

    public User getSharedUser() {
        return sharedUser;
    }

    public void setSharedUser(User sharedUser) {
        this.sharedUser = sharedUser;
    }

    public File getSharedFile() {
        return sharedFile;
    }

    public void setSharedFile(File sharedFile) {
        this.sharedFile = sharedFile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Shared [sharedUser=" + sharedUser + ", sharedFile=" + sharedFile + ", message=" + message + ", date="
                + date + "]";
    }

}