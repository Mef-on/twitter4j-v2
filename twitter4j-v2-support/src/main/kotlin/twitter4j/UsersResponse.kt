package twitter4j

class UsersResponse : TwitterResponse {

    @Transient
    private var rateLimitStatus: RateLimitStatus? = null

    @Transient
    private var accessLevel = 0

    val users: List<User2> = mutableListOf()

    // includes.polls
    val pollsMap = HashMap<Long, Poll>()

    // includes.users
    val usersMap = HashMap<Long, User2>()

    // includes.places
    val placesMap = HashMap<String, Place2>()

    // includes.tweets
    val tweetsMap = HashMap<Long, Tweet>()

    // meta
    var meta: Meta? = null

    constructor(res: HttpResponse, isJSONStoreEnabled: Boolean) {
        rateLimitStatus = RateLimitStatusJSONImpl.createFromResponseHeader(res)
        accessLevel = ParseUtil.toAccessLevel(res)

        parse(res.asJSONObject(), isJSONStoreEnabled)
    }

    constructor(json: JSONObject, isJSONStoreEnabled: Boolean = false) {

        parse(json, isJSONStoreEnabled)
    }

    private fun parse(jsonObject: JSONObject, isJSONStoreEnabled: Boolean) {
        val users = users as MutableList
        users.clear()

        val includes = jsonObject.optJSONObject("includes")

        //--------------------------------------------------
        // create maps from includes
        //--------------------------------------------------
        V2Util.collectPolls(includes, pollsMap)
        V2Util.collectUsers(includes, usersMap)
        V2Util.collectPlaces(includes, placesMap)
        V2Util.collectTweets(includes, tweetsMap)

        // TODO includes.media ...

        //--------------------------------------------------
        // create users from data
        //--------------------------------------------------
        when (val data = jsonObject.get("data")) {
            is JSONArray -> {
                for (i in 0 until data.length()) {
                    val e = data.getJSONObject(i)

                    users.add(User2.parse(e))
                }
            }

            is JSONObject -> {
                // e.g. getMe()
                users.add(User2.parse(data))
            }
        }

        //--------------------------------------------------
        // meta
        //--------------------------------------------------
        meta = V2Util.parseMeta(jsonObject)

        if (isJSONStoreEnabled) {
            TwitterObjectFactory.registerJSONObject(this, jsonObject)
        }
    }

    override fun getRateLimitStatus(): RateLimitStatus? {
        return rateLimitStatus
    }

    override fun getAccessLevel(): Int {
        return accessLevel
    }

    override fun toString(): String {
        return "UsersResponse(rateLimitStatus=$rateLimitStatus, accessLevel=$accessLevel, users=$users, pollsMap=$pollsMap, usersMap=$usersMap, tweetsMap=$tweetsMap, meta=$meta)"
    }

}