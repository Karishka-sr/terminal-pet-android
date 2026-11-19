package com.terminalpet.app;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Pet pet;
    private final Map<String, String[]> art = new LinkedHashMap<>();

    private TextView tvPet;
    private TextView tvMood;
    private TextView tvStats;
    private TextView tvMessage;
    private LinearProgressIndicator progressHunger;
    private LinearProgressIndicator progressEnergy;
    private LinearProgressIndicator progressHappy;
    private MaterialButton btnFeed;
    private MaterialButton btnPlay;
    private MaterialButton btnSleep;
    private MaterialButton btnTick;
    private MaterialButton btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildArt();

        tvPet = findViewById(R.id.tvPet);
        tvMood = findViewById(R.id.tvMood);
        tvStats = findViewById(R.id.tvStats);
        tvMessage = findViewById(R.id.tvMessage);
        progressHunger = findViewById(R.id.progressHunger);
        progressEnergy = findViewById(R.id.progressEnergy);
        progressHappy = findViewById(R.id.progressHappy);
        btnFeed = findViewById(R.id.btnFeed);
        btnPlay = findViewById(R.id.btnPlay);
        btnSleep = findViewById(R.id.btnSleep);
        btnTick = findViewById(R.id.btnTick);
        btnNew = findViewById(R.id.btnNew);

        pet = Pet.load(this);
        if (pet.bornOn.isEmpty()) {
            pet.bornOn = java.time.LocalDate.now().toString();
            pet.save(this);
        }

        btnFeed.setOnClickListener(v -> act(pet::onFeed));
        btnPlay.setOnClickListener(v -> act(pet::onPlay));
        btnSleep.setOnClickListener(v -> act(pet::onSleep));
        btnTick.setOnClickListener(v -> act(pet::onTick));
        btnNew.setOnClickListener(v -> {
            pet.reset();
            pet.save(this);
            render("Новый питомец родился прямо в твоём телефоне! ✨");
            bounce();
        });

        render("Привет! Это я, твой терминальный питомец. Заботься обо мне!");
    }

    private interface Action {
        String run();
    }

    private void act(Action a) {
        if (!pet.alive) {
            render("Питомец ушёл в большой скроллбуфер. Нажми «Новый питомец».");
            setButtonsEnabled(false);
            return;
        }
        String message = a.run();
        pet.save(this);
        render(message);
        if ("sleep".equals(pet.mood())) {
            // немного вздремнуть визуально — трясём зверька
            ObjectAnimator.ofFloat(tvPet, "translationY", 0f, 4f, 0f, -4f, 0f)
                    .setDuration(600)
                    .start();
        } else {
            bounce();
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        btnFeed.setEnabled(enabled);
        btnPlay.setEnabled(enabled);
        btnSleep.setEnabled(enabled);
        btnTick.setEnabled(enabled);
    }

    private void bounce() {
        ObjectAnimator.ofFloat(tvPet, "scaleX", 1f, 1.12f, 1f)
                .setDuration(400)
                .start();
        ObjectAnimator.ofFloat(tvPet, "scaleY", 1f, 1.12f, 1f)
                .setDuration(400)
                .start();
    }

    private void render(String message) {
        String mood = pet.mood();
        tvPet.setText(String.join("\n", art.get(mood)));
        tvMood.setText(emoji(mood) + " " + mood);
        tvMood.setTextColor(ContextCompat.getColor(this, colorFor(mood)));

        progressHunger.setProgress(pet.hunger);
        progressEnergy.setProgress(pet.energy);
        progressHappy.setProgress(pet.happiness);
        tvStats.setText(
                "Возраст: " + pet.age + " тиков   ·   День рождения: " + pet.bornOn
                        + "   ·   Накормлен: " + pet.timesFed + " раз"
        );
        if (!pet.alive) {
            tvMood.setText("(;_;) " + getString(R.string.hint_dead));
            setButtonsEnabled(false);
        }
        tvMessage.setText(message);
    }

    private String emoji(String mood) {
        switch (mood) {
            case "happy": return "(^_^)";
            case "hungry": return "(o.o)";
            case "sleepy": return "(-_-)";
            case "asleep": return "(u.u)zZ";
            default: return "(;_;)";
        }
    }

    private int colorFor(String mood) {
        if ("happy".equals(mood)) return R.color.accent_cyan;
        if ("sad".equals(mood)) return R.color.accent_violet;
        return R.color.text_primary;
    }

    private void buildArt() {
        art.put("happy", new String[]{
                "   _   _  ",
                "  ( \\_/ ) ",
                "  ( ^.^ ) ",
                " z > ~ < z",
                "  	     ",
                "/‾‾‾‾‾‾‾‾‾‾‾\\"
        });
        art.put("hungry", new String[]{
                "   _   _  ",
                "  ( \\_/ ) ",
                "  ( o.o ) ",
                " z > ~ < z",
                "  ~~~~~  ",
                "  🍽  "
        });
        art.put("sleepy", new String[]{
                "   _   _  ",
                "  ( \\_/ ) ",
                "  ( -_- ) ",
                " z > ~ < z",
                "   ….….   "
        });
        art.put("sad", new String[]{
                "   _   _  ",
                "  ( \\_/ ) ",
                "  ( ;_; ) ",
                " z > ~ < z",
                "   ~~~~   "
        });
        art.put("asleep", new String[]{
                "   _   _  ",
                "  ( \\_/ ) ",
                "  ( u.u ) ",
                " z > ~ < z",
                "zZz  zZz  "
        });
    }
}