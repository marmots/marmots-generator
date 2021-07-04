package org.marmots.generator.exceptions;

public class ValidationException extends RuntimeException {

  /**
   * generated uid
   */
  private static final long serialVersionUID = 2831720204631773869L;

  public ValidationException() {

  }

  public ValidationException(String arg0) {
    super(arg0);
  }

  public ValidationException(Throwable arg0) {
    super(arg0);
  }

  public ValidationException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public ValidationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

}
