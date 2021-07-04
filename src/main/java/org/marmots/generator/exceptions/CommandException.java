package org.marmots.generator.exceptions;

public class CommandException extends Exception {

  /**
   * generated uid
   */
  private static final long serialVersionUID = 2831720204631773869L;

  public CommandException() {

  }

  public CommandException(String arg0) {
    super(arg0);
  }

  public CommandException(Throwable arg0) {
    super(arg0);
  }

  public CommandException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public CommandException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

}
