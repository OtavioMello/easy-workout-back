package tcc.project.easy_workout.common.exception.model;

import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

@Getter
@Setter
public class ConflictException extends ServerErrorException {

    public ConflictException(String message, Response.Status status) {
        super(message, status);
    }
}
