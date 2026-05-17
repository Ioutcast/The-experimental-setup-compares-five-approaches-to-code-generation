package generated.policy;

import java.util.Set;

/** Requirement: Проверка автора, руководителя подразделения, согласующего и администратора. Coverage estimate: 0.39. */
public final class ReqAccessBase {
    public boolean canApprove(String actorId, String authorId, Set<String> roles, String requiredRole) {
        return !actorId.equals(authorId) && roles.contains(requiredRole);
    }

    public boolean canRead(String actorId, String authorId, boolean departmentManager) {
        return actorId.equals(authorId) || departmentManager;
    }
}