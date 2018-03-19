import org.yanning.gradle.vcs_lib.extension.App
import org.yanning.gradle.vcs_lib.extension.RepositoryGit
import java.io.File

fun main(args: Array<String>) {
    System.getProperties().setProperty(App.KEY_VCS_LIB_HOME, File(System.getProperty("user.home"), ".vcsLib").absolutePath)
    var repo= RepositoryGit()
    repo.username("admin")
    repo.password("admin")
//    repo.url="http://admin@127.0.0.1:8080/r/temp123.git"
    repo.url="http://admin@127.0.0.1:8080/r/lib_arcface.git"
    repo.update()
    repo.commit()
    repo.upload()
}