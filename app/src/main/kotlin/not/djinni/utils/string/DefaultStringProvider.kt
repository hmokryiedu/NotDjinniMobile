package not.djinni.utils.string

import android.content.Context
import org.koin.core.annotation.Single

@Single(binds = [StringProvider::class])
internal class DefaultStringProvider(private val context: Context) : StringProvider {

    override fun getString(resId: Int): String {
        return context.resources.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.resources.getString(resId, *formatArgs)
    }
}