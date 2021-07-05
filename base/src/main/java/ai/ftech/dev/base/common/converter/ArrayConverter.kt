package ai.ftech.dev.base.common.converter

import java.util.*

class ArrayConverter<S, D>(converter: IConverter<S, D>) :
    IConverter<Array<S>?, List<D>?> {
    private val mConverter: IConverter<S, D> = converter

    override fun convert(source: Array<S>?): List<D> {
        val result: MutableList<D> = ArrayList()
        if (source != null) {
            for (i in source.indices) {
                result.add(mConverter.convert(source[i]))
            }
        }
        return result
    }

}