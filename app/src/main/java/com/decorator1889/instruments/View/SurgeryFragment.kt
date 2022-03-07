package com.decorator1889.instruments.View

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.appodeal.ads.Appodeal
import com.decorator1889.instruments.Adapters.SurgeryInstrumentAdapter
import com.decorator1889.instruments.LocalDb.RealmDbSurgery
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.Models.SurgeryInstrumentModel
import com.decorator1889.instruments.R
import com.decorator1889.instruments.util.DisposableManager
import com.decorator1889.instruments.util.DisposingObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_instrument.*


class   SurgeryFragment : Fragment() {
    private var adapter: SurgeryInstrumentAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instrument, container, false)
        val typeSurgery = arguments!!.getString("surgeryType")
        showCountryNameAndCodeFromDatabase(typeSurgery!!)
        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = activity as MainActivity
        activity.showUpButton()
        setHasOptionsMenu(true)
    }
    override fun onDestroy() {
        DisposableManager.dispose()
        super.onDestroy()

    }

    private fun showCountryNameAndCodeFromDatabase(typeSurgery: String) {
        RealmDbSurgery.countryCodeAndNameAsObservable()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe(object : DisposingObserver<RealmResults<SurgeryInstrumentModel>>() {
                override fun onNext(value: RealmResults<SurgeryInstrumentModel>) {
                    setUpRecyclerView(typeSurgery)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun setUpRecyclerView(typeSurgery: String) {
        loader.visibility = View.GONE
         adapter = SurgeryInstrumentAdapter(
            RealmDbSurgery.realm.where(SurgeryInstrumentModel::class.java)
                .equalTo( "type", typeSurgery)
                .sort("instrument_name")
                .findAll(), typeSurgery)
        rcv.layoutManager = LinearLayoutManager(requireContext())
        rcv.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val item: MenuItem = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(searchView.query.isEmpty()) {
                    adapter?.filter?.filter("")
                }
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)

    }
}