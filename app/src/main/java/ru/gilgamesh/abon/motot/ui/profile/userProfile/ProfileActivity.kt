package ru.gilgamesh.abon.motot.ui.profile.userProfile

import android.content.DialogInterface
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LinearGradient
import android.graphics.Shader
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gilgamesh.abon.core.data.api.AuthApi
import ru.gilgamesh.abon.core.data.model.response.IdentifierResponse
import ru.gilgamesh.abon.core.data.model.response.MessageResponse
import ru.gilgamesh.abon.core.glide.CustomGlideMethod
import ru.gilgamesh.abon.motot.R
import ru.gilgamesh.abon.motot.data.api.ContactImageApi
import ru.gilgamesh.abon.motot.databinding.ActivityProfileBinding
import ru.gilgamesh.abon.motot.imageGallery.ImageFullScreenActivity
import ru.gilgamesh.abon.motot.imageGallery.ImageGalleryActivity
import ru.gilgamesh.abon.motot.model.ActivityResult
import ru.gilgamesh.abon.motot.model.App
import ru.gilgamesh.abon.motot.presentation.routeRecord.RouteRecordActivity
import ru.gilgamesh.abon.motot.ui.LoginActivity
import ru.gilgamesh.abon.motot.ui.profile.AchievementActivity
import ru.gilgamesh.abon.motot.ui.profile.EditProfileActivity
import ru.gilgamesh.abon.motot.ui.profile.ListSubscriptionsActivity
import ru.gilgamesh.abon.userprofile.data.model.UserInfo
import ru.gilgamesh.abon.userprofile.data.model.UserInfoAchievement
import ru.gilgamesh.abon.userprofile.presentation.imageGallery.ItemImg
import ru.gilgamesh.abon.userprofile.presentation.imageGallery.RecyclerViewImgGalleryAdapter
import ru.gilgamesh.abon.userprofile.presentation.profile.ProfileEffect
import ru.gilgamesh.abon.userprofile.presentation.profile.ProfileEvent
import java.util.stream.Collectors
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    companion object {
        private val TAG: String = ProfileActivity::class.java.simpleName
        const val PHOTO_GALLERY_COLUMN_COUNT = 3
        const val PHOTO_MAX_WIDTH = 2048
        const val PHOTO_MAX_HEIGHT = 2048
        const val PHOTO_REQUEST_CODE = 10
    }

    @Inject
    lateinit var contactImageApi: ContactImageApi

    @Inject
    lateinit var authApi: AuthApi

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var recyclerViewImgGalleryAdapter: RecyclerViewImgGalleryAdapter
    private lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        initialize()
        bindEvents()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effectsFlow.collect { effect ->
                    when (effect) {
                        ProfileEffect.ShowError -> {
                            Toast.makeText(
                                this@ProfileActivity,
                                getString(R.string.httpall),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ProfileEffect.ShowProfile -> {
                            viewModel.sendEvent(ProfileEvent.Ui.LoadPhotoGallery)
                            showUI(effect.data)
                        }

                        is ProfileEffect.LoadAvatarByGlide -> {
                            loadUserAvatar(effect.imgId)
                        }

                        is ProfileEffect.LoadAvatarBySex -> {
                            loadUserAvatarDefault(effect.sex)
                        }

                        is ProfileEffect.LoadCoverByGlide -> {
                            loadUserCover(effect.imgId)
                        }

                        is ProfileEffect.LoadCoverByRes -> {
                            loadUserCoverDefault()
                        }

                        is ProfileEffect.LoadPhotoGallery -> {
                            setPhotoGallery(effect.data)
                        }

                        is ProfileEffect.LoadPhotoGalleryError -> {
                            Toast.makeText(
                                this@ProfileActivity,
                                getString(R.string.err_load_photo_gallery),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ProfileEffect.GotoListSubscribers -> {
                            gotoListSubscribers()
                        }

                        is ProfileEffect.GotoListSubscriptions -> {
                            gotoListSubscriptions()
                        }

                        is ProfileEffect.GotoEditProfile -> {
                            gotoEditProfile()
                        }

                        is ProfileEffect.GotoAddFriend -> {
                            gotoAddFriend()
                        }

                        is ProfileEffect.GotoAchievement -> {
                            gotoAchievement(effect.data)
                        }

                        is ProfileEffect.GotoFullAvatar -> {
                            gotoFullAvatar(effect.imgId)
                        }

                        is ProfileEffect.ShowMenu -> {
                            showMenu()
                        }

                        is ProfileEffect.GotoPickPhoto -> {
                            gotoPickPhoto()
                        }

                        is ProfileEffect.GotoFullSizeGallery -> {
                            gotoFullSizeGallery(effect.data, effect.position)
                        }

                        is ProfileEffect.GalleryDeleteError -> {
                            Toast.makeText(
                                this@ProfileActivity,
                                getString(R.string.err_delete_photo_gallery),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ProfileEffect.GalleryAddError -> {
                            Toast.makeText(
                                this@ProfileActivity,
                                getString(R.string.err_add_photo_gallery),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        viewModel.sendEvent(ProfileEvent.Ui.LoadProfile)

        with(binding) {
            profileCountSubscribers.setOnClickListener(View.OnClickListener { view: View? ->
                viewModel.sendEvent(ProfileEvent.Ui.GotoListSubscribers)
            })
            profileCountSubscriptions.setOnClickListener(View.OnClickListener { view: View? ->
                viewModel.sendEvent(ProfileEvent.Ui.GotoListSubscriptions)
            })
            addFriendBtn.setOnClickListener(View.OnClickListener { view: View? ->
                viewModel.sendEvent(ProfileEvent.Ui.GotoAddFriend)
            })
            iconAchRegFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(
                    ProfileEvent.Ui.GotoAchievement
                )
            })
            iconAchRouteFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(
                    ProfileEvent.Ui.GotoAchievement
                )
            })
            iconAchSubsFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(
                    ProfileEvent.Ui.GotoAchievement
                )
            })
            iconAchKmFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(
                    ProfileEvent.Ui.GotoAchievement
                )
            })
            iconAchEventFa.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(
                    ProfileEvent.Ui.GotoAchievement
                )
            })

            imgProfileActAvatar.setOnClickListener(View.OnClickListener { v: View? ->
                viewModel.sendEvent(
                    ProfileEvent.Ui.GotoFullAvatar
                )
            })

            menuBtn.setOnClickListener(View.OnClickListener { v: View ->
                viewModel.sendEvent(
                    ProfileEvent.Ui.ClickShowMenu
                )
            })

            btnProfileAddPhoto.setOnClickListener({
                viewModel.sendEvent(
                    ProfileEvent.Ui.ClickAddPhoto
                )
            })
        }
    }

    private fun gotoEditProfile() {
        Intent(this@ProfileActivity, EditProfileActivity::class.java).apply {
            someActivityResultLauncher.launch(this)
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

    private fun gotoPickPhoto() {
        ImagePicker.Companion.with(this@ProfileActivity).crop().galleryOnly() //.compress(512)
            .maxResultSize(PHOTO_MAX_WIDTH, PHOTO_MAX_HEIGHT).start(PHOTO_REQUEST_CODE)
    }

    private fun showMenu() {
        val popup = PopupMenu(
            this@ProfileActivity, binding.menuBtn, Gravity.END
        )
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            this@ProfileActivity.onOptionsItemSelected(
                item!!
            )
        })
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_profile, popup.menu)
        popup.show()
    }

    private fun gotoFullAvatar(imgId: Long) {
        Intent(this, ImageFullScreenActivity::class.java).apply {
            putExtra(
                ImageFullScreenActivity.GLIDE_TYPE,
                ImageFullScreenActivity.GLIDE_TYPE_AVATAR
            )
            putExtra(ImageFullScreenActivity.ID, imgId)
            startActivity(this)
        }
    }

    private fun gotoAddFriend() {
        Intent(this@ProfileActivity, ListSubscriptionsActivity::class.java).apply {
            putExtra(
                ListSubscriptionsActivity.TYPE_EXTRA, ListSubscriptionsActivity.TYPE_ADD_FRIEND
            )
            someActivityResultLauncher.launch(this)
        }
    }

    private fun gotoListSubscribers() {
        Intent(this@ProfileActivity, ListSubscriptionsActivity::class.java).apply {
            putExtra(
                ListSubscriptionsActivity.TYPE_EXTRA, ListSubscriptionsActivity.TYPE_SUBSCRIBERS
            )
            someActivityResultLauncher.launch(this)
        }
    }

    private fun gotoListSubscriptions() {
        Intent(this@ProfileActivity, ListSubscriptionsActivity::class.java).apply {
            putExtra(
                ListSubscriptionsActivity.TYPE_EXTRA, ListSubscriptionsActivity.TYPE_SUBSCRIPTIONS
            )
            someActivityResultLauncher.launch(this)
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
            CustomGlideMethod.getContactImageByIdByte(
                imgProfileActAvatar.context, lng, imgProfileActAvatar
            )
        }
    }

    private fun loadUserAvatarDefault(string: String) {
        with(binding) {
            imgProfileActAvatar.setImageResource(CustomGlideMethod.getDefaultAvatarBySexResId(string))
        }
    }

    private fun loadUserCover(lng: Long) {
        with(binding) {
            CustomGlideMethod.getContactImageByIdByte(
                imgProfileActCover.context, lng, imgProfileActCover
            )
        }
    }

    private fun loadUserCoverDefault() {
        with(binding) {
            imgProfileActCover.setImageResource(R.drawable.rectangle_1525)
        }
    }

    private fun initialize() {
        with(binding) {
            profileRecyclerView.setLayoutManager(
                GridLayoutManager(
                    this@ProfileActivity, PHOTO_GALLERY_COLUMN_COUNT
                )
            )
            setRoFaImageView(iconAchRegFa, 0f)
            setRoFaImageView(iconAchRouteFa, 0f)
            setRoFaImageView(iconAchSubsFa, 0f)
            setRoFaImageView(iconAchKmFa, 0f)
            setRoFaImageView(iconAchEventFa, 0f)
        }
    }

    private fun bindEvents() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                setResult(999)
            }
        }

        someActivityResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == ActivityResult.PROFILE_EDIT.value) {
                viewModel.sendEvent(ProfileEvent.Ui.LoadProfile)
            } else if (result.resultCode == ActivityResult.PROFILE_COUNT_SUBS.value) {
                viewModel.sendEvent(ProfileEvent.Ui.LoadProfile)
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBtn.setOnClickListener(View.OnClickListener { v: View? -> finish() })

        recyclerViewImgGalleryAdapter =
            RecyclerViewImgGalleryAdapter(
                listenerItem = RecyclerViewImgGalleryAdapter.OnItemClickListener { position: Int ->
                viewModel.sendEvent(ProfileEvent.Ui.GotoFullSizeGallery(position))
            },
                listenerItemMenu = RecyclerViewImgGalleryAdapter.OnItemClickDeleteListener { imgId: Long ->
                    viewModel.sendEvent(ProfileEvent.Ui.DeletePhoto(imgId))
                })
        binding.profileRecyclerView.setAdapter(recyclerViewImgGalleryAdapter)

        binding.layoutDistance.setOnClickListener(View.OnClickListener { v: View? ->
            try {
                val lm = this@ProfileActivity.getSystemService(LOCATION_SERVICE) as LocationManager
                val gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (!gps_enabled) {
                    val builder = AlertDialog.Builder(this@ProfileActivity)
                    builder.setTitle(getString(R.string.ups))
                        .setMessage(getString(R.string.err_location_service)).setPositiveButton(
                            android.R.string.ok,
                            DialogInterface.OnClickListener { dialog: DialogInterface?, id: Int -> })
                    builder.create().show()
                    return@OnClickListener
                }
            } catch (ignored: Exception) {
            }
            startActivity(Intent(this@ProfileActivity, RouteRecordActivity::class.java))
            finish()
        })
    }

    private fun gotoAchievement(data: UserInfoAchievement) {
        Intent(this@ProfileActivity, AchievementActivity::class.java).apply {
            with(data) {
                putExtra("reg", registrationFlg)
                putExtra("route1", routeCreate1Flg)
                putExtra("route2", routeCreate2Flg)
                putExtra("route3", routeCreate3Flg)
                putExtra("subs1", subscription1Flg)
                putExtra("subs2", subscription2Flg)
                putExtra("subs3", subscription3Flg)
                putExtra("km1", km1Flg)
                putExtra("km2", km2Flg)
                putExtra("km3", km3Flg)
                putExtra("event1", event1Flg)
                putExtra("event2", event2Flg)
                putExtra("event3", event3Flg)
            }
            startActivity(this)
        }
    }

    private fun showUI(data: UserInfo) {
        with(binding) {
            textUserName.text =
                if (data.firstName.isNullOrEmpty()) data.nickName else data.firstName

            textUserNickName.text = String.format(
                getString(R.string.dynamic_user_nick_name), data.nickName
            )

            profileCountSubscriptions.text = String.format(
                getString(R.string.dynamic_count_subscriptionsD), data.countSubscriptions ?: 0
            )
            profileCountSubscribers.text = String.format(
                getString(R.string.dynamic_count_subscribersD), data.countSubscribers ?: 0
            )

            profileCountRoute.text = String.format(
                getString(R.string.dynamic_count_routeD),
                data.countMyRoute ?: 0,
                data.countFinishRoute ?: 0
            )

            profileCountCompetition.text = String.format(
                getString(R.string.dynamic_count_competitionD),
                data.countMyCompetition ?: 0,
                data.countFinishCompetition ?: 0
            )

            textPilotMoto.text = String.format(
                getString(R.string.dynamic_pilot_moto),
                "${data.motoBrand ?: ""} ${data.motoModel ?: ""}"
            )
            layoutPilot.isVisible = "${data.motoBrand ?: ""}${data.motoModel ?: ""}".isNotEmpty()

            textUserAbout.text =
                if (data.about.isNullOrEmpty()) getString(R.string.lbl_about_default) else data.about

            userVerifiedImg.isVisible = data.verifiedFlg == true

            //Обработка ачивок
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
        }
        //Обработка плашки дистанции
        fillDistanceLayout(data.distance)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        //Переход назад по кнопке в верхнем меню
        if (itemId == android.R.id.home) {
            finish()
            setResult(999)
            return true
        } else if (itemId == R.id.action_edit_profile) {
            viewModel.sendEvent(ProfileEvent.Ui.GotoEditProfile)
            return true
        } else if (itemId == R.id.action_logout) {
            authApi!!.signout().enqueue(object : Callback<MessageResponse?> {
                override fun onResponse(
                    call: Call<MessageResponse?>, response: Response<MessageResponse?>
                ) {
                    App.clearLocalStorage(this@ProfileActivity)
                    runOnUiThread(Runnable {
                        val loginIntent = Intent(this@ProfileActivity, LoginActivity::class.java)
                        loginIntent.putExtra("auth", "false")
                        finish()
                        startActivity(loginIntent)
                    })
                }

                override fun onFailure(call: Call<MessageResponse?>, throwable: Throwable) {
                }
            })
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_CODE) {
                data?.data?.path?.let {
                    viewModel.sendEvent(ProfileEvent.Ui.AddPhoto(it))
                }
            }
        }
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

    private fun fillDistanceLayout(_distance: Double?) {
        var distance = 0
        if (_distance != null) distance = _distance.toInt()
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
}