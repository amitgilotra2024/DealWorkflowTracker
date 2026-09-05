package com.app.dealworkflowtracker.engine;

import com.app.dealworkflowtracker.domain.DealEvent;
import com.app.dealworkflowtracker.domain.DealState;
import org.springframework.stereotype.Component;

@Component
public class DealStateTransitionEngine {

    public DealState transition(DealState currentState, DealEvent event) {
        return switch (currentState) {
            case DealState.DraftState draft -> switch (event) {
                case DealEvent.SubmitForUnderwriting submit -> new DealState.UnderwritingState(submit.analyst());
                case DealEvent.Reject reject -> new DealState.RejectedState(reject.actor(), reject.reason());
                default -> throwInvalidTransition(currentState, event);
            };

            case DealState.UnderwritingState underwriting -> switch (event) {
                case DealEvent.PassUnderwriting pass -> new DealState.ComplianceCheckState(pass.analyst(), true);
                case DealEvent.Reject reject -> new DealState.RejectedState(reject.actor(), reject.reason());
                default -> throwInvalidTransition(currentState, event);
            };

            case DealState.ComplianceCheckState compliance -> switch (event) {
                case DealEvent.Approve app -> new DealState.ApprovedState(app.approver(), app.approvalNotes());
                case DealEvent.Reject reject -> new DealState.RejectedState(reject.actor(), reject.reason());
                default -> throwInvalidTransition(currentState, event);
            };

            case DealState.ApprovedState approved ->
                    throw new IllegalStateException("Approved deals are immutable and cannot transition.");
            case DealState.RejectedState rejected ->
                    throw new IllegalStateException("Rejected deals cannot transition to new states.");
        };
    }

    private DealState throwInvalidTransition(DealState state, DealEvent event) {
        throw new IllegalStateException(
                String.format("Cannot execute event '%s' while deal is in '%s' state.",
                        event.getClass().getSimpleName(), state.name())
        );
    }
}