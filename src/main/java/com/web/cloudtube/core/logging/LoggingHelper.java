package com.web.cloudtube.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingHelper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String packageName = "com.web.cloudtube";

    public void printStackTrace() {
        Thread thread = Thread.currentThread();
        for (StackTraceElement ste : thread.getStackTrace())
        {
            if(ste.toString().contains(packageName)) {
                System.out.println(ste);
            }
        }
    }
}
