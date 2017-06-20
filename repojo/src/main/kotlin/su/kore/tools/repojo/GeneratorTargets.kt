package su.kore.tools.repojo

/**
 * Created by adashkov on 13.04.2017.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GeneratorTargets(val value: Array<String>)