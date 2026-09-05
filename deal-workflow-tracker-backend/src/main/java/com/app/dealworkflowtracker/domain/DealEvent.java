package com.app.dealworkflowtracker.domain;

public sealed interface DealEvent permits
        DealEvent.SubmitForUnderwriting,
        DealEvent.PassUnderwriting,
        DealEvent.Approve,
        DealEvent.Reject {

    record SubmitForUnderwriting(String analyst) implements DealEvent {}
    record PassUnderwriting(String analyst) implements DealEvent {}
    record Approve(String approver, String approvalNotes) implements DealEvent {}
    record Reject(String actor, String reason) implements DealEvent {}
}