package su.kore.tools.repojo

import com.squareup.javapoet.TypeSpec
import su.kore.tools.repojo.meta.ClassInfo

/**
 * Created by adashkov on 09.06.2017.
 */

interface SourceGenerator {
    fun generate(generate: Generate, classInfo: ClassInfo): TypeSpec
}