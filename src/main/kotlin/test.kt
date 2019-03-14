import org.yanning.gradle.vcs_lib.core.AppConfig
import org.yanning.gradle.vcs_lib.core.Conf
import org.yanning.gradle.vcs_lib.core.ConfKey
import org.yanning.gradle.vcs_lib.extension.MavenBuild
import org.yanning.gradle.vcs_lib.utils.Log
import java.io.File
fun main(args: Array<String>) {
//    System.getProperties().setProperty(App.KEY_VCS_LIB_HOME, File(System.getProperty("user.home"), ".vcsLib").absolutePath)
//    var repo= RepositoryGit()
//    repo.username("admin")
//    repo.password("admin")
////    repo.url="http://admin@127.0.0.1:8080/r/temp123.git"
//    repo.url="http://admin@127.0.0.1:8080/r/lib_arcface.git"
//    repo.update()
//    repo.commit()
//    repo.upload()
//    RepositorySvn(VCSLibConf("file:/C:/Users/Administrator/.vcsLib/","svn://182.61.137.210:19091/Android/.vcsLibs","yanning","123456")).update()

//    Log.err(File(URI(File("C:\\Users").toURI().toString())).absolutePath)
    Log.err(MavenBuild::class.java.getResource("vcsLibUpload_aar.gradle").toString())
//    Conf(File(".vcsLib")).setConf(ConfKey.vcsLibHome,AppConfig.vcsLibHome.absolutePath).resetMavenConf().resetRepoConf().apply()
}

class Test{
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Log.err(javaClass.getResource("vcsLibUpload_aar.gradle").toString())
        }
    }
}