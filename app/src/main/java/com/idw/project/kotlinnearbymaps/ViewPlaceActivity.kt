package com.idw.project.kotlinnearbymaps

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.idw.project.kotlinnearbymaps.Common.Common
import com.idw.project.kotlinnearbymaps.Model.PlaceDetail
import com.idw.project.kotlinnearbymaps.Remote.IGoogleAPIService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewPlaceActivity : AppCompatActivity() {

    internal lateinit var mService: IGoogleAPIService
    var mPlace:PlaceDetail?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)

        //init service
        mService = Common.googleApiService

        //set empty for alll text view
        place_name.text=""
        place_address.text=""
        place_open_hour.text=""

        btn_show_map.setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mPlace!!.result.url))
            startActivity(mapIntent)
        }

        btn_view_direction.setOnClickListener {
            val viewDirections = Intent(this, ViewDirectionsActivity::class.java)
            startActivity(viewDirections)
        }

        if (Common.currentResult!!.photos != null && Common.currentResult!!.photos.size>0)
            Picasso.with(this)
                .load(getPhotoOfPlace(Common.currentResult!!.photos[0].photoReference, 1000))
                .into(photo)


        //load rating
        if (Common.currentResult!!.rating != null )
            rating_bar.rating = Common.currentResult!!.rating.toFloat()
        else
            rating_bar.visibility = View.GONE

        //load openinghours
        if (Common.currentResult!!.openingHours != null )
            place_open_hour.text = "Open Now : ${Common.currentResult!!.openingHours.openNow}"
        else
            place_open_hour.visibility = View.GONE


        //use service to fetch address and name
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult!!.placeId))
            .enqueue(object :Callback<PlaceDetail>{
                override fun onFailure(call: Call<PlaceDetail>, t: Throwable) {
                    Toast.makeText(baseContext, "${t.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<PlaceDetail>, response: Response<PlaceDetail>) {
                    mPlace = response.body()

                    place_address.text = mPlace!!.result.formattedAddress
                    place_name.text = mPlace!!.result.name

                }

            })
    }

    private fun getPlaceDetailUrl(placeId: String): String {
        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?place_id=$placeId")
        url.append("&key=AIzaSyA4B_cSRek1B_dkgZIIhMsdLvMMBwS2P4U")
        return  url.toString()
    }

    private fun getPhotoOfPlace(photoReference: String, maxWidth: Int): String {
        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("?photoreference=$photoReference")
        url.append("&key=AIzaSyA4B_cSRek1B_dkgZIIhMsdLvMMBwS2P4U")
        return  url.toString()
    }
}
