package su.kore.tools.repojo

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * Created by krad on 12.05.2017.
 */


object Global {
    lateinit var elementUtils: Elements
    lateinit var typeUtils: Types
    lateinit var filer: Filer
    lateinit var messager: Messager
}
