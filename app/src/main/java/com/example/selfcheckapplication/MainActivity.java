package com.example.selfcheckapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText preventionRecovery, notes;
    private SharedPreferences sharedPreferences;

    // **項目の順番を保持するため LinkedHashMap を使用**
    public static final Map<Integer, String> SECTION_TITLES = new LinkedHashMap<>();

    static {
        SECTION_TITLES.put(R.id.sleep_section, "睡眠");
        SECTION_TITLES.put(R.id.diet_section, "食事");
        SECTION_TITLES.put(R.id.exercise_section, "運動");
        SECTION_TITLES.put(R.id.stress_section, "ストレス");
        SECTION_TITLES.put(R.id.no_anxiety_section, "不安がない");
        SECTION_TITLES.put(R.id.calm_section, "心穏やか");
        SECTION_TITLES.put(R.id.talk_section, "誰とでも話せる");
        SECTION_TITLES.put(R.id.movement_section, "体の動き");
        SECTION_TITLES.put(R.id.concentration_section, "集中力");
        SECTION_TITLES.put(R.id.condition_section, "体調");
        SECTION_TITLES.put(R.id.self_trust_section, "自分を信頼");
        SECTION_TITLES.put(R.id.other_trust_section, "他人を信頼");
        SECTION_TITLES.put(R.id.trusted_section, "他人から信頼");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preventionRecovery = findViewById(R.id.prevention_recovery);
        notes = findViewById(R.id.notes);

        sharedPreferences = getSharedPreferences("SelfCheckData", MODE_PRIVATE);

        // **✅ 項目名をセット**
        setSectionTitles();

        // **✅ 初回起動時に UI をリセットし、データは消さない**
        if (isFirstLaunch()) {
            resetUI();
            markFirstLaunch();
            Log.d("MainActivity", "初回起動時に UI をリセットしました（データは保持）");
        }

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> {
            saveData();
            resetUI(); // **データ送信後に画面をリセット**
            Toast.makeText(this, "データが保存されました", Toast.LENGTH_SHORT).show();
        });

        Button btnShowTable = findViewById(R.id.btn_show_table);
        btnShowTable.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TableActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetUI(); // **画面が開くたびに UI をリセット**
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("MainActivity", "戻るボタンが押されました。画面をリセット");
        resetUI(); // **戻るボタンが押されたときにリセット**
    }

    /**
     * **画面をリセット**
     */
    private void resetUI() {
        preventionRecovery.setText(""); // **テキスト部分もクリア**
        notes.setText(""); // **備考部分もクリア**

        for (int sectionId : getSectionIds()) {
            View sectionView = findViewById(sectionId);
            if (sectionView != null) {
                RadioGroup radioGroup = sectionView.findViewById(R.id.item_radio_group);
                if (radioGroup != null) {
                    radioGroup.clearCheck();
                }
            }
        }

        Log.d("MainActivity", "画面の入力をリセットしました（ラジオボタン & テキスト）");
    }

    /**
     * **データを保存**
     */
    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("prevention_recovery", preventionRecovery.getText().toString());
        editor.putString("notes", notes.getText().toString());
        editor.apply();

        Log.d("MainActivity", "データを保存しました");
    }

    /**
     * **初回起動時のチェック**
     */
    private boolean isFirstLaunch() {
        return sharedPreferences.getBoolean("isFirstLaunch", true);
    }

    /**
     * **初回起動フラグを false に設定**
     */
    private void markFirstLaunch() {
        sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply();
    }

    /**
     * **項目名を設定**
     */
    private void setSectionTitles() {
        for (int sectionId : SECTION_TITLES.keySet()) {
            setSectionTitle(sectionId, SECTION_TITLES.get(sectionId));
        }
    }

    /**
     * **各 FrameLayout 内の TextView にタイトルを設定**
     */
    private void setSectionTitle(int frameLayoutId, String title) {
        View sectionView = findViewById(frameLayoutId);
        if (sectionView != null) {
            TextView label = sectionView.findViewById(R.id.item_label);
            if (label != null) {
                label.setText(title);
                Log.d("MainActivity", "設定: " + title);
            }
        }
    }

    /**
     * **すべてのセクションの ID を取得**
     */
    private int[] getSectionIds() {
        int[] sectionIds = new int[SECTION_TITLES.size()];
        int i = 0;
        for (Integer key : SECTION_TITLES.keySet()) {
            sectionIds[i++] = key;
        }
        return sectionIds;
    }
}
