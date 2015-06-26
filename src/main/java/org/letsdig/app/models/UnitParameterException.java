package org.letsdig.app.models;

/**
 * Created by adrian on 6/15/15.
 */
public class UnitParameterException extends Exception {

    private String squareId;

    public UnitParameterException(String message, String squareId) {
        super(message);
        this.squareId = squareId;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Unable to create unit for square " + this.getSquareId();
    }

    public String getSquareId() {
        return squareId;
    }
}
