package su.kore.tools.repojo

import com.google.auto.service.AutoService
import com.google.gson.Gson
import javax.annotation.processing.*
import javax.lang.model.element.TypeElement

/**
 * Created by krad on 12.04.2017.
 */
@SupportedAnnotationTypes("su.kore.tools.repojo.MetaClass", "su.kore.tools.repojo.GeneratorTargets")
@AutoService(Processor::class)
class AnotationProcessor : AbstractProcessor() {
    lateinit var mainProcessor: MainProcessor
    val gson = Gson()

    override fun init(processingEnv: ProcessingEnvironment?) {
        if (processingEnv?.elementUtils != null && processingEnv.typeUtils != null && processingEnv.filer != null && processingEnv.messager != null) {
            Global.elementUtils = processingEnv.elementUtils
            Global.typeUtils = processingEnv.typeUtils
            Global.filer = processingEnv.filer
            Global.messager = processingEnv.messager
            mainProcessor = MainProcessor()
        }
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        val targets = getTargets(roundEnv!!)

        if (!targets.isEmpty() && annotations != null) {
            for (annotation in annotations) {
                if (annotation.qualifiedName.toString() == MetaClass::class.qualifiedName) {
                    val annotated = roundEnv.getElementsAnnotatedWith(annotation)
                    if (annotated != null) {
                        mainProcessor.process(annotated, targets)
                    }
                }
            }
        }

        return true
    }

    private fun getTargets(roundEnv: RoundEnvironment): Set<String> {
        val configEntities = roundEnv.getElementsAnnotatedWith(GeneratorTargets::class.java)
        return configEntities.flatMap { it.getAnnotation(GeneratorTargets::class.java).value.asList() }.toSet()
    }
}