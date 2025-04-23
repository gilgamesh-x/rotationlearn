package ru.gilgamesh.abon.ratingbrand.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.core.glide.CustomGlideMethod
import ru.gilgamesh.abon.ratingbrand.R
import ru.gilgamesh.abon.ratingbrand.domain.RatingMotorcycleFeatureContract
import ru.gilgamesh.abon.ratingbrand.presentation.recyclerViewRatingBrand.RatingMotorcycleAdapter
import ru.gilgamesh.abon.userprofile.domain.repository.UserRepository
import javax.inject.Inject

@AndroidEntryPoint
class RatingMotorcycleFragment : Fragment() {
    @Inject
    internal lateinit var userRepository: UserRepository

    private var contract: RatingMotorcycleFeatureContract? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RatingMotorcycleFeatureContract) {
            contract = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        contract = null
    }

    private val viewModel: RatingMotorcycleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rating_motorcycle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val avatar: ImageView = view.findViewById(R.id.img_profile_avatar)
        val nickname: TextView = view.findViewById(R.id.nickname)
        val motorcycle: TextView = view.findViewById(R.id.motorcycle)

        lifecycleScope.launch {
            userRepository.getUserInfo().collect {
                it.miniAvatarId?.let { imgId ->
                    CustomGlideMethod.getContactImageByIdByte(
                        avatar.context, imgId, avatar
                    )
                } ?: run {
                    avatar.setImageResource(CustomGlideMethod.getDefaultAvatarBySexResId(it.sex))
                }
                nickname.text = it.nickName

                it.motoBrand?.let { brand ->
                    it.motoModel?.let { model ->
                        motorcycle.text = "${brand} ${model}"
                    } ?: run {
                        motorcycle.text = brand
                    }
                } ?: run {
                    nickname.text = ""
                    motorcycle.text = ""
                }
            }
        }

        avatar.setOnClickListener {
            contract?.openProfile()
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.ratingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = RatingMotorcycleAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() >= adapter.itemCount - ADAPTER_ITEM_REFRESH) {
                    viewModel.getNextPage()
                }
            }
        })

        viewModel.items.observe(viewLifecycleOwner) { items ->
            items?.let {
                adapter.addItems(it)
            }
        }

        viewModel.getFirstPage()

        val chipGroup: ChipGroup = view.findViewById(R.id.chipGroup)
        chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.first()) {
                R.id.chip1 -> {
                    adapter.clearItems()
                    viewModel.setModeBrand()
                    viewModel.getFirstPage()
                }

                R.id.chip2 -> {
                    adapter.clearItems()
                    viewModel.setModeBrandModel()
                    viewModel.getFirstPage()
                }
            }
        }
    }

    override fun onDestroyView() {
        viewModel.clear()
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = RatingMotorcycleFragment()
        const val ADAPTER_ITEM_REFRESH = 5
    }
}