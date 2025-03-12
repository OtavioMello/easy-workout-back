package tcc.project.easy_workout.auth.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import tcc.project.easy_workout.auth.security.JsonWebTokenService;

import javax.ws.rs.ForbiddenException;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthValidation {

    public static void validateResourceAccessByAuthorizationUserId(String id, String authorization){
        var tokenUserId = JsonWebTokenService.getUserId(authorization.split(" ")[1]);
        if (!id.equals(tokenUserId)){
            throw new ForbiddenException("Authenticated user hasn't access to this resource");
        }
    }
}
