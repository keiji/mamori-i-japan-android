package jp.mamori_i.app.screen.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import jp.mamori_i.app.R
import jp.mamori_i.app.extension.setUpToolBar
import jp.mamori_i.app.extension.showErrorDialog
import jp.mamori_i.app.ui.ProgressHUD
import kotlinx.android.synthetic.main.activity_input_organization_code.*
import kotlinx.android.synthetic.main.activity_input_organization_code.toolBar
import kotlinx.android.synthetic.main.ui_select_text.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class InputOrganizationCodeActivity: AppCompatActivity(),
    InputOrganizationCodeNavigator {
    companion object {
        const val KEY = "jp.mamori_i.app.screen.profile.InputOrganizationCodeActivity"
    }

    private val viewModel: InputOrganizationCodeViewModel by viewModel()
    private val disposable: CompositeDisposable by inject()
    private lateinit var transitionEntity: InputOrganizationCodeTransitionEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初期設定
        initialize()
        // viewの初期設定
        setupViews()
        // viewModelとのbind
        bind()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun initialize() {
        setContentView(R.layout.activity_input_organization_code)
        viewModel.navigator = this
        intent?.let { intent ->
            (intent.getSerializableExtra(KEY) as? InputOrganizationCodeTransitionEntity)?.let { entity ->
                // 引き継ぎデータあり
                transitionEntity = entity
            }
        }
    }

    private fun setupViews() {
        // ツールバー
        setUpToolBar(toolBar, "組織コード")

        codeInputText.requestFocus()
        codeInputText.textChanges()
            .map { code ->
                code.isNotBlank()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { enabled ->
                updateButton.isEnabled = enabled
            }.addTo(disposable)

        updateButton.setOnClickListener {
            viewModel.onClickUpdateButton(codeInputText.text.toString())
        }
    }

    private fun bind() {
        viewModel.updateError
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { error ->
                showErrorDialog(error)
            }.addTo(disposable)
    }

    override fun showProgress() {
        ProgressHUD.show(this)
    }

    override fun hideProgress() {
        ProgressHUD.hide()
    }

    override fun finishWithCompleteMessage(message: String) {
        finish()
    }
}