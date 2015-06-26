package org.letsdig.app.models;

/**
 * Created by adrian on 6/26/15.
 */
public class LevelParameterException extends Exception {

    public LevelParameterException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Unable to create level.";
    }
}
