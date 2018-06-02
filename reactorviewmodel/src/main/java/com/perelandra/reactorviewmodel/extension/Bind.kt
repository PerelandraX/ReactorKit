package com.perelandra.reactorviewmodel.extension

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer

fun <T> Observable<T>.bind(to: Consumer<in T>) = this.subscribe(to)
fun <T> Observable<T>.bind(to: Observer<in T>) = this.subscribe(to)