package badmexican333.volumizer17;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_request);
    }

    public void getPassword(View view)
    {
        EditText messageView = (EditText)findViewById(R.id.passwordInput);

        String password = messageView.getText().toString();

        if(!password.equals(""))
        {

        }
        else
        {
            Toast toast = Toast.makeText(this,"Incorrect Password Entered", Toast.LENGTH_SHORT);
            toast.show();
        }


    }
}
