import org.gradle.internal.impldep.com.google.gson.GsonBuilder
import org.yanning.gradle.vcs_lib.core.RepoConfig
import org.yanning.gradle.vcs_lib.extension.RepoHelper
import java.io.File
import kotlin.concurrent.thread

class Test {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            RepoConfig.loadConfs(File("temp.conf.json")).also { confs ->
                println(GsonBuilder().setPrettyPrinting().create().toJson(confs))
                confs.forEach { (_, u) ->
                    testSVN_1(u)
                }
            }
        }

        fun testGIT(conf: RepoConfig) {

        }

        fun testSVN_0(conf: RepoConfig) {
            RepoHelper(conf).connectRepo()?.also {svn->
                thread(start = true) {
                    for (index in 0..10){
                        thread(start = true){
                            svn.update()
                        }
                    }
                }
            }
        }

        fun testSVN_1(conf: RepoConfig) {
            RepoHelper(conf).connectRepo()?.also {svn->
                svn.update()
//                svn.upload()
            }
        }
    }
}