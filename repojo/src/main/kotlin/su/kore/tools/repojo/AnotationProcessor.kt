package su.kore.tools.repojo

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.element.TypeElement

/**
 * Created by krad on 12.04.2017.
 */
@SupportedAnnotationTypes("su.kore.tools.repojo.Pojo", "su.kore.tools.repojo.GeneratorConfiguration")
class AnotationProcessor : AbstractProcessor() {
    var mainProcessor: MainProcessor? = null;

    override fun init(processingEnv: ProcessingEnvironment?) {
        if (processingEnv?.elementUtils != null && processingEnv.typeUtils != null && processingEnv.filer != null && processingEnv.messager != null) {
            mainProcessor = MainProcessor(elementUtils = processingEnv.elementUtils, filler = processingEnv.filer, messager = processingEnv.messager, typeUtils = processingEnv.typeUtils)
        }
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        readConfig(roundEnv!!)

        if (annotations != null && mainProcessor != null) {
            for (annotation in annotations) {
                val annotated = roundEnv?.getElementsAnnotatedWith(annotation)
                if (annotated != null) {
                    (mainProcessor as MainProcessor).process(annotated)
                }
            }
        }

        return true
    }

    private fun readConfig(roundEnv: RoundEnvironment) {
        val configEntities = roundEnv.getElementsAnnotatedWith(GeneratorConfiguration::class.java)
        for (config in configEntities) {
            val value = config.getAnnotation(GeneratorConfiguration::class.java).value
            println(value)
        }
    }
}