package su.kore.tools.repojo.generators

import com.squareup.javapoet.*
import su.kore.tools.repojo.Generate
import su.kore.tools.repojo.SourceGenerator
import su.kore.tools.repojo.meta.ClassInfo
import su.kore.tools.repojo.meta.Property
import javax.lang.model.element.Modifier

/**
 * Created by adashkov on 09.06.2017.
 */

class PojoGenerator : SourceGenerator {
    override fun generate(generate: Generate, classInfo: ClassInfo): TypeSpec {
        val classBuilder = TypeSpec.classBuilder("${classInfo.type.simpleName}${generate.suffix}")
        for (property in classInfo.properties) {
            if (!property.excludes.contains(generate.target)) {
                val typeName = TypeName.get(property.type)
                classBuilder.addField(createField(typeName, property))
                classBuilder.addMethod(createGetter(typeName, property))
                classBuilder.addMethod(createSetter(typeName, property))
            }
        }
        return classBuilder.build()
    }

    private fun createField(typeName: TypeName, property: Property): FieldSpec {
        return FieldSpec.builder(typeName, property.name).addModifiers(Modifier.PRIVATE).build()
    }

    private fun createGetter(typeName: TypeName, property: Property): MethodSpec {
        val prefix : String
        if (typeName.isPrimitive && "boolean" == typeName.toString()) {
            prefix = "is"
        } else{
            prefix = "get"
        }

        val name = prefix+property.name.capitalize()

        val methodBuilder = MethodSpec.methodBuilder(name)
                .returns(typeName)
                .addModifiers(Modifier.PUBLIC)
                .addCode("return ${property.name};\n")

        return methodBuilder.build()
    }

    private fun createSetter(typeName: TypeName, property: Property): MethodSpec {
        val name = "set${property.name.capitalize()}"
        val parameterSpec = ParameterSpec.builder(typeName, property.name).build()
        val methodBuilder = MethodSpec.methodBuilder(name)
                .addParameter(parameterSpec)
                .addModifiers(Modifier.PUBLIC)
                .addCode("this.${property.name} = ${property.name};\n")
        return methodBuilder.build()
    }
}