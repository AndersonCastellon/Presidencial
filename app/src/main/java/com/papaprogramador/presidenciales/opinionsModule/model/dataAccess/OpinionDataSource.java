package com.papaprogramador.presidenciales.opinionsModule.model.dataAccess;

import android.support.v4.util.Pair;

import com.papaprogramador.presidenciales.common.pojo.Like;
import com.papaprogramador.presidenciales.common.pojo.Opinion;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface OpinionDataSource {

	Observable<List<Opinion>> getOpinions(long timeStamp);

	Observable<Pair<Boolean, Opinion>> addOpinionNotifier();

	Single<Boolean> removeOpinionNotifier();

	Single<Boolean> deleteOpinion(Opinion opinion);

	Single<Boolean> toggleLike(Like like);

	Observable<Pair<Like, Boolean>> addLikeNotifier(String opinionId);

	Single<Boolean> removeLikeNotifier(String opinionId);

}
