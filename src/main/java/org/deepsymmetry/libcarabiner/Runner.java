package org.deepsymmetry.libcarabiner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Manages the extraction of a compatible version of the Carabiner executable for the current platform, as well
 * as starting and stopping the process when needed, with appropriate command-line arguments and I/O redirection.
 *
 * Created by James Elliott on 2020-01-18.
 */
public class Runner {

    /**
     * Holds the singleton instance of this class.
     */
    private static final Runner ourInstance = new Runner();

    /**
     * Get the singleton instance of this class.
     *
     * @return the only instane of this class that exists.
     */
    public static Runner getInstance() {
        return ourInstance;
    }

    /**
     * Prevent direct instantiation.
     */
    private Runner() {
    }

    /**
     * Returns the operating system name or abbreviation that will be used to search for an embedded Carabiner
     * executable. This will be "Mac", "Win", or "Linux", or (for any other non-supported platform) the value of
     * the "os.name" System property converted to lower case and with whitespace removed.
     *
     * @return The operating system component of the executable resource name.
     */
    private String getOSComponent()
    {
        final String osName = System.getProperty("os.name").toLowerCase().replace(" ", "");

        if (osName.equals("macosx") || osName.equals("macos")) {
            return "Mac";
        }

        if (osName.contains("windows")) {
            return "Win";
        }

        if (osName.contains("linux")) {
            return "Linux";
        }

        return osName;
    }

    /**
     * Returns the CPU architecture abbreviation that will be used to search for an embedded Carabiner executable.
     * This will be "x86", "x64" (architecture names i386 und amd64 are converted accordingly), "arm", or "arm64",
     * or (when the architecture is unsupported) the value of the "os.arch" System property converted to lower-case
     * and with whitespace removed.
     *
     * @return The CPU architecture component of the executable resource name.
     */
    private String getArchComponent()
    {
        final String arch = System.getProperty("os.arch").toLowerCase().replace(" ", "");
        if (arch.equals("i386")) {
            return "x86";
        }

        if (arch.equals("amd64") || arch.equals("x86_64")) {
            return "x64";
        }

        if (arch.equals("aarch64")) {
            return "arm64";
        }

        if (arch.equals("armhf") || arch.equals("aarch32") || arch.equals("armv7l")) {
            return "arm";
        }

        return arch;
    }

    /**
     * Determines the name by which a Carabiner binary compatible with the current operating system and processor
     * architecture would be identified.
     *
     * @return the name of the resource holding the compatible binary, if one can be found.
     */
    public String getExecutableName() {
        return "Carabiner_" + getOSComponent() + "_" + getArchComponent();
    }

    public boolean canRunCarabiner() {
        return Runner.class.getResource(getExecutableName()) != null;
    }

    /**
     * Holds the file into which we extract and run Carabiner.
     */
    private static File carabiner;

    /**
     * Creates the temporary file used for unpacking the native Carabiner executable.
     * This file is marked to be deleted when the JVM exits.
     *
     * @return The temporary file for the native executable.
     *
     * @throws IOException if there is a problem creating the file.
     */
    private static File createTempFile() throws IOException {

        if (carabiner != null) {
            // We have already created it, so simply return it
            return carabiner;
        }

        try {
            carabiner = File.createTempFile("Carabiner", ".exe");

            if (!carabiner.canWrite()) {
                throw new IOException("Unable to write to temporary file " + carabiner);
            }

            carabiner.deleteOnExit();
            return carabiner;

        } catch (final IOException e) {
            throw new IOException("Unable to create temporary directory for the Carabiner executable: " + e, e);
        }
    }

    /**
     * Copies the specified input stream to the specified output file.
     *
     * @param input   The input stream to be copied.
     * @param output  The output file to which the stream should be copied.
     *
     * @throws IOException If the copy failed.
     */
    private static void copy(final InputStream input, final File output) throws IOException {
        // TODO: Just call this directly, rather than using this method.
        Files.copy(input, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
