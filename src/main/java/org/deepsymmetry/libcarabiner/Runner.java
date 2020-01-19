package org.deepsymmetry.libcarabiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Manages the extraction of a compatible version of the Carabiner executable for the current platform, as well
 * as starting and stopping the process when needed, with appropriate command-line arguments, and logs any output
 * or errors produced by the process.
 *
 * Created by James Elliott on 2020-01-18.
 */
public class Runner {

    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    /**
     * Holds the singleton instance of this class.
     */
    private static final Runner ourInstance = new Runner();

    /**
     * Get the singleton instance of this class.
     *
     * @return the only instance of this class that exists.
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

    /**
     * Checks whether it will be possible to run Carabiner on the current computer.
     *
     * @return {@code true} if we expect to be able to extract and run a compatible Carabiner binary.
     */
    public boolean canRunCarabiner() {
        return Runner.class.getResource(getExecutableName()) != null;
    }

    /**
     * Holds the file into which we extract and run Carabiner.
     */
    private File carabiner;

    /**
     * Creates the temporary Carabiner executable file compatible with the current operating system and
     * processor architecture if we haven't already done so. This file is marked to be deleted when the JVM exits.
     *
     * @return The temporary file holding the native executable.
     *
     * @throws IOException if there is a problem creating the file.
     * @throws IllegalStateException if we can't find a compatible binary.
     */
    private File createExecutable() throws IOException {

        if (carabiner != null) {
            // We have already created it, so simply return it
            return carabiner;
        }

        InputStream binary = Runner.class.getResourceAsStream(getExecutableName());
        if (binary == null) {
            throw new IllegalStateException("Incompatible platform: there is no Carabiner binary named " + getExecutableName());
        }

        try {
            carabiner = File.createTempFile("Carabiner", ".exe");
            carabiner.deleteOnExit();

            if (!carabiner.canWrite()) {
                throw new IOException("Unable to write to temporary file " + carabiner);
            }

            Files.copy(binary, carabiner.toPath(), StandardCopyOption.REPLACE_EXISTING);
            if (!carabiner.setExecutable(true)) {
                throw new IOException("Unable to make binary file executable, " + carabiner);
            }
            return carabiner;

        } catch (final IOException e) {
            throw new IOException("Unable to create temporary directory for the Carabiner executable: " + e, e);
        }
    }

    /**
     * Holds the process that is running our managed copy of Carabiner, if it is running.
     */
    private final AtomicReference<Process> process = new AtomicReference<>(null);

    /**
     * The port on which our managed copy of Carabiner will listen.
     */
    private final AtomicInteger port = new AtomicInteger(17000);

    /**
     * Set the port on which the managed Carabiner instance should listen. The default value is 17000.
     *
     * @param carabinerPort the TCP port that we will attempt to configure Carabiner to use.
     *
     * @throws IllegalArgumentException if {@code carabinerPort} is less than 1 or greater than 32767
     * @throws IllegalStateException if our Carabiner instance is already running when this method is called
     */
    public void setPort(int carabinerPort) {
        if (carabinerPort < 1 || carabinerPort > 32767) {
            throw new IllegalArgumentException("Carabiner port must be in the range 1-32767.");
        }
        if (process.get() !=  null) {
            throw new IllegalStateException("Carabiner port can only be set when it is not running.");
        }
        port.set(carabinerPort);
    }

    /**
     * The minimum interval Carabiner will wait between sending updates when changes are occurring on
     * the Link session (milliseconds).
     */
    private final AtomicInteger updateInterval = new AtomicInteger(20);

    /**
     * Set the minimum interval between updates delivered by Carabiner when changes are occurring on
     * the Link session. The default value is 20, to deliver updates up to fifty times per second.
     *
     * @param interval the number of milliseconds carabiner will wait from the last time it delivered a
     *                 session update message to send the next if there has been another change.
     *
     * @throws IllegalArgumentException if {@code interval} is less than 1 or greater than 1000
     * @throws IllegalStateException if our Carabiner instance is already running when this method is called
     */
    public void setUpdateInterval(int interval) {
        if (interval < 1 || interval > 1000) {
            throw new IllegalArgumentException("Carabiner update interval must be in the range 1-1000.");
        }
        if (process.get() !=  null) {
            throw new IllegalStateException("Carabiner update interval can only be set when it is not running.");
        }
        updateInterval.set(interval);
    }

    /**
     * Called whenever we detect the Carabiner process has ended because one of the pipes from it has closed.
     * If this is the first we noticed it, report information in the log file.
     */
    private void reportCarabinerEnding() {
        Process wasRunning = process.getAndSet(null);
        if (wasRunning != null) {
            try {
                final int status = wasRunning.waitFor();
                if (status == 0) {
                    logger.info("Carabiner process exited normally.");
                } else if ((status == 143 && !getOSComponent().equals("Win")) || (status == 1 && getOSComponent().equals("Win"))) {
                    logger.info("Carabiner process forcibly terminated.");
                } else {
                    logger.warn("Carabiner process exited with status " + status);
                }
            } catch (InterruptedException e) {
                logger.error("Inexplicably interrupted waiting for Carabiner process to finish", e);
            }
        }
    }

    /**
     * Start our managed Carabiner instance running if it isn't already.
     *
     * @throws IOException if there is a problem creating the Carabiner process.
     */
    public synchronized void start() throws IOException {
        if (process.get() == null) {
            final File binary = createExecutable();
            final String[] command = {
                    binary.getAbsolutePath(), "--daemon",
                    "--port", String.valueOf(port.get()),
                    "--poll", String.valueOf(updateInterval.get())
            };

            final Process started = Runtime.getRuntime().exec(command);
            process.set(started);

            // Send Carabiner's standard output to our log at the "info" level.
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(started.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.info(line.trim());
                    }
                } catch (IOException e) {
                    logger.error("Problem reading Carabiner output", e);
                    stop();  // Make sure the process is shut down, although it probably already is.
                }
                reportCarabinerEnding();
            }, "Carabiner Output Logger").start();

            // Send Carabiner's error output to our log at the "error" level.
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(started.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logger.error(line.trim());
                    }
                } catch (IOException e) {
                    logger.error("Problem reading Carabiner errors", e);
                    stop();  // Make sure the process is shut down, although it probably already is.
                }
                reportCarabinerEnding();
            }, "Carabiner Error Logger").start();
        }
    }

    /**
     * Shut down our managed Carabiner instance if it is running.
     */
    public synchronized void stop() {
        final Process wasRunning = process.get();
        if (wasRunning != null) {
            wasRunning.destroy();
        }
    }
}
