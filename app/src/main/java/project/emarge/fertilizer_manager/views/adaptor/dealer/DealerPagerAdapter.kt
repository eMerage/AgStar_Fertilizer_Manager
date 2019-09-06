package project.emarge.fertilizer_manager.views.adaptor.dealer

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.views.fragment.dealer.DealerApprovedFragment
import project.emarge.fertilizer_manager.views.fragment.dealer.DealerAssignFragment
import project.emarge.fertilizer_manager.views.fragment.dealer.DealerUnapprovedFragment


private val TAB_TITLES = arrayOf(
    R.string.dealer_tab_1,
    R.string.dealer_tab_2,
    R.string.dealer_tab_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class DealerPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
      return when (position) {
            0 -> DealerUnapprovedFragment.newInstance(position + 1)
            1 -> DealerApprovedFragment.newInstance(position + 1)
            else -> DealerAssignFragment.newInstance(position + 1)
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}