package com.udc.master.tfm.tracksports;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.udc.master.tfm.tracksports.common.dialog.CustomDialogBuilder;
import com.udc.master.tfm.tracksports.fragments.diary.DiaryFragment;
import com.udc.master.tfm.tracksports.fragments.home.HomeFragment;
import com.udc.master.tfm.tracksports.fragments.profile.ProfileFragment;
import com.udc.master.tfm.tracksports.fragments.profile.add.AddProfileActivity;
import com.udc.master.tfm.tracksports.fragments.settings.SettingsFragment;
import com.udc.master.tfm.tracksports.locationtracker.FallbackLocationTracker;
import com.udc.master.tfm.tracksports.locationtracker.LocationTracker;
import com.udc.master.tfm.tracksports.utils.AlarmUtils;
import com.udc.master.tfm.tracksports.utils.ConstantsUtils;
import com.udc.master.tfm.tracksports.utils.NotificationsUtils;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesTypes;
import com.udc.master.tfm.tracksports.utils.preferences.PreferencesUtils;
import com.google.android.gms.maps.MapFragment;
import com.udc.master.tfm.tracksports.bbdd.profiles.Profile;

/**
 * Actividad principal que se carga cuando se arranca la aplicacion.
 * Esta estara compuesta por un conjunto de 5 fragmentos cada una en una pestana
 * y que se podran navegar entre ellos.
 * @author adrianoca
 *
 */
public class MainActivity extends FragmentActivity implements OnTabChangeListener, ViewPager.OnPageChangeListener {

    /** Vista con los distintos fragmentos*/
    private TabHost mTabHost;
    /** Vista paginada con los fragmentos*/
    private ViewPager mViewPager;
    /** Servicio de localizacion */
    private LocationTracker locationTracker;
    /** Variable para controlar salir de la aplicacion al pulsar dos veces el boton atras */
    private boolean doubleBackToExitPressedOnce = false;
    /** Notificacion persistente mientras se ejecuta la aplicacion */
    private Notification appRunningNotification;
    /** Manejador de las notificaciones */
    private NotificationManager notificationManager;
    /** Variable que indica si se inicia el ejercicio de manera automatica */
    private boolean autoStart;

    private Integer notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se inicializan las preferencias de la aplicacion
        PreferencesUtils.initDefaultPreferences(getApplicationContext());

        //Se comprueba si se invoca la actividad para que se ejecute el ejercicio de forma inmediata
        Intent intentAuto = getIntent();
        if (intentAuto != null && intentAuto.getExtras() != null) {
            autoStart = intentAuto.getExtras().getBoolean(ConstantsUtils.AUTO_START_PARAM, false);
            notificationId = intentAuto.getExtras().getInt(ConstantsUtils.NOTIFICATION_ID_PARAM);
        }

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Se instancia el servicio de localizacion
        locationTracker = new FallbackLocationTracker(this);
        //Se inicializan los tabs de la aplicacion
        initialiteTabHost();
        initialiteViewPager();
        //Se crea una notificacion para indicar que la aplicacion esta corriendo
        createRunningAppNotification();

        if (!autoStart) {
            //Si no se ha establecido un usuario por defecto se lanza la actividad de crearlo
            checkDefaultProfile();
            //Si el GPS está deshabilitado se muestra una alerta
            checkLocationTrackerEnabled();
        }

