package repo

import kotlinx.browser.window

interface UserRepo {
    fun isAwareShareIsPublic() : Boolean
    fun setAwareShareIsPublic(isAware : Boolean)
}

class UserRepoImpl : UserRepo {
    companion object{
        private const val KEY_IS_AWARE_SHARE_IS_PUBLIC = "is_aware_share_is_public"
    }
    override fun isAwareShareIsPublic(): Boolean {
        return window.localStorage.getItem(KEY_IS_AWARE_SHARE_IS_PUBLIC)?.toBoolean() ?: false
    }

    override fun setAwareShareIsPublic(isAware: Boolean) {
        window.localStorage.setItem(KEY_IS_AWARE_SHARE_IS_PUBLIC, isAware.toString())
    }

}