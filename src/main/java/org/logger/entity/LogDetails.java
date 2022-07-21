package org.logger.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Table(name = "log_details")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LogDetails {
    @Id
    private String id;
    private Long duration;
    private String type;
    private String host;
    private Boolean alert;
}
