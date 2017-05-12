package su.kore.tools.repojo

/**
 * Created by krad on 12.05.2017.
 */

enum class TargetType {POJO, BUILDER, POJO_WITH_BUILDER }

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Pojo

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Generate(val target: String, val targetType: TargetType, val packageName: String, val suffix: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Exclude(val target: String)