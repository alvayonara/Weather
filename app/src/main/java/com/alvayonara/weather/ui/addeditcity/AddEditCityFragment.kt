package com.alvayonara.weather.ui.addeditcity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alvayonara.navigation.NavigationCommand
import com.alvayonara.weather.App
import com.alvayonara.weather.databinding.FragmentAddEditCityBinding
import com.alvayonara.weather.ui.ViewModelFactory
import javax.inject.Inject

class AddEditCityFragment : Fragment() {

    private var _binding: FragmentAddEditCityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val _addEditCityViewModel: AddEditCityViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeViewModel()
    }

    private fun setupView() {

    }

    private fun subscribeViewModel() {
        _addEditCityViewModel.navigation.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { command ->
                when (command) {
                    is NavigationCommand.To -> findNavController().navigate(command.directions)
                    is NavigationCommand.Back -> findNavController().navigateUp()
                }
            }
        }
    }
}