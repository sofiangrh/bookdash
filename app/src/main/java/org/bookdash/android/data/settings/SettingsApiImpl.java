package org.bookdash.android.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.bookdash.android.config.RemoteConfigSettingsApi;
import org.bookdash.android.domain.model.firebase.FireLanguage;

import java.util.concurrent.Callable;

import rx.Single;

/**
 * @author rebeccafranks
 * @since 15/11/05.
 */
public class SettingsApiImpl implements SettingsApi {
    public static final String FIRE_LANGUAGE_PREF = "fire_language_pref";
    public static final String PREF_IS_SUBSCRIBED_NEW_BOOK_NOTIFICATIONS = "pref_new_book_notification";
    private static final String PREF_IS_FIRST_TIME = "is_first_time";
    private final Context context;
    private final RemoteConfigSettingsApi remoteConfig;

    public SettingsApiImpl(Context context, RemoteConfigSettingsApi remoteConfigSettingsApi) {
        this.context = context;
        this.remoteConfig = remoteConfigSettingsApi;
    }

    @Override
    public boolean isFirstTime() {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_IS_FIRST_TIME, true);

    }

    @Override
    public void setIsFirstTime(boolean isFirstTime) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(PREF_IS_FIRST_TIME, isFirstTime);
        editor.apply();
    }


    @Override
    public Single<Boolean> saveSelectedLanguage(final FireLanguage fireLanguage) {
        return Single.defer(new Callable<Single<Boolean>>() {
            @Override
            public Single<Boolean> call() throws Exception {

                Gson gson = new Gson();
                String json = gson.toJson(fireLanguage);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

                editor.putString(FIRE_LANGUAGE_PREF, json);
                editor.apply();
                return Single.just(true);
            }
        });
    }

    @Override
    public Single<FireLanguage> getSavedLanguage() {
        return Single.defer(new Callable<Single<FireLanguage>>() {
            @Override
            public Single<FireLanguage> call() throws Exception {
                Gson gson = new Gson();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                String json = sharedPreferences.getString(FIRE_LANGUAGE_PREF, "");
                if (json.isEmpty()) {
                    return Single.just(new FireLanguage(remoteConfig.getDefaultLanguageName(),
                            remoteConfig.getDefaultLanguageAbbreviation(), true, remoteConfig.getDefaultLanguageId()));
                }
                return Single.just(gson.fromJson(json, FireLanguage.class));
            }
        });
    }

    @Override
    public Single<Boolean> isSubscribedToNewBookNotification() {
        return Single.defer(new Callable<Single<Boolean>>() {
            @Override
            public Single<Boolean> call() throws Exception {
                return Single.just(PreferenceManager.getDefaultSharedPreferences(context)
                        .getBoolean(PREF_IS_SUBSCRIBED_NEW_BOOK_NOTIFICATIONS, true));
            }
        });
    }

    @Override
    public Single<Boolean> saveNewBookNotificationPreference(final boolean onOff) {
        return Single.defer(new Callable<Single<Boolean>>() {
            @Override
            public Single<Boolean> call() throws Exception {
                return Single.just(PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean(PREF_IS_SUBSCRIBED_NEW_BOOK_NOTIFICATIONS, onOff).commit());
            }
        });
    }
}
