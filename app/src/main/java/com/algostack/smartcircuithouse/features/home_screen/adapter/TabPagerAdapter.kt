package com.algostack.smartcircuithouse.features.home_screen.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import androidx.core.content.ContextCompat
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.BookedFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.DoubleFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.HomeFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.InOutFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.SingleFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.UnbookedFragment

class TabPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val tabTitles = arrayOf("Home", "Single", "Double", "Booked", /*"Booked",*/ "Unbooked")
    private val tabIcons = intArrayOf(
        R.drawable.ic_home,
        R.drawable.ic_single_bed,
        R.drawable.ic_double_bed,
        /*  R.drawable.ic_home,*/
        R.drawable.ic_booked,
        R.drawable.ic_unbooked
    )

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SingleFragment()
            2 -> DoubleFragment()
            3 -> InOutFragment()
            /* 4 -> BookedFragment()*/
            4 -> UnbookedFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val drawable = ContextCompat.getDrawable(context, tabIcons[position])
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

        val spannableStringBuilder = SpannableStringBuilder()
        spannableStringBuilder.append("  ")
        drawable?.let {
            val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BASELINE)
            spannableStringBuilder.setSpan(
                imageSpan,
                0,
                1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        spannableStringBuilder.append("\n")
        spannableStringBuilder.append(tabTitles[position])

        return spannableStringBuilder
    }


}
