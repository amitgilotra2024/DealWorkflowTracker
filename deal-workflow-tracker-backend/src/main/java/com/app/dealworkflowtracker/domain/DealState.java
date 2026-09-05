package com.app.dealworkflowtracker.domain;

public sealed interface DealState permits
        DealState.DraftState,
        DealState.UnderwritingState,
        DealState.ComplianceCheckState,
        DealState.ApprovedState,
        DealState.RejectedState {

    String name();

    record DraftState() implements DealState {
        @Override public String name() { return "DRAFT"; }
    }

    record UnderwritingState(String assignedAnalyst) implements DealState {
        @Override public String name() { return "UNDERWRITING"; }
    }

    record ComplianceCheckState(String analyst, boolean riskVerified) implements DealState {
        @Override public String name() { return "COMPLIANCE_CHECK"; }
    }

    record ApprovedState(String approvedBy, String approvalNotes) implements DealState {
        @Override public String name() { return "APPROVED"; }
    }

    record RejectedState(String rejectedBy, String reason) implements DealState {
        @Override public String name() { return "REJECTED"; }
    }

    static DealState fromString(String stateStr, String actor, String notes) {
        if (stateStr == null) return new DraftState();
        return switch (stateStr.toUpperCase()) {
            case "DRAFT" -> new DraftState();
            case "UNDERWRITING" -> new UnderwritingState(actor != null ? actor : "UNASSIGNED");
            case "COMPLIANCE_CHECK" -> new ComplianceCheckState(actor, true);
            case "APPROVED" -> new ApprovedState(actor, notes);
            case "REJECTED" -> new RejectedState(actor, notes);
            default -> throw new IllegalArgumentException("Unknown state in DB: " + stateStr);
        };
    }
}