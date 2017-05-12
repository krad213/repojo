package su.kore.tools.repojo

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import su.kore.tools.repojo.Global.elementUtils
import su.kore.tools.repojo.exceptions.MoreThanOneElementsException
import su.kore.tools.repojo.meta.ClassInfo
import su.kore.tools.repojo.meta.Property
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement


/**
 * Created by krad on 13.04.2017.
 */
class MainProcessor {

    fun process(annotatedSet: Set<Element>) {
        annotatedSet.forEach { process(it) }
    }

    fun process(element: Element) {
        if (element is TypeElement) {
            val getters = element.getters()
            val properties = getters.map { createPropery(it as ExecutableElement, element) }
            val generate = element.getAnnotationsByType(Generate::class.java).asList()
            val classInfo = ClassInfo(element, properties, generate)
            write(classInfo)
        }
    }

    fun write(classInfo: ClassInfo) {
        for (generate in classInfo.generate) {
            //TODO: this is quite wrong, redo to plugable generators
            if (generate.targetType == TargetType.POJO) {
                val classBuilder = TypeSpec.classBuilder("${classInfo.type.simpleName}${generate.suffix}")
                for (property in classInfo.properties) {
                    if (!property.excludes.contains(generate.id)) {
                        val typeName = TypeName.get(property.type);
                        classBuilder.addField(FieldSpec.builder(typeName, property.name).build())
                    }
                }

                val javaFile = JavaFile.builder(generate.packageName, classBuilder.build()).build()

                javaFile.writeTo(Global.filer)
            }
        }
    }


    private fun createPropery(getter: ExecutableElement, classElement: TypeElement): Property {
        val name: String
        if (getter.simpleName.startsWith("get")) {
            name = getter.simpleName.substring("get".length).decapitalize()
        } else {
            name = getter.simpleName.substring("is".length).decapitalize()
        }
        val type = getter.returnType

        val excludes = getter.getAnnotationsByType(Exclude::class.java).asList().map { it.id }

        val readOnly = classElement.getMethod("set${name.capitalize()}") == null

        return Property(name, type, readOnly, excludes)
    }


}

fun TypeElement.getters(): List<Element> {
    val members = elementUtils.getAllMembers(this)
    val getters = members.filter { it.kind == ElementKind.METHOD && (it.simpleName.startsWith("get") || it.simpleName.startsWith("is")) && "getClass" != it.simpleName.toString() }
    return getters
}

fun TypeElement.getMethod(name: String): ExecutableElement? {
    val members = elementUtils.getAllMembers(this)
    val methods = members.filter { it.kind == ElementKind.METHOD && it.simpleName.toString() == name }
    if (methods.size > 1) {
        throw MoreThanOneElementsException()
    } else if (methods.isEmpty()) {
        return null
    } else {
        return methods[0] as ExecutableElement?
    }
}