package su.kore.tools.repojo.generators

import com.squareup.javapoet.*
import su.kore.tools.repojo.Generate
import su.kore.tools.repojo.SourceGenerator
import su.kore.tools.repojo.erasureEquals
import su.kore.tools.repojo.meta.ClassInfo
import su.kore.tools.repojo.meta.Property
import javax.lang.model.element.Modifier

/**
 * Created by adashkov on 09.06.2017.
 */

class PojoWithBuilderGenerator : SourceGenerator {
    override fun generate(generate: Generate, classInfo: ClassInfo): TypeSpec {
        val classBuilder = TypeSpec.classBuilder("${classInfo.type.simpleName}${generate.suffix}")
        val builderClassBuilder = TypeSpec.classBuilder("Builder").addModifiers(Modifier.PUBLIC)


        val privateConstructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE)
        val publicConstructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess("Builder"),"builder")

        for (property in classInfo.properties) {
            if (!property.excludes.contains(generate.target)) {
                val typeName = TypeName.get(property.type)

                privateConstructorBuilder.addCode(emptyValueOf(property))
                publicConstructorBuilder.addCode(initFromBuilder(property))

                classBuilder.addField(createField(typeName, property))
                classBuilder.addMethod(createGetter(typeName, property))

                builderClassBuilder.addMethod(createBuilderSetter(typeName, property))
                builderClassBuilder.addField(createBuilderField(typeName, property))
            }
        }
        classBuilder.addMethod(privateConstructorBuilder.build())
        classBuilder.addMethod(publicConstructorBuilder.build())
        classBuilder.addType(builderClassBuilder.build())
        return classBuilder.build()
    }

    private fun initFromBuilder(property: Property): CodeBlock {
        return CodeBlock.of("${property.name} = builder.${property.name};\n")
    }

    private fun emptyValueOf(property: Property): CodeBlock {
        val type = property.type
        val codeBlock : CodeBlock
        when {
            type.erasureEquals(List::class.java) -> codeBlock = CodeBlock.of("${property.name} = \$T.EMPTY_LIST;\n", ClassName.bestGuess("java.util.Collections"))
            type.erasureEquals(Map::class.java) -> codeBlock = CodeBlock.of("${property.name} = \$T.EMPTY_MAP;\n", ClassName.bestGuess("java.util.Collections"))
            type.erasureEquals(Set::class.java) -> codeBlock = CodeBlock.of("${property.name} = \$T.EMPTY_SET;\n", ClassName.bestGuess("java.util.Collections"))
            type.kind.isPrimitive -> {
                when {
                    type.toString() == "boolean" -> codeBlock = CodeBlock.of("${property.name} = false;\n")
                    type.toString() == "long" -> codeBlock = CodeBlock.of("${property.name} = 0L;\n")
                    type.toString() == "float" -> codeBlock = CodeBlock.of("${property.name} = 0F;\n")
                    type.toString() == "double" -> codeBlock = CodeBlock.of("${property.name} = 0D;\n")
                    else -> codeBlock = CodeBlock.of("${property.name} = 0;\n")
                }
            }
            else -> codeBlock = CodeBlock.of("${property.name} = null;\n")
        }
        return codeBlock
    }

    private fun createBuilderSetter(typeName: TypeName, property: Property): MethodSpec {
        val name = property.name.capitalize()
        val parameterSpec = ParameterSpec.builder(typeName, property.name).build()
        val methodBuilder = MethodSpec.methodBuilder(name)
                .returns(ClassName.bestGuess("Builder"))
                .addParameter(parameterSpec)
                .addModifiers(Modifier.PUBLIC)
                .addCode("this.${property.name} = ${property.name};\n" +
                        "return this;" +
                        "\n")
        return methodBuilder.build()
    }

    private fun createField(typeName: TypeName, property: Property): FieldSpec {
        return FieldSpec.builder(typeName, property.name).addModifiers(Modifier.PRIVATE, Modifier.FINAL).build()
    }

    private fun createBuilderField(typeName: TypeName, property: Property): FieldSpec {
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
}