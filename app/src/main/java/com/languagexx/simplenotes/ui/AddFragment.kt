package com.languagexx.simplenotes.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.languagexx.simplenotes.R
import com.languagexx.simplenotes.persistence.Note
import com.languagexx.simplenotes.util.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add.*
import java.time.LocalDateTime
import javax.inject.Inject


class AddFragment : DaggerFragment() {

    @Inject
    lateinit var viewmodelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel


    // Method #1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    // Method #2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        btnAdd.setOnClickListener {
            Navigation.findNavController(requireActivity(),R.id.container).popBackStack()
        }
    }

    // Method #3
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveNoteToDatabase() {

        (activity as MainActivity?)?.showFloatingButton()

        if (validations()) {
            Toast.makeText(activity, "Note is saved", Toast.LENGTH_SHORT).show()
            saveNote()
        } else
            Toast.makeText(activity, "Note is Discarded", Toast.LENGTH_SHORT).show()

    }

    // Method #4
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroyView() {
        super.onDestroyView()
        saveNoteToDatabase()
    }


    // Method #5
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveNote() {
        val note = Note(0,addTitle.text.toString(),addDescription.text.toString(), LocalDateTime.now().second)

        //If title is null set Empty Title
        if (addTitle.text.isNullOrEmpty()) {
           note.title = "Empty Title"

            //Call viewmodel to save the data
            noteViewModel.insert(note)

        }else{
            //Call viewmodel to save the data
            noteViewModel.insert(note)
        }
    }

    // Method #6
    fun validations(): Boolean {
        return !(addTitle.text.isNullOrEmpty()
                && addDescription.text.isNullOrEmpty())
    }


    // Method #7
    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(this,viewmodelProviderFactory).get(NoteViewModel::class.java)
    }
}