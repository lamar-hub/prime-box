package com.lamar.primebox.web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "shared")
public class SharedFile {

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

    public SharedFile(String message, long date) {
        this.message = message;
        this.date = date;
    }

    @PrePersist
    protected void onCreate() {
        this.date = new Date().getTime();
    }

}
