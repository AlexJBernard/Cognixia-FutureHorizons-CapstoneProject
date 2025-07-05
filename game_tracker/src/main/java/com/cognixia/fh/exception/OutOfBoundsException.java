package com.cognixia.fh.exception;

/**
 * The following exception is used when the user tries to apply a number to a field that goes out of bounds
 */
public class OutOfBoundsException extends Exception {
  public OutOfBoundsException(String errorMessage) {
    super(errorMessage);
  }
}
