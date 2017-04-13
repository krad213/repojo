package su.kore.tools.repojo

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * Created by krad on 13.04.2017.
 */
class MainProcessor(elementUtils: Elements, messager: Messager, filler: Filer, typeUtils: Types) {
    val elementUtils = elementUtils
    val messager = messager
    val filler = filler
    val typeUtils = typeUtils
    fun process(annotatedSet: Set<Element>) {
        annotatedSet.forEach { process(it) }
    }

    fun process(element: Element) {
        if (element is TypeElement) {
            val members = elementUtils.getAllMembers(element)
            val getters = members.filter { it.kind == ElementKind.METHOD && it.simpleName.startsWith("get") }

        }
    }


}