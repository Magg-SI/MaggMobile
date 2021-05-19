package pl.tysia.maggstone.ui

import androidx.appcompat.app.AppCompatActivity
import pl.tysia.maggstone.ui.loading.LoadingFragment
import pl.tysia.maggstone.ui.loading.ProgressFragment

abstract class BaseActivity : AppCompatActivity() {
    companion object{
        const val LOADING_TAG = "pl.tysia.maggstone.loading_dialog"

        private val loadingFragment: LoadingFragment = LoadingFragment()
        private val progressFragment : ProgressFragment = ProgressFragment()
    }

    protected open fun showBlockingLoading(show: Boolean){
        if (show && !loadingFragment.isAdded){
            loadingFragment.show(supportFragmentManager, LOADING_TAG)
        }else if (!show && loadingFragment.showsDialog){
            loadingFragment.dismiss()
        }
    }

    protected open fun showBlockingProgress(show: Boolean) {
        if (show && !progressFragment.isAdded){
            progressFragment.show(supportFragmentManager, LOADING_TAG)
        }else if (!show && progressFragment.showsDialog){
            progressFragment.dismiss()
        }
    }

    protected open fun setProgressValue(progress : Int) {
        if (progressFragment.isVisible){
            progressFragment.setProgress(progress)
        }
    }

}