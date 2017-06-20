package su.kore.tools.repojo

/**
 * Created by adashkov on 13.04.2017.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GeneratorConfiguration(val value:String)


data class GenConfig (val targets:List<String>)