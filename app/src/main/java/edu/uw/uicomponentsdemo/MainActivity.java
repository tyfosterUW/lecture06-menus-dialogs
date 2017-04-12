package edu.uw.uicomponentsdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

        //add a context menu to the big button
        registerForContextMenu(findViewById(R.id.button));
    }

    //toggles the action bar
    public void handleButton(View v) {

        ActionBar toolbar = getSupportActionBar();
        if(toolbar.isShowing())
            toolbar.hide();
        else
            toolbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true; //we've provided a menu!
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.hello_menu_item:
                Log.v(TAG, "Hello!");
                return true; //handled
            case R.id.alert_menu_item:
                showHelloDialog();
                return true; //handled
            case R.id.dialogfrag_menu_item:
                HelloDialogFragment.newInstance().show(getSupportFragmentManager(), null);
                return true; //handled
            case R.id.movies_menu_item:
                MoviesFragment.newInstance("Star Wars").show(getSupportFragmentManager(), null);
                return true; //handled
            case R.id.toast_menu_item:
                Toast.makeText(this, "Hi!", Toast.LENGTH_SHORT).show();
                return true; //handled
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.v(TAG, "You clicked a context menu item!");
        return super.onContextItemSelected(item);
    }

    public void showHelloDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!")
                .setMessage("Danger Will Robinson!"); //note chaining
        builder.setPositiveButton("I see it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.v(TAG, "You clicked okay! Good times :)");
            }
        });
        builder.setNegativeButton("Noooo...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "You clicked cancel! Sad times :(");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static class HelloDialogFragment extends DialogFragment {

        public static HelloDialogFragment newInstance() {

            Bundle args = new Bundle();
            HelloDialogFragment fragment = new HelloDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Alert!")
                    .setMessage("Danger Will Robinson!"); //note chaining
            builder.setPositiveButton("I see it!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.v(TAG, "You clicked okay! Good times :)");
                }
            });
            builder.setNegativeButton("Noooo...", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v(TAG, "You clicked cancel! Sad times :(");
                }
            });

            AlertDialog dialog = builder.create();
            return dialog;
        }
    }
}
