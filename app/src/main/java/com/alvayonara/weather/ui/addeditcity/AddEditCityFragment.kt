package com.alvayonara.weather.ui.addeditcity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alvayonara.common.extension.getThrowable
import com.alvayonara.common.extension.gone
import com.alvayonara.common.extension.showErrorSnackbar
import com.alvayonara.common.extension.visible
import com.alvayonara.core.domain.model.Current
import com.alvayonara.navigation.NavigationCommand
import com.alvayonara.weather.App
import com.alvayonara.weather.R
import com.alvayonara.weather.databinding.FragmentAddEditCityBinding
import com.alvayonara.weather.ui.InteractionViewModel
import com.alvayonara.weather.ui.ViewModelFactory
import com.alvayonara.weather.utils.Field.hideKeyboard
import com.alvayonara.weather.utils.Field.observeField
import javax.inject.Inject

class AddEditCityFragment : Fragment() {

    private var _binding: FragmentAddEditCityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val _addEditCityViewModel: AddEditCityViewModel by viewModels { factory }

    private val _interactionViewModel: InteractionViewModel by activityViewModels()
    private val args: AddEditCityFragmentArgs by navArgs()

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
        _addEditCityViewModel.checkButtonState(
            listOf(
                observeField(binding.edtLocation),
                observeField(binding.edtLatitude),
                observeField(binding.edtLongitude)
            )
        )

        binding.ivBack.setOnClickListener {
            _addEditCityViewModel.navigateBack()
        }

        args.let { arguments ->
            binding.apply {
                tvAddEditWeather.text =
                    if (arguments.isEdit) getString(R.string.txt_edit_weather) else getString(R.string.txt_add_weather)

                arguments.weather.let {
                    edtLocation.setText(it.location)
                    edtLatitude.setText(it.latitude)
                    edtLongitude.setText(it.longitude)
                }

                btnAddEditWeather.setOnClickListener {
                    hideKeyboard(requireActivity())
                    if (arguments.isEdit) {
                        arguments.weather.let { weatherEntity ->
                            weatherEntity.copy(
                                location = binding.edtLocation.text.toString(),
                                latitude = binding.edtLatitude.text.toString(),
                                longitude = binding.edtLongitude.text.toString()
                            ).let { weather ->
                                _addEditCityViewModel.edit(weather)
                            }
                        }
                    } else {
                        _addEditCityViewModel.add(
                            Current(
                                location = binding.edtLocation.text.toString(),
                                latitude = binding.edtLatitude.text.toString(),
                                longitude = binding.edtLongitude.text.toString()
                            )
                        )
                    }
                }
            }
        }
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

        _addEditCityViewModel.isValid.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isValid ->
                binding.btnAddEditWeather.isEnabled = isValid
            }
        }

        _addEditCityViewModel.add.observe(viewLifecycleOwner) {
            when (it) {
                is AddEditCityViewModel.Add.Loading -> binding.pbAddEdit.visible()
                is AddEditCityViewModel.Add.Success -> {
                    binding.pbAddEdit.gone()
                    _interactionViewModel.setIsAdded(true)
                    _addEditCityViewModel.navigateBack()
                }
                is AddEditCityViewModel.Add.LocationExist -> {
                    binding.pbAddEdit.gone()
                    binding.sbAddEdit.showErrorSnackbar(getString(R.string.txt_location_exist))
                }
                is AddEditCityViewModel.Add.Failed -> {
                    binding.pbAddEdit.gone()
                    it.data.let { throwable ->
                        binding.sbAddEdit.showErrorSnackbar(
                            getString(
                                R.string.txt_error,
                                throwable.getThrowable()
                            )
                        )
                    }
                }
            }
        }

        _addEditCityViewModel.edit.observe(viewLifecycleOwner) {
            when (it) {
                is AddEditCityViewModel.Edit.Loading -> binding.pbAddEdit.visible()
                is AddEditCityViewModel.Edit.Success -> {
                    binding.pbAddEdit.gone()
                    args.weather.let { weatherEntity ->
                        weatherEntity.copy(
                            location = binding.edtLocation.text.toString(),
                            latitude = binding.edtLatitude.text.toString(),
                            longitude = binding.edtLongitude.text.toString()
                        ).let { weather ->
                            _interactionViewModel.setIsEdited(weather)
                        }
                    }
                    _addEditCityViewModel.navigateBack()
                }
                is AddEditCityViewModel.Edit.Failed -> {
                    binding.pbAddEdit.gone()
                    it.data.let { throwable ->
                        binding.sbAddEdit.showErrorSnackbar(
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