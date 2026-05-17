package generated.policy;

import java.util.Set;

/** Requirement: Делегирование согласования заместителю при отсутствии основного исполнителя. Coverage estimate: 0.75. */
public final class ReqDelegation {
    public boolean canApprove(String actorId, String authorId, Set<String> roles, String requiredRole) {
        return !actorId.equals(authorId) && roles.contains(requiredRole);
    }

    public boolean canRead(String actorId, String authorId, boolean departmentManager) {
        return actorId.equals(authorId) || departmentManager;
    }
}