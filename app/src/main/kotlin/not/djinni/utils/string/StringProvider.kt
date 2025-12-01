package not.djinni.utils.string

interface StringProvider {

    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any): String
}