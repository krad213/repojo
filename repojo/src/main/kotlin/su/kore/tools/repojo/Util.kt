package su.kore.tools.repojo

import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

/**
 * Created by adashkov on 09.06.2017.
 */

fun TypeMirror.isAssignable(other : Class<out Any>) : Boolean {
    val typeMirror = Global.elementUtils.getTypeElement(other.typeName).asType()
    return Global.typeUtils.isAssignable(this, typeMirror)
}

fun TypeMirror.erasureEquals(other : Class<out Any>) : Boolean {
    val typeMirror = Global.elementUtils.getTypeElement(other.typeName).asType()
    val thisErasure = Global.typeUtils.erasure(this)
    val otherErasure = Global.typeUtils.erasure(typeMirror);

    return Global.typeUtils.isSameType(thisErasure, otherErasure)
}

fun TypeMirror.isSameType(other : Class<out Any>) : Boolean {
    val typeMirror = Global.elementUtils.getTypeElement(other.typeName).asType()
    return Global.typeUtils.isSameType(this, typeMirror)
}