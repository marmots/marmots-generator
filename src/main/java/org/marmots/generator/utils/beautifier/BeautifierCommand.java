package org.marmots.generator.utils.beautifier;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class BeautifierCommand {
  private String id;
  private String command;
  private String extensions;

  public static BeautifierCommand create(String id, String command, String extensions) {
    BeautifierCommand beautifierCommand = new BeautifierCommand();
    beautifierCommand.setId(id);
    beautifierCommand.setCommand(command);
    beautifierCommand.setExtensions(extensions);
    return beautifierCommand;
  }

  public boolean matches(String fileName) {
    for (String ext : getExtensions().split(",| |;")) {
      if (!StringUtils.isEmpty(ext) && (ext.equals(FilenameUtils.getExtension(fileName)) || FilenameUtils.wildcardMatch(fileName, ext))) {
        return true;
      }
    }
    return false;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getExtensions() {
    return extensions;
  }

  public void setExtensions(String extensions) {
    this.extensions = extensions;
  }

}