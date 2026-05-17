package generated.policy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Requirement: Проверка автора, руководителя подразделения, согласующего и администратора. Coverage: 0.90. */
public final class ReqAccessBase {
    public AccessDecision canRead(Actor actor, RequestView request) {
        if (actor.hasRole("ADMIN") || actor.userId.equals(request.authorId)) {
            return AccessDecision.allow("Actor can read own or administrated request");
        }
        if (actor.departmentCode.equals(request.departmentCode) && actor.hasRole("DEPARTMENT_MANAGER")) {
            return AccessDecision.allow("Department manager can read department request");
        }
        return AccessDecision.deny("Actor has no read permissions for request");
    }

    public AccessDecision canApprove(Actor actor, RequestView request, String requiredRole) {
        if (actor.userId.equals(request.authorId)) {
            return AccessDecision.deny("Author cannot approve own request");
        }
        if (actor.hasRole(requiredRole) || actor.delegatedRoles.contains(requiredRole)) {
            return AccessDecision.allow("Actor has required approval role");
        }
        return AccessDecision.deny("Required approval role is missing: " + requiredRole);
    }

    public static final class Actor {
        public final String userId;
        public final String departmentCode;
        public final Set<String> roles;
        public final Set<String> delegatedRoles;

        public Actor(String userId, String departmentCode, Set<String> roles, Set<String> delegatedRoles) {
            this.userId = Objects.requireNonNull(userId, "userId");
            this.departmentCode = Objects.requireNonNull(departmentCode, "departmentCode");
            this.roles = Collections.unmodifiableSet(new HashSet<>(roles));
            this.delegatedRoles = Collections.unmodifiableSet(new HashSet<>(delegatedRoles));
        }

        boolean hasRole(String role) {
            return roles.contains(role);
        }
    }

    public static final class RequestView {
        public final String requestId;
        public final String authorId;
        public final String departmentCode;

        public RequestView(String requestId, String authorId, String departmentCode) {
            this.requestId = Objects.requireNonNull(requestId, "requestId");
            this.authorId = Objects.requireNonNull(authorId, "authorId");
            this.departmentCode = Objects.requireNonNull(departmentCode, "departmentCode");
        }
    }

    public static final class AccessDecision {
        public final boolean allowed;
        public final String reason;

        private AccessDecision(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
        }

        static AccessDecision allow(String reason) {
            return new AccessDecision(true, reason);
        }

        static AccessDecision deny(String reason) {
            return new AccessDecision(false, reason);
        }
    }
}