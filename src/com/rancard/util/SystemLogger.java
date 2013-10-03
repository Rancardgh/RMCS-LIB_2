package com.rancard.util;

import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Mustee
 * Date: 9/17/13
 * Time: 5:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemLogger {
    private Class c;

    public SystemLogger(Class c) {
        this.c = c;
    }

    public void log(Level level, String message) {
        System.out.println(new Date() + "\t[" + c.getName() + "]\t" + level.toString() + "\t" + message);
    }

    public enum Level {
        DEBUG,
        INFO,
        ERROR
    }
}
