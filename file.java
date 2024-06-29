package com.example.acharya;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button createButton;
    private Button saveButton;
    private Button openButton;
    private Button deleteButton;
    private Button renameButton;
    private TextView filePathTextView;
    private File file;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        createButton = findViewById(R.id.createButton);
        saveButton = findViewById(R.id.saveButton);
        openButton = findViewById(R.id.openButton);
        deleteButton = findViewById(R.id.deleteButton);
        renameButton = findViewById(R.id.renameButton);
        filePathTextView = findViewById(R.id.filePathTextView);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
            }
        });

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameFile();
            }
        });
    }

    private void createFile() {
        if (file != null) {
            Toast.makeText(this, "A file has already been created.", Toast.LENGTH_SHORT).show();
            return;
        }

        String text = editText.getText().toString();

        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter some text.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "External storage is not writable.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            File directory = getExternalFilesDir(null);
            file = new File(directory, "my_file.txt");
            File newTextFile = new File(directory, "my_file.txt");
            FileWriter writer = new FileWriter(newTextFile);
            writer.append(text);
            writer.flush();
            writer.close();
            Toast.makeText(this, "File created successfully.", Toast.LENGTH_SHORT).show();
            enableButtons();
            showFilePath(newTextFile);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create the file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFile() {
        if (file == null) {
            Toast.makeText(this, "First create a file.", Toast.LENGTH_SHORT).show();
            return;
        }

        String text = editText.getText().toString();

        try {
            FileWriter writer = new FileWriter(file);
            writer.append(text);
            writer.flush();
            writer.close();
            Toast.makeText(this, "File saved successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save the file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFile() {
        if (file == null) {
            Toast.makeText(this, "First create a file.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            reader.close();

            editText.setText(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to open the file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFile() {
        if (file == null) {
            Toast.makeText(this, "First create a file.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isDeleted = file.delete();
        if (isDeleted) {
            Toast.makeText(this, "File deleted successfully.", Toast.LENGTH_SHORT).show();
            editText.setText("");
            file = null;
            disableButtons();
            hideFilePath();
        } else {
            Toast.makeText(this, "Failed to delete the file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void renameFile() {
        if (file == null) {
            Toast.makeText(this, "First create a file.", Toast.LENGTH_SHORT).show();
            return;
        }

        String newFileName = editText.getText().toString();
        if (newFileName.isEmpty()) {
            Toast.makeText(this, "Please enter a new file name.", Toast.LENGTH_SHORT).show();
            return;
        }

        File directory = file.getParentFile();
        File newFile = new File(directory, newFileName + ".txt");
        boolean isRenamed = file.renameTo(newFile);

        if (isRenamed) {
            Toast.makeText(this, "File renamed successfully.", Toast.LENGTH_SHORT).show();
            file = newFile;
            showFilePath(file);
        } else {
            Toast.makeText(this, "Failed to rename the file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableButtons() {
        saveButton.setEnabled(true);
        deleteButton.setEnabled(true);
        renameButton.setEnabled(true);
    }

    private void disableButtons() {
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
        renameButton.setEnabled(false);
    }

    private void showFilePath(File file) {
        String filePath = "File Path: " + file.getAbsolutePath();
        filePathTextView.setText(filePath);
        filePathTextView.setVisibility(View.VISIBLE);
    }

    private void hideFilePath() {
        filePathTextView.setVisibility(View.GONE);
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}