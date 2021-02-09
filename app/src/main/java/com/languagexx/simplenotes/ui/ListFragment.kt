package com.languagexx.simplenotes.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.languagexx.simplenotes.R
import com.languagexx.simplenotes.persistence.Note
import com.languagexx.simplenotes.ui.adapter.NoteAdapter
import com.languagexx.simplenotes.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

class ListFragment : DaggerFragment(),
    NoteAdapter.Interaction {

    private lateinit var noteAdapter: NoteAdapter

    private lateinit var noteViewModel: NoteViewModel

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var allNotes: List<Note>



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.mymenu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.sort_by_date -> {
                allNotes = allNotes.sortedWith(compareBy { it.date })
                initRecyclerView()
                return true
            }
            R.id.sort_by_name -> {
                allNotes = allNotes.sortedWith(compareBy { it.title?.length })
                initRecyclerView()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allNotes = mutableListOf()
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()    // Step 1
        initRecyclerView()  // Step 2
        observerLiveData()  // Step 3
    }

    private fun observerLiveData() {
        noteViewModel.getAllNotes().observe(viewLifecycleOwner, Observer { lisOfNotes ->
            lisOfNotes?.let {
                allNotes = it
                noteAdapter.swap(it)
            }
        })
    }

    private fun initRecyclerView() {

        recyclerView.apply {
            noteAdapter = NoteAdapter(
                allNotes,
                this@ListFragment
            )
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = noteAdapter
            val swipe = ItemTouchHelper(initSwipeToDelete())
            swipe.attachToRecyclerView(recyclerView)
        }
    }

    private fun setupViewModel() {
        noteViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(NoteViewModel::class.java)
    }

    private fun initSwipeToDelete(): ItemTouchHelper.SimpleCallback {
        //Swipe recycler view items on RIGHT
        return object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                noteViewModel.delete(allNotes.get(position))
                val note = allNotes.get(position)
                Toast.makeText(activity, "Note Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(position: Int, item: Note) {
        val navDirection = ListFragmentDirections.actionListFragmentToEditFragment(item)
        findNavController().navigate(navDirection)
    }
}


