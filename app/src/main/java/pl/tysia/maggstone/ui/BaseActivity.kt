package pl.tysia.maggstone.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    companion object{
        const val LOADING_TAG = "pl.tysia.maggstone.loading_dialog"
    }

    private val loadingFragment: LoadingFragment = LoadingFragment()

    protected open fun showBlockingProgress(show: Boolean){
        if (show){
            loadingFragment.show(supportFragmentManager, LOADING_TAG)
        }else if (!show && loadingFragment.showsDialog){
            loadingFragment.dismiss()
        }
    }
}