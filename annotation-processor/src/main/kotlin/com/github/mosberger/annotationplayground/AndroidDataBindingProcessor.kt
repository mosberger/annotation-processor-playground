package com.github.mosberger.annotationplayground

import com.github.mosberger.annotationplayground.annotations.AnnotatedActivity
import com.github.mosberger.annotationplayground.annotations.DataBinding
import com.github.mosberger.annotationplayground.annotations.ViewModel
import com.github.mosberger.annotationplayground.model.DataBindingProperties
import com.github.mosberger.annotationplayground.model.ElementProperties
import com.github.mosberger.annotationplayground.model.ViewModelProperties
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

/**
 * Created by domi on 05.02.16.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("ch.domi.annotationplayground")
class AndroidDataBindingProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        for (element in getElementsOfType(roundEnvironment, AnnotatedActivity::class.java)) {
            val elementProperties = getProperties(element)

            val url = this.javaClass.classLoader.getResource("velocity.properties")

            val properties = Properties()
            properties.load(url.openStream())

            val velocityEngine = VelocityEngine(properties)
            val javaFile = processingEnv.filer.createSourceFile("${element.qualifiedName}Gen")
            val writer = javaFile.openWriter()
            val template = velocityEngine.getTemplate("AnnotatedActivity.vm")

            val velocityContext = VelocityContext()
            velocityContext.put("properties", elementProperties)
            template.merge(velocityContext, writer)
            writer.close()
        }

        return true
    }

    private fun getProperties(element: TypeElement): ElementProperties {
        val packageName = (element.enclosingElement as PackageElement).qualifiedName.toString()
        val className = element.simpleName.toString()
        val layout = element.getAnnotation(AnnotatedActivity::class.java).layout

        val viewModelPropertiesList = arrayListOf<ViewModelProperties>()
        for (viewModel in getElementsOfType(element, ViewModel::class.java)) {
            val field = viewModel.simpleName.toString()
            val viewModelClassName = viewModel.asType().toString()
            val property = viewModel.getAnnotation(ViewModel::class.java).setter

            viewModelPropertiesList.add(ViewModelProperties(field, viewModelClassName, property))
        }

        val binding = getElementsOfType(element, DataBinding::class.java).first()
        val bindingName = binding.simpleName.toString()
        val dataBindingProperties = DataBindingProperties(bindingName)

        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Done")

        return ElementProperties(packageName, className, layout, viewModelPropertiesList, dataBindingProperties)
    }

    private fun getElementsOfType(roundEnvironment: RoundEnvironment, clazz: Class<out Annotation>) =
            roundEnvironment.getElementsAnnotatedWith(clazz).map { it as TypeElement }

    private fun getElementsOfType(element: Element, clazz: Class<out Annotation>) =
            element.enclosedElements.filter { it.kind.isField and (it.getAnnotation(clazz) != null) }.map { it as VariableElement }
}