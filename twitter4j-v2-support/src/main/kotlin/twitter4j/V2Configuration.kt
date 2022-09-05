package twitter4j

import twitter4j.conf.Configuration
import java.util.concurrent.ConcurrentHashMap

data class V2Configuration(val baseURL: String)

internal object V2ConfigurationContainer {
    val v2ConfigurationMap = ConcurrentHashMap<Configuration, V2Configuration>()
}

val Configuration.v2Configuration: V2Configuration
    get() {
        return if (V2ConfigurationContainer.v2ConfigurationMap.containsKey(this)) {
            V2ConfigurationContainer.v2ConfigurationMap[this]!!
        } else {
            val url =
                if (restBaseURL == "https://api.twittertwitter.com/1.1/") "https://api.twitter.com/2/"
                else restBaseURL

            V2ConfigurationContainer.v2ConfigurationMap.putIfAbsent(this, V2Configuration(url))
            v2Configuration
        }
    }
