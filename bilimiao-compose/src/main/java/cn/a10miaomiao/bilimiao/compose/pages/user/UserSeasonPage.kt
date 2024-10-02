package cn.a10miaomiao.bilimiao.compose.pages.user

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import cn.a10miaomiao.bilimiao.compose.base.ComposePage
import cn.a10miaomiao.bilimiao.compose.base.stringPageArg
import cn.a10miaomiao.bilimiao.compose.common.diViewModel
import cn.a10miaomiao.bilimiao.compose.pages.user.content.UserSeasonDetailContent
import com.a10miaomiao.bilimiao.comm.store.UserStore
import com.a10miaomiao.bilimiao.store.WindowStore
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance


class UserSeasonPage : ComposePage() {

    val id = stringPageArg("id")

    val title = stringPageArg("title")

    override val route: String
        get() = "user/season/${id}?title=${title}"

    @Composable
    override fun AnimatedContentScope.Content(navEntry: NavBackStackEntry) {
        val viewModel: UserSeasonPageViewModel = diViewModel()
        val seasonId = navEntry.arguments?.get(id) ?: ""
        val seasonTitle = navEntry.arguments?.get(title) ?: ""
        UserSeasonPageContent(viewModel, seasonId, seasonTitle)
    }
}

private class UserSeasonPageViewModel(
    override val di: DI,
) : ViewModel(), DIAware {

    private val userStore by instance<UserStore>()

}


@Composable
private fun UserSeasonPageContent(
    viewModel: UserSeasonPageViewModel,
    seasonId: String,
    seasonTitle: String,
) {
    val windowStore: WindowStore by rememberDI { instance() }
    val windowState = windowStore.stateFlow.collectAsState().value
    val windowInsets = windowState.windowInsets

    UserSeasonDetailContent(
        seasonId = seasonId,
        seasonTitle = seasonTitle,
        showTowPane = false,
        hideFirstPane = false,
        onChangeHideFirstPane = {}
    )
}