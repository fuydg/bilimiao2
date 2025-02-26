package com.a10miaomiao.bilimiao.page.video

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.a10miaomiao.bilimiao.compose.BilimiaoPageRoute
import cn.a10miaomiao.bilimiao.compose.pages.playlist.PlayListPage
import cn.a10miaomiao.bilimiao.compose.pages.user.UserSeasonDetailPage
import cn.a10miaomiao.bilimiao.compose.pages.user.UserSpacePage
import cn.a10miaomiao.bilimiao.cover.CoverActivity
import cn.a10miaomiao.miao.binding.android.view._isEnabled
import cn.a10miaomiao.miao.binding.android.view._leftPadding
import cn.a10miaomiao.miao.binding.android.view._rightPadding
import cn.a10miaomiao.miao.binding.android.view._show
import cn.a10miaomiao.miao.binding.android.view._topPadding
import cn.a10miaomiao.miao.binding.android.widget._isChecked
import cn.a10miaomiao.miao.binding.android.widget._text
import cn.a10miaomiao.miao.binding.android.widget._textColor
import cn.a10miaomiao.miao.binding.android.widget._textColorResource
import cn.a10miaomiao.miao.binding.miaoEffect
import cn.a10miaomiao.miao.binding.miaoMemo
import com.a10miaomiao.bilimiao.MainActivity
import com.a10miaomiao.bilimiao.R
import com.a10miaomiao.bilimiao.comm.BiliNavigation
import com.a10miaomiao.bilimiao.comm.MiaoUI
import com.a10miaomiao.bilimiao.comm._isRefreshing
import com.a10miaomiao.bilimiao.comm._network
import com.a10miaomiao.bilimiao.comm.connectStore
import com.a10miaomiao.bilimiao.comm.datastore.SettingConstants
import com.a10miaomiao.bilimiao.comm.datastore.SettingPreferences
import com.a10miaomiao.bilimiao.comm.delegate.player.BasePlayerDelegate
import com.a10miaomiao.bilimiao.comm.diViewModel
import com.a10miaomiao.bilimiao.comm.entity.player.PlayListFrom
import com.a10miaomiao.bilimiao.comm.entity.video.UgcEpisodeInfo
import com.a10miaomiao.bilimiao.comm.entity.video.UgcSectionInfo
import com.a10miaomiao.bilimiao.comm.entity.video.VideoPageInfo
import com.a10miaomiao.bilimiao.comm.entity.video.VideoRelateInfo
import com.a10miaomiao.bilimiao.comm.entity.video.VideoStaffInfo
import com.a10miaomiao.bilimiao.comm.entity.video.VideoTagInfo
import com.a10miaomiao.bilimiao.comm.lazyUiDi
import com.a10miaomiao.bilimiao.comm.miaoBindingUi
import com.a10miaomiao.bilimiao.comm.mypage.MenuItemPropInfo
import com.a10miaomiao.bilimiao.comm.mypage.MenuKeys
import com.a10miaomiao.bilimiao.comm.mypage.MyPage
import com.a10miaomiao.bilimiao.comm.mypage.myMenuItem
import com.a10miaomiao.bilimiao.comm.mypage.myPageConfig
import com.a10miaomiao.bilimiao.comm.navigation.FragmentNavigatorBuilder
import com.a10miaomiao.bilimiao.comm.navigation.MainNavArgs
import com.a10miaomiao.bilimiao.comm.navigation.navigateToCompose
import com.a10miaomiao.bilimiao.comm.recycler.GridAutofitLayoutManager
import com.a10miaomiao.bilimiao.comm.recycler._miaoAdapter
import com.a10miaomiao.bilimiao.comm.recycler._miaoLayoutManage
import com.a10miaomiao.bilimiao.comm.recycler._miaoMultiAdapter
import com.a10miaomiao.bilimiao.comm.recycler.footerViews
import com.a10miaomiao.bilimiao.comm.recycler.headerViews
import com.a10miaomiao.bilimiao.comm.recycler.lParams
import com.a10miaomiao.bilimiao.comm.recycler.miaoBindingItemUi
import com.a10miaomiao.bilimiao.comm.store.PlayListStore
import com.a10miaomiao.bilimiao.comm.store.PlayerStore
import com.a10miaomiao.bilimiao.comm.store.UserStore
import com.a10miaomiao.bilimiao.comm.utils.BiliUrlMatcher
import com.a10miaomiao.bilimiao.comm.utils.NumberUtil
import com.a10miaomiao.bilimiao.comm.views
import com.a10miaomiao.bilimiao.comm.wrapInSwipeRefreshLayout
import com.a10miaomiao.bilimiao.commponents.video.videoItem
import com.a10miaomiao.bilimiao.config.ViewStyle
import com.a10miaomiao.bilimiao.config.config
import com.a10miaomiao.bilimiao.page.video.comment.VideoCommentListFragment
import com.a10miaomiao.bilimiao.store.WindowStore
import com.a10miaomiao.bilimiao.widget._setContent
import com.a10miaomiao.bilimiao.widget.expandableTextView
import com.a10miaomiao.bilimiao.widget.expandabletext.ExpandableTextView
import com.a10miaomiao.bilimiao.widget.expandabletext.app.LinkType
import com.a10miaomiao.bilimiao.widget.rcImageView
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.materialswitch.MaterialSwitch
import com.kongzue.dialogx.dialogs.PopTip
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import splitties.dimensions.dip
import splitties.experimental.InternalSplittiesApi
import splitties.views.backgroundColor
import splitties.views.bottomPadding
import splitties.views.dsl.core.frameLayout
import splitties.views.dsl.core.horizontalLayout
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.lParams
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.verticalLayout
import splitties.views.dsl.core.view
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.recyclerview.recyclerView
import splitties.views.horizontalPadding
import splitties.views.imageResource
import splitties.views.leftPadding
import splitties.views.padding
import splitties.views.rightPadding
import splitties.views.topPadding
import splitties.views.verticalPadding


