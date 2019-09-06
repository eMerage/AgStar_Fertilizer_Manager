package project.emarge.fertilizer_manager.views.adaptor.product

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.views.fragment.products.ProductsAssignedFragment
import project.emarge.fertilizer_manager.views.fragment.products.ProductsToAssignFragment


private val TAB_TITLES = arrayOf(
    R.string.products_tab_1,
    R.string.products_tab_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class ProductsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
      return when (position) {
            0 -> ProductsAssignedFragment.newInstance(position + 1)
            else -> ProductsToAssignFragment.newInstance(position + 1)
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}