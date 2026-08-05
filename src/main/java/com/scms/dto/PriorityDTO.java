package com.scms.dto;

import com.scms.enums.PriorityType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityDTO {

    private Long id;

    @NotNull(message = "Priority is required")
    private PriorityType name;

    @NotNull(message = "Priority level is required")
    @Min(value = 1, message = "Minimum priority level is 1")
    @Max(value = 4, message = "Maximum priority level is 4")
    private Integer level;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @Min(value = 1, message = "Response SLA must be greater than 0")
    private Integer responseSlaHours;

}