package su.kore.tools.repojo.meta

import javax.lang.model.type.TypeMirror

/**
 * Created by krad on 12.05.2017.
 */

data class Property(val name: String, val type: TypeMirror, val readOnly: Boolean, val excludes: List<String>)
