package com.senijoshua.pods.data.local.util.dbcache

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DbCacheLimitImpl @Inject constructor(): DbCacheLimit {
    /**
     * The clear limit is the max amount of time beyond which
     * cached podcast data will be considered stale and deleted from the DB.
     */
    override val clearCacheLimit: Long =
        TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
}
