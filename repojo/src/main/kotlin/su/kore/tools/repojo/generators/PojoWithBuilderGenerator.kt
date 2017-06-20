package su.kore.tools.repojo.generators

import com.squareup.javapoet.*
import su.kore.tools.repojo.AbstractSourceGenerator
import su.kore.tools.repojo.Generate
import su.kore.tools.repojo.erasureEquals
import su.kore.tools.repojo.meta.ClassInfo
import su.kore.tools.repojo.meta.Property
import javax.lang.model.element.Modifier

/**
 * Created by adashkov on 09.06.2017.
 */

class PojoWithBuilderGenerator() : AbstractSourceGenerator() {
    override fun generate(generate: Generate, classInfo: ClassInfo, knownClassesMap: HashMap<TypeName, ClassInfo>): TypeSpec {
        val targetClassName = "${classInfo.simpleName}${generate.suffix}"
        val classBuilder = TypeSpec.classBuilder(targetClassName).addModifiers(Modifier.PUBLIC)
        val builderClassBuilder = TypeSpec.classBuilder("Builder").addModifiers(Modifier.PUBLIC)


        val privateConstructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE)
        val publicConstructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.bestGuess("Builder"), "builder")

        val builderBuildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess(targetClassName))
                .addCode("return new $targetClassName(this);\n")
        builderClassBuilder.addMethod(builderBuildMethod.build())

        val builderCopyMethodParam = ParameterSpec.builder(ClassName.bestGuess(targetClassName), "other").build()
        val builderCopyMethod = MethodSpec.methodBuilder("copy").addModifiers(Modifier.PUBLIC).addParameter(builderCopyMethodParam)


        val toStringMethod = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.bestGuess("java.lang.String"))
                .addAnnotation(Override::class.java)
                .addCode("\$T stringBuilder = new \$T();\n", ClassName.bestGuess("java.lang.StringBuilder"), ClassName.bestGuess("java.lang.StringBuilder"))
                .addCode("stringBuilder.append(\"$targetClassName\");\n")
                .addCode("stringBuilder.append(\"[\\n\");\n")


        for (property in classInfo.properties) {
            if (!property.excludes.contains(generate.target)) {
                val typeName = resolveTypeName(property.type, generate, knownClassesMap)

                privateConstructorBuilder.addCode(emptyValueOf(property, typeName))
                publicConstructorBuilder.addCode(copyFieldValue("builder", property))
                builderCopyMethod.addCode(copyFieldValue("other", property))

                classBuilder.addField(createField(typeName, property))
                classBuilder.addMethod(createGetter(typeName, property))

                builderClassBuilder.addMethod(createBuilderSetter(typeName, property))
                builderClassBuilder.addField(createBuilderField(typeName, property))

                toStringMethod.addCode("stringBuilder.append(\"${property.name}=\"+${property.name});\n")
                        .addCode("stringBuilder.append(\"\\n\");\n")
            }
        }
        toStringMethod.addCode("stringBuilder.append(\"]\");\n")
                .addCode("return stringBuilder.toString();\n")

        builderClassBuilder.addMethod(builderCopyMethod.build())
        classBuilder.addMethod(privateConstructorBuilder.build())
        classBuilder.addMethod(publicConstructorBuilder.build())
        classBuilder.addType(builderClassBuilder.build())
        classBuilder.addMethod(toStringMethod.build())
        return classBuilder.build()
    }

    private fun copyFieldValue(paramName: String, property: Property): CodeBlock {
        val codeBlock: CodeBlock
        when {
            property.type.erasureEquals(List::class.java) -> codeBlock = CodeBlock.of("${property.name} = new \$T<>($paramName.${property.name});\n", ClassName.bestGuess("java.util.ArrayList"))
            property.type.erasureEquals(Map::class.java) -> codeBlock = CodeBlock.of("${property.name} = new \$T<>($paramName.${property.name});\n", ClassName.bestGuess("java.util.HashMap"))
            property.type.erasureEquals(Set::class.java) -> codeBlock = CodeBlock.of("${property.name} = new \$T<>($paramName.${property.name});\n", ClassName.bestGuess("java.util.HashSet"))
            else -> codeBlock = CodeBlock.of("${property.name} = $paramName.${property.name};\n")
        }
        return codeBlock
    }

    private fun emptyValueOf(property: Property, type : TypeName): CodeBlock {
        val codeBlock: CodeBlock
        when {
            type.erasureEquals(List::class.java) -> codeBlock = CodeBlock.of("${property.name} = \$T.EMPTY_LIST;\n", ClassName.bestGuess("java.util.Collections"))
            type.erasureEquals(Map::class.java) -> codeBlock = CodeBlock.of("${property.name} = \$T.EMPTY_MAP;\n", ClassName.bestGuess("java.util.Collections"))
            type.erasureEquals(Set::class.java) -> codeBlock = CodeBlock.of("${property.name} = \$T.EMPTY_SET;\n", ClassName.bestGuess("java.util.Collections"))
            type.isPrimitive -> {
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
        val name = property.name
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
        val prefix: String
        if (typeName.isPrimitive && "boolean" == typeName.toString()) {
            prefix = "is"
        } else {
            prefix = "get"
        }

        val name = prefix + property.name.capitalize()

        val methodBuilder = MethodSpec.methodBuilder(name)
                .returns(typeName)
                .addModifiers(Modifier.PUBLIC)
                .addCode("return ${property.name};\n")

        return methodBuilder.build()
    }
}