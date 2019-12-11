package com.iut.rroubaix.calculatrice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.iut.rroubaix.calculatrice.Operation.*;

public class Calculatrice extends AppCompatActivity implements View.OnClickListener {

    private float calcul_premier = 0;
    private float calcul_second = 0;
    private Operation processOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculatrice);
    }

    public void onClick(View view) {
        TextView resultTextView = findViewById(R.id.calculResult);
        float resultParsed = 0;

        try {
            resultParsed = Float.parseFloat(resultTextView.getText().toString());

        } catch (Exception e) {
            resultTextView.setText("Erreur de calcul");
        }

        switch (view.getId()) {
            case R.id.b_plus :
                storeResult(resultParsed);
                resultTextView.setText("");
                processOp = ADDITION;
                break;
            case R.id.b_moins :
                storeResult(resultParsed);
                resultTextView.setText("");
                processOp = SOUSTRACTION;
                break;
            case R.id.b_fois :
                storeResult(resultParsed);
                resultTextView.setText("");
                processOp = MULTIPLICATION;
                break;
            case R.id.b_diviser :
                storeResult(resultParsed);
                resultTextView.setText("");
                processOp = DIVISION;
                break;
            case R.id.b_egal :
                storeResult(resultParsed);
                resultTextView.setText(processResult(processOp));
                calcul_second = 0;
                calcul_premier = 0;
                break;
        }

    }

    private String processResult(Operation processOp) {
        float total = 0;
        switch (processOp) {
            case ADDITION:
                total = calcul_second + calcul_premier;
                break;
            case SOUSTRACTION:
                total = calcul_second - calcul_premier;
                break;
            case MULTIPLICATION:
                total = calcul_second * calcul_premier;
                break;
            case DIVISION:
                if (calcul_premier == 0) {
                    try {
                        total = calcul_second / calcul_premier;
                    } catch (NumberFormatException e) {
                        return String.valueOf(R.string.divZero);
                    }
                    break;
                }
        }
        return String.valueOf(total);
    }

    private void storeResult(float resultParsed) {
        if(calcul_second == 0)
            calcul_second = resultParsed;
        else
            calcul_premier = resultParsed;
    }
}
