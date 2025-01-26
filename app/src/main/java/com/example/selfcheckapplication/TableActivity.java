package com.example.selfcheckapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TableActivity extends AppCompatActivity {

    private TableLayout dataTable;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "TableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        dataTable = findViewById(R.id.data_table);
        sharedPreferences = getSharedPreferences("SelfCheckData", MODE_PRIVATE);

        updateTable();
    }

    /**
     * **セルフチェックの結果をテーブルに表示**
     */
    private void updateTable() {
        dataTable.removeAllViews(); // **古いデータを削除**

        // **ヘッダー行を追加**
        TableRow headerRow = new TableRow(this);
        addCellToRow(headerRow, "項目", true);
        addCellToRow(headerRow, "評価", true);
        dataTable.addView(headerRow);

        // **各データをテーブルに追加**
        for (int sectionId : MainActivity.SECTION_TITLES.keySet()) {
            String title = MainActivity.SECTION_TITLES.get(sectionId);
            String value = sharedPreferences.getString("option_text_" + sectionId, "未選択");

            TableRow row = new TableRow(this);
            addCellToRow(row, title, false);
            addCellToRow(row, value, false);
            dataTable.addView(row);
        }

        // **追加情報（予防・回復対処 & 備考）**
        addTextDataToTable("予防・回復対処", sharedPreferences.getString("prevention_recovery", ""));
        addTextDataToTable("備考", sharedPreferences.getString("notes", ""));
    }

    /**
     * **テーブルの行にセルを追加**
     */
    private void addCellToRow(TableRow row, String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 8, 16, 8);
        if (isHeader) {
            textView.setTextSize(16);
            textView.setTypeface(null, android.graphics.Typeface.BOLD); // **ヘッダーは太字**
        }
        row.addView(textView);
    }

    /**
     * **「予防・回復対処」「備考」をテーブルに追加**
     */
    private void addTextDataToTable(String title, String value) {
        TableRow row = new TableRow(this);
        addCellToRow(row, title, false);
        addCellToRow(row, value.isEmpty() ? "なし" : value, false);
        dataTable.addView(row);
        Log.d(TAG, title + ": " + value);
    }
}
