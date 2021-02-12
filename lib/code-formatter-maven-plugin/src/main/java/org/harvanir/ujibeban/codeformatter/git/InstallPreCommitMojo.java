package org.harvanir.ujibeban.codeformatter.git;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/** @author Harvan Irsyadi */
@Mojo(name = "install-pre-commit", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class InstallPreCommitMojo extends AbstractMojo {
  private static final String PRE_COMMIT = "pre-commit";

  private static final String OUTPUT_FILE_NAME = ".git/hooks/" + PRE_COMMIT;

  public void execute() throws MojoExecutionException {
    getLog().debug("====== InstallPreCommitMojo ======");
    try (FileOutputStream fos = new FileOutputStream(OUTPUT_FILE_NAME)) {
      InputStream fis = getClass().getClassLoader().getResourceAsStream(PRE_COMMIT);

      if (fis == null) {
        getLog().error(String.format("No %s file found in classpath.", PRE_COMMIT));
        throw new FileNotFoundException(PRE_COMMIT);
      }

      final byte[] bytes = new byte[2048];
      int length;

      while ((length = fis.read(bytes)) >= 0) {
        fos.write(bytes, 0, length);
      }

      fis.close();

      setPermission();
    } catch (final FileNotFoundException e) {
      throw new MojoExecutionException("No file found", e);
    } catch (final IOException e) {
      throw new MojoExecutionException("Exception reading files", e);
    }
  }

  private void setPermission() throws IOException {
    Set<PosixFilePermission> perms =
        Files.readAttributes(Paths.get(OUTPUT_FILE_NAME), PosixFileAttributes.class).permissions();

    perms.add(PosixFilePermission.OWNER_READ);
    perms.add(PosixFilePermission.OWNER_WRITE);
    perms.add(PosixFilePermission.OWNER_EXECUTE);

    Files.setPosixFilePermissions(Paths.get(OUTPUT_FILE_NAME), perms);
  }
}
