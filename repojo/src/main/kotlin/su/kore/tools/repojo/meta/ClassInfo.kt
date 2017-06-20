package su.kore.tools.repojo.meta

import com.squareup.javapoet.TypeName
import su.kore.tools.repojo.Generate
import javax.lang.model.element.TypeElement

/**
 * Created by krad on 12.05.2017.
 */

data class ClassInfo(val type: TypeName, val simpleName : String, val properties: List<Property>, val generate: List<Generate>)