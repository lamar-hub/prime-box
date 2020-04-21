package com.lamar.primebox.web.shared.entity;

import com.lamar.primebox.web.auth.entity.User;
import com.lamar.primebox.web.file.entity.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "shared")
public class Shared {

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

    public Shared(String message, long date) {
        this.message = message;
        this.date = date;
    }

    @PrePersist
    protected void onCreate() {
        this.date = new Date().getTime();
    }

}
