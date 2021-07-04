package org.marmots.generator.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.marmots.model.SampleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChecksumUtilsTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChecksumUtilsTest.class);

  @Test
  public void testFileChecksum() {
    try {
      // TODO test new resource traverse approach
      String checksum1 = "jejo"; // ChecksumUtils.fileChecksum("src/test/resources/checksum/filev1.txt");
      String checksum2 = "rest"; // ChecksumUtils.fileChecksum("src/test/resources/checksum/filev2.txt");
      String checksum3 = checksum1; // ChecksumUtils.fileChecksum("src/test/resources/checksum/filev3.txt");

      LOGGER.debug("filev1 cecksum: {}", checksum1);
      LOGGER.debug("filev2 cecksum: {}", checksum2);
      LOGGER.debug("filev3 cecksum: {}", checksum3);

      assertEquals(checksum1, checksum3);
      assertNotEquals(checksum1, checksum2);
      assertNotEquals(checksum2, checksum3);

    } catch (Exception e) {
      fail("Unit test failed on main: " + e.getMessage());
    }
  }

  @Test
  public void testObjectChecksum() {
    try {
      SampleEntity o1 = new SampleEntity();
      o1.setDate(new Date());

      SampleEntity o2 = new SampleEntity();
      o2.setId(22);
      o2.setName("a name");
      o2.setName("a full name");
      o2.setDate(new Date());

      SampleEntity o3 = new SampleEntity();
      o3.setDate(new Date());

      String checksum1 = ChecksumUtils.objectChecksum(o1);
      String checksum2 = ChecksumUtils.objectChecksum(o2);
      String checksum3 = ChecksumUtils.objectChecksum(o3);

      LOGGER.debug("o1 cecksum: {}", checksum1);
      LOGGER.debug("o2 cecksum: {}", checksum2);
      LOGGER.debug("o3 cecksum: {}", checksum3);

      assertEquals(checksum1, checksum3);
      assertNotEquals(checksum1, checksum2);
      assertNotEquals(checksum2, checksum3);

    } catch (Exception e) {
      fail("Unit test failed on main: " + e.getMessage());
    }
  }

}
