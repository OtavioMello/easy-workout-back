package tcc.project.easy_workout.common.exception.model;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConflictException extends WebApplicationException {

    public ConflictException(String message) {
        super(message, Response.Status.CONFLICT);
    }
}
