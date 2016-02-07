package com.github.mosberger.annotationplayground.annotations

/**
 * Created by domi on 05.02.16.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class AnnotatedActivity(val layout: Int)
