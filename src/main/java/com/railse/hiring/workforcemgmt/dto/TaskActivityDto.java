package com.railse.hiring.workforcemgmt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workforcemgmt.model.TaskActivity.ActivityType;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't show authorId for history items
public class TaskActivityDto {
    private ActivityType type;
    private String details;
    private Long authorId;
    private Long timestamp;
}