package su.kore.tools.repojo.meta

import com.squareup.javapoet.TypeName

/**
 * Created by krad on 12.05.2017.
 */

data class Property(val name: String, val type: TypeName, val readOnly: Boolean, val excludes: List<String>)