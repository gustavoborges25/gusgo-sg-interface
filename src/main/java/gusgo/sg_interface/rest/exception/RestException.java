package gusgo.sg_interface.rest.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;


    public RestException(String message) {
        this.message = message;
    }

}
