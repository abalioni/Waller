package br.com.leonardo.waller.model;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/*
 * Created by Leonardo de Matos on 06/03/17.
 */
@SuppressWarnings("SpellCheckingInspection")
public interface WallerService {
    //MARK - USER WS's
    @GET("photos")
    Observable<List<Photo>>photos(@Query("client_id") String clientId, @Query("page") Integer page, @Query("per_page") Integer perPage);
}