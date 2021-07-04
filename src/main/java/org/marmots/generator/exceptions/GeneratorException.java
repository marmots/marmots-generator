package org.marmots.generator.exceptions;

public class GeneratorException extends Exception {
  /**
   * generated uid
   */
  private static final long serialVersionUID = 4265971488742542220L;

  public GeneratorException() {
  }

  public GeneratorException(String message) {
    super(message);
  }

  public GeneratorException(Throwable cause) {
    super(cause);
  }

  public GeneratorException(String message, Throwable cause) {
    super(message, cause);
  }

  public GeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
