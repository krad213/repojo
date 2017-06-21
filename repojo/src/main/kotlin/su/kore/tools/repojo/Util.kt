package su.kore.tools.repojo

import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import javax.lang.model.type.MirroredTypesException

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

//Some magic is required to get Class variable value in Javac context
fun Generate.getGeneratorClassVal(): Class<out Any> {
    try {
        this.generatorClass
    } catch (ex: MirroredTypesException) {
        return Class.forName(ex.typeMirrors.iterator().next().toString())
    }
    throw UnknownError("This should not happen")
}