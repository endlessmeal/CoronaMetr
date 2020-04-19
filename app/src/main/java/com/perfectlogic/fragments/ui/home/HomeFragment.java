package com.perfectlogic.fragments.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.perfectlogic.fragments.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView totalRussia;
    private TextView totalText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        totalRussia = root.findViewById(R.id.totalRussia);
        totalText = root.findViewById(R.id.totalText);

        GetData gd = new GetData();
        gd.execute();

        Date currentDay = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("dd MMMM", myDateFormatSymbols);
        totalText.setText("Всего заболевших в России на " + dateFormat.format(currentDay));

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
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

        String total;

        @Override
        protected Void doInBackground(Void... voids) {
            Document doc = null;

            try {
                doc = Jsoup.connect("https://coronavirus-monitor.ru/coronavirus-v-rossii/").get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Elements element = doc.getElementsByClass("info-block disease");
            Elements elem = element.select("i");
            String elems = elem.text();

            if (elems != null) {
                total = elems ;
            } else {
                total = "Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            totalRussia.setText(total);
        }
    }
}
