package com.decorator1889.instruments.View


import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.decorator1889.instruments.Adapters.InstrumentAdapter
import com.decorator1889.instruments.LocalDb.RealmDb
import com.decorator1889.instruments.LocalDb.RealmDb.realm
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.Models.InstrumentModel
import com.decorator1889.instruments.R
import com.decorator1889.instruments.util.DisposableManager
import com.decorator1889.instruments.util.DisposingObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_instrument.*


class InstrumentFragment : Fragment() {
    private var adapter: InstrumentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instrument, container, false)
        val typeCompany = arguments!!.getString("type")
        showCountryNameAndCodeFromDatabase(typeCompany!!)
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


    private fun showCountryNameAndCodeFromDatabase(type: String) {
        RealmDb.countryCodeAndNameAsObservable()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe(object : DisposingObserver<RealmResults<InstrumentModel>>() {
                override fun onNext(value: RealmResults<InstrumentModel>) {
                    setUpRecyclerView(type)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    private fun setUpRecyclerView(type: String) {
        loader.visibility = View.GONE
         adapter = InstrumentAdapter(
             realm.where(InstrumentModel::class.java)
                 .equalTo("type", type)
                 .sort("instrument_name")
                 .findAll(), type)
        rcv.layoutManager = LinearLayoutManager(requireContext())
        rcv.adapter = adapter
    }
}