class VideoInfoFragment : Fragment(), DIAware, MyPage {

    companion object : FragmentNavigatorBuilder() {
        const val TYPE_AV = "AV"
        const val TYPE_BV = "BV"
        override val name = "main"
        override fun FragmentNavigatorDestinationBuilder.init() {
            deepLink("bilimiao://video/{id}")
            deepLink("bilibili://video/{id}")
            argument(MainNavArgs.type) {
                type = NavType.StringType
                defaultValue = TYPE_AV
            }
            argument(MainNavArgs.id) {
                type = NavType.StringType
                nullable = false
            }
        }

        fun createArguments(
            id: String
        ): Bundle {
            val type = if (id.indexOf("BV") == 0) {
                TYPE_BV
            } else {
                TYPE_AV
            }
            return bundleOf(
                MainNavArgs.type to type,
                MainNavArgs.id to id,
            )
        }
    }

    override val di: DI by lazyUiDi(ui = { ui })

    private val viewModel by diViewModel<VideoInfoViewModel>(di)

    private val windowStore by instance<WindowStore>()

    private val userStore by instance<UserStore>()

    private val playerStore by instance<PlayerStore>()

    private val playListStore by instance<PlayListStore>()

    private val basePlayerDelegate by instance<BasePlayerDelegate>()

    override val pageConfig = myPageConfig {
        val info = viewModel.info
        title = info?.let {
            "${it.bvid} /\nAV${it.aid}"
        } ?: "视频详情"
        menus = listOf(
            myMenuItem {
                key = MenuKeys.more
                iconResource = R.drawable.ic_more_vert_grey_24dp
                title = "更多"
            },
            myMenuItem {
                key = 1
                iconResource = R.drawable.ic_column_comm
                val replyNum = NumberUtil.converStringOrNull(info?.stat?.reply)
                title = replyNum ?: "评论"
                contentDescription = if (replyNum == null) "评论" else "评论(数量:${replyNum}条)"
            },
//            myMenuItem {
//                key = 2
//                iconResource = R.drawable.ic_column_share
//                title = NumberUtil.converString(info?.stat?.share?.toString() ?: "分享")
//            },
            myMenuItem {
                key = 3
                iconResource = if (info?.req_user?.favorite == null) {
                    R.drawable.ic_column_unstar
                } else {
                    R.drawable.ic_column_star
                }
                val favoriteNum = NumberUtil.converStringOrNull(info?.stat?.favorite)
                title = favoriteNum ?: "收藏"
                contentDescription = if (favoriteNum == null) "收藏" else "收藏(数量:${favoriteNum}个)"
            },
            myMenuItem {
                key = 4
                iconResource = if (info?.req_user?.coin == null) {
                    R.drawable.ic_column_uncoin
                } else {
                    R.drawable.ic_column_coin
                }
                val coinNum = NumberUtil.converStringOrNull(info?.stat?.coin)
                title = coinNum ?: "投币"
                contentDescription = if (coinNum == null) "投币" else "投币(数量:${coinNum}枚)"
            },
            myMenuItem {
                key = 5
                iconResource = if (info?.req_user?.like == null) {
                    R.drawable.ic_column_unlike
                } else {
                    R.drawable.ic_column_like
                }
                val likeNum = NumberUtil.converStringOrNull(info?.stat?.like)
                title = likeNum ?: "点赞"
                contentDescription = if (likeNum == null) "点赞" else "点赞(数量:${likeNum}次)"
            },
        )
    }

