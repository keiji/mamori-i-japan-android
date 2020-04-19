package jp.co.tracecovid19.screen.menu

import android.app.Activity
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import jp.co.tracecovid19.data.model.Profile
import jp.co.tracecovid19.data.repository.profile.ProfileRepository
import jp.co.tracecovid19.screen.profile.InputPrefectureTransitionEntity
import jp.co.tracecovid19.screen.profile.InputJobTransitionEntity


class SettingViewModel(private val profileRepository: ProfileRepository,
                       private val disposable: CompositeDisposable): ViewModel() {

    lateinit var navigator: SettingNavigator
    val profile = PublishSubject.create<Profile>()

    private var _profile: Profile? = null

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun fetchProfile(activity: Activity) {
        profileRepository.fetchProfile(activity).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _profile = it
                    profile.onNext(it)
                },
                onError = {
                    // TODO エラー
                }
            ).addTo(disposable)
    }

    fun onClickPrefecture() {
        _profile?.let {
            navigator.goToInputPrefecture(InputPrefectureTransitionEntity(it, false))
        }
    }

    fun onClickWork() {
        _profile?.let {
            navigator.goToInputWork(InputJobTransitionEntity(it, false))
        }
    }

}