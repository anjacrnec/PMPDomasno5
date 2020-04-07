package com.example.pmpdomasno2;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.system.Os;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;


public class KreirajNovProduktFragment extends Fragment {
    ArrayList<Produkt> listaProdukti;
    ArrayList<String> novoDodadeni;
   ArrayList<String> listaProduktiIminja;
    Boolean daliValiden;
    String porakaValidnost;

    ListView lvProduktiKratko;
    Boolean proveriLvProduktiKratko;


    public KreirajNovProduktFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v= inflater.inflate(R.layout.fragment_kreiraj_nov_produkt, container, false);

        int t=Tema.odrediTema(getContext());
        TextView tv=(TextView)v.findViewById(R.id.txtNov);
        LinearLayout ll=(LinearLayout) v.findViewById(R.id.ll);
        Tema.setTemaSliki(getContext(),t,tv);
        Tema.setTemaSliki(getContext(),t,ll);
            //Intent intent = getActivity().getIntent();
            //listaProdukti = intent.getParcelableArrayListExtra("listaProduktiIntent");
           listaProduktiIminja = new ArrayList<String>();
            novoDodadeni = new ArrayList<String>();
            if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT) {
               listaProdukti = KreirajNovProizvodActivity.getListaProdukt();
               listaProduktiIminja = KreirajNovProizvodActivity.getListaIminja();
              // for (int i = 0; i < listaProdukti.size(); i++)
                    //listaProduktiIminja.add(listaProdukti.get(i).getIme());

            }
            else
            {
                listaProdukti=OsnovenFragment.getListProdukt();
                for (int i = 0; i < listaProdukti.size(); i++)
                    listaProduktiIminja.add(listaProdukti.get(i).getIme());
            }



        final Button btnNovProdukt=(Button) v.findViewById(R.id.btnNovProdukt);
        btnNovProdukt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                EditText et=(EditText)v.findViewById(R.id.tbNovProdukt);
                try {
                    kreirajNovProdukt(et);
                    if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
                    {
                        OsnovenFragment.adapter.notifyDataSetChanged();
                        OsnovenFragment.momentalnoSelektirani.add(new Produkt(listaProdukti.get(listaProdukti.size()-1).getIme(),listaProdukti.get(listaProdukti.size()-1).getCounter()));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });


        return v;
    }

    public Boolean kreirajNovProdukt(EditText et) throws FileNotFoundException {

       daliValiden=true;
        porakaValidnost="";
        proveriValidnostNanNovProdukt(et);
        if(daliValiden) {
            PrintStream ps = new PrintStream(getActivity().openFileOutput("novoDodadeniProdukti", getActivity().MODE_APPEND));
            String ime = et.getText().toString();
            ps.println(ime);
            ps.close();
            Produkt p = new Produkt(ime,ime, 0, R.drawable.placeholder);
            listaProdukti.add(p);
            listaProduktiIminja.add(ime);
            novoDodadeni.add(ime);

            et.setText("");
            String poraka = getContext().getResources().getString(R.string.uspeshnoKreiranProdukt) + ime;
            Toast.makeText(getActivity(), poraka+" "+porakaValidnost, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(),porakaValidnost,Toast.LENGTH_SHORT).show();
        }

        return daliValiden;
    }

    public void proveriValidnostNanNovProdukt(EditText et)
    {

        Resources res=getContext().getResources();
        String ime=et.getText().toString();
        char [] imeChar=ime.toCharArray();

        if(ime=="" || ime.isEmpty())
        {
            porakaValidnost=res.getString(R.string.warningProduktBezIme);
            daliValiden=false;

        }
        else {

            for (int i = 0; i < imeChar.length; i++) {
                if (Character.isDigit(imeChar[i])) {
                    porakaValidnost = res.getString(R.string.warningProduktiBroj, ime);
                    daliValiden = false;
                }
            }

            if (daliValiden) {
                for (int i = 0; i < listaProdukti.size(); i++) {
                    if (ime.equalsIgnoreCase(listaProdukti.get(i).getIme()) || ime.equalsIgnoreCase(listaProdukti.get(i).getKod())) {
                        porakaValidnost = res.getString(R.string.warningProduktistoIme, ime);
                        daliValiden = false;

                    }
                    if (!daliValiden)
                        break;
                }
            }
        }
    }


}
