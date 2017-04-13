package edu.uw.uicomponentsdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoviesFragment frag = MoviesFragment.newInstance("Menu");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, frag)
                .commit();

    }

    public void handleButton(View v) {
        Log.v(TAG, "You clicked me!");
        ActionBar actionBar = getSupportActionBar();

        if(actionBar.isShowing()) {
            actionBar.hide();
        } else {
            actionBar.show();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.hi_menu_item:
                showHelloDialog();
                return true;

            case R.id.bye_menu_item:
                Log.v(TAG, "BYE");


                Context context = this;
                String message = "sliced bread browned on both sides by exposure to radiant heat.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, message, duration);
                toast.show();

                Toast.makeText(this, "sliced bread browned on both sides by exposure to radiant heat.", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "sliced bread browned on both sides by exposure to radiant heat.", Toast.LENGTH_LONG).show();

                Toast.makeText(this, "somethingdifferent", Toast.LENGTH_LONG).show();

                Toast.makeText(this, "sliced bread browned on both sides by exposure to radiant heat.", Toast.LENGTH_LONG).show();

                Toast.makeText(this, "sliced bread browned on both sides by exposure to radiant heat.", Toast.LENGTH_LONG).show();

                Toast.makeText(this, "last message", Toast.LENGTH_LONG).show();


                return true;

            case R.id.reasonable_menu_item:
                Log.v(TAG, "Sounds reasonable.");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showHelloDialog() {
        Log.v(TAG, "HI");

        HelloDialogFrament frag = HelloDialogFrament.newInstance();
        frag.show(getSupportFragmentManager(), null);

    }

    public static class HelloDialogFrament extends DialogFragment{

        public static HelloDialogFrament newInstance() {
            
            Bundle args = new Bundle();
            
            HelloDialogFrament fragment = new HelloDialogFrament();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("GREETINGS")
                    .setMessage("This is my hello");
            builder.setPositiveButton("Hello!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v(TAG, "They said hi back!");

                }
            });

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }
    }
}
