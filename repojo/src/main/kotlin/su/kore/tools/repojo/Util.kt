package su.kore.tools.repojo

import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName

/**
 * Created by adashkov on 09.06.2017.
 */


fun TypeName.erasureEquals(other : Class<out Any>) : Boolean {
    if (this is ParameterizedTypeName) {
        return other.canonicalName.toString() == this.rawType.toString()
    } else {
        return other.canonicalName.toString() == this.toString()
    }
}
