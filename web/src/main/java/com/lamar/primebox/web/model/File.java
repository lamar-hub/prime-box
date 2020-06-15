package com.lamar.primebox.web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "file")
public class File {

    @NotBlank
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "file_id")
    private String fileId;

    @NotBlank
    @Column(name = "filename")
    private String filename;

    @NotBlank
    @Column(name = "type")
    private String type;

    @Positive
    @NotNull
    @Column(name = "size")
    private long size;

    @NotNull
    @Column(name = "last_modified")
    private long lastModified;

    @Valid
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public File(String filename, String type, int size, long lastModified) {
        this.filename = filename;
        this.type = type;
        this.size = size;
        this.lastModified = lastModified;
    }

}
