package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import ru.gilgamesh.abon.motot.R
import ru.gilgamesh.abon.motot.model.App
import ru.gilgamesh.abon.motot.model.CustomGlideApp
import ru.gilgamesh.abon.motot.ui.profile.ProfileActivity
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating.recyclerViewRatingBrand.RatingMotorcycleAdapter

@AndroidEntryPoint
class RatingMotorcycleFragment : Fragment() {

    companion object {
        fun newInstance() = RatingMotorcycleFragment()
        const val ADAPTER_ITEM_REFRESH = 5
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
        if (App.contactInfo != null) {
            if (App.contactInfo.miniAvatarId != null) {
                CustomGlideApp.getContactImageByIdByte(
                    avatar.context, App.contactInfo.miniAvatarId, avatar
                )
            } else {
                avatar.setImageResource(CustomGlideApp.getDefaultAvatarBySexResId(App.contactInfo.sex))
            }
        }

        avatar.setOnClickListener {
            if (App.checkGuestRolePopup(
                    context,
                    parentFragmentManager
                )
            ) return@setOnClickListener

            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        val nickname: TextView = view.findViewById(R.id.nickname)
        val motorcycle: TextView = view.findViewById(R.id.motorcycle)
        if (App.contactInfo != null) {
            nickname.text = App.contactInfo.nickName
            if (App.contactInfo.motoBrand != null && App.contactInfo.motoBrand.isNotEmpty()) {
                if (App.contactInfo.motoModel != null && App.contactInfo.motoModel.isNotEmpty()) {
                    motorcycle.text = "${App.contactInfo.motoBrand} ${App.contactInfo.motoModel}"
                } else {
                    motorcycle.text = App.contactInfo.motoBrand
                }
            } else {
                motorcycle.text = getString(R.string.lbl_rating_3)
            }
        } else {
            nickname.text = ""
            motorcycle.text = ""
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.ratingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        //viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val adapter = RatingMotorcycleAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (layoutManager != null &&
                    layoutManager.findLastCompletelyVisibleItemPosition() >= adapter.itemCount - ADAPTER_ITEM_REFRESH
                ) {
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

        val chipBrand: Chip = view.findViewById(R.id.chip1)
        chipBrand.setOnClickListener {
            adapter.clearItems()
            viewModel.setModeBrand()
            viewModel.getFirstPage()
        }

        val chip2025: Chip = view.findViewById(R.id.chip2)
        chip2025.setOnClickListener {
            adapter.clearItems()
            viewModel.setModeBrandModel()
            viewModel.getFirstPage()
        }
    }

    override fun onDestroyView() {
        viewModel.clear()
        super.onDestroyView()
    }
}