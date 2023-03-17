package com.fphoenixcorneae.happyjoke.const

/**
 * @desc：常量
 * @date：2023/03/09 13:43
 */
object Constant {
    const val PROJECT_TOKEN = "E4185C527B23474E85C2478E181D7EFE"

    /**
     * @desc：导航路由
     * @date：2023/03/17 09:40
     */
    object NavRoute {
        const val SPLASH = "splash"
        const val MAIN = "main"

        /**
         * @desc：
         * @date：2023/03/17 10:45
         */
        object Main {
            const val HOMEPAGE = "main_homepage"
            const val SWEEP = "main_sweep"
            const val MESSAGE = "main_message"
            const val MINE = "main_mine"
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