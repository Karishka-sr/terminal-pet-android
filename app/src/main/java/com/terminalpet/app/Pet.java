package com.terminalpet.app;

import android.content.Context;
import android.content.SharedPreferences;

/** Состояние питомца + сохранение. Логика та же, что в терминальной версии. */
public class Pet {
    public static final int MAX = 100;

    private static final String PREFS = "terminal_pet";
    private static final String K_HUNGER = "hunger";
    private static final String K_ENERGY = "energy";
    private static final String K_HAPPY = "happiness";
    private static final String K_AGE = "age";
    private static final String K_FED = "fed";
    private static final String K_BORN = "born";

    public int hunger = 100;
    public int energy = 100;
    public int happiness = 100;
    public int age = 0;
    public int timesFed = 0;
    public boolean alive = true;
    public String bornOn = "";

    public static Pet load(Context ctx) {
        Pet p = new Pet();
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        p.hunger = sp.getInt(K_HUNGER, 100);
        p.energy = sp.getInt(K_ENERGY, 100);
        p.happiness = sp.getInt(K_HAPPY, 100);
        p.age = sp.getInt(K_AGE, 0);
        p.timesFed = sp.getInt(K_FED, 0);
        p.bornOn = sp.getString(K_BORN, "");
        p.alive = p.hunger > 0 || p.happiness > 0;
        return p;
    }

    public void save(Context ctx) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putInt(K_HUNGER, hunger)
                .putInt(K_ENERGY, energy)
                .putInt(K_HAPPY, happiness)
                .putInt(K_AGE, age)
                .putInt(K_FED, timesFed)
                .putString(K_BORN, bornOn)
                .apply();
    }

    public void reset() {
        hunger = 100;
        energy = 100;
        happiness = 100;
        age = 0;
        timesFed = 0;
        bornOn = "";
        alive = true;
    }

    public String mood() {
        if (!alive) return "sad";
        if (energy <= 15) return "asleep";
        if (hunger < 30) return "hungry";
        if (energy <= 40) return "sleepy";
        if (happiness < 30) return "sad";
        return "happy";
    }

    public String onFeed() {
        if (!alive) return "…питомца больше нет в этом терминале.";
        boolean wasStarving = hunger < 20;
        hunger = Math.min(MAX, hunger + 35);
        timesFed++;
        happiness = Math.min(MAX, happiness + 5);
        if (wasStarving) return "Наконец-то поел! Спасибо, я умирал с голоду. 😭";
        if (hunger >= MAX) return "Сыт по горло и довольно урчит. 🍖";
        return "Грызёт угощение. Умм, вкусно!";
    }

    public String onPlay() {
        if (!alive) return "…питомца больше нет в этом терминале.";
        if (energy < 15) return "Слишком устал, просится спать. 💤";
        if (hunger < 30) return "Слишком голоден, чтобы играть. Сначала покорми!";
        energy = Math.max(0, energy - 15);
        happiness = Math.min(MAX, happiness + 10);
        hunger = Math.max(0, hunger - 5);
        return "Гоняется за курсором! Это было весело. 🎾";
    }

    public String onSleep() {
        if (!alive) return "…питомца больше нет в этом терминале.";
        energy = Math.min(MAX, energy + 50);
        happiness = Math.min(MAX, happiness + 5);
        return "Сладко зевает и засыпает прямо на клавиатуре. zZz… 💤";
    }

    public String onTick() {
        if (!alive) return "Питомец ушёл в большой скроллбуфер…";
        age++;
        hunger = Math.max(0, hunger - 3);
        if (happiness > 0 && hunger == 0) {
            happiness = Math.max(0, happiness - 10);
        }
        if (age % 3 == 0) {
            energy = Math.max(0, energy - 2);
        }
        if (hunger == 0 && happiness == 0) {
            alive = false;
            happiness = 0;
            energy = 0;
            return "…питомец тихо попрощался и ушёл в большой скроллбуфер. 💔";
        }
        return randomIdle();
    }

    private String randomIdle() {
        String[] idle = {
            "Тик-так. Время идёт, а я живу в твоём телефоне. ⏳",
            "Скучаю… Покорми меня или поиграй!",
            "Смотрю на твои уведомления. Никому не скажу. 🤫",
            "Терминал скучает по тебе, когда приложение закрыто."
        };
        return idle[(age + timesFed) % idle.length];
    }
}