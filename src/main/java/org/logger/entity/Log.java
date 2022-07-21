package org.logger.entity;

import lombok.Data;

@Data
public class Log {
    private String id;
    private String state;
    private String type;
    private String host;
    private Long timestamp;
}
