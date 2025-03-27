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
import androidx.core.view.isVisible
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
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingDistanceListAdapter
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem

@AndroidEntryPoint
class RatingDistanceFragment : Fragment(), RatingDistanceListAdapter.OnItemClickListener {
    private var _binding: FragmentRatingDistanceBinding? = null
    private val binding get() = _binding!!
    private var adapter: RatingDistanceListAdapter? = null

    companion object {
        fun newInstance() = RatingDistanceFragment()
        private const val ADAPTER_ITEM_REFRESH = 5
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

        binding.imgProfileAvatar.setOnClickListener {
            gotoProfile()
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

        adapter = RatingDistanceListAdapter(this)
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
                viewModel.uiStateProfile.collect { state ->
                    updateUIProfile(state)
                }
            }
        }
        viewModel.handleIntent(RatingDistanceIntent.LoadProfile)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUI(state)
                }
            }
        }
        viewModel.handleIntent(RatingDistanceIntent.LoadFirstPageCurrentYear)

        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.first() == binding.chip2024.id) {
                adapter?.clearItems()
                viewModel.handleIntent(RatingDistanceIntent.LoadFirstPageByYear(2024))
            } else if (checkedIds.first() == binding.chip2025.id) {
                adapter?.clearItems()
                viewModel.handleIntent(RatingDistanceIntent.LoadFirstPageCurrentYear)
            }
        }
    }

    private fun updateUI(state: RatingDistanceState) {
        when (state.contentState) {

            is RatingDistanceLCEState.Content -> {
                binding.progressBar.isVisible = false
                state.contentState.data?.let {
                    if (it.isNotEmpty()) {
                        adapter?.addItems(it)
                    }
                }
            }

            is RatingDistanceLCEState.Error -> {
                binding.progressBar.isVisible = false
                Log.e("RatingDistanceFragment", "Error: " + state.contentState.message)
                Toast.makeText(requireContext(), getString(R.string.httpall), Toast.LENGTH_SHORT)
                    .show()
            }

            is RatingDistanceLCEState.Loading -> {
                binding.progressBar.isVisible = true
            }

            null -> {}
        }
    }

    private fun updateUIProfile(state: RatingDistanceStateProfile) {
        when (state.contentState) {
            RatingDistanceLCEStateProfile.Content -> {
                if (state.miniAvatarId != null) {
                    CustomGlideApp.getContactImageByIdByte(
                        binding.imgProfileAvatar.context,
                        state.miniAvatarId,
                        binding.imgProfileAvatar
                    )
                } else {
                    binding.imgProfileAvatar.setImageResource(
                        CustomGlideApp.getDefaultAvatarBySexResId(state.sex)
                    )
                }
                binding.nickname.text = state.nickName
                binding.motorcycle.text = state.motorcycle
                binding.distanceTextView.text = String.format(
                    getString(R.string.dynamic_profile_distance_1), state.distance
                )
            }

            is RatingDistanceLCEStateProfile.Error -> {}
            RatingDistanceLCEStateProfile.Loading -> {}
            null -> {}
        }
    }

    private fun gotoProfile() {
        if (App.checkGuestRolePopup(
                context, parentFragmentManager
            )
        ) return
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun gotoEnemyProfile(contactId: Long) {
        if (App.checkGuestRolePopup(
                context, parentFragmentManager
            )
        ) return
        val intent = Intent(
            this.context, EnemyProfileActivity::class.java
        )
        intent.putExtra("prevActivity", "RatingDistanceFragment")
        intent.putExtra("contactId", contactId)
        startActivity(intent)
    }

    override fun onItemClick(item: RatingItem) {
        if (App.contactEqual(item.contactId)) {
            gotoProfile()
        } else {
            gotoEnemyProfile(item.contactId)
        }
    }
}