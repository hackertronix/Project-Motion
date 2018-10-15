package com.execube.genesis.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.execube.genesis.model.Movie
import com.execube.genesis.utils.API
import com.execube.genesis.utils.API.Companion
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HostViewModel: ViewModel() {

  private val listOfMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
  private val toastMessage: MutableLiveData<String> = MutableLiveData()



  fun getListOfMovies(): LiveData<ArrayList<Movie>>{
    return listOfMovies.let { it }
  }

  fun getToastMessage():LiveData<String>{
    return toastMessage.let { it }
  }

  fun getPopularMovies(): Disposable{

    return  API.create().getPopularMovies()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe ({
          if(it.results.size>0)
          {
            listOfMovies.value = it.results
          }
        },{
          toastMessage.value = it.message
        })

  }
}