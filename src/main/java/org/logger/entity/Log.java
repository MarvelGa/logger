package org.logger.entity;

import lombok.Data;
@Data
public class Log {
   public enum StateType{
        STARTED, FINISHED;
    }
    private String id;
    private StateType state;
    private String type;
    private String host;
    private Long timestamp;
}
