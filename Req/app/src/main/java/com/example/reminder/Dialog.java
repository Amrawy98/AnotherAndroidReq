package com.example.reminder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.security.PrivateKey;

public class Dialog extends AppCompatDialogFragment {
    private EditText content;
    private CheckBox imp;
    private DialogLestener dialogLestener;
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_custom, null);
        builder.setView(view).setTitle("Add Reminder").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Commit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String new_content =  content.getText().toString();
                Boolean new_imp = imp.isChecked();
                dialogLestener.reminderInfo(new_content,new_imp);
            }
        });
        content = view.findViewById(R.id.new_rem);
        imp = view.findViewById(R.id.isImportant);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialogLestener = (DialogLestener) context;
    }

    public interface DialogLestener
    {
        void reminderInfo(String content, boolean important);

    }
}
