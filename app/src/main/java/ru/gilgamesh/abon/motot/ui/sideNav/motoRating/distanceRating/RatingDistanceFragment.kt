package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.R
import ru.gilgamesh.abon.motot.databinding.FragmentRatingDistanceBinding
import ru.gilgamesh.abon.motot.model.App
import ru.gilgamesh.abon.motot.model.CustomGlideApp
import ru.gilgamesh.abon.motot.ui.profile.EnemyProfileActivity
import ru.gilgamesh.abon.motot.ui.profile.ProfileActivity
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating.RatingMotorcycleFragment
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingAdapter

@AndroidEntryPoint
class RatingDistanceFragment : Fragment() {
    private var _binding: FragmentRatingDistanceBinding? = null
    private val binding get() = _binding!!
    private var adapter: RatingAdapter? = null

    companion object {
        fun newInstance() = RatingDistanceFragment()
        const val ADAPTER_ITEM_REFRESH = 5
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }

    private val viewModel: RatingDistanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingDistanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (App.contactInfo != null) {
            if (App.contactInfo.miniAvatarId != null) {
                CustomGlideApp.getContactImageByIdByte(
                    binding.imgProfileAvatar.context,
                    App.contactInfo.miniAvatarId,
                    binding.imgProfileAvatar
                )
            } else {
                binding.imgProfileAvatar.setImageResource(
                    CustomGlideApp.getDefaultAvatarBySexResId(App.contactInfo.sex)
                )
            }
        }

        binding.imgProfileAvatar.setOnClickListener {
            if (App.checkGuestRolePopup(
                    context, parentFragmentManager
                )
            ) return@setOnClickListener
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        if (App.contactInfo != null) {
            binding.nickname.text = App.contactInfo.nickName
            if (App.contactInfo.motoBrand != null && App.contactInfo.motoBrand.isNotEmpty()) {
                if (App.contactInfo.motoModel != null && App.contactInfo.motoModel.isNotEmpty()) {
                    binding.motorcycle.text =
                        "${App.contactInfo.motoBrand} ${App.contactInfo.motoModel}"
                } else {
                    binding.motorcycle.text = App.contactInfo.motoBrand
                }
            } else {
                binding.motorcycle.text = ""
            }
        } else {
            binding.nickname.text = ""
            binding.motorcycle.text = ""
        }

        if (App.contactInfo == null || App.contactInfo.distance == null) {
            binding.distanceTextView.text =
                String.format(getString(R.string.dynamic_profile_distance_1), 0)
        } else {
            binding.distanceTextView.text = String.format(
                getString(R.string.dynamic_profile_distance_1), App.contactInfo.distance.toInt()
            )
        }

        val shader: Shader = LinearGradient(
            0f,
            binding.distanceTextView.textSize,
            binding.distanceTextView.paint.measureText(binding.distanceTextView.text as String),
            binding.distanceTextView.textSize,
            "#FF7919".toColorInt(),
            "#FFCF53".toColorInt(),
            Shader.TileMode.REPEAT
        )
        binding.distanceTextView.paint.setShader(shader)

        val recyclerView: RecyclerView = view.findViewById(R.id.ratingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        //viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        adapter =
            RatingAdapter(
                mutableListOf(),
                RatingAdapter.OnItemClickListener { _view, position ->
                    val contactId: Long? = adapter?.getContactId(position)
                    if (contactId != null) {
                        if (contactId > 0) {
                            if (App.checkGuestRolePopup(
                                    context, parentFragmentManager
                                )
                            ) return@OnItemClickListener

                            if (App.contactEqual(contactId)) {
                                startActivity(Intent(this.context, ProfileActivity::class.java))
                            } else {
                                val intent = Intent(
                                    this.context, EnemyProfileActivity::class.java
                                )
                                intent.putExtra("prevActivity", "RatingDistanceFragment")
                                intent.putExtra("contactId", contactId)
                                startActivity(intent)
                            }
                        }
                    }
                })
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() >= (adapter?.itemCount
                        ?: 0) - RatingMotorcycleFragment.ADAPTER_ITEM_REFRESH
                ) {
                    viewModel.handleIntent(RatingDistanceIntent.LoadNextPage)
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUI(state)
                }
            }
        }

        viewModel.handleIntent(RatingDistanceIntent.LoadFirstPageCurrentYear)

        binding.chip2024.setOnClickListener {
            adapter?.clearItems()
            viewModel.handleIntent(RatingDistanceIntent.LoadFirstPageByYear(2024))
        }

        binding.chip2025.setOnClickListener {
            adapter?.clearItems()
            viewModel.handleIntent(RatingDistanceIntent.LoadFirstPageCurrentYear)
        }
    }

    private fun updateUI(state: RatingDistanceState) {
        if (state.loading) {
            Log.d("RatingDistanceFragment", "updateUI: state.loading")
        }
        if (state.error.isNotEmpty()) {
            Log.e("RatingDistanceFragment", "updateUI: " + state.error.toString())
            Toast.makeText(requireContext(), getString(R.string.httpall), Toast.LENGTH_SHORT).show()
        }

        state.items?.let {
            if (it.isNotEmpty()) {
                adapter?.addItems(state.items)
            }
        }
    }
}