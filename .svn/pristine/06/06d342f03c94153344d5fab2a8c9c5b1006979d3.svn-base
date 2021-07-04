package org.marmots.generator.utils.resources;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** A {@link URLStreamHandler} that handles resources on the classpath. */
public class URLClasspathHandler extends URLStreamHandler {

  /** The classloader to find resources from. */
  private final ClassLoader classLoader;

  public URLClasspathHandler() {
    this.classLoader = getClass().getClassLoader();
  }

  public URLClasspathHandler(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Override
  protected URLConnection openConnection(URL u) throws IOException {
    final URL resourceUrl = classLoader.getResource(u.getPath());
    return resourceUrl.openConnection();
  }
}
