package twitter4j

import java.text.SimpleDateFormat
import java.util.*
import java.util.TimeZone
import kotlin.collections.ArrayList

/**
 * Returns Tweets composed by a single user, specified by the requested user ID.
 *
 * @throws TwitterException when Twitter service or network is unavailable
 * @see "https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets"
 */
@Throws(TwitterException::class)
fun Twitter.getUserTweets(
    userId: Long,
    endTime: Date? = null,
    exclude: String? = null,
    expansions: String? = null,
    maxResults: Int? = null,
    mediaFields: String? = null,
    paginationToken: String? = null,
    placeFields: String? = null,
    pollFields: String? = null,
    sinceId: Long? = null,
    startTime: Date? = null,
    tweetFields: String? = null,
    untilId: Long? = null,
    userFields: String? = null,
): TweetsResponse {

    if (this !is TwitterImpl) throw IllegalStateException("invalid twitter4j impl")

    ensureAuthorizationEnabled()

    val params = ArrayList<HttpParameter>()

    if (endTime != null) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        params.add(HttpParameter("end_time", sdf.format(endTime)))
    }

    if (exclude != null) {
        params.add(HttpParameter("exclude", exclude))
    }

    if (expansions != null) {
        params.add(HttpParameter("expansions", expansions))
    }

    if (maxResults != null) {
        params.add(HttpParameter("max_results", maxResults))
    }

    if (mediaFields != null) {
        params.add(HttpParameter("media.fields", mediaFields))
    }

    if (paginationToken != null) {
        params.add(HttpParameter("pagination_token", paginationToken))
    }

    if (placeFields != null) {
        params.add(HttpParameter("place.fields", placeFields))
    }

    if (pollFields != null) {
        params.add(HttpParameter("poll.fields", pollFields))
    }

    if (sinceId != null) {
        params.add(HttpParameter("since_id", sinceId))
    }

    if (startTime != null) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        params.add(HttpParameter("start_time", sdf.format(startTime)))
    }

    if (tweetFields != null) {
        params.add(HttpParameter("tweet.fields", tweetFields))
    }

    if (untilId != null) {
        params.add(HttpParameter("until_id", untilId))
    }

    if (userFields != null) {
        params.add(HttpParameter("user.fields", userFields))
    }

    return V2ResponseFactory().createTweetsResponse(
        http.get(
            conf.v2Configuration.baseURL + "users/" + userId + "/tweets",
            params.toTypedArray(),
            auth,
            this
        ),
        conf
    )
}

