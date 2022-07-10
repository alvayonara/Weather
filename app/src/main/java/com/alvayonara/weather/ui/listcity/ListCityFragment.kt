package com.alvayonara.weather.ui.listcity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvayonara.common.extension.getThrowable
import com.alvayonara.common.extension.gone
import com.alvayonara.common.extension.showErrorSnackbar
import com.alvayonara.common.extension.visible
import com.alvayonara.navigation.NavigationCommand
import com.alvayonara.weather.App
import com.alvayonara.weather.R
import com.alvayonara.weather.databinding.FragmentListCityBinding
import com.alvayonara.weather.ui.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class ListCityFragment : Fragment() {

    private var _binding: FragmentListCityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val _listCityViewModel: ListCityViewModel by viewModels { factory }

    @Inject
    lateinit var listCityAdapter: ListCityAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeViewModel()
    }

    private fun setupView() {
        binding.fabAddCity.setOnClickListener {

        }

        binding.rvListCity.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = listCityAdapter
        }

        listCityAdapter.onItemClick = {
            _listCityViewModel.navigate(
                ListCityFragmentDirections.actionListCityFragmentToDetailCityFragment(
                    it
                )
            )
        }
    }

    private fun subscribeViewModel() {
        _listCityViewModel.navigation.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { command ->
                when (command) {
                    is NavigationCommand.To -> findNavController().navigate(command.directions)
                    is NavigationCommand.Back -> findNavController().navigateUp()
                }
            }
        }

        _listCityViewModel.list.observe(viewLifecycleOwner) {
            Log.d("listdata:", it.toString())
            when (it) {
                is ListCityViewModel.ListCity.Loading -> binding.pbListCity.visible()
                is ListCityViewModel.ListCity.Success -> {
                    binding.pbListCity.gone()
                    listCityAdapter.setListCity(it.data)
                }
                is ListCityViewModel.ListCity.Empty -> {
                    binding.pbListCity.gone()
                    binding.sbListCity.showErrorSnackbar(getString(R.string.txt_empty))
                }
                is ListCityViewModel.ListCity.Failed -> {
                    binding.pbListCity.gone()
                    it.data.let { throwable ->
                        binding.sbListCity.showErrorSnackbar(
                            getString(
                                R.string.txt_error,
                                throwable.getThrowable()
                            )
                        )
                    }
                }
            }
        }
    }
}