package vn.tale.architecture.home;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import vn.tale.architecture.ActivityScope;
import vn.tale.architecture.common.redux.Store;
import vn.tale.architecture.home.epic.LoadEpic;
import vn.tale.architecture.home.epic.LoadMoreEpic;
import vn.tale.architecture.home.epic.RefreshEpic;
import vn.tale.architecture.model.manager.HomeModel;

/**
 * Created by Giang Nguyen on 3/27/17.
 */
@Module
public class HomeModule {

  @ActivityScope
  @Provides Store<HomeState> provideViewModel(HomeModel homeModel) {
    return Store.<HomeState>builder()
        .initialState(HomeState.idle())
        .effects(
            new LoadEpic(homeModel),
            new RefreshEpic(homeModel),
            new LoadMoreEpic(homeModel))
        .reducer(new HomeReducer())
        .make();
  }

  @Provides
  HomeViewModel provideHomeViewModel(Store<HomeState> store) {
    final Observable<HomeState> state$ = store.state$()
        .observeOn(AndroidSchedulers.mainThread());
    return new HomeViewModel(state$);
  }
}
