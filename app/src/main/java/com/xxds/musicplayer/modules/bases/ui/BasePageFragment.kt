package com.xxds.musicplayer.modules.bases.ui


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.loader.app.LoaderManager
import com.xxds.musicplayer.R
import org.jetbrains.anko.find

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
abstract class BasePageFragment : BaseFragment() {

    protected lateinit var mToolBar: Toolbar
    private lateinit var mTitle: TextView
    private lateinit var statusBar: View
    protected lateinit var fm: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var linearLayout = inflater.inflate(R.layout.fragment_base,container,false) as LinearLayout

        linearLayout.addView(createContentView(inflater,container))

        mToolBar = linearLayout.findViewById(R.id.include_tool_bar)
        mTitle = mToolBar.findViewById(R.id.toolbar_title)
        (activity as AppCompatActivity).setSupportActionBar(mToolBar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        statusBar = linearLayout.findViewById(R.id.fake_status_bar)
        mLoaderManager = LoaderManager.getInstance(this)
        fm = fragmentManager!!
        setHasOptionsMenu(true)
        return  linearLayout
    }
    fun setTitle(title: String) {
        mTitle.text = title
    }
    fun setStatusBarColor(@ColorRes color: Int) {
        statusBar.setBackgroundResource(color)
    }
    fun hideStatStatusBar() {
        statusBar.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            android.R.id.home -> fm.popBackStack()
        }
        return true
    }

    abstract  fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View


}
