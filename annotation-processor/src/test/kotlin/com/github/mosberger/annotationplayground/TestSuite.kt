package com.github.mosberger.annotationplayground

import org.junit.Test
import com.google.common.truth.Truth.ASSERT
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

/**
 * Created by domi on 11.02.16.
 */
class TestSuite {
    private val SOURCE = listOf(
                        "package test;",
                        "import com.github.mosberger.annotationplayground.annotations.AnnotatedActivity;",
                        "@AnnotatedActivity(1)",
                        "public class MainActivity {",
                        "}"
    )
    private val RESULT = listOf(
                        "package test;",
                        "public class MainActivityUtil {",
                        "}"
    )

    @Test
    fun testy() {
        ASSERT
                .about(javaSource())
                .that(JavaFileObjects.forSourceLines("MainActivity",SOURCE))
                .processedWith(AndroidDataBindingProcessor())
                .compilesWithoutError()
    }
}