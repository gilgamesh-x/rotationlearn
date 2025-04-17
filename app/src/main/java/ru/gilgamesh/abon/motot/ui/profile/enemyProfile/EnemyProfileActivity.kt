package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.R
import ru.gilgamesh.abon.motot.databinding.ActivityEnemyProfileBinding
import ru.gilgamesh.abon.motot.imageGallery.ImageFullScreenActivity
import ru.gilgamesh.abon.motot.imageGallery.ImageGalleryActivity
import ru.gilgamesh.abon.motot.model.ActivityResult
import ru.gilgamesh.abon.motot.model.App
import ru.gilgamesh.abon.motot.model.CustomGlideApp
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserAchievementResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi
import ru.gilgamesh.abon.motot.ui.bottomNav.chat.ChatCardActivity
import ru.gilgamesh.abon.motot.ui.complain.ComplainBottomSheetFragment
import ru.gilgamesh.abon.motot.ui.complain.ComplainTypeEnum
import ru.gilgamesh.abon.motot.ui.profile.AchievementActivity
import ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery.ItemImg
import ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery.RecyclerViewImgGalleryAdapter
import java.util.stream.Collectors

@AndroidEntryPoint
class EnemyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEnemyProfileBinding
    private val viewModel: EnemyProfileViewModel by viewModels()
    private lateinit var recyclerViewImgGalleryAdapter: RecyclerViewImgGalleryAdapter

    fun resetProfile() {
        with(binding) {
            textUserName.resetLoader()
            textUserNickName.resetLoader()
            enmProfileCountSubscribers.resetLoader()
            enmProfileCountSubscriptions.resetLoader()
            enmProfileCountRoute.resetLoader()
            enmCountCompetition.resetLoader()
            textPilotMoto.resetLoader()
            enmCountCompetition.isVisible = false
            enmProfileCountRoute.isVisible = false
            enmProfileCountSubscriptions.isVisible = false
            setRoFaImageView(iconAchRegFa, 0f)
            setRoFaImageView(iconAchRouteFa, 0f)
            setRoFaImageView(iconAchSubsFa, 0f)
            setRoFaImageView(iconAchKmFa, 0f)
            setRoFaImageView(iconAchEventFa, 0f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEnemyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        resetProfile()

        // Скрыть ActionBar
        supportActionBar?.hide()

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effectsFlow.collect { effect ->
                    when (effect) {
                        EnemyProfileEffect.ShowError -> {
                            Toast.makeText(
                                this@EnemyProfileActivity,
                                getString(R.string.httpall),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is EnemyProfileEffect.ShowProfile -> {
                            viewModel.sendEvent(EnemyProfileEvent.Ui.LoadUserImg)
                            viewModel.sendEvent(EnemyProfileEvent.Ui.LoadUserCoverImg)
                            viewModel.sendEvent(EnemyProfileEvent.Ui.LoadPhotoGallery)
                            showUI(effect.data, effect.enableMsg)
                        }

                        is EnemyProfileEffect.LoadAvatarByGlide -> {
                            loadUserAvatar(effect.imgId)
                        }

                        is EnemyProfileEffect.LoadAvatarBySex -> {
                            loadUserAvatarDefault(effect.sex)
                        }

                        is EnemyProfileEffect.LoadCoverByGlide -> {
                            loadUserCover(effect.imgId)
                        }

                        EnemyProfileEffect.LoadCoverByRes -> {
                            loadUserCoverDefault()
                        }

                        is EnemyProfileEffect.LoadPhotoGallery -> {
                            setPhotoGallery(effect.data)
                        }

                        is EnemyProfileEffect.GotoFullSizeGallery -> {
                            if (effect.data.isNotEmpty()) {
                                gotoFullSizeGallery(effect.data, effect.position)
                            }
                        }

                        is EnemyProfileEffect.GotoFullSizeAvatar -> {
                            effect.imgId?.let {
                                gotoFullSizeAvatar(it)
                            }
                        }

                        is EnemyProfileEffect.GotoBack -> {
                            goBack()
                        }

                        is EnemyProfileEffect.GotoBackRefresh -> {
                            goBackRefresh()
                        }

                        is EnemyProfileEffect.ShowMenu -> {
                            showMenu()
                        }

                        is EnemyProfileEffect.ShowComplain -> {
                            showComplain(effect.id)
                        }

                        is EnemyProfileEffect.OpenAchievement -> {
                            openAchievement(effect.ach)
                        }

                        is EnemyProfileEffect.Subscribed -> {
                            subscribed(effect.count)
                        }

                        is EnemyProfileEffect.Unsubscribed -> {
                            unsubscribed(effect.count)
                        }

                        is EnemyProfileEffect.GotoChat -> {
                            goChat(effect.chatId, effect.chatName)
                        }

                        is EnemyProfileEffect.GotoChatNew -> {
                            goChatNew(effect.companionId, effect.chatName)
                        }
                    }
                }
            }
        }

        with(intent) {
            viewModel.putInitData(
                getLongExtra("contactId", -1),
                getLongExtra("notificationId", -1),
                getStringExtra("prevActivity") ?: ""
            )
        }

        viewModel.sendEvent(
            EnemyProfileEvent.Ui.LoadProfile
        )

        with(binding) {
            profileRecyclerView.isVisible = false
            profileEmptyPhoto.isVisible = true
            imgEnmProfileActAvatar.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.ClickAvatar)
            })

            backBtn.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.GoBack)
            })

            menuBtn.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.ClickShowMenu)
            })

            iconAchRegFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.GotoAchievement)
            })

            iconAchRouteFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.GotoAchievement)
            })

            iconAchSubsFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.GotoAchievement)
            })

            iconAchKmFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.GotoAchievement)
            })

            iconAchEventFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.GotoAchievement)
            })

            btnEnemyProfileSubs.setOnClickListener(View.OnClickListener { view: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.ClickSubscribe)
            })

            btnEnemyProfileUnsubs.setOnClickListener(View.OnClickListener { view: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.ClickUnsubscribe)
            })

            btnEnemyProfileMsg.setOnClickListener(View.OnClickListener { view: View? ->
                viewModel.sendEvent(EnemyProfileEvent.Ui.ClickMessage)
            })
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.sendEvent(EnemyProfileEvent.Ui.GoBack)
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun goChatNew(lng: Long, string: String) {
        Intent(this@EnemyProfileActivity, ChatCardActivity::class.java).apply {
            putExtra("companionId", lng).putExtra(
                "chatName", string
            )
            putExtra("prevActivity", TAG)
            startActivity(this)
        }
    }

    private fun goChat(lng: Long, string: String) {
        Intent(this@EnemyProfileActivity, ChatCardActivity::class.java).apply {
            putExtra("chatId", lng).putExtra(
                "chatName", string
            )
            putExtra("prevActivity", TAG)
            startActivity(this)
        }
    }

    private fun unsubscribed(count: Int) {
        with(binding) {
            enmProfileCountSubscribers.text = String.format(
                getString(
                    R.string.dynamic_count_subscribersD
                ), count
            )
            btnEnemyProfileSubs.isVisible = true
            btnEnemyProfileUnsubs.isVisible = false
        }
    }

    private fun subscribed(count: Int) {
        with(binding) {
            enmProfileCountSubscribers.text = String.format(
                getString(
                    R.string.dynamic_count_subscribersD
                ), count
            )
            btnEnemyProfileSubs.isVisible = false
            btnEnemyProfileUnsubs.isVisible = true
        }
    }

    private fun openAchievement(data: UserAchievementResponse) {
        //TODO переделать на parcel
        Intent(this@EnemyProfileActivity, AchievementActivity::class.java).apply {
            with(data) {
                putExtra("reg", registrationFlg ?: false)
                putExtra("route1", routeCreate1Flg ?: false)
                putExtra("route2", routeCreate2Flg ?: false)
                putExtra("route3", routeCreate3Flg ?: false)
                putExtra("subs1", subscription1Flg ?: false)
                putExtra("subs2", subscription2Flg ?: false)
                putExtra("subs3", subscription3Flg ?: false)
                putExtra("km1", km1Flg ?: false)
                putExtra("km2", km2Flg ?: false)
                putExtra("km3", km3Flg ?: false)
                putExtra("event1", event1Flg ?: false)
                putExtra("event2", event2Flg ?: false)
                putExtra("event3", event3Flg ?: false)
            }
            startActivity(this)
        }
    }

    private fun showComplain(lng: Long) {
        ComplainBottomSheetFragment(ComplainTypeEnum.PROFILE, lng).apply {
            show(
                supportFragmentManager, this.tag
            )
        }
    }

    private fun showMenu() {
        val popup = PopupMenu(this@EnemyProfileActivity, binding.menuBtn)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem ->
            this@EnemyProfileActivity.onOptionsItemSelected(
                item
            )
        })
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_enemy_profile, popup.menu)

        val positionOfMenuItem = 0
        val item = popup.menu[positionOfMenuItem]
        val s = SpannableString(getString(R.string.menu_complain))
        s.setSpan(ForegroundColorSpan(getColor(R.color.moto_menu_red)), 0, s.length, 0)
        item.title = s
        popup.show()
    }

    private fun gotoFullSizeAvatar(lng: Long) {
        Intent(this, ImageFullScreenActivity::class.java).apply {
            putExtra(ImageFullScreenActivity.GLIDE_TYPE, ImageFullScreenActivity.GLIDE_TYPE_AVATAR)
            putExtra(ImageFullScreenActivity.ID, lng)
            startActivity(this)
        }
    }

    private fun gotoFullSizeGallery(
        data: List<IdentifierResponse>, pos: Int
    ) {
        with(getSharedPreferences(App.IMAGE_FULL, MODE_PRIVATE).edit()) {
            putString(
                "jsonGallery", Gson().toJson(
                    data.stream().map<Long> { it.id }.collect(
                        Collectors.toList()
                    )
                )
            )
            putInt("position", pos)
            apply()
        }
        Intent(this, ImageGalleryActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun setPhotoGallery(data: List<ItemImg>) {
        with(binding) {
            if (data.isEmpty()) {
                profileRecyclerView.isVisible = false
                profileEmptyPhoto.isVisible = true
            } else {
                recyclerViewImgGalleryAdapter.submitList(data)
                profileRecyclerView.isVisible = true
                profileEmptyPhoto.isVisible = false
            }
        }
    }

    private fun loadUserAvatar(lng: Long) {
        with(binding) {
            CustomGlideApp.getContactImageByIdByte(
                imgEnmProfileActAvatar.context, lng, imgEnmProfileActAvatar
            )
        }
    }

    private fun loadUserAvatarDefault(string: String) {
        with(binding) {
            imgEnmProfileActAvatar.setImageResource(CustomGlideApp.getDefaultAvatarBySexResId(string))
        }
    }

    private fun loadUserCover(lng: Long) {
        with(binding) {
            CustomGlideApp.getContactImageByIdByte(
                imgEnmProfileActCover.context, lng, imgEnmProfileActCover
            )
        }
    }

    private fun loadUserCoverDefault() {
        with(binding) {
            imgEnmProfileActCover.setImageResource(R.drawable.rectangle_1525)
        }
    }

    private fun initialize() {
        val layoutManager: GridLayoutManager = object : GridLayoutManager(this, 3) {
            override fun canScrollVertically(): Boolean {
                return false // Запретить вертикальную прокрутку
            }
        }
        with(binding) {
            profileRecyclerView.setLayoutManager(layoutManager)
            profileRecyclerView.setHasFixedSize(true)

            recyclerViewImgGalleryAdapter =
                RecyclerViewImgGalleryAdapter(listenerItem = RecyclerViewImgGalleryAdapter.OnItemClickListener { position: Int ->
                    viewModel.sendEvent(EnemyProfileEvent.Ui.GotoFullSizeGallery(position))
                }, listenerItemMenu = RecyclerViewImgGalleryAdapter.OnItemClickDeleteListener {})
            profileRecyclerView.setAdapter(recyclerViewImgGalleryAdapter)
        }
    }

    private fun goBack() {
        finish()
    }

    private fun goBackRefresh() {
        setResult(ActivityResult.PROFILE_COUNT_SUBS.value)
        finish()
    }

    private fun showUI(data: UserInfoApi, enableMsg: Boolean) {
        with(binding) {
            textUserName.text =
                if (data.firstName.isNullOrEmpty()) data.nickName else data.firstName
            textUserNickName.text =
                String.format(getString(R.string.dynamic_user_nick_name), data.nickName)
            enmProfileCountSubscriptions.text = String.format(
                getString(R.string.dynamic_count_subscriptionsD), data.countSubscriptions ?: 0
            )
            enmProfileCountSubscriptions.isVisible = true

            enmProfileCountSubscribers.text = String.format(
                getString(R.string.dynamic_count_subscribersD), data.countSubscribers ?: 0
            )
            enmProfileCountSubscribers.isVisible = true
            //TODO расчет значение не на ui
            enmProfileCountRoute.text = String.format(
                getString(R.string.dynamic_count_routeD),
                data.countMyRoute ?: 0,
                data.countFinishRoute ?: 0
            )
            enmProfileCountRoute.isVisible = true

            enmCountCompetition.text = String.format(
                getString(R.string.dynamic_count_competitionD),
                data.countMyCompetition ?: 0,
                data.countFinishCompetition ?: 0
            )
            enmCountCompetition.isVisible = true

            textPilotMoto.text = String.format(
                getString(R.string.dynamic_pilot_moto),
                "${data.motoBrand ?: ""} ${data.motoModel ?: ""}"
            )
            layoutPilot.isVisible = "${data.motoBrand ?: ""}${data.motoModel ?: ""}".isNotEmpty()

            textUserAbout.text = data.about ?: ""
            textUserAboutHeader.isVisible = !data.about.isNullOrEmpty()
            textUserAbout.isVisible = !data.about.isNullOrEmpty()

            btnEnemyProfileSubs.isVisible = !data.subscriber
            btnEnemyProfileUnsubs.isVisible = data.subscriber ?: false

            userVerifiedImg.isVisible = data.verifiedFlg ?: false

            setRoFaImageView(
                iconAchRegFa,
                data.achievement?.let { if (it.registrationFlg) 1f else 0f } ?: run { 0f })

            setRoFaImageView(
                iconAchSubsFa,
                data.achievement?.let { if (it.subscription1Flg) 1f else 0f } ?: run { 0f })

            setRoFaImageView(
                iconAchRouteFa,
                data.achievement?.let { if (it.routeCreate1Flg) 1f else 0f } ?: run { 0f })

            setRoFaImageView(
                iconAchKmFa,
                data.achievement?.let { if (it.km1Flg) 1f else 0f } ?: run { 0f })

            setRoFaImageView(
                iconAchEventFa,
                data.achievement?.let { if (it.event1Flg) 1f else 0f } ?: run { 0f })

            //Обработка плашки дистанции
            fillDistanceLayout(data.distance)

            if (enableMsg) {
                buttonsHelpText.isVisible = false
                btnEnemyProfileMsg.isVisible = true
            } else {
                btnEnemyProfileMsg.isVisible = false
                buttonsHelpText.isVisible = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                viewModel.sendEvent(EnemyProfileEvent.Ui.GoBack)
            }

            R.id.action_complain_profile -> {
                viewModel.sendEvent(EnemyProfileEvent.Ui.ClickComplain)
            }

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    /**
     *
     * @param imageView
     * @param saturation - 0 - RO, 1 - FA
     */
    private fun setRoFaImageView(imageView: ImageView, saturation: Float) {
        val matrix = ColorMatrix()
        matrix.setSaturation(saturation)

        val filter = ColorMatrixColorFilter(matrix)
        imageView.colorFilter = filter
    }

    @Suppress("MagicNumber")
    private fun fillDistanceLayout(_distance: Double?) {
        var distance = _distance?.toInt() ?: run { 0 }
        with(binding) {
            distanceTextView.text = String.format(
                getString(R.string.dynamic_profile_distance_1), distance
            )
            val shader: Shader = LinearGradient(
                0f,
                distanceTextView.textSize,
                distanceTextView.paint.measureText(distanceTextView.text as String?),
                distanceTextView.textSize,
                "#FF7919".toColorInt(),
                "#FFCF53".toColorInt(),
                Shader.TileMode.REPEAT
            )
            distanceTextView.paint.shader = shader
            val DISTANCE_POINT_START = 200
            val DISTANCE_POINT_END = 800
            val repeat = distance.toDouble() / DISTANCE_POINT_END
            val repeatInt = repeat.toInt()
            firstDistancePointTextView.text = String.format(
                getString(R.string.dynamic_profile_distance_2),
                repeatInt * DISTANCE_POINT_END + DISTANCE_POINT_START
            )
            secondDistancePointTextView.text = String.format(
                getString(R.string.dynamic_profile_distance_2),
                repeatInt * DISTANCE_POINT_END + DISTANCE_POINT_START * 2
            )
            thirdDistancePointTextView.text = String.format(
                getString(R.string.dynamic_profile_distance_2),
                repeatInt * DISTANCE_POINT_END + DISTANCE_POINT_START * 3
            )

            var progress = 0
            val res = distance - repeatInt * DISTANCE_POINT_END
            if (res >= 0 && res < DISTANCE_POINT_START) {
                progress = res / (DISTANCE_POINT_START / 2)
            } else {
                if (res > DISTANCE_POINT_START && res <= DISTANCE_POINT_START * 2) {
                    progress = ((res - DISTANCE_POINT_START) / (DISTANCE_POINT_START / 8)) + 2
                } else {
                    if (res > DISTANCE_POINT_START * 2 && res <= DISTANCE_POINT_START * 3) {
                        progress =
                            ((res - DISTANCE_POINT_START * 2) / (DISTANCE_POINT_START / 8)) + 10
                    } else {
                        if (res > DISTANCE_POINT_START * 3) {
                            progress =
                                ((res - (DISTANCE_POINT_END - DISTANCE_POINT_START)) / (DISTANCE_POINT_START / 2)) + 19
                        }
                    }
                }
            }
            distanceProgressBar.progress = progress
        }
    }

    companion object {
        @JvmField
        val TAG: String = EnemyProfileActivity::class.java.simpleName
    }
}