package cn.a10miaomiao.bilimiao.compose.pages.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bilibili.app.view.v1.ViewGRPC
import bilibili.app.view.v1.ViewPage
import bilibili.app.view.v1.ViewReply
import bilibili.app.view.v1.ViewReq
import cn.a10miaomiao.bilimiao.compose.common.navigation.PageNavigation
import com.a10miaomiao.bilimiao.comm.delegate.player.BasePlayerDelegate
import com.a10miaomiao.bilimiao.comm.delegate.player.VideoPlayerSource
import com.a10miaomiao.bilimiao.comm.entity.player.PlayListFrom
import com.a10miaomiao.bilimiao.comm.network.BiliGRPCHttp
import com.a10miaomiao.bilimiao.comm.store.FilterStore
import com.a10miaomiao.bilimiao.comm.store.PlayListStore
import com.a10miaomiao.bilimiao.comm.store.PlayerStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class VideoDetailViewModel(
    override val di: DI,
    val id: String,
) : ViewModel(), DIAware {

    private val pageNavigation by instance<PageNavigation>()
    private val basePlayerDelegate by instance<BasePlayerDelegate>()

    private val filterStore: FilterStore by instance()
    private val playerStore: PlayerStore by instance()
    private val playListStore: PlayListStore by instance()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading
    private val _fail = MutableStateFlow<Any?>(null)
    val fail: StateFlow<Any?> get() = _fail
    private val _detailData = MutableStateFlow<ViewReply?>(null)
    val detailData: StateFlow<ViewReply?> get() = _detailData

    init {
        loadData()
    }

    fun loadData() = viewModelScope.launch {
        try {
            _loading.value = true
            _fail.value = null
            val req = if (id.startsWith("BV")) {
                ViewReq(
                    bvid = id,
                )
            } else {
                ViewReq(
                    aid = id.toLong(),
                )
            }
            val res = BiliGRPCHttp.request {
                ViewGRPC.view(req)
            }.awaitCall()
            _detailData.value = res
        } catch (e: Exception) {
            e.printStackTrace()
            _fail.value = e
        } finally {
            _isRefreshing.value = false
            _loading.value = false
        }
    }

    fun playVideo() {
        val detail = detailData.value ?: return
        val pages = detail.pages
        if (pages.isNotEmpty()) {
            playVideo(pages[0])
        }
    }
    fun playVideo(viewPage: ViewPage) {
        val detail = detailData.value ?: return
        val arc = detail.arc ?: return
        val author = arc.author ?: return
        val page = viewPage.page ?: return
        val viewPages = detail.pages
        val ugcSeason = detail.ugcSeason
        val title = if (viewPages.size > 1) {
            page.part
        } else {
            arc.title
        }
        val cid = page.cid
        val isAutoPlaySeason = false
        if (isAutoPlaySeason && ugcSeason != null) {
            // 将合集加入播放列表
            val playListFromId = (playListStore.state.from as? PlayListFrom.Season)?.seasonId
                ?: (playListStore.state.from as? PlayListFrom.Section)?.seasonId
            if (playListFromId != ugcSeason.id.toString() ||
                !playListStore.state.inListForAid(arc.aid.toString())) {
                // 当前播放列表来源不是当前合集或视频不在播放列表中时，创建新播放列表
                // 以合集创建播放列表
                val index = if (ugcSeason.sections.size > 1) {
                    ugcSeason.sections.indexOfFirst { section ->
                        section.episodes.indexOfFirst { it.aid == arc.aid } != -1
                    }
                } else { 0 }
                playListStore.setPlayList(ugcSeason, index)
            }
        } else if (!playListStore.state.inListForAid(arc.aid.toString())) {
            // 当前视频不在播放列表中时，如果未正在播放或播放列表为空则创建新的播放列表，否则将视频加入列表尾部
            if (playListStore.state.items.isEmpty()
                || playerStore.state.aid.isEmpty()) {
                // 以当前视频创建新的播放列表
                val playListItem = playListStore.run {
                    arc.toPlayListItem(viewPages)
                }
                playListStore.setPlayList(
                    name = arc.title,
                    from = playListItem.from,
                    items = listOf(
                        playListItem,
                    )
                )
            } else {
                // 将视频添加到播放列表末尾
                playListStore.addItem(playListStore.run {
                    arc.toPlayListItem(viewPages)
                })
            }
        }

        // 播放视频
        basePlayerDelegate.openPlayer(
            VideoPlayerSource(
                mainTitle = arc.title,
                title = title,
                coverUrl = arc.pic,
                aid = arc.aid.toString(),
                id = cid.toString(),
                ownerId = author.mid.toString(),
                ownerName = author.name,
            ).apply {
                pages = viewPages
                    .mapNotNull {
                        it.page
                    }.map {
                        VideoPlayerSource.PageInfo(
                            cid = it.cid.toString(),
                            title = it.part,
                        )
                    }
            }
        )
    }
}