package com.scms.validation;

import com.scms.enums.ComplaintStatus;
import com.scms.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class ComplaintValidator {

    private static final Map<ComplaintStatus, Set<ComplaintStatus>> STATUS_TRANSITIONS = new EnumMap<>(
            ComplaintStatus.class);

    static {

        STATUS_TRANSITIONS.put(
                ComplaintStatus.SUBMITTED,
                EnumSet.of(
                        ComplaintStatus.ASSIGNED,
                        ComplaintStatus.REJECTED));

        STATUS_TRANSITIONS.put(
                ComplaintStatus.ASSIGNED,
                EnumSet.of(
                        ComplaintStatus.IN_PROGRESS,
                        ComplaintStatus.REJECTED));

        STATUS_TRANSITIONS.put(
                ComplaintStatus.IN_PROGRESS,
                EnumSet.of(
                        ComplaintStatus.RESOLVED,
                        ComplaintStatus.REJECTED));

        STATUS_TRANSITIONS.put(
                ComplaintStatus.RESOLVED,
                EnumSet.of(
                        ComplaintStatus.CLOSED));

        STATUS_TRANSITIONS.put(
                ComplaintStatus.CLOSED,
                EnumSet.noneOf(ComplaintStatus.class));

        STATUS_TRANSITIONS.put(
                ComplaintStatus.REJECTED,
                EnumSet.noneOf(ComplaintStatus.class));
    }

    private static final Set<ComplaintStatus> EDITABLE_STATUS = EnumSet.of(
            ComplaintStatus.SUBMITTED,
            ComplaintStatus.ASSIGNED);

    public void validateStatusTransition(
            ComplaintStatus currentStatus,
            ComplaintStatus newStatus) {

        if (currentStatus == newStatus) {
            return;
        }

        Set<ComplaintStatus> allowedStatus = STATUS_TRANSITIONS.getOrDefault(
                currentStatus,
                EnumSet.noneOf(ComplaintStatus.class));

        if (!allowedStatus.contains(newStatus)) {

            throw new BadRequestException(
                    "Invalid status transition from "
                            + currentStatus
                            + " to "
                            + newStatus);
        }
    }

    public void validateEditableStatus(
            ComplaintStatus status) {

        if (!EDITABLE_STATUS.contains(status)) {

            throw new BadRequestException(
                    "Complaint cannot be edited in status "
                            + status);
        }
    }

    public void validateAssignment(
            ComplaintStatus status) {

        if (status != ComplaintStatus.SUBMITTED &&
                status != ComplaintStatus.ASSIGNED) {

            throw new BadRequestException(
                    "Complaint can only be assigned in SUBMITTED or ASSIGNED status.");
        }
    }

    public void validateResolution(
            ComplaintStatus status,
            String resolutionNotes) {

        if (status == ComplaintStatus.RESOLVED &&
                (resolutionNotes == null ||
                        resolutionNotes.isBlank())) {

            throw new BadRequestException(
                    "Resolution notes are required.");
        }
    }

}