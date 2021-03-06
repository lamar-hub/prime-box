package com.lamar.primebox.notification.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
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

    @Column(name = "notification_transaction_id")
    private String notificationTransactionId;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "notification_state")
    private NotificationState notificationState;

    @Positive
    @Column(name = "notification_mtime")
    private long mtime;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notification_template_model", joinColumns = @JoinColumn(name = "notification_id"))
    @MapKeyColumn(name = "model_key")
    @Column(name = "model_value")
    private Map<String, String> templateModel = new HashMap<>();

    @PrePersist
    @PreUpdate
    public void setMtime() {
        mtime = Instant.now().toEpochMilli();
    }
    
    
}
