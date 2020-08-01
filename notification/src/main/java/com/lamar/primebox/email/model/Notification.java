package com.lamar.primebox.email.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "notification")
public class Notification {

    @NotBlank
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "notification_id")
    private String notificationId;

    @NotBlank
    @Column(name = "notification_to")
    private String notificationTo;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @PositiveOrZero
    @NotNull
    @Column(name = "notification_attempt_count")
    private Integer attemptCount;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "template_model", joinColumns = @JoinColumn(name = "notification_id"))
    @MapKeyColumn(name = "model_key")
    @Column(name = "model_value")
    private Map<String, String> templateModel = new HashMap<>();

}
