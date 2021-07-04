package org.marmots.generator.utils.beautifier;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BeautifierCommandTest {

  @Test
  public void testMatches() {
    BeautifierCommand command = BeautifierCommand.create("doesn't matter", "doesn't matter", "jejo, rest, *.java, jejo.*");
    assertTrue("command matches jejo", command.matches("re.jejo"));
    assertTrue("command matches rest", command.matches("re.rest"));
    assertTrue("command matches java", command.matches("re.java"));
    assertTrue("command matches java", command.matches("puta.java"));
    assertTrue("command matches jejo.*", command.matches("jejo.java"));
    assertTrue("command matches jejo.*", command.matches("jejo.txt"));
    assertTrue("command doesn't match jota", !command.matches("re.jota"));
    assertTrue("command doesn't match puta", !command.matches("re.puta"));
  }

}
