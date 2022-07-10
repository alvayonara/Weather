package com.alvayonara.weather.ui.listcity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.weather.R
import com.alvayonara.weather.databinding.ItemListCityBinding

class ListCityAdapter : RecyclerView.Adapter<ListCityAdapter.ListCityViewHolder>() {

    private var _items: MutableList<WeatherEntity> = ArrayList()
    var onItemClick: ((WeatherEntity) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListCity(listCity: List<WeatherEntity>?) {
        if (listCity == null) return
        _items.clear()
        _items.addAll(listCity)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCityViewHolder =
        ListCityViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_city, parent, false)
        )


    override fun onBindViewHolder(holder: ListCityViewHolder, position: Int) =
        holder.bindItem(_items[position])

    override fun getItemCount(): Int = _items.size

    inner class ListCityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListCityBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bindItem(weatherEntity: WeatherEntity) {
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

            itemView.setOnClickListener {
                onItemClick?.invoke(_items[adapterPosition])
            }
        }
    }
}