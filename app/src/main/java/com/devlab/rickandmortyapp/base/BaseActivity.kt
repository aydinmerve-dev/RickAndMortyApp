package com.devlab.rickandmortyapp.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.devlab.rickandmortyapp.BR
import com.devlab.rickandmortyapp.util.OnBackPressListener
import com.devlab.rickandmortyapp.util.extensions.setViewListener
import com.devlab.rickandmortyapp.util.extensions.viewModel

abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    val viewModel: VM by viewModel()

    lateinit var vi: DB

    var onBackPressListener: OnBackPressListener? = null

    abstract fun getLayoutId(): Int

    @CallSuper
    protected open fun initViews() {
    }

    @CallSuper
    protected open fun setListeners() {
    }

    @CallSuper
    protected open fun setReceivers() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vi = DataBindingUtil.setContentView(this, getLayoutId())
        vi.lifecycleOwner = this
        vi.setVariable(BR.viewModel, viewModel)

        viewModel.fragmentManager = supportFragmentManager

        /**
         * ViewModel'e listener setlenir
         */
        setViewListener()

        viewModel.arguments = intent.extras
        viewModel.onViewCreated(savedInstanceState)

        initViews()

        setListeners()
        setReceivers()
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()

        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (viewModel.isBackEnable()) {
            if (viewModel.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            if (onBackPressListener != null &&
                onBackPressListener!!.isBackEnable()
            ) {
                if (onBackPressListener!!.onBackPressed()) {
                    super.onBackPressed()
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        viewModel.onDestroy()

        super.onDestroy()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}