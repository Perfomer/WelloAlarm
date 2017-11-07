package com.volkovmedia.perfo.welloalarm.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Scope;

public class Annotations {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ActivityContext {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplicationContext {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DatabaseInfo {}

    @Scope
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PerActivity {}

}
