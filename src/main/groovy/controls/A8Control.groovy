package controls

import com.offbytwo.jenkins.JenkinsServer
import com.offbytwo.jenkins.JenkinsTriggerHelper
import com.offbytwo.jenkins.model.Build
import com.offbytwo.jenkins.model.BuildResult
import com.offbytwo.jenkins.model.BuildWithDetails
import com.offbytwo.jenkins.model.JobWithDetails
import org.apache.log4j.Logger
import utils.LogUtils

import java.util.concurrent.TimeUnit

/**
 *
 * @author: assilzm
 * create: 2017-2-9 10:54.
 * description:
 */
class A8Control {

    private final static Logger logger = LogUtils.getLogger(A8Control.class)


    static JenkinsServer jenkinsBeiJing = new JenkinsServer(new URI("http://10.3.4.240:8080"), "jobconf", "maxc")

    static JenkinsServer jenkinsChengDu = new JenkinsServer(new URI("http://10.5.6.169:8080"), "jobconf", "maxc")


    final static String v5JobName = "v5-build-all"
    final static String h5JobName = "m3-build"

    final static String testEnvironmentJobName = "v5-docker-build-test-enviorment"


    static String buildV5() {
        return build(v5JobName, jenkinsBeiJing)
    }

    static String buildH5() {
        return build(h5JobName, jenkinsBeiJing)
    }

    static String buildTestEnvironment() {
        return build(testEnvironmentJobName, jenkinsChengDu)
    }

    static String build(String jobName, JenkinsServer jenkinsServer) {
        BuildWithDetails buildByNumber = new JenkinsTriggerHelper(jenkinsServer).triggerJobAndWaitUntilFinished(jobName)
        logger.debug("Build has finished.")
        logger.debug("Number: " + buildByNumber.getNumber())
        BuildWithDetails details = buildByNumber.details()
        logger.debug("BuiltOn: " + details.getBuiltOn())
        logger.debug("Duration: " + details.getDuration())
        logger.debug("Result: " + details.getResult())
        if (details.getResult() == BuildResult.SUCCESS)
            return "${jobName}构建成功，耗时${cost(details.getDuration())},url：${details.getUrl()}"
        else
            return "${jobName}构建失败，url：${details.getUrl()}"

    }


    static String cost(Long duration) {

        def minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        def seconds = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        if (minutes > 0)
            return String.format("%d分%d秒", minutes, seconds)
        else
            return String.format("%d秒", seconds)
    }
}
