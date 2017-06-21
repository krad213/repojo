package su.kore.tools.repojo.generators

import com.squareup.javapoet.*
import su.kore.tools.repojo.AbstractSourceGenerator
import su.kore.tools.repojo.Generate
import su.kore.tools.repojo.meta.ClassInfo
import su.kore.tools.repojo.meta.Property
import javax.lang.model.element.Modifier

/**
 * Created by adashkov on 09.06.2017.
 */

class PojoGenerator : AbstractSourceGenerator() {

    override fun generate(generate: Generate, classInfo: ClassInfo, knownClassesMap: HashMap<TypeName, ClassInfo>): TypeSpec {
        val classBuilder = TypeSpec.classBuilder(getGeneratedName(classInfo, generate)).addModifiers(Modifier.PUBLIC)
        for (property in classInfo.properties) {
            if (!property.excludes.contains(generate.target)) {
                val type = resolveTypeName(property.type, generate, knownClassesMap)
                classBuilder.addField(createField(property, type))
                classBuilder.addMethod(createGetter(property, type))
                classBuilder.addMethod(createSetter(property, type))
            }
        }
        return classBuilder.build()
    }

    private fun getGeneratedName(classInfo: ClassInfo, generate: Generate) = "${classInfo.simpleName}${generate.suffix}"

    private fun createField(property: Property, type: TypeName): FieldSpec {
        return FieldSpec.builder(type, property.name).addModifiers(Modifier.PRIVATE).build()
    }

    private fun createGetter(property: Property, typeName: TypeName): MethodSpec {
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

    private fun createSetter(property: Property, type: TypeName): MethodSpec {
        val name = "set${property.name.capitalize()}"
        val parameterSpec = ParameterSpec.builder(type, property.name).build()
        val methodBuilder = MethodSpec.methodBuilder(name)
                .addParameter(parameterSpec)
                .addModifiers(Modifier.PUBLIC)
                .addCode("this.${property.name} = ${property.name};\n")
        return methodBuilder.build()
    }
}