package com.fphoenixcorneae.happyjoke.const

/**
 * @desc：常量
 * @date：2023/03/09 13:43
 */
object Constant {
    const val HOST_URL = "http://tools.cretinzp.com/"
    const val PROJECT_TOKEN = "E4185C527B23474E85C2478E181D7EFE"
    const val WEB_PRIVACY = "http://jokes-avatar.cretinzp.com/static/web/jokes-privacy.html"
    const val WEB_PROTO = "http://jokes-avatar.cretinzp.com/static/web/jokes-protocol.html"
    const val LEDOU_INTRO = "http://mxnzp-static.mxnzp.com/jokes/html/ledou_intro.html"
    const val SOCIAL_NOTICE = "http://mxnzp-static.mxnzp.com/jokes/html/social_notice.html"
    const val USER_RANK = "http://mxnzp-static.mxnzp.com/jokes/html/user_rank.html"
    const val PAGE_LIMIT = 10

    /**
     * @desc：Key
     * @date：2023/05/04 16:37
     */
    object Key {
        const val TARGET_USER_ID = "target_user_id"
        const val TARGET_JOKE_ID = "target_joke_id"
        const val WEB_URL = "web_url"
        const val JOKE_AUDIT_STATUS = "joke_audit_status"
    }

    /**
     * @desc：用户
     * @date：2023/04/24 15:32
     */
    object User {
        const val IS_LOGGED_IN = "user_is_logged_in"
        const val TOKEN = "user_token"
        const val USER_INFO = "user_info"
    }

    /**
     * @desc：导航路由
     * @date：2023/03/17 09:40
     */
    object NavRoute {
        const val SPLASH = "splash"
        const val MAIN = "main"
        const val JOKE_POST = "joke_post"
        const val LOGIN = "login"
        const val PASSWORD_RESET = "password_reset"
        const val SETTINGS = "settings"
        const val USER_DETAILS = "user_details"
        const val JOKE_DETAILS = "joke_details"
        const val JOKE_AUDIT = "joke_audit"
        const val WEB = "web"

        /**
         * @desc：主页
         * @date：2023/03/17 10:45
         */
        object Main {
            const val HOMEPAGE = "main_homepage"
            const val SWEEP = "main_sweep"
            const val MESSAGE = "main_message"
            const val ME = "main_me"
        }
    }

    /**
     * @desc：偏好
     * @date：2023/03/10 14:03
     */
    object Preferences {
        // 同意隐私政策
        const val AGREE_PRIVACY_POLICY = "agree_privacy_policy"
    }
}