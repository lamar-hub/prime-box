package com.lamar.primebox.web.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "user")
public class User {

    private static final long serialVersionUID = 4832588063114528104L;

    @NotBlank
    @Id
    @Column(name = "user_id")
    private String userId;

    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "surname")
    private String surname;

    @PositiveOrZero
    @Column(name = "used_storage")
    private long stored;

    @Positive
    @Column(name = "capacity")
    private long limit;

    @Valid
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserCredentials userCredentials;

}
