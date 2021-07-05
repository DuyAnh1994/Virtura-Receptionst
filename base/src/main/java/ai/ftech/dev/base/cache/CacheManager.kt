package ai.ftech.dev.base.cache

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.util.LruCache

class CacheManager : ComponentCallbacks2 {
    private val mCacheStore: LruCache<String, ICache<String, *>?> = LruCache<String, ICache<String, *>?>(
        Int.MAX_VALUE
    )

    override fun onTrimMemory(level: Int) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            val snapshot: Map<String, ICache<String, *>?> = mCacheStore.snapshot()
            for (id in snapshot.keys) {
                val cache: ICache<String, *>? = mCacheStore[id]
                cache?.removeAll()
            }
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            val snapshot: Map<String, ICache<String, *>?> = mCacheStore.snapshot()
            for (id in snapshot.keys) {
                val cache: ICache<String, *>? = mCacheStore[id]
                cache?.trimToSize(cache.size / 2)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {}
    override fun onLowMemory() {}
    fun hasCache(cacheName: String): Boolean {
        return mCacheStore[cacheName] != null
    }

    fun <T> createCache(
        cacheName: String,
        maxSize: Int,
        factory: ICacheFactory<T>
    ): ICache<String, T>? {
        if (hasCache(cacheName)) {
            return mCacheStore[cacheName] as ICache<String, T>?
        }
        val cache: ICache<String, T> = factory.createCache(cacheName, maxSize) as ICache<String, T>
        mCacheStore.put(cacheName, cache)
        return cache
    }

    fun getCache(cacheName: String): ICache<String, *>? {
        return mCacheStore[cacheName]
    }

    companion object {
        private var sInstance: CacheManager? = null
        val instance: CacheManager?
            get() {
                if (sInstance == null) {
                    synchronized(CacheManager::class.java) {
                        if (sInstance == null) {
                            sInstance = CacheManager()
                        }
                    }
                }
                return sInstance
            }
    }
}