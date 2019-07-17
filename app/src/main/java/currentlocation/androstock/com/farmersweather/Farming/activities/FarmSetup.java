package currentlocation.androstock.com.farmersweather.Farming.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import currentlocation.androstock.com.farmersweather.R;

public class FarmSetup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_setup);

        populateSoilSpinner();
        populateStateSpinner();

        Button btn = (Button) findViewById(R.id.activity_farm_setup_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etArea = (EditText) findViewById(R.id.et_area);
                String area = etArea.getText().toString();
                Spinner spSoil = (Spinner)findViewById(R.id.soil_type_spinner);
                String soil = spSoil.getSelectedItem().toString();
                Spinner spState = (Spinner)findViewById(R.id.state_spinner);
                String state = spState.getSelectedItem().toString();
                if (!area.isEmpty() && soil != null && state != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("pref", MODE_PRIVATE).edit();
                    editor.putString("area", area);
                    editor.putString("area_available", area);
                    editor.putString("soil", soil);
                    editor.putString("state", state);
                    editor.apply();
                    //finish();
                    Intent intent = new Intent(FarmSetup.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void populateStateSpinner() {
        Spinner dropdown = (Spinner)findViewById(R.id.state_spinner);
        String[] items = new String[]{
                "Abim","Adjumani","Agago","Alebtong","Amolatar","Amudat","Amuria","Amuru","Apac","Arua","Budaka","Bududa","Bugiri","Buhweju","Buikwe","Bukedea","Bukomansimbi","Bukwo","Bulambuli","Buliisa","Bundibugyo","Bushenyi","Busia","Butaleja","Butambala","Buvuma","Buyende","Dokolo"
                ,"Gomba","Gulu","Hoima","Ibanda","Iganga","Isingiro","Jinja","Kaabong","Kabale","Kabarole","Kaberamaido","Kalangala","Kaliro","Kalungu","Kampala","Kamuli","Kamwenge","Kanungu","Kapchorwa","Kasese","Katakwi","Kayunga","Kibaale","Kiboga","Kibuku","Kiruhuura"
                ,"Kiryandongo","Kisoro","Kitgum","Koboko","Kole","Kotido","Kumi","Kween","Kyankwanzi","Kyegegwa","Kyenjojo","Lamwo","Lira","Luuka","Luwero","Lwengo","Lyantonde","Manafwa","Masaka","Masindi","Mayuge","Mbale","Mbarara","Mitoma","Mityana","Moroto","Moyo"
                ,"Mpigi","Mubende","Mukono","Nakapiripirit","Nakaseke","Nakasongola","Namayingo","Namutumba","Napak","Nebbi","Ngora","Ntoroko","Ntungamo","Nwoya","Nyadri","Otuke","Oyam","Pader","Pallisa","Rakai","Rubirizi","Rukungiri","Sembabule","Serere","Sheema","Sironko","Soroti"
                ,"Tororo","Wakiso","Yumbe","Zombo"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    private void populateSoilSpinner() {
        Spinner dropdown = (Spinner)findViewById(R.id.soil_type_spinner);
        String[] items = new String[]{"Alluvial", "Loamy", "Black", "Red", "Laterite"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }
}
