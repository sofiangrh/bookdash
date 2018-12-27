package org.bookdash.android.presentation.readbook

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel

import org.bookdash.android.presentation.utils.SingleLiveEvent

/**
 * @author rebeccafranks
 * @since 2017/10/27.
 */

class ReadBookViewModel : ViewModel() {
    val pageBackEventTrigger = SingleLiveEvent<Void>()
    val pageForwardEventTrigger = SingleLiveEvent<Void>()


}
