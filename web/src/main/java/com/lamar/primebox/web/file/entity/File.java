package com.lamar.primebox.web.file.entity;

import com.lamar.primebox.web.auth.entity.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "file_id")
    private String fileID;
    @Column(name = "filename")
    private String filename;
    @Column(name = "type")
    private String type;
    @Column(name = "size")
    private long size;
    @Column(name = "last_modified")
    private long lastModified;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public File() {
    }

    public File(String filename, String type, int size, long lastModified) {
        this.filename = filename;
        this.type = type;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "File [fileID=" + fileID + ", filename=" + filename + ", type=" + type + ", size=" + size
                + ", lastModified=" + lastModified + ", user=" + user + "]";
    }

}
