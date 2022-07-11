package com.alvayonara.weather.ui.detailcity

import android.annotation.SuppressLint
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
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.navigation.NavigationCommand
import com.alvayonara.weather.App
import com.alvayonara.weather.R
import com.alvayonara.weather.databinding.FragmentDetailCityBinding
import com.alvayonara.weather.ui.InteractionViewModel
import com.alvayonara.weather.ui.ViewModelFactory
import javax.inject.Inject

class DetailCityFragment : Fragment() {

    private var _binding: FragmentDetailCityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val _detailCityViewModel: DetailCityViewModel by viewModels { factory }

    private val _interactionViewModel: InteractionViewModel by activityViewModels()
    private val args: DetailCityFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeViewModel()
    }

    private fun setupView() {
        binding.ivBack.setOnClickListener {
            _detailCityViewModel.navigateBack()
        }

        binding.ivEdit.setOnClickListener {
            args.let {
                _detailCityViewModel.navigate(DetailCityFragmentDirections.actionDetailCityFragmentToAddEditCityFragment(true, it.weather))
            }
        }

        binding.ivDelete.setOnClickListener {
            args.weather.let {
                _detailCityViewModel.delete(
                    it.latitude!!,
                    it.longitude!!
                )
            }
        }

        args.weather.let { _detailCityViewModel.getDetail(it) }
    }

    private fun subscribeViewModel() {
        _detailCityViewModel.navigation.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { command ->
                when (command) {
                    is NavigationCommand.To -> findNavController().navigate(command.directions)
                    is NavigationCommand.Back -> findNavController().navigateUp()
                }
            }
        }

        _detailCityViewModel.detail.observe(viewLifecycleOwner) {
            when (it) {
                is DetailCityViewModel.Detail.Loading -> binding.pbDetail.visible()
                is DetailCityViewModel.Detail.Success -> {
                    binding.pbDetail.gone()
                    setupDetailView(it.data)
                }
                is DetailCityViewModel.Detail.Failed -> {
                    binding.pbDetail.gone()
                    it.data.let { throwable ->
                        binding.sbDetail.showErrorSnackbar(
                            getString(
                                R.string.txt_error,
                                throwable.getThrowable()
                            )
                        )
                    }
                }
            }
        }

        _detailCityViewModel.delete.observe(viewLifecycleOwner) {
            when (it) {
                is DetailCityViewModel.Delete.Loading -> binding.pbDetail.visible()
                is DetailCityViewModel.Delete.Success -> {
                    binding.pbDetail.gone()
                    _interactionViewModel.setIsDeleted(true)
                    _detailCityViewModel.navigateBack()
                }
            }
        }

        _interactionViewModel.edit.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { edited ->
                binding.sbDetail.showErrorSnackbar(getString(R.string.txt_edited))
                _detailCityViewModel.getDetail(edited)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDetailView(weatherEntity: WeatherEntity) {
        binding.apply {
            tvLocation.text = weatherEntity.location
            tvWeather.text = weatherEntity.currentWeather
            tvLatitudeLongitude.text =
                "Latitude ${weatherEntity.latitude} - Longitude ${weatherEntity.longitude}"
            tvHumidity.text = "Humidity ${weatherEntity.humidity}"
            tvPressure.text = "Pressure ${weatherEntity.pressure}"
            tvTemperature.text = "Temperature ${weatherEntity.temperature}"
            tvWindSpeed.text = "Wind Speed ${weatherEntity.windSpeed}"
        }
    }
}