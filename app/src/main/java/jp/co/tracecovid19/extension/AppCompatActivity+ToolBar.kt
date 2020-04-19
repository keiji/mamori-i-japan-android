package jp.co.tracecovid19.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import jp.co.tracecovid19.R

fun AppCompatActivity.setUpToolBar(toolbar: Toolbar, title: String, subTitle: String = "", isShowBackButton: Boolean = true, onClickBackButton: (() -> Unit)? = null) {
    setSupportActionBar(toolbar)
    supportActionBar?.title = title
    supportActionBar?.subtitle = subTitle
    if (isShowBackButton) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            onClickBackButton?.invoke() ?: finish()
        }
    }
}