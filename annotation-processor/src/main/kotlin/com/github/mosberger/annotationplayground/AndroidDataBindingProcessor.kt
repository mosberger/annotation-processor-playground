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
import javax.annotation.processing.*
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
class AndroidDataBindingProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes() = hashSetOf(AnnotatedActivity::class.java.canonicalName)

    override fun process(annotations: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        for (element in getElementsOfType(roundEnvironment, AnnotatedActivity::class.java)) {
            val elementProperties = getProperties(element)
            writeProperties(element, elementProperties)
        }

        return true
    }

    private fun writeProperties(element: TypeElement, elementProperties: ElementProperties) {
        val url = this.javaClass.classLoader.getResource("velocity.properties")
        val properties = Properties()
        properties.load(url.openStream())

        val velocityEngine = VelocityEngine(properties)
        val javaFile = processingEnv.filer.createSourceFile("${element.qualifiedName}Util")
        val writer = javaFile.openWriter()
        val template = velocityEngine.getTemplate("AnnotatedActivity.vm")

        val velocityContext = VelocityContext()
        velocityContext.put("properties", elementProperties)
        template.merge(velocityContext, writer)
        writer.close()
    }

    private fun getProperties(element: TypeElement): ElementProperties {
        val packageName = (element.enclosingElement as PackageElement).qualifiedName.toString()
        val className = element.simpleName.toString()
        val layout = element.getAnnotation(AnnotatedActivity::class.java).value

        val dataBindingPropertiesList = arrayListOf<DataBindingProperties>()
        for (binding in getElementsOfType(element, DataBinding::class.java)) {
            val bindingName = binding.simpleName.toString()
            val bindingValue = binding.getAnnotation(DataBinding::class.java).value
            dataBindingPropertiesList.add(DataBindingProperties(bindingName, bindingValue))

            processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "x = ${binding.asType()}")
        }

        val viewModelPropertiesList = arrayListOf<ViewModelProperties>()
        for (viewModel in getElementsOfType(element, ViewModel::class.java)) {
            val field = viewModel.simpleName.toString()
            val viewModelClassName = viewModel.asType().toString()
            val property = viewModel.getAnnotation(ViewModel::class.java).property
            val bindingSetter = viewModel.getAnnotation(ViewModel::class.java).bindingSetter
            viewModelPropertiesList.add(ViewModelProperties(field, viewModelClassName, property, bindingSetter))
        }

        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Done")

        return ElementProperties(packageName, className, layout, viewModelPropertiesList, dataBindingPropertiesList)
    }

    private fun getElementsOfType(roundEnvironment: RoundEnvironment, clazz: Class<out Annotation>) =
            roundEnvironment.getElementsAnnotatedWith(clazz).map { it as TypeElement }

    private fun getElementsOfType(element: Element, clazz: Class<out Annotation>) =
            element.enclosedElements.filter { it.kind.isField and (it.getAnnotation(clazz) != null) }.map { it as VariableElement }
}
