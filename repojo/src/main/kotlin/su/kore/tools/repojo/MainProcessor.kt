package su.kore.tools.repojo

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeName
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
    val knownClassesMap = HashMap<TypeName, ClassInfo>()

    fun process(annotatedSet: Set<Element>, targets: Set<String>) {
        annotatedSet.forEach { createMetadata(it) }
        knownClassesMap.values.forEach { write(it, targets) }
    }


    fun createMetadata(element: Element) {
        if (element is TypeElement) {
            val getters = element.getters()
            val properties = getters.map { createPropery(it as ExecutableElement, element) }
            val generate = getGenerateInfos(element)
            val classInfo = ClassInfo(TypeName.get(element.asType()), element.simpleName.toString(), properties, generate)
            knownClassesMap.put(classInfo.type, classInfo)
        }
    }

    private fun getGenerateInfos(element: Element): ArrayList<Generate> {
        val generate = ArrayList<Generate>();
        val fromListAnnotation = element.getAnnotation(GenerateList::class.java)?.value?.asList()
        val fromRepeatableAnnotation = element.getAnnotationsByType(Generate::class.java)?.asList()

        if (fromListAnnotation != null) {
            generate.addAll(fromListAnnotation)
        }

        if (fromRepeatableAnnotation != null) {
            generate.addAll(fromRepeatableAnnotation)
        }
        return generate
    }

    fun write(classInfo: ClassInfo, targets: Set<String>) {
        for (generate in classInfo.generate) {
            if (targets.contains(generate.target)) {
                val generatorClass = Class.forName(generate.generatorClass)
                val generator = generatorClass.newInstance() as SourceGenerator
                val javaFile = JavaFile.builder(generate.packageName, generator.generate(generate, classInfo, knownClassesMap)).build()
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

        val excludes = getter.getAnnotationsByType(Exclude::class.java).asList().map { it.target }

        val readOnly = classElement.getMethod("set${name.capitalize()}") == null

        return Property(name, TypeName.get(type), readOnly, excludes)
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