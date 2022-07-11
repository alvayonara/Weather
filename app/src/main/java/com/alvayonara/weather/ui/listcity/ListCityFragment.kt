package com.alvayonara.weather.ui.listcity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.callbacks.onShow
import com.alvayonara.common.extension.getThrowable
import com.alvayonara.common.extension.gone
import com.alvayonara.common.extension.showErrorSnackbar
import com.alvayonara.common.extension.visible
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.domain.model.Current
import com.alvayonara.navigation.NavigationCommand
import com.alvayonara.weather.App
import com.alvayonara.weather.R
import com.alvayonara.weather.databinding.FragmentListCityBinding
import com.alvayonara.weather.ui.InteractionViewModel
import com.alvayonara.weather.ui.ViewModelFactory
import com.alvayonara.weather.utils.Map.getAddressFromLatLong
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListCityFragment : Fragment() {

    private var _binding: FragmentListCityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val _listCityViewModel: ListCityViewModel by viewModels { factory }

    private val _interactionViewModel: InteractionViewModel by activityViewModels()

    @Inject
    lateinit var listCityAdapter: ListCityAdapter

    private lateinit var permissionListener: MultiplePermissionsListener
    private var _current: Current? = null

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
        setupLocation()
        setupView()
        subscribeViewModel()
    }

    private fun setupLocation() {
        requestPermissions(listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
            action = {
                initCurrentLocation()
            }, actionDeny = { showDenyPermission() })
    }

    @SuppressLint("MissingPermission")
    private fun initCurrentLocation() {
        val mLocationRequest = LocationRequest.create().apply {
            interval = 200
            fastestInterval = 200
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
        }

        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (data in locationResult.locations) {
                    lifecycleScope.launch {
                        val currentLatLng =
                            LatLng(data.latitude, data.longitude)
                        val location = getAddressFromLatLong(requireActivity(), currentLatLng)

                        _current = Current(
                            data.latitude.toString(),
                            data.longitude.toString(),
                            location.orEmpty()
                        )

                        _listCityViewModel.getAllWeather(_current!!)
                    }
                }
            }
        }

        LocationServices.getFusedLocationProviderClient(requireActivity())
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    private fun setupView() {
        binding.fabAddCity.setOnClickListener {
            _listCityViewModel.navigate(
                ListCityFragmentDirections.actionListCityFragmentToAddCityFragment(false, WeatherEntity())
            )
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

        _interactionViewModel.add.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isAdded ->
                if (isAdded) {
                    binding.sbListCity.showErrorSnackbar(getString(R.string.txt_added))
                    _listCityViewModel.getAllWeather(_current!!)
                }
            }
        }

        _interactionViewModel.delete.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isDeleted ->
                if (isDeleted) {
                    binding.sbListCity.showErrorSnackbar(getString(R.string.txt_deleted))
                    _listCityViewModel.getAllWeather(_current!!)
                }
            }
        }
    }

    private fun requestPermissions(
        permissions: List<String>,
        action: () -> Unit, actionDeny: () -> Unit
    ) {
        Dexter.withContext(requireActivity())
            .withPermissions(permissions)
            .withListener(getPermissionsListener(action, actionDeny))
            .check()
    }

    private fun getPermissionsListener(
        action: () -> Unit, actionDeny: () -> Unit
    ): MultiplePermissionsListener {
        if (!this::permissionListener.isInitialized) {
            permissionListener = object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let { rpt ->
                        if (rpt.areAllPermissionsGranted()) action()
                        else actionDeny()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }
        }
        return permissionListener
    }

    private fun showDenyPermission() {
        MaterialDialog(requireActivity()).show {
            title(R.string.request_permission)
            message(R.string.request_permission_1)
            positiveButton(R.string.request_permission_2) { dialog ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .also { intent ->
                        intent.data = Uri.parse("package:${requireActivity().packageName}")
                        startActivity(intent)
                    }
                dialog.dismiss()
            }
            negativeButton(R.string.request_permission_3) { dialog ->
                dialog.dismiss()
            }
            cancelable(false)
            onShow {
                it.getActionButton(WhichButton.POSITIVE).updateTextColor(Color.BLACK)
                it.getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.BLACK)
            }
        }
    }
}