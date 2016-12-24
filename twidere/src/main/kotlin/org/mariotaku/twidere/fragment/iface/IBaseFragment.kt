/*
 * 				Twidere - Twitter client for Android
 * 
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.fragment.iface

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import org.mariotaku.twidere.constant.IntentConstants
import java.util.*

interface IBaseFragment {
    val extraConfiguration: Bundle?
        get() = null

    val tabPosition: Int
        get() = (this as Fragment).arguments?.getInt(IntentConstants.EXTRA_TAB_POSITION, -1) ?: -1

    val tabId: Long
        get() = (this as Fragment).arguments?.getLong(IntentConstants.EXTRA_TAB_ID, -1L) ?: -1L

    fun requestFitSystemWindows() {
        val fragment = this as Fragment
        val activity = fragment.activity
        val parentFragment = fragment.parentFragment
        val callback: IBaseFragment.SystemWindowsInsetsCallback
        if (parentFragment is IBaseFragment.SystemWindowsInsetsCallback) {
            callback = parentFragment
        } else if (activity is IBaseFragment.SystemWindowsInsetsCallback) {
            callback = activity
        } else {
            return
        }
        val insets = Rect()
        if (callback.getSystemWindowsInsets(insets)) {
            fitSystemWindows(insets)
        }
    }

    fun fitSystemWindows(insets: Rect) {
        val fragment = this as Fragment
        fragment.view?.setPadding(insets.left, insets.top, insets.right, insets.bottom)
    }

    interface SystemWindowsInsetsCallback {
        fun getSystemWindowsInsets(insets: Rect): Boolean
    }

    fun executeAfterFragmentResumed(action: (IBaseFragment) -> Unit)

    class ActionHelper(private val fragment: IBaseFragment) {

        private var fragmentResumed: Boolean = false
        private val actionQueue = LinkedList<(IBaseFragment) -> Unit>()

        fun dispatchOnPause() {
            fragmentResumed = false
        }

        fun dispatchOnResumeFragments() {
            fragmentResumed = true
            executePending()
        }


        private fun executePending() {
            if (!fragmentResumed) return
            var action: ((IBaseFragment) -> Unit)?
            do {
                action = actionQueue.poll()
                action?.invoke(fragment)
            } while (action != null)
        }

        fun executeAfterFragmentResumed(action: (IBaseFragment) -> Unit) {
            actionQueue.add(action)
            executePending()
        }
    }
}
