package su.kore.tools.repojo

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import su.kore.tools.repojo.meta.ClassInfo

/**
 * Created by adashkov on 09.06.2017.
 */

interface SourceGenerator {
    fun generate(generate: Generate, classInfo: ClassInfo, knownClassesMap: HashMap<TypeName, ClassInfo>): TypeSpec
}

abstract class AbstractSourceGenerator : SourceGenerator {
    protected fun resolveTypeName(typeName: TypeName, generate: Generate, typeMap: HashMap<TypeName, ClassInfo>): TypeName {
        if (typeName is ParameterizedTypeName) {
            return resolveParamitrizedTypeName(typeName, generate, typeMap)
        }

        val ref = typeMap[typeName]
        if (ref == null) {
            return typeName
        } else {
            return ClassName.bestGuess("${generate.packageName}.${ref.simpleName}${generate.suffix}")
        }
    }

    private fun resolveParamitrizedTypeName(typeName: ParameterizedTypeName, generate: Generate, typeMap: HashMap<TypeName, ClassInfo>): TypeName {
        val newArguments = ArrayList<TypeName>()
        typeName.typeArguments.forEach {
            newArguments.add(resolveTypeName(it, generate, typeMap))
        }

        val className = resolveTypeName(typeName.rawType, generate, typeMap) as ClassName

        return ParameterizedTypeName.get(className, *newArguments.toTypedArray())
    }
}