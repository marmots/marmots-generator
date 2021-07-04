package org.marmots.generator.utils.beautifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO find a pure java beautifier
// TODO especificar extensions i beautifiers a la configuració
public class Beautifier {
  private static final Logger LOGGER = LoggerFactory.getLogger(Beautifier.class);

  private BeautifierCommand[] commands = { BeautifierCommand.create("js-beautify", "js-beautify -r -s 2 ", "json, html"),
      BeautifierCommand.create("clang-format", "clang-format -style=Google -sort-includes -i ", "java, js, jsp, xml") };

  private static Beautifier instance;

  private Beautifier() {

  }

  public static Beautifier getInstance() {
    if (instance == null) {
      instance = new Beautifier();
    }
    return instance;
  }

  public void beautifyFolder(File file) throws InterruptedException {
    LOGGER.trace("processing file: {}", file);
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        beautifyFolder(f);
      }
    } else {
      beautify(file);
    }
  }

  public void beautify(List<String> files) throws InterruptedException {
    for (String s : files) {
      beautify(new File(s));
    }
  }

  private void beautify(File file) throws InterruptedException {
    for (BeautifierCommand command : commands) {
      if (command.matches(file.getPath())) {
        String cmd = command.getCommand() + " " + file.getPath();
        try {
          Process process = Runtime.getRuntime().exec(cmd);
          int exitVal = process.waitFor();
          LOGGER.info("beautify command (exit:{}): {}", exitVal, cmd);
        } catch (IOException e) {
          LOGGER.error("Ups! IOException...", e);
        }
      }
    }
  }

  public BeautifierCommand[] getCommands() {
    return commands;
  }

  public void setCommands(BeautifierCommand[] commands) {
    this.commands = commands;
  }
}
