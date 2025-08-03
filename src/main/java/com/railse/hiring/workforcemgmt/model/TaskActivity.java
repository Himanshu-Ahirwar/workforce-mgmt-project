package com.railse.hiring.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskActivity {
    public enum ActivityType {
        HISTORY, COMMENT
    }
    private ActivityType type;
    private String details;
    private Long authorId; // Used for comments
    private Long timestamp;
}