package com.perfectlogic.fragments.ui.dashboard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.perfectlogic.fragments.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextView totalWorld;
    private TextView totalText;

    private TextView totalDead;
    private TextView totalHealth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        totalWorld = root.findViewById(R.id.totalWorld);
        totalText = root.findViewById(R.id.totalText);

        totalHealth = root.findViewById(R.id.totalWorldHealth);
        totalDead = root.findViewById(R.id.totalWorldDead);

        GetWorldData gwd = new GetWorldData();
        gwd.execute();

        Date currentDay = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("dd MMMM", myDateFormatSymbols);
        totalText.setText("Всего заболевших в Мире на " + dateFormat.format(currentDay));

        return root;
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols(){

        @Override
        public String[] getMonths() {
            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        }

    };

    class GetWorldData extends AsyncTask<Void, Void, Void> {

        String totalWorldDisease;
        String totalWorldHealth;
        String totalWorldDead;

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;

            try {
                doc = Jsoup.connect("https://www.worldometers.info/coronavirus/").get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Elements totalWorldByClass = doc.getElementsByClass("maincounter-number");
            Elements totalWorldByTag = totalWorldByClass.tagName("span");
            String total = totalWorldByTag.get(0).text();

            Elements totalHealthByClass = doc.getElementsByClass("maincounter-number");
            Elements totalHealthByTag = totalHealthByClass.tagName("span");
            String total2 = totalHealthByTag.get(1).text();

            Elements totalDeadByClass = doc.getElementsByClass("maincounter-number");
            Elements totalDeadByTag = totalDeadByClass.tagName("span");
            String total3 = totalDeadByTag.get(2).text();

            if (total != null && total2 != null && total3 != null) {
                totalWorldDisease = total;
                totalWorldHealth = total2;
                totalWorldDead = total3;
            } else {
                totalWorldDisease = "Error";
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            totalWorld.setText(totalWorldDisease);
            totalHealth.setText(totalWorldHealth);
            totalDead.setText(totalWorldDead);
        }
    }
}
