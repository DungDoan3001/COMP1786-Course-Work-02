package com.dungdoan.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_view_result, txt_view_solution;

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0;
    Button btn_plus, btn_subtract, btn_divide, btn_multiply;
    Button btn_c, btn_ac, btn_open_bracket, btn_close_bracket, btn_equal, btn_dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleAssignIds();
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        String btn_text = btn.getText().toString();
        String calculateData = txt_view_solution.getText().toString();

        if(btn_text.equals(getString(R.string.btn_ac))) {
            txt_view_solution.setText(getString(R.string.txt_view_0));
            txt_view_result.setText(getString(R.string.txt_view_0));
            return;
        }

        if (btn_text.equals(getString(R.string.btn_equal))) {
            txt_view_solution.setText(txt_view_result.getText());
            return;
        }

        if(btn_text.equals(getString(R.string.btn_c))) {
            if(calculateData.length()==1){
                calculateData = getString(R.string.txt_view_0);
            } else {
                calculateData = calculateData.substring(0, calculateData.length() - 1);
            }
        } else {
            calculateData = calculateData + btn_text;
        }

        txt_view_solution.setText(calculateData.replaceFirst("^0+(?!$)", ""));

        String result = getResult(calculateData);

        if(!result.equals("Error")) {
            txt_view_result.setText(result);
        }
    }

    private String getResult(String input) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();

            String result = context.evaluateString(scriptable, input, "Javascript", 1, null).toString();

            return  result;
        } catch (Exception ex) {
            return "Error";
        }
    }

    private void handleAssignIds() {
        txt_view_result = findViewById(R.id.text_view_result);
        txt_view_solution = findViewById(R.id.text_view_solution);

        assignId(btn_plus, R.id.btn_plus);
        assignId(btn_subtract, R.id.btn_subtract);
        assignId(btn_multiply, R.id.btn_multiply);
        assignId(btn_divide, R.id.btn_divide);

        assignId(btn_c, R.id.btn_c);
        assignId(btn_ac, R.id.btn_ac);
        assignId(btn_close_bracket, R.id.btn_close_bracket);
        assignId(btn_open_bracket, R.id.btn_open_bracket);
        assignId(btn_equal, R.id.btn_equal);
        assignId(btn_dot, R.id.btn_dot);

        assignId(btn_1, R.id.btn_1);
        assignId(btn_2, R.id.btn_2);
        assignId(btn_3, R.id.btn_3);
        assignId(btn_4, R.id.btn_4);
        assignId(btn_5, R.id.btn_5);
        assignId(btn_6, R.id.btn_6);
        assignId(btn_7, R.id.btn_7);
        assignId(btn_8, R.id.btn_8);
        assignId(btn_9, R.id.btn_9);
        assignId(btn_0, R.id.btn_0);
    }

    private void assignId(Button btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
}