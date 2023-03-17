package hn.single.imageapp.common.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import hn.single.imageapp.common.utils.Logger
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment<S : Any, VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    open var useSharedViewModel: Boolean = false
    private val disposableContainer = CompositeDisposable()
    protected lateinit var mViewModel: VM
    protected lateinit var mViewBinding: VB

    protected abstract fun getViewModelClass(): Class<VM>

    protected abstract fun getViewBinding(): VB

    fun fragmentNavController(): NavController? {
        return (activity as? BaseActivity<*>)?.activityNavController()
    }

    fun showLoadingProgress(isShowLoading: Boolean) {
        Logger.d("isNeedShowLoading() == $isShowLoading")
        (activity as BaseActivity<*>).showLoadingProgress(isShowLoading)
    }

    abstract fun useSharedViewModel(): Boolean

    abstract fun initViews()

    abstract fun observeView()

    abstract fun observeData()

    //fun Disposable.addToContainer() = disposableContainer.add(this)

    private fun init() {
        mViewBinding = getViewBinding()
        mViewModel = if (useSharedViewModel) {
            ViewModelProvider(requireActivity())[getViewModelClass()]
        } else {
            ViewModelProvider(this)[getViewModelClass()]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initViews()
        observeView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //disposableContainer.clear()
    }

}