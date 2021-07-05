package ai.ftech.dev.base.cache

class DefaultCacheFactory<T> : ICacheFactory<T> {

    override fun createCache(name: String?, maxSize: Int): ICache<String?, T> {
        return LruCacheImpl(maxSize)
    }
}