    override fun onMenuItemClick(view: View, menuItem: MenuItemPropInfo) {
        super.onMenuItemClick(view, menuItem)
        val info = viewModel.info
        val needLogin = when(menuItem.key) {
            3, 4, 5 -> true
            else -> false
        }
        if (!userStore.isLogin() && needLogin) {
            PopTip.show("请先登录")
            return
        }
        when (menuItem.key) {
            MenuKeys.more -> {
                // 更多
                val pm = VideoMorePopupMenu(
                    activity = requireActivity(),
                    anchor = view,
                    viewModel = viewModel,
                    playerStore = viewModel.playerStore,
                    playListStore = viewModel.playListStore,
                )
                pm.show()
            }
            1 -> {
                // 评论
                if (info != null) {
//                    val nav = (activity as? MainActivity)?.pointerNav?.navController
//                        ?: requireActivity().findNavController(R.id.nav_host_fragment)
//                    val args = VideoCommentListFragment.createArguments(
//                        info.aid,
//                        info.title,
//                        info.pic,
//                        info.owner.name,
//                    )
//                    nav.navigate(VideoCommentListFragment.actionId, args)
                }
            }
            2 -> {
                // 分享
                if (info == null) {
                    PopTip.show("视频信息未加载完成，请稍后再试")
                    return
                }
                var shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "bilibili视频分享")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "${info.title} https://www.bilibili.com/video/${info.bvid}"
                    )
                }
                requireActivity().startActivity(Intent.createChooser(shareIntent, "分享"))
            }
            3 -> {
                // 收藏
                if (info != null) {
                    val nav = Navigation.findNavController(
                        requireActivity(),
                        R.id.nav_bottom_sheet_fragment
                    )
                    val args = VideoAddFavoriteFragment.createArguments(info.aid)
                    nav.navigate(VideoAddFavoriteFragment.actionId, args)
                }
            }
            4 -> {
                // 投币
                if (info != null) {
                    val nav = Navigation.findNavController(
                        requireActivity(),
                        R.id.nav_bottom_sheet_fragment
                    )
                    val num = if (info.copyright == 2) {
                        1
                    } else {
                        2
                    }
                    val args = VideoCoinFragment.createArguments(num)
                    nav.navigate(VideoCoinFragment.actionId, args)
                }
            }
            5 -> {
                // 点赞
                viewModel.requestLike()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ui.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        if (playerStore.state.aid == viewModel.id) {
            requireActivity().lifecycleScope.launch {
                val openMode = SettingPreferences.mapData(requireActivity()) {
                    it[PlayerOpenMode] ?: SettingConstants.PLAYER_OPEN_MODE_DEFAULT
                }
                if (openMode and SettingConstants.PLAYER_OPEN_MODE_AUTO_CLOSE != 0) {
                    // 自动关闭
                    basePlayerDelegate.closePlayer()
                }
            }
        }
        super.onDestroy()
    }

    fun confirmCoin(num: Int) {
        viewModel.requestCoin(num)
    }

    fun confirmFavorite(favIds: List<String>, addIds: List<String>, delIds: List<String>) {
        viewModel.requestFavorite(favIds, addIds, delIds)
    }

    private fun playVideo(page: VideoPageInfo) {
        val info = viewModel.info
        if (info != null) {
            viewModel.playVideo(info, page)
        }
    }

    private fun toSelfLink(view: View, url: String) {
        val urlInfo = BiliUrlMatcher.findIDByUrl(url)
        val urlType = urlInfo[0]
        var urlId = urlInfo[1]
        if (urlType == "BV") {
            urlId = "BV$urlId"
        }

        when (urlType) {
            "AV", "BV" -> {
                val args = createArguments(urlId)
                Navigation.findNavController(view)
                    .navigate(actionId, args)
            }
            else -> {
                BiliUrlMatcher.toUrlLink(view, url)
            }
        }
    }

    private fun toUser(view: View, mid: String) {
        val nav = Navigation.findNavController(view)
        nav.navigateToCompose(
            BilimiaoPageRoute.Entry.UserSpace,
            mid,
        )
    }

    private val handleUpperClick = View.OnClickListener {
        viewModel.info?.let { info ->
            toUser(it, info.owner.mid)
        }
    }

    private val handleTitleClick = View.OnClickListener {
        if (it is TextView) {
            PopTip.show(it.text)
        }
    }

    private val handleCoverClick = View.OnClickListener {
        CoverActivity.launch(requireContext(), viewModel.id)
    }

    private val handleMorePageClick = View.OnClickListener {
        viewModel.info?.let { info ->
            val nav = Navigation.findNavController(requireActivity(), R.id.nav_bottom_sheet_fragment)
            nav.navigateToCompose(
                BilimiaoPageRoute.Entry.VideoPages,
                info.aid
            )
        }
    }

    private val handleVideoHistoryClick = View.OnClickListener {
        val history = viewModel.info?.history ?: return@OnClickListener
        val page = viewModel.pages.find { it.cid == history.cid }
        if (page == null) {
            PopTip.show("未找到分P:" + history.cid)
        } else {
            playVideo(page)
        }
    }

    private val handleUpperItemClick = OnItemClickListener { adapter, view, position ->
        val item = viewModel.staffs[position]
        toUser(view, item.mid)
    }

    private val handlePageItemClick = OnItemClickListener { adapter, view, position ->
        val item = viewModel.pages[position]
        playVideo(item)
    }

    private val handleUgcEpisodeClick = OnItemClickListener { adapter, view, position ->
        val item = viewModel.ugcSeasonEpisodes[position]
        if (item is UgcEpisodeInfo) {
            viewModel.changeVideo(item.aid)
        }
    }

    private val handleTagsItemClick = OnItemClickListener { adapter, view, position ->
        val item = viewModel.tags[position]
        val nav = findNavController()
        nav.navigateToCompose(
            BilimiaoPageRoute.Entry.Search,
            param = item.tag_name
        )
    }

    private val handleRelateItemClick = OnItemClickListener { adapter, view, position ->
        val item = adapter.getItem(position)
        if (item is VideoRelateInfo) {
            if (item.goto == "av") {
                val args = createArguments(item.aid!!)
                Navigation.findNavController(view)
                    .navigate(actionId, args)
            } else {
                val url = item.uri
                val re = BiliNavigation.navigationTo(view, url)
                if (!re) {
                    if (url.indexOf("bilibili://") == 0) {
                        PopTip.show("不支持打开的链接：$url")
                    } else {
                        BiliUrlMatcher.toUrlLink(view, url)
                    }
                }
            }
        }
    }

    private val handleAutoPlaySeasonCheckedChange = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        viewModel.updateIsAutoPlaySeason(isChecked)
    }

    private val handlePlayListClick = View.OnClickListener {
        val nav = findNavController()
        nav.navigateToCompose(BilimiaoPageRoute.Entry.PlayList)
    }

    private val handleUgcSeasonDetailClick = View.OnClickListener {
        val ugcSeason = viewModel.ugcSeason ?: return@OnClickListener
        val nav = findNavController()
        nav.navigateToCompose(
            BilimiaoPageRoute.Entry.UserSeasonDetail,
            "id=${ugcSeason.id}&title=${ugcSeason.title}"
        )
    }

    private val handleRefresh = SwipeRefreshLayout.OnRefreshListener {
        viewModel.loadData()
    }

    private val handleLinkClickListener =
        ExpandableTextView.OnLinkClickListener { view, linkType, content, selfContent -> //根据类型去判断
            when (linkType) {
                LinkType.LINK_TYPE -> {
                    val url = content
                    val re = BiliNavigation.navigationTo(view, url)
                    if (!re) {
                        if (url.indexOf("bilibili://") == 0) {
                            PopTip.show("不支持打开的链接：$url")
                        } else {
                            BiliUrlMatcher.toUrlLink(view, url)
                        }
                    }
                }
                LinkType.MENTION_TYPE -> {
//                PopTip.show("你点击了@用户 内容是：$content")
                }
                LinkType.SELF -> {
                    toSelfLink(view, selfContent)
                }
            }
        }

    val pageItemUi = miaoBindingItemUi<VideoPageInfo> { item, index ->
        frameLayout {
            layoutParams = lParams {
                width = wrapContent
                height = matchParent
                rightMargin = dip(5)
            }
            contentDescription = "分P${item.page}：${item.part}"
            val enabled = playerStore.state.cid != item.cid
            setBackgroundResource(R.drawable.shape_corner)
            _isEnabled = enabled

            views {
                +textView {
                    horizontalPadding = dip(10)
                    verticalPadding = dip(5)
                    maxWidth = dip(120)
                    minWidth = dip(60)
                    maxLines = 1
                    gravity = Gravity.LEFT
                    ellipsize = TextUtils.TruncateAt.END
                    textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START
                    _textColorResource = if (enabled) {
                        R.color.text_black
                    } else {
                        config.themeColorResource
                    }
                    _text = item.part
                }..lParams {
                    gravity = Gravity.CENTER
                }
            }
        }
    }

    fun MiaoUI.pageView(): View {
        return horizontalLayout {
            views {
                +frameLayout {

                    views {
                        +recyclerView {
                            val lm = LinearLayoutManager(context)
                            lm.orientation = LinearLayoutManager.HORIZONTAL
                            scrollBarSize = 0
                            _miaoLayoutManage(lm)

                            _miaoAdapter(
                                items = viewModel.pages,
                                itemUi = pageItemUi,
                                depsAry = arrayOf(viewModel.id, playerStore.state.cid),
                            ) {
                                setOnItemClickListener(handlePageItemClick)
                            }
                        }..lParams {
                            width = matchParent
                            height = matchParent
                        }

                        +imageView {
                            scaleType = ImageView.ScaleType.FIT_XY
                            imageResource = R.drawable.shape_gradient
                        }..lParams {
                            gravity = Gravity.RIGHT
                            width = dip(10)
                            height = matchParent
                        }
                    }
                }..lParams {
                    width = matchParent
                    height = matchParent
                    weight = 1f
                }

                +imageView {
                    setImageResource(R.drawable.ic_navigate_next_black_24dp)
                    setBackgroundResource(config.selectableItemBackgroundBorderless)
                    setOnClickListener(handleMorePageClick)
//                    setOnClickListener {
//                        val fragment = PagesFragment.newInstance(viewModel.id, viewModel.pages, 0)
//                        MainActivity.of(context)
//                            .showBottomSheet(fragment)
//                    }
                    _show = viewModel.pages.size > 2
                }..lParams(dip(24), dip(24)) {
                    gravity = Gravity.CENTER
                }
            }
        }
    }

    fun MiaoUI.playListView(): View {
        val playListState = playListStore.state
        return horizontalLayout {
            backgroundColor = config.blockBackgroundColor
            gravity = Gravity.CENTER_VERTICAL
            setPadding(
                config.smallPadding,
                config.pagePadding,
                config.smallPadding,
                config.pagePadding,
            )
            apply(ViewStyle.roundRect(dip(10)))
            _show = !playListState.isEmpty()
            val currentPosition = playListState.indexOfAid(viewModel.id)
            val listSize = playListState.items.size
            val inList = currentPosition > -1
            setOnClickListener(handlePlayListClick)

            views {
                +verticalLayout {
                    views {
                        +textView {
                            _text = if(inList) {
                                "当前播放列表："
                            } else {
                                "不在当前播放列表中："
                            }
                            textSize = 14f
                        }
                        +textView {
                            _text = if(playListState.loading){
                                "加载中"
                            } else {
                                playListState.name ?: ""
                            }
                            textSize = 14f
                            setTextColor(config.foregroundAlpha45Color)
                        }
                    }
                }..lParams(matchParent, wrapContent) {
                    weight = 1f
                }
                +textView {
//                    _show = currentPosition >= 0
                    _text = if (inList) {
                        "${currentPosition + 1}/${listSize}"
                    } else {
                        "${listSize}个视频"
                    }
                    setTextColor(config.foregroundColor)
                }..lParams(wrapContent, wrapContent)
                +imageView {
                    setImageResource(R.drawable.ic_navigate_next_black_24dp)
                    imageTintList = ColorStateList.valueOf(config.foregroundColor)
                    setBackgroundResource(config.selectableItemBackgroundBorderless)
                }..lParams(dip(24), dip(24))
            }
        }
    }

    @OptIn(InternalSplittiesApi::class)
    val ugcSeasonEpisodeUi = miaoBindingItemUi<Any> { item, index ->
        if (item is UgcEpisodeInfo) {
            val selected = item.aid == viewModel.id
            horizontalLayout {
                padding = config.smallPadding
                setBackgroundResource(config.selectableItemBackground)
                views {
                    +rcImageView {
                        radius = dip(5)
                        _network(item.cover, "@672w_378h_1c_")
                    }..lParams {
                        width = dip(60)
                        height = dip(40)
                        rightMargin = dip(5)
                    }
                    +verticalLayout {
                        views {
                            +textView {
                                maxLines = 1
                                ellipsize = TextUtils.TruncateAt.END
                                textSize = 14f
                                _textColor = if (selected) {
                                    config.themeColor
                                } else {
                                    config.foregroundColor
                                }
                                _text = item.title
                            }..lParams(matchParent, wrapContent)
                            +textView {
                                maxLines = 1
                                ellipsize = TextUtils.TruncateAt.END
                                textSize = 12f
                                _textColor = if (selected) {
                                    config.themeColor
                                } else {
                                    config.foregroundAlpha45Color
                                }
                                _text = item.cover_right_text
                            }..lParams(matchParent, wrapContent)
                        }
                    }..lParams(dip(200), wrapContent)
                }
            }
        } else if (item is UgcSectionInfo) {
            verticalLayout {
                padding = config.smallPadding
                layoutParams = ViewGroup.LayoutParams(
                    dip(80),
                    wrapContent,
                )
                gravity = Gravity.END
                views {
                    +textView {
                        _text = item.title + ":"
                        gravity = Gravity.END
                        textSize = 14f
                        _textColor = config.foregroundColor
                    }..lParams(matchParent, wrapContent)
                    +textView {
                        _text = "${item.episodes.size}个视频"
                        gravity = Gravity.END
                        textSize = 12f
                        _textColor = config.foregroundAlpha45Color
                    }..lParams(matchParent, wrapContent)
                }
            }
        } else {
            view<View>()
        }
    }

    @OptIn(InternalSplittiesApi::class)
    fun MiaoUI.ugcSeasonView(): View {
        val ugcSeason = viewModel.ugcSeason
        return verticalLayout {
            backgroundColor = config.blockBackgroundColor
            apply(ViewStyle.roundRect(dip(10)))
            _show = ugcSeason != null

            views {
                +horizontalLayout {
                    padding = config.smallPadding
                    views {
                        +textView {
                            _text = "合集 · ${ugcSeason?.title}"
                            textSize = 14f
                        }..lParams(matchParent, wrapContent) {
                            weight = 1f
                        }
                        +view<MaterialSwitch> {
                            text = "自动连播"
                            thumbTintList = ColorStateList(
                                arrayOf(
                                    intArrayOf(android.R.attr.state_checked),
                                    intArrayOf()
                                ),
                                intArrayOf(
                                    config.colorSurface,
                                    config.foregroundAlpha45Color,
                                )
                            )
                            padding = 0
                            switchPadding = dip(5)
                            scaleX = 0.8f
                            scaleY = 0.8f
                            translationX = dip(10f)
                            _isChecked = viewModel.isAutoPlaySeason
                            setOnCheckedChangeListener(handleAutoPlaySeasonCheckedChange)
                        }..lParams(wrapContent, dip(10))
                    }
                }
                +recyclerView {
                    val lm = GridLayoutManager(context, 4)
                    lm.orientation = LinearLayoutManager.HORIZONTAL
                    lm.spanSizeLookup = miaoMemo(Unit) {
                        object : SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                val item = viewModel.ugcSeasonEpisodes[position]
                                if (item is UgcSectionInfo) {
                                    return 4
                                }
                                return 1
                            }
                        }
                    }
                    _miaoLayoutManage(lm)

                    _miaoMultiAdapter(
                        items = viewModel.ugcSeasonEpisodes,
                        itemUi = ugcSeasonEpisodeUi,
                        depsArr = arrayOf(viewModel.id, playerStore.state.cid),
                    ) {
                        setOnItemClickListener(handleUgcEpisodeClick)
                    }
                    miaoEffect(arrayOf(viewModel.id, viewModel.ugcSeasonEpisodes.size)) {
                        val aid = viewModel.id
                        val position = viewModel.ugcSeasonEpisodes.indexOfFirst {
                            (it as? UgcEpisodeInfo)?.aid == aid
                        }
                        if (position > 0) {
                            scrollToPosition(position)
                        }
                    }
                }..lParams {
                    width = matchParent
                    height = wrapContent
                }

                +textView {
                    text = "查看合集>"
                    setTextColor(config.themeColor)
                    setBackgroundResource(config.selectableItemBackgroundBorderless)
                    setOnClickListener(handleUgcSeasonDetailClick)
                    padding = config.smallPadding
                }..lParams {
                    width = wrapContent
                    height = wrapContent
                    gravity = Gravity.CENTER_HORIZONTAL
                    bottomMargin = config.smallPadding
                }
            }
        }
    }

    val upperItemUi = miaoBindingItemUi<VideoStaffInfo> { item, index ->
        verticalLayout {
            layoutParams = ViewGroup.LayoutParams(dip(64), wrapContent)
            topPadding = dip(10)
            bottomPadding = dip(5)
            gravity = Gravity.CENTER
            setBackgroundResource(config.selectableItemBackground)

            views {
                +rcImageView {
                    isCircle = true
                    _network(item.face, "@200w_200h")
                }..lParams {
                    height = dip(40)
                    width = dip(40)
                }

                +textView {
                    setTextColor(config.foregroundColor)
                    gravity = Gravity.CENTER
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                    _text = item.name
                }
                +textView {
                    setTextColor(config.themeColor)
                    textSize = 12f
                    gravity = Gravity.CENTER
                    maxLines = 1
                    _text = item.title
                }
            }
        }
    }

    fun MiaoUI.upperView(): View {
        return verticalLayout {

            val isMutableUpper = viewModel.info?.staff?.isEmpty() == false

            views {
                // 单个up主信息
                +horizontalLayout {
                    setBackgroundResource(config.selectableItemBackground)
                    setOnClickListener(handleUpperClick)
                    _show = !isMutableUpper

                    views {
                        +rcImageView {
                            isCircle = true
                            _network(viewModel.info?.owner?.face, "@200w_200h")
                        }..lParams {
                            height = dip(40)
                            width = dip(40)
                        }

                        +verticalLayout {

                            views {
                                +textView {
                                    setTextColor(config.foregroundColor)
                                    _text = viewModel.info?.owner?.name ?: ""
                                }
                                +textView {
                                    textSize = 12f
                                    setTextColor(config.foregroundAlpha45Color)
                                    _text = "发表于 " + NumberUtil.converCTime(viewModel.info?.pubdate)
                                }
                            }

                        }..lParams {
                            leftMargin = dip(8)
                        }
                    }
                }..lParams {
                    width = matchParent
                }

                // 多个up主信息
                +recyclerView {
                    _show = isMutableUpper
                    scrollBarSize = 0
                    _miaoLayoutManage(
                        LinearLayoutManager(context).apply {
                            orientation = LinearLayoutManager.HORIZONTAL
                        }
                    )
                    _miaoAdapter(
                        itemUi = upperItemUi,
                        items = viewModel.staffs
                    ) {
                        setOnItemClickListener(handleUpperItemClick)
                    }
                }

                +textView {
                    _show = isMutableUpper
                    textSize = 12f
                    setTextColor(config.foregroundAlpha45Color)
                    _text = "发表于 " + NumberUtil.converCTime(viewModel.info?.pubdate)
                }..lParams {
                    topMargin = dip(10)
                }
            }

        }
    }

    val itemTagUi = miaoBindingItemUi<VideoTagInfo> { item, index ->
        frameLayout {
            views {
                +frameLayout {
                    apply(ViewStyle.roundRect(dip(5)))
                    setBackgroundResource(config.selectableItemBackground)

                    views {
                        +textView {
                            backgroundColor = config.blockBackgroundColor
                            padding = dip(5)
                            _text = item.tag_name
                        }
                    }
                }..lParams {
                    rightMargin = dip(8)
                    bottomMargin = dip(5)
                }
            }
        }
    }

    fun MiaoUI.tagsView(): View {
        return recyclerView {
            isNestedScrollingEnabled = false
            _miaoLayoutManage(
                FlexboxLayoutManager(requireActivity()).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                }
            )
            _miaoAdapter(
                itemUi = itemTagUi,
                items = viewModel.tags
            ) {
                setOnItemClickListener(handleTagsItemClick)
            }
        }
    }

    fun MiaoUI.headerView(): View {
        val videoInfo = viewModel.info
        return verticalLayout {
            views {
                +horizontalLayout {
                    views {
                        +rcImageView {
                            contentDescription = "视频封面"
                            radius = dip(5)
                            setOnClickListener(handleCoverClick)
                            _network(videoInfo?.pic, "@672w_378h_1c_")
                        }..lParams {
                            width = dip(150)
                            height = dip(100)
                            rightMargin = dip(10)
                        }

                        +verticalLayout {

                            views {
                                // 标题
                                +textView {
                                    textSize = 16f
                                    ellipsize = TextUtils.TruncateAt.END
                                    maxLines = 4
                                    setTextColor(config.foregroundColor)
                                    setTextIsSelectable(true)
                                    setOnClickListener(handleTitleClick)
                                    _text = videoInfo?.title ?: ""
                                }..lParams(weight = 1f)

                                // 播放量
                                +horizontalLayout {

                                    views {

                                        +imageView {
                                            imageTintList =
                                                ColorStateList.valueOf(config.foregroundAlpha45Color)
                                            setImageResource(R.drawable.ic_info_views)
                                        }..lParams(dip(14), dip(14)) {
                                            gravity = Gravity.CENTER
                                        }
                                        +textView {
                                            textSize = 12f
                                            setTextColor(config.foregroundAlpha45Color)
                                            _text =
                                                NumberUtil.converString(videoInfo?.stat?.view ?: "")
                                        }..lParams {
                                            leftMargin = dip(3)
                                            rightMargin = dip(16)
                                        }

                                        +imageView {
                                            imageTintList =
                                                ColorStateList.valueOf(config.foregroundAlpha45Color)
                                            setImageResource(R.drawable.ic_info_danmakus)
                                        }..lParams(dip(14), dip(14)) {
                                            gravity = Gravity.CENTER
                                        }
                                        +textView {
                                            textSize = 12f
                                            setTextColor(config.foregroundAlpha45Color)
                                            _text = NumberUtil.converString(
                                                videoInfo?.stat?.danmaku ?: ""
                                            )
                                        }..lParams {
                                            leftMargin = dip(3)
                                            rightMargin = dip(16)
                                        }
                                    }

                                }..lParams {
                                    width = matchParent
                                    height = wrapContent
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            }
                        }..lParams(matchParent, matchParent)


                    }
                }

                +upperView()..lParams {
                    width = matchParent
                    height = wrapContent
                    topMargin = dip(10)
                }
                +pageView()..lParams {
                    width = matchParent
                    height = dip(48)
                    topMargin = dip(10)
                }
                +horizontalLayout {
                    apply(ViewStyle.roundRect(dip(5)))
                    backgroundColor = config.blockBackgroundColor

                    val history = videoInfo?.history
                    _show = history != null && history.progress > 0
                            && playerStore.state.aid != videoInfo?.aid

                    views {
                        +textView {
                            padding = dip(5)
                            rightPadding = 0
                            text = "上次看到："
                        }
                        +textView {
                            setBackgroundResource(config.selectableItemBackground)
                            setOnClickListener(handleVideoHistoryClick)
                            setTextColor(config.themeColor)
                            padding = dip(5)
                            leftPadding = 0

                            miaoEffect(history) {
                                if (history == null) {
                                    text = ""
                                } else {
                                    val pageName = viewModel.pages.find {
                                        it.cid == history.cid
                                    }?.part?.takeIf {
                                        viewModel.pages.size > 1
                                    }
                                    visibility = View.VISIBLE
                                    val progress = NumberUtil.converDuration(history.progress)
                                    text = if (pageName.isNullOrEmpty()) {
                                        progress
                                    } else {
                                        "$pageName $progress"
                                    }
                                }
                            }
                        }
                    }
                }..lParams {
                    width = wrapContent
                    height = matchParent
                    topMargin = dip(10)
                }

                +expandableTextView {
                    setLineSpacing(dip(4).toFloat(), 1.0f)
                    textSize = 14f
                    setMaxLine(2)
                    isNeedContract = true
                    isNeedExpend = true
                    setNeedMention(false)
                    isNeedSelf = true
                    setNeedConvertUrl(false)
                    _setContent(viewModel.info?.desc ?: "")
                    linkClickListener = handleLinkClickListener
                }..lParams {
                    width = matchParent
                    height = wrapContent
                    topMargin = dip(10)
                }

                +playListView()..lParams {
                    width = matchParent
                    topMargin = dip(10)
                }
                +textView {
                    val playListFrom = playListStore.state.from
                    val ugcSeason = viewModel.ugcSeason
                    _show = viewModel.isAutoPlaySeason &&
                            ugcSeason != null &&
                            playListFrom != null &&
                            ugcSeason.id != playListFrom.let {
                                (it as? PlayListFrom.Season)?.seasonId ?:
                                (it as? PlayListFrom.Section)?.seasonId
                            }
                    text = "将自动替换当前播放列表"
                    textSize = 12f
                    setTextColor(0xFFFF2222.toInt())
                    gravity = Gravity.END
                }..lParams {
                    width = matchParent
                    topMargin = dip(10)
                }
                +ugcSeasonView()..lParams {
                    width = matchParent
                    topMargin = dip(10)
                }

                +tagsView()..lParams {
                    width = matchParent
                    height = wrapContent
                    topMargin = dip(10)
                }
            }

        }
    }

    val relateItemUi = miaoBindingItemUi<VideoRelateInfo> { item, index ->
        videoItem(
            title = item.title,
            pic = item.pic,
            upperName = item.owner?.name,
            playNum = item.stat?.view,
            damukuNum = item.stat?.danmaku,
            duration = NumberUtil.converDuration(item.duration),
        )
    }

    val ui = miaoBindingUi {
        connectStore(viewLifecycleOwner, windowStore)
        connectStore(viewLifecycleOwner, playerStore)
        connectStore(viewLifecycleOwner, playListStore)
        val contentInsets = windowStore.getContentInsets(parentView)
        val info = viewModel.info
        // 监听info改变，修改页面标题
        miaoEffect(listOf(info, info?.req_user, info?.staff)) {
            pageConfig.notifyConfigChanged()
        }
        recyclerView {
            _leftPadding = contentInsets.left
            _rightPadding = contentInsets.right
            backgroundColor = config.windowBackgroundColor
            _miaoLayoutManage(
                GridAutofitLayoutManager(requireContext(), requireContext().dip(300))
            )

            val mAdapter = _miaoAdapter(
                items = viewModel.relates,
                itemUi = relateItemUi,
            ) {
                setOnItemClickListener(handleRelateItemClick)
            }

            headerViews(mAdapter) {
                +headerView().apply {
                    horizontalPadding = config.pagePadding
                    _topPadding = contentInsets.top + config.pagePadding
                }..lParams(matchParent, wrapContent)
            }
            footerViews(mAdapter) {
                +frameLayout {
                    _topPadding = contentInsets.bottom
                }
            }
        }.wrapInSwipeRefreshLayout {
            setColorSchemeResources(config.themeColorResource)
            setOnRefreshListener(handleRefresh)
            _isRefreshing = viewModel.loading
        }
    }

}