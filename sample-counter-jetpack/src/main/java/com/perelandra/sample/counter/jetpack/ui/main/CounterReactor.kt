package com.perelandra.sample.counter.jetpack.ui.main

import androidx.lifecycle.MutableLiveData
import com.perelandra.reactorkit.aac.ReactorViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class CounterReactor
  : ReactorViewModel<CounterReactor.Action, CounterReactor.Mutation, CounterReactor.State>() {

  companion object {
    private val TAG = CounterReactor::class.java.simpleName
  }

  override var initialState: State = State()

  sealed class Action {
    object Increase : Action()
    object Decrease : Action()
  }

  sealed class Mutation {
    object IncreaseValue : Mutation()
    object DecreaseValue : Mutation()
    data class SetLoading(val isLoading: Boolean) : Mutation()
  }

  data class State(
      val count: MutableLiveData<Int> = MutableLiveData(0),
      val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
  )

  override fun mutate(action: Action): Observable<Mutation> = when (action) {
    is Action.Increase -> Observable.concat(
        Observable.just(Mutation.SetLoading(true)),
        Observable.just(Mutation.IncreaseValue).delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()),
        Observable.just(Mutation.SetLoading(false)))

    is Action.Decrease -> Observable.concat(
        Observable.just(Mutation.SetLoading(true)),
        Observable.just(Mutation.DecreaseValue).delay(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()),
        Observable.just(Mutation.SetLoading(false)))
  }

  override fun reduce(state: State, mutation: Mutation): State = when (mutation) {
    is Mutation.IncreaseValue -> state.apply { count.value = count.value?.let { it + 1 } }
    is Mutation.DecreaseValue -> state.apply { count.value = count.value?.let { it - 1 } }
    is Mutation.SetLoading -> state.apply { isLoading.value = mutation.isLoading }
  }
}
