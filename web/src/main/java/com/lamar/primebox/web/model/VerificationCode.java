package com.lamar.primebox.web.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "verification_code")
public class VerificationCode {

    private static final long serialVersionUID = 4832588063114528105L;

    @NotBlank
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "code_id")
    private String codeId;

    @NotNull
    @Column(name = "code")
    private String code;

    @Positive
    @Column(name = "created")
    private long created;

    @Column(name = "active")
    private boolean active;

    @Valid
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
