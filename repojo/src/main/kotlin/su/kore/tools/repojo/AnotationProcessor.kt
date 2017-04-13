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
    var mainProcessor: MainProcessor? = null;

    override fun init(processingEnv: ProcessingEnvironment?) {
        if (processingEnv?.elementUtils != null && processingEnv.typeUtils != null && processingEnv.filer != null && processingEnv.messager != null) {
            mainProcessor = MainProcessor(elementUtils = processingEnv.elementUtils, filler = processingEnv.filer, messager = processingEnv.messager, typeUtils = processingEnv.typeUtils)
        }
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
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
}