        //Si se va a ejecutar un ejercicio pendiente, se elimina la notificacion
        if (notificationId != null) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        }

        //Se crea una alarma para controlar los ejercicios pendientes de ejecutar
        Boolean reduceInterval = PreferencesUtils.getPreferences(PreferencesTypes.IS_INTERVAL_EXECUTE_PENDING_EXERCISE, Boolean.class, this);
        long interval = 0;
        if (Boolean.TRUE.equals(reduceInterval)) {
            Integer checkPendingExerciseCloseMinutes = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_CLOSE_MINUTES, Integer.class, this);
            interval = checkPendingExerciseCloseMinutes * 60 * 1000;
        } else {
            Integer checkPendingExerciseHours = PreferencesUtils.getPreferences(PreferencesTypes.NOTIFICATION_CHECK_PENDING_EXERCISE_HOURS, Integer.class, this);
            interval = checkPendingExerciseHours * 60 * 60 * 1000;
        }
        AlarmUtils.createCheckPendingExercisesAlarm(this, interval);
    }

    /**
     * Metodo que inicializa el conjunto de Tabs
     */
    private void initialiteTabHost() {
//		View homeView = getLayoutInflater().inflate(R.layout.tab_home, mTabHost, false);
//		View mapView = getLayoutInflater().inflate(R.layout.tab_map, mTabHost, false);
//		View diaryView = getLayoutInflater().inflate(R.layout.tab_diary, mTabHost, false);
//		View profileView = getLayoutInflater().inflate(R.layout.tab_profile, mTabHost, false);
//	    View settingsView = getLayoutInflater().inflate(R.layout.tab_settings, mTabHost, false);

        mTabHost.setup();
        TabFactory tabFactory = new TabFactory(getBaseContext());
//		TabSpec tabSpecHome = mTabHost.newTabSpec("tab1").setIndicator(homeView);
//		TabSpec tabSpecMap = mTabHost.newTabSpec("tab2").setIndicator(mapView);
//		TabSpec tabSpecDiary = mTabHost.newTabSpec("tab3").setIndicator(diaryView);
//		TabSpec tabSpecProfile = mTabHost.newTabSpec("tab4").setIndicator(profileView);
//		TabSpec tabSpecSettings = mTabHost.newTabSpec("tab5").setIndicator(settingsView);
        TabSpec tabSpecHome = mTabHost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.home_black));
        TabSpec tabSpecMap = mTabHost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.map_black));
        TabSpec tabSpecDiary = mTabHost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.diary_black));
        TabSpec tabSpecProfile = mTabHost.newTabSpec("tab4").setIndicator("", getResources().getDrawable(R.drawable.profile_black));
        TabSpec tabSpecSettings = mTabHost.newTabSpec("tab5").setIndicator("", getResources().getDrawable(R.drawable.settings_black));
        tabSpecHome.setContent(tabFactory);
        tabSpecMap.setContent(tabFactory);
        tabSpecDiary.setContent(tabFactory);
        tabSpecProfile.setContent(tabFactory);
        tabSpecSettings.setContent(tabFactory);
        mTabHost.addTab(tabSpecHome);
        mTabHost.addTab(tabSpecMap);
        mTabHost.addTab(tabSpecDiary);
        mTabHost.addTab(tabSpecProfile);
        mTabHost.addTab(tabSpecSettings);
        mTabHost.setOnTabChangedListener(this);

        //Se inicializa el color de fondo
        TabWidget widget = mTabHost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_indicator_holo);
        }
    }

    /**
     * Metodo que inicializa el swipe entre los diferentes fragmentos
     */
    private void initialiteViewPager() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment(locationTracker, autoStart));
        //fragments.add(new MapFragment());
        fragments.add(new DiaryFragment());
        fragments.add(new ProfileFragment());
        fragments.add(new SettingsFragment());

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int pos) {
        mTabHost.setCurrentTab(pos);
    }

    @Override
    public void onTabChanged(String tabId) {
        mViewPager.setCurrentItem(mTabHost.getCurrentTab());
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onBackPressed() {
        //Para salir de la aplicacion se pulsara dos veces el boton atras
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            //Se finaliza la aplicacion
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), getString(R.string.text_check_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /**
     * Metodo que crea una notificacion para indicar que la aplicacion esta corriendo
     */
    @SuppressWarnings("deprecation")
    private void createRunningAppNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.running)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.text_notification_running_app))
                .setContentIntent(pendingIntent);
        appRunningNotification = mBuilder.getNotification();
        appRunningNotification.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(NotificationsUtils.APP_RUNNING_NOTIFICATION_ID, appRunningNotification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Al finalizar la aplicacion se elimina la notificacion
        if (notificationManager != null) {
            notificationManager.cancel(NotificationsUtils.APP_RUNNING_NOTIFICATION_ID);
        }
    }

    /**
     * Metodo que comprueba si se ha establecido un usuario por defecto,
     * si no es asi se lanza la actividad para crearlo
     */
    private void checkDefaultProfile() {
        if (PreferencesUtils.getPreferences(PreferencesTypes.DEFAULT_PROFILE, Profile.class, this) == null) {
            Intent intent = new Intent(this, AddProfileActivity.class);
            intent.putExtra(ConstantsUtils.SET_DEFAULT_PARAM, true);
            startActivity(intent);
        }
    }

    /**
     * Metodo que comprueba si el localizador de posicion esta habilitado
     * si no es asi, se lanza una alertar al usuario
     */
    private void checkLocationTrackerEnabled() {
        if (!locationTracker.isEnabled()) {
            CustomDialogBuilder alertDialog = new CustomDialogBuilder(this);
            alertDialog.setTitle(R.string.gps_disabled_settings);
            alertDialog.setMessage(R.string.gps_disabled_settings_msg);
            //Si se presiona el boton de configuracion
            alertDialog.setPositiveButton(R.string.gps_disabled_settings_ok_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            //Si se presiona el boton de cancelar
            alertDialog.setNegativeButton(R.string.gps_disabled_settings_cancel_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            //Se muestra el dialogo
            alertDialog.show();
        }
    }
}
