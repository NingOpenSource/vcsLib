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
//                testUpload(confs[confs.keys.toList()[0]]!!)
                confs.forEach { (_, u) ->
                    testUpload(u)
                }
            }
        }

        fun testUpload(conf: RepoConfig) {
            RepoHelper(conf).connectRepo()?.also {repo->
                thread(start = true) {
                    for (index in 0..10){
                        thread(start = true){
                            repo.upload()
                        }
                    }
                }
            }
        }

        fun testUpdate(conf: RepoConfig) {
            RepoHelper(conf).connectRepo()?.also {repo->
                thread(start = true) {
                    for (index in 0..10){
                        thread(start = true){
                            repo.updateWithLimit()
                        }
                    }
                }
            }
        }
    }
}