package badmexican333.volumizer17;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class helpMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_menu);

        TextView helptext =(TextView)findViewById(R.id.helpText);

        helptext.setMovementMethod(new ScrollingMovementMethod());
    }
}
