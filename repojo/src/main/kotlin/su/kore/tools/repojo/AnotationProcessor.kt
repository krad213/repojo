package su.kore.tools.repojo

import com.google.auto.service.AutoService
import javax.annotation.processing.*
import javax.lang.model.element.TypeElement

/**
 * Created by krad on 12.04.2017.
 */
@SupportedAnnotationTypes("su.kore.tools.repojo.Pojo")
@AutoService(Processor::class)
class AnotationProcessor : AbstractProcessor() {
    lateinit var mainProcessor: MainProcessor

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
        if (annotations != null) {
            for (annotation in annotations) {
                val annotated = roundEnv?.getElementsAnnotatedWith(annotation)
                if (annotated != null) {
                    (mainProcessor as MainProcessor).process(annotated)
                }
            }
        }

        return true
    }
}