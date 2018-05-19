/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Command {

    void execute(final String[] args);

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface CommandInfo {

        String name();

        String[] aliases();

    }

}