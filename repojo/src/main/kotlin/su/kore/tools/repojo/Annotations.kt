package su.kore.tools.repojo

import kotlin.reflect.KClass

/**
 * Created by krad on 12.05.2017.
 */

enum class TargetType {POJO, BUILDER, POJO_WITH_BUILDER }

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MetaClass

@Repeatable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Generate(val target: String, val generatorClass: KClass<out Any>, val packageName: String, val suffix: String)

annotation class GenerateList(val value:Array<Generate>)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Exclude(val value: Array<String>)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GeneratorTargets(val value: Array<String>)