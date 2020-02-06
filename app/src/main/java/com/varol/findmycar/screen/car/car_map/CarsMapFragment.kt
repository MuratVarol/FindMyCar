package com.varol.findmycar.screen.car.car_map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.varol.findmycar.R
import com.varol.findmycar.base.BaseFragment
import com.varol.findmycar.databinding.FragmentCarsMapBinding
import com.varol.findmycar.internal.extension.notifyDataChange
import com.varol.findmycar.internal.extension.observeNonNull
import com.varol.findmycar.internal.extension.showPopup
import com.varol.findmycar.internal.extension.toLatLng
import com.varol.findmycar.internal.popup.PopupCallback
import com.varol.findmycar.internal.popup.PopupUiModel
import com.varol.findmycar.screen.car.CarsViewModel
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.RequestExecutor
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val LOCATION_REQUEST_CODE = 1001

class CarsMapFragment : BaseFragment<CarsViewModel, FragmentCarsMapBinding>(CarsViewModel::class),
    GoogleMap.OnMarkerClickListener,
    OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    override val getLayoutId: Int
        get() = R.layout.fragment_cars_map

    override val viewModel: CarsViewModel by sharedViewModel(from = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_main)
    })

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var markers: List<PlaceViewEntity>

    companion object {
        private const val DURATION_MAP_ANIMATION = 1200
        private const val ZOOM_LEVEL_MAP_ANIMATION = 8.5f
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun initialize() {
        observeCarList()
        binding.cvLocation.setOnClickListener {
            checkLocationPermissionAndFetchData(true)
        }
        binding.cvBounds.setOnClickListener {
            notifyCarList()
        }
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_car_map) as SupportMapFragment

        OnMapAndViewReadyListener(mapFragment, this)
    }

    private fun observeCarList() {
        viewModel.carList.observeNonNull(this) { carList ->
            markers = carList.map {
                PlaceViewEntity(
                    it.plateNumber,
                    it.location,
                    it.modelName
                )
            }
            checkMapReadyThen {
                fillMarkers(markers)
            }
        }
    }

    private fun notifyCarList() {
        viewModel.carList.notifyDataChange()
    }

    private fun fillMarkers(markers: List<PlaceViewEntity>) {
        val boundsBuilder = LatLngBounds.Builder()
        markers.map { place -> boundsBuilder.include(place.position) }
        val bounds = boundsBuilder.build()
        with(map) {
            clear()
            uiSettings.run {
                isMyLocationButtonEnabled = true
            }
            setOnMarkerClickListener(this@CarsMapFragment)
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50), DURATION_MAP_ANIMATION, null)
        }
        addMarkersToMap(map, markers)
    }

    private fun addMarkersToMap(map: GoogleMap, places: List<PlaceViewEntity>) {
        val icon =
            BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi_marker)

        places.map {
            with(it) {
                map.addMarker(
                    com.google.android.gms.maps.model.MarkerOptions()
                        .position(position)
                        .title(title)
                        .snippet(tag)
                        .icon(icon)
                        .draggable(false)
                )
            }
        }
    }

    private fun checkLocationPermissionAndFetchData(forceUpdate: Boolean = false) {
        //TODO: this is not best approach but works for now, fix it with better approach later
        // we have the data, so we have loc.
        // no need to set loc listener and fetch data on return of list fragment
        if (viewModel.carList.value != null && !forceUpdate)
            return

        AndPermission.with(this)
            .runtime()
            .permission(PERMISSIONS)
            .rationale { _, _, executor ->
                showNeedLocationPermissionDialog(executor)
            }
            .onGranted {
                setLocationListenerIfGpsEnabled()
            }
            .onDenied {
                showEnablePermissionInSettingsDialog()
            }.start()
    }

    private fun setLocationListenerIfGpsEnabled() {
        if (isLocationEnabled()) {
            setLocationListener()
        } else {
            showEnableGPSDialog()
        }
    }

    private fun setLocationListener() {
        activity?.let { activity ->
            viewModel.showLocationWaitingProgress()
            fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(activity).apply {
                    lastLocation.addOnSuccessListener { location: Location? ->
                        viewModel.hideLocationWaitingProgress()
                        map.isMyLocationEnabled = true
                        location?.let {
                            checkMapReadyThen {
                                zoomToLatLng(it.toLatLng())
                            }
                        } ?: run {
                            viewModel.showErrorBar(context?.getString(R.string.error_location_can_not_be_gathered).toString())
                        }
                    }
                }
        } ?: kotlin.run {
            viewModel.showErrorBar(context?.getString(R.string.error_wtf_error).toString())
        }
    }

    private fun zoomToLatLng(latLng: LatLng) {
        val center = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL_MAP_ANIMATION)
        map.animateCamera(center, DURATION_MAP_ANIMATION, null)
    }

    private fun showNeedLocationPermissionDialog(executor: RequestExecutor) {
        showPopup(
            PopupUiModel(
                message = context?.getString(R.string.need_location_permission),
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    executor.execute()
                }

                override fun onCancelClick() {
                    executor.cancel()
                }
            }
        )
    }

    private fun showEnableGPSDialog() {
        showPopup(
            PopupUiModel(
                message = context?.getString(R.string.please_enable_gps),
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        LOCATION_REQUEST_CODE
                    )
                }

                override fun onCancelClick() {
                    viewModel.showInformBar(context?.getString(R.string.no_option_left_to_enable_gps).toString())
                }
            }
        )
    }

    private fun showEnablePermissionInSettingsDialog() {
        showPopup(
            PopupUiModel(
                message = context?.getString(R.string.please_enable_gps),
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    startInstalledAppDetailsActivity()
                }

                override fun onCancelClick() {
                    showUnableToInitLocationManagerDialog()
                }
            }
        )
    }

    private fun showUnableToInitLocationManagerDialog() {
        showPopup(
            PopupUiModel(
                message = context?.getString(R.string.do_you_want_to_locate_sydney),
                addCancelButton = true
            ),
            object : PopupCallback {
                override fun onConfirmClick() {
                    zoomToLatLng(LatLng(-33.8568022, 151.2143847))
                }

                override fun onCancelClick() {
                    viewModel.showErrorBar(context?.getString(R.string.is_device_has_gps).toString())
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // Android Pie returns RESULT_CANCELLED even user enable GPS, no need to check for resultCode
            setLocationListener()
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
    }

    private fun checkMapReadyThen(stuffToDo: () -> Unit) {
        if (!::map.isInitialized /*|| !::places.isInitialized || places.isNullOrEmpty()*/) {
            println("map is not ready")
        } else {
            stuffToDo()
        }
    }

}


private class OnMapAndViewReadyListener(
    private val mapFragment: SupportMapFragment,
    private val toBeNotified: OnGlobalLayoutAndMapReadyListener
) : ViewTreeObserver.OnGlobalLayoutListener,
    OnMapReadyCallback {

    private val mapView: View? = mapFragment.view

    private var isViewReady = false
    private var isMapReady = false
    private var map: GoogleMap? = null

    interface OnGlobalLayoutAndMapReadyListener {
        fun onMapReady(googleMap: GoogleMap?)
    }

    init {
        registerListeners()
    }

    private fun registerListeners() {
        if (mapView?.width != 0 && mapView?.height != 0) {
            isViewReady = true
        } else {
            mapView.viewTreeObserver.addOnGlobalLayoutListener(this)
        }

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        isMapReady = true
        fireCallbackIfReady()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    override fun onGlobalLayout() {
        mapView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        isViewReady = true
        fireCallbackIfReady()
    }

    private fun fireCallbackIfReady() {
        if (isViewReady && isMapReady) {
            toBeNotified.onMapReady(map)
        }
    }
}