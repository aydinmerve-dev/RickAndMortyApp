package com.devlab.rickandmortyapp.ui.feed

import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlab.rickandmortyapp.R
import com.devlab.rickandmortyapp.base.BaseActivity
import com.devlab.rickandmortyapp.databinding.ActivityCharacterFeedBinding
import com.devlab.rickandmortyapp.domain.models.CharacterModel
import com.devlab.rickandmortyapp.ui.detail.CharacterDetailActivity
import com.devlab.rickandmortyapp.util.RecyclerScrollListener
import com.devlab.rickandmortyapp.util.enums.ListMode
import com.devlab.rickandmortyapp.util.extensions.hideKeyboard
import com.devlab.rickandmortyapp.util.extensions.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterFeedActivity : BaseActivity<ActivityCharacterFeedBinding, CharacterFeedVM>() {

    private var isAdapterSet = false

    lateinit var adapter: CharacterAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_character_feed
    }

    override fun initViews() {
        super.initViews()

        adapter = object : CharacterAdapter() {
            override fun onClickedItem(item: CharacterModel) {
                viewModel.startActivity(
                    CharacterDetailActivity::class,
                    bundleOf(Pair("characterId", item.characterId))
                )
            }

            override fun onClickedFavorite(item: CharacterModel) {
                viewModel.onClickedFavorite(item)
            }
        }

        vi.rvCharacters.layoutManager = LinearLayoutManager(this)
        vi.rvCharacters.adapter = adapter

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.character_status_entries,
            R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        vi.spinnerCharacterStatus.adapter = adapter

        val searchPlate =
            vi.searchView.findViewById(
                vi.searchView.resources.getIdentifier(
                    "android:id/search_plate",
                    null,
                    null
                )
            ) as LinearLayout
        searchPlate.background = null

        val id = vi.searchView.context.resources.getIdentifier(
            "android:id/search_src_text", null,
            null
        );
        val textView: TextView = vi.searchView.findViewById(id)
        textView.setTextAppearance(this, R.style.MediumTextStyle)

        vi.searchView.onActionViewExpanded()
        vi.searchView.isIconifiedByDefault = false
        vi.searchView.queryHint = "Search name.."

        if (!vi.searchView.isFocused) {
            vi.searchView.clearFocus()
        }
    }

    override fun setListeners() {
        super.setListeners()

        vi.swipeRefresh.setOnRefreshListener { viewModel.onClickedRefresh() }

        vi.rvCharacters.addOnScrollListener(object : RecyclerScrollListener(vi.rvCharacters.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return viewModel.isLastPage()
            }

            override fun loadMoreItems() {
                viewModel.loadMoreItems()
            }

            override fun isLoading(): Boolean {
                return viewModel.isCharactersLoading()
            }
        })

        vi.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery = newText
                if(newText.isNullOrEmpty())
                    viewModel.searchCharacters()
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchCharacters()
                hideKeyboard()
                return true
            }
        })

        vi.spinnerCharacterStatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        viewModel.searchStatus = vi.spinnerCharacterStatus.selectedItem as String
                    }else{
                        viewModel.searchStatus = null
                    }

                    viewModel.searchCharacters()
                }
            }
    }

    override fun setReceivers() {
        super.setReceivers()

        observe(viewModel.characters) {
            if (!isAdapterSet) {
                isAdapterSet = true

                vi.rvCharacters.adapter = adapter.apply {
                    submitList(it)
                }
            } else {
                vi.rvCharacters.adapter?.notifyDataSetChanged()
            }
        }

        observe(viewModel.listMode) {
            if(it == ListMode.GRID){
                vi.rvCharacters.layoutManager = GridLayoutManager(this, 2)
            }else{
                vi.rvCharacters.layoutManager = LinearLayoutManager(this)
            }
        }
    }

}