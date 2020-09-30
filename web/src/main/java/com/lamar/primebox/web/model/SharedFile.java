package com.lamar.primebox.web.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "shared")
public class SharedFile implements Serializable {

    @Valid
    @Id
    @ManyToOne
    @JoinColumn(name = "shared_user_id")
    private User sharedUser;

    @Valid
    @Id
    @ManyToOne
    @JoinColumn(name = "shared_file_id")
    private File sharedFile;

    @Column(name = "message")
    private String message;

    @Positive
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
