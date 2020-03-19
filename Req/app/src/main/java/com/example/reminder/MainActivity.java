package com.example.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, Dialog.DialogLestener , EditDialog.EditDialogListener {
    RemindersDbAdapter db;
    ListView listView;
    RemindersSimpleCursorAdapter cursorAda;
    Cursor cursor;
    int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        db = new RemindersDbAdapter(this);
        db.open();
        this.cursor= db.fetchAllReminders();
        cursorAda = new RemindersSimpleCursorAdapter(this,
                R.layout.reminders_row,
                cursor,
                new String[]{RemindersDbAdapter.COL_CONTENT},
                new int[]{R.id.remindertodo},
                0
        );
        listView.setAdapter(cursorAda);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
                showPopUp(view);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add :
                Toast.makeText(this,"you want to add",Toast.LENGTH_SHORT).show();
                openDialog();
                ////TODO:code el add Done yastaaaaa
                return true;
            case R.id.exit:
                finish();
                /////TODO:code el exit can't hear can't see can't smell
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void addReminderInfo(String content, boolean important) {
        db.createReminder(content,important);
        this.cursor=db.fetchAllReminders();
        cursor.moveToFirst();
        cursorAda = new RemindersSimpleCursorAdapter(MainActivity.this,
                R.layout.reminders_row,
                cursor,
                new String[]{RemindersDbAdapter.COL_CONTENT},
                new int[]{R.id.remindertodo},
                0);
        listView.setAdapter(cursorAda);

    //TODO: ADD in DB Done
    }


    @Override
    public void editReminderInfo(String content, boolean important)
    {
        cursor.moveToPosition(selectedIndex);
        Reminder reminder = db.fetchReminderById(cursor.getInt(0));
        reminder.setContent(content);
        reminder.setImportant(important?1:0);
        db.updateReminder(reminder);
        this.cursor=db.fetchAllReminders();
        cursor.moveToFirst();
        cursorAda = new RemindersSimpleCursorAdapter(MainActivity.this,
                R.layout.reminders_row,
                cursor,
                new String[]{RemindersDbAdapter.COL_CONTENT},
                new int[]{R.id.remindertodo},
                0);
        listView.setAdapter(cursorAda);
    //TODO: Edit an Item

    }


    public void showPopUp(View V)
    {
        PopupMenu pop =new PopupMenu(this,V);
        pop.setOnMenuItemClickListener(this);
        pop.inflate(R.menu.edit_menu);
        pop.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       switch (item.getItemId())
       {
           case R.id.edit:
               Toast.makeText(this,"you want to edit",Toast.LENGTH_SHORT).show();
               openEditDialog();
               return true;
           case R.id.delete:
               //TODO: DELETE LOGIC
               cursor.moveToPosition(selectedIndex);
               //Reminder reminder = db.fetchReminderById(cursor.getInt(0));
               //db.deleteReminderById(reminder.getId());
               db.deleteReminderById(cursor.getInt(0));
               cursor=db.fetchAllReminders();
               cursor.moveToFirst();
               cursorAda.changeCursor(cursor);
//               cursorAda = new RemindersSimpleCursorAdapter(MainActivity.this,
//                       R.layout.reminders_row,
//                       cursor,
//                       new String[]{RemindersDbAdapter.COL_CONTENT},
//                       new int[]{R.id.remindertodo},
//                       0);
               listView.setAdapter(cursorAda);
               Toast.makeText(this,"you want to delete",Toast.LENGTH_SHORT).show();
               return true;
           default:
               return false;

       }

       }


    public void openEditDialog()
    {
        EditDialog dialog = new EditDialog();
        //TODO keep text of selected item
        cursor.moveToPosition(selectedIndex);
        dialog.setImpVal(cursor.getInt(2)==1);
        dialog.setText(cursor.getString(1));
         /* t.setText(cursor.getString(1));
    b.setChecked(cursor.getInt(2)==1);*/
        dialog.show(getSupportFragmentManager(),"EditDialog");
    }

    public void openDialog()
    {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(),"Dialog");
    }

}

