package com.example.biblite;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils extends AsyncTask<String, Void, String> {
    static ArrayList<PodcastEpisode> eps = new ArrayList<PodcastEpisode>();
    MainFragment f;
public HttpUtils(MainFragment f)
{
    this.f = f;
    eps = new ArrayList<PodcastEpisode>();
}
    protected String doInBackground(String... urls) {
return getContents(urls[0]);

    }

    public static String getContents(String url) {
        String contents ="";

        try {
            URLConnection conn = new URL(url).openConnection();

            InputStream in = conn.getInputStream();
            contents = convertStreamToString(in);
        } catch (MalformedURLException e) {
Log.d("exception", "malformed");
        } catch (IOException e) {
            Log.d("exception", "IO");
            e.printStackTrace();
        }

        return contents;
    }

    private static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    protected void onPostExecute(String str) {
        List<String> l = extractUrls(str);
        List<String> finals = new ArrayList<String>();
        List<String> finalst = new ArrayList<String>();
        boolean ch1 = false;
        for(String s: l) {

            if (!s.endsWith(".mp3")&&(!s.endsWith(".wav"))&&(!s.endsWith(".wmv"))&&(!s.endsWith(".flv")))
                    continue;


            s = s.split("<")[0];
            s = s.split("\\?")[0];
            if (finals.contains(s) && MainActivity.RSS_TESTING==false)
            {
                continue;
            }
            finals.add(s);
            String[] sp = s.split("\\.");
            String r = sp[sp.length-2];
            String[] sr = r.split("\\/");
            String z = sr[sr.length-1];
            if (finalst.contains(z)&& MainActivity.RSS_TESTING==false)
            continue;
            finalst.add(z);
            z = z.replaceAll("\\d","");
            z = capitalize(z);
            if (!ch1){
            eps.add(new PodcastEpisode(z, s, true));
            ch1 = true;}
            else
                eps.add(new PodcastEpisode(z, s, false));

        }


        ImgAdapter x = new ImgAdapter(f.getActivity(), eps);

        MainFragment.getGridView().setAdapter(x);


         MainFragment.getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position!=PodcastEpisode.open-3) return;
                Toast toast = Toast.makeText(f.getContext(), "Loading...", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(f.getActivity(), DetailActivity.class).

                        putExtra("title", eps.get(position).title.replace('-', ' ')).
                        putExtra("url", eps.get(position).link)
                  ;

                f.startActivity(intent);

            }
        });


    }
    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}

