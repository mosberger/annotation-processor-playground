package com.github.mosberger.annotationplayground

import org.junit.Test
import com.google.common.truth.Truth.ASSERT
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource

/**
 * Created by domi on 11.02.16.
 */
class TestSuite {
    @Test
    fun processorTest() {
        ASSERT
                .about(javaSource())
                .that(JavaFileObjects.forResource("MainActivity.java"))
                .processedWith(AndroidDataBindingProcessor())
                .failsToCompile()
    }
}