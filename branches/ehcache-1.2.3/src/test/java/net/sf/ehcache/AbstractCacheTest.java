/**
 *  Copyright 2003-2006 Greg Luck
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sf.ehcache;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Common fields and methods required by most test cases
 *
 * @author <a href="mailto:gluck@thoughtworks.com">Greg Luck</a>
 * @version $Id$
 */
public abstract class AbstractCacheTest extends TestCase {

    /**
     * Where the config is
     */
    public static final String SRC_CONFIG_DIR = "src/main/config/";
    /**
     * Where the test config is
     */
    public static final String TEST_CONFIG_DIR = "src/test/resources/";


    /**
     * Where the test classes are compiled.
     */
    public static final String TEST_CLASSES_DIR = "target/test-classes/";

    /**
     * name for sample cache 1
     */
    protected final String sampleCache1 = "sampleCache1";
    /**
     * name for sample cache 2
     */
    protected final String sampleCache2 = "sampleCache2";
    /**
     * the CacheManager instance
     */
    protected CacheManager manager;

    /**
     * setup test
     */
    protected void setUp() throws Exception {
        manager = CacheManager.create();
    }

    /**
     * teardown
     */
    protected void tearDown() throws Exception {
        manager.shutdown();
    }


    /**
     * @param name
     * @throws IOException
     */
    protected void deleteFile(String name) throws IOException {
        String diskPath = System.getProperty("java.io.tmpdir");
        final File diskDir = new File(diskPath);
        File dataFile = new File(diskDir, name + ".data");
        if (dataFile.exists()) {
            dataFile.delete();
        }
        File indexFile = new File(diskDir, name + ".index");
        if (indexFile.exists()) {
            indexFile.delete();
        }
    }

    /**
     * Measure memory used by the VM.
     *
     * @return
     * @throws InterruptedException
     */
    protected long measureMemoryUse() throws InterruptedException {
        System.gc();
        Thread.sleep(2000);
        System.gc();
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * Runs a set of threads, for a fixed amount of time.
     */
    protected void runThreads(final List executables) throws Exception {

        final long endTime = System.currentTimeMillis() + 10000;
        final Throwable[] errors = new Throwable[1];

        // Spin up the threads
        final Thread[] threads = new Thread[executables.size()];
        for (int i = 0; i < threads.length; i++) {
            final Executable executable = (Executable) executables.get(i);
            threads[i] = new Thread() {
                public void run() {
                    try {
                        // Run the thread until the given end time
                        while (System.currentTimeMillis() < endTime) {
                            executable.execute();
                        }
                    } catch (Throwable t) {
                        // Hang on to any errors
                        errors[0] = t;
                    }
                }
            };
            threads[i].start();
        }

        // Wait for the threads to finish
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        // Throw any error that happened
        if (errors[0] != null) {
            throw new Exception("Test thread failed.", errors[0]);
        }
    }


    /**
     * A runnable, that can throw an exception.
     */
    protected interface Executable {
        /**
         * Executes this object.
         *
         * @throws Exception
         */
        void execute() throws Exception;
    }

}
