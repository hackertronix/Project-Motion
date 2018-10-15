package com.execube.genesis.host

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.execube.genesis.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class HostActivity : AppCompatActivity() {

  private lateinit var viewModel: HostViewModel
  private lateinit var disposable: CompositeDisposable
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_host)

    disposable = CompositeDisposable()
    viewModel = ViewModelProviders.of(this).get(HostViewModel::class.java)
  }

  override fun onResume() {
    super.onResume()
    disposable.add(viewModel.getPopularMovies())

    viewModel.getListOfMovies().observe(this, Observer {
      Toast.makeText(this@HostActivity,it.toString(),Toast.LENGTH_SHORT).show()
    })
   }
}
