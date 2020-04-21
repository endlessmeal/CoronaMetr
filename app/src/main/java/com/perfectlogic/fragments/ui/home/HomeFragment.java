package com.perfectlogic.fragments.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.perfectlogic.fragments.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView totalRussia;
    private TextView totalText;

    private TextView totalDead;
    private TextView totalHealth;
    private TextView totalTodayRussia;
    private TextView totalTodayHealth;
    private TextView totalTodayDead;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        totalRussia = root.findViewById(R.id.totalRussia);
        totalText = root.findViewById(R.id.totalText);

        totalHealth = root.findViewById(R.id.totalHealth);
        totalDead = root.findViewById(R.id.totalDead);

        totalTodayRussia = root.findViewById(R.id.totalTodayRussia);
        totalTodayHealth = root.findViewById(R.id.totalTodayHealth);
        totalTodayDead = root.findViewById(R.id.totalTodayDead);

        GetData gd = new GetData();
        gd.execute();

        //для установки русского отображения числа и месяца
        Date currentDay = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("dd MMMM", myDateFormatSymbols);
        totalText.setText("Всего заболевших в России на " + dateFormat.format(currentDay));

        return root;
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }

    };

    public class GetData extends AsyncTask<Void, Void, Void> {

        String totalRussiaDisease;
        String totalRussiaHealth;
        String totalRussiaDead;

        String dailyRussiaDisease;
        String dailyRussiaHealth;
        String dailyRussiaDead;

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;

            try {
                doc = Jsoup.connect("https://coronavirus-monitor.ru/coronavirus-v-rossii/").get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //получаем количество заболевших
            Elements totalRussiaByClass = doc.getElementsByClass("info-block disease");
            Elements totalRussiaByTag = totalRussiaByClass.select("i");
            Elements dailyRussiaByTag = totalRussiaByClass.select("span");
            String total = totalRussiaByTag.text();
            String dailyTotal = dailyRussiaByTag.text();


            //получаем количество выздоровевших
            Elements totalHealthByClass = doc.getElementsByClass("info-block healed");
            Elements totalHealthByTag = totalHealthByClass.select("i");
            Elements dailyHealthByTag = totalHealthByClass.select("span");
            String total2 = totalHealthByTag.text();
            String dailyTotal2 = dailyHealthByTag.text();

            //получаем количество погибших
            Elements totalDeadByClass = doc.getElementsByClass("info-block deaths");
            Elements dailyDeadByTag = totalDeadByClass.select("span");
            Elements totalDeadByTag = totalDeadByClass.select("i");
            String total3 = totalDeadByTag.text();
            String dailyTotal3 = dailyDeadByTag.text();



            if (total != null && total2 != null && total3 != null) {
                totalRussiaDead = total3;
                totalRussiaHealth = total2;
                totalRussiaDisease = total;
            } else {
                totalRussiaDisease = "Error";
            }

            if (dailyTotal != null && dailyTotal2 != null && dailyTotal3 != null) {
                dailyRussiaDisease = dailyTotal;
                dailyRussiaHealth = dailyTotal2;
                dailyRussiaDead = dailyTotal3;
            } else {
                dailyRussiaDisease = "Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            totalDead.setText(totalRussiaDead);
            totalHealth.setText(totalRussiaHealth);
            totalRussia.setText(totalRussiaDisease);

            totalTodayDead.setText(dailyRussiaDead);
            totalTodayHealth.setText(dailyRussiaHealth);
            totalTodayRussia.setText(dailyRussiaDisease);
        }
    }
}
