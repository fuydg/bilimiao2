package com.a10miaomiao.bilimiao

import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import cn.a10miaomiao.bilimiao.compose.ComposeFragment
import com.a10miaomiao.bilimiao.comm.navigation.ComposeFragmentNavigatorBuilder
import com.a10miaomiao.bilimiao.comm.navigation.FragmentNavigatorBuilder
//import com.a10miaomiao.bilimiao.page.TestFragment
import com.a10miaomiao.bilimiao.page.download.DownloadVideoCreateFragment
import com.a10miaomiao.bilimiao.page.setting.*
import com.a10miaomiao.bilimiao.template.TemplateFragment
import com.a10miaomiao.bilimiao.page.video.*
import com.a10miaomiao.bilimiao.page.video.comment.*
import kotlin.reflect.KClass


object MainNavGraph {
    // Counter for id's. First ID will be 1.
    private var id_counter = 100

    object dest {
        val main = id_counter++
        val template = id_counter++
        val compose = id_counter++
        val web = id_counter++
    }

    private val defaultNavOptionsBuilder: NavOptionsBuilder.() -> Unit = {
        anim {
            enter = R.anim.miao_fragment_open_enter
            exit = R.anim.miao_fragment_open_exit
            popEnter = R.anim.miao_fragment_close_enter
            popExit = R.anim.miao_fragment_close_exit
        }
    }

    fun createGraph(navController: NavController, startDestination: Int) {
        navController.graph = navController.createGraph(0, startDestination) {
            addFragment(TemplateFragment::class, TemplateFragment.Companion, dest.template)
            addFragment(ComposeFragment::class, ComposeFragmentNavigatorBuilder, dest.compose)
//            addFragment(TestFragment::class, TestFragment.Companion)

            addFragment(VideoInfoFragment::class, VideoInfoFragment.Companion)
            addFragment(VideoCoinFragment::class, VideoCoinFragment.Companion)
            addFragment(VideoAddFavoriteFragment::class, VideoAddFavoriteFragment.Companion)

            addFragment(VideoCommentListFragment::class, VideoCommentListFragment.Companion)
            addFragment(VideoCommentDetailFragment::class, VideoCommentDetailFragment.Companion)
            addFragment(ReplyDetailFragment::class, ReplyDetailFragment.Companion)
            addFragment(SendCommentFragment::class, SendCommentFragment.Companion)

            addFragment(DownloadVideoCreateFragment::class, DownloadVideoCreateFragment.Companion)

            addFragment(ThemeSettingFragment::class, ThemeSettingFragment.Companion)
        }
    }

    fun NavGraphBuilder.addFragment(
        kClass: KClass<out Fragment>,
        builder: FragmentNavigatorBuilder,
        _id: Int = id_counter++,
    ) {
        val id = if (builder.id == 0) _id else builder.id
        val actionId = if (builder.actionId == 0) id_counter++ else builder.actionId
        destination(
            FragmentNavigatorDestinationBuilder(
                provider[FragmentNavigator::class],
                id,
                kClass,
            ).apply {
                builder.run { build(id, actionId) }
            }
        )
        action(actionId) {
            destinationId = id
            navOptions(defaultNavOptionsBuilder)
        }
    }

}