package com.brainshift.app;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    static class Q {
        String q, a, hint;
        Q(String q, String a, String hint) {
            this.q = q;
            this.a = a;
            this.hint = hint;
        }
    }

    ArrayList<Q> questions = new ArrayList<>();
    int current = 0;
    int xp = 0, score = 0, streak = 0, solved = 0;

    TextView question, stats, routine, feedback, xpText;
    EditText answer;
    Button check;

    int dp(int n) {
        return (int)(n * getResources().getDisplayMetrics().density);
    }

    GradientDrawable bg(int color, int radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    TextView tv(String text, int size) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(Color.rgb(25,25,35));
        return t;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        loadQuestions();

        android.content.SharedPreferences p = getPreferences(0);
        xp = p.getInt("xp", 0);
        score = p.getInt("score", 0);
        streak = p.getInt("streak", 0);
        solved = p.getInt("solved", 0);

        createUI();
        showQuestion();
    }

    void loadQuestions() {
        questions.add(new Q("What is 25% of 80?", "20", "25% is one-fourth."));
        questions.add(new Q("What comes next: 3, 6, 9, 12, ?", "15", "Add 3."));
        questions.add(new Q("You have ₹500 and spend ₹135. How much is left?", "365", "Subtract 135."));
        questions.add(new Q("What is 10% of ₹300?", "30", "10% means one-tenth."));
        questions.add(new Q("Which is greater: 47 or 74?", "74", "Compare the tens."));
        questions.add(new Q("What is half of 90?", "45", "Divide by 2."));
        questions.add(new Q("₹100 becomes ₹120. What is the profit percentage?", "20", "Profit is ₹20 on ₹100."));
        questions.add(new Q("What comes next: 2, 4, 8, 16, ?", "32", "The numbers double."));
        questions.add(new Q("If 5 pens cost ₹50, what does 1 pen cost?", "10", "Divide by 5."));
        questions.add(new Q("A ₹200 item gets ₹20 discount. Selling price?", "180", "Subtract ₹20."));
        questions.add(new Q("₹1,000 earns ₹100 interest. Total amount?", "1100", "Add principal and interest."));
        questions.add(new Q("Which is smaller: 86 or 68?", "68", "Compare the tens."));
        questions.add(new Q("What is 15 + 27?", "42", "Add tens and ones."));
        questions.add(new Q("A product costs ₹400 and sells for ₹500. Profit?", "100", "Selling price minus cost."));
        questions.add(new Q("If today is Monday, what day is after tomorrow?", "Wednesday", "Tomorrow is Tuesday."));
        questions.add(new Q("A clock shows 6. Which number is opposite 6?", "12", "Look across the centre."));
        questions.add(new Q("What is 20% of 50?", "10", "One-fifth of 50."));
        questions.add(new Q("What comes before 70?", "69", "One less than 70."));
        questions.add(new Q("What comes after 88?", "89", "One more than 88."));
        questions.add(new Q("A ₹1,000 investment earns 10% for one year. Interest?", "100", "10% of ₹1,000."));
    }

    void createUI() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18),dp(22),dp(18),dp(22));
        root.setBackgroundColor(Color.rgb(246,247,251));

        TextView brand = tv("BRAIN SHIFT",12);
        brand.setTextColor(Color.rgb(91,75,219));
        brand.setTypeface(null,1);
        root.addView(brand);

        TextView title = tv("Focus. Think. Grow. 🧠",24);
        title.setTypeface(null,1);
        root.addView(title);

        LinearLayout levelBox = new LinearLayout(this);
        levelBox.setPadding(dp(15),dp(12),dp(15),dp(12));
        levelBox.setGravity(Gravity.CENTER_VERTICAL);
        levelBox.setBackground(bg(Color.WHITE,18));

        xpText = tv("",15);
        xpText.setTextColor(Color.rgb(91,75,219));
        xpText.setTypeface(null,1);

        levelBox.addView(xpText);
        root.addView(levelBox,
                new LinearLayout.LayoutParams(-1,dp(55)));

        routine = tv("",14);
        routine.setPadding(0,dp(15),0,dp(10));
        root.addView(routine);

        stats = tv("",13);
        stats.setPadding(0,0,0,dp(12));
        root.addView(stats);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(17),dp(17),dp(17),dp(17));
        card.setBackground(bg(Color.WHITE,20));

        TextView label = tv("YOUR NEXT CHALLENGE",11);
        label.setTextColor(Color.GRAY);
        label.setTypeface(null,1);
        card.addView(label);

        question = tv("",21);
        question.setPadding(0,dp(18),0,dp(20));
        card.addView(question);

        answer = new EditText(this);
        answer.setHint("Your answer");
        answer.setSingleLine(true);
        answer.setTextSize(17);
        card.addView(answer,
                new LinearLayout.LayoutParams(-1,dp(55)));

        check = new Button(this);
        check.setText("CHECK");
        check.setTextColor(Color.WHITE);
        check.setBackground(bg(Color.rgb(91,75,219),15));

        LinearLayout.LayoutParams bp =
                new LinearLayout.LayoutParams(-1,dp(55));
        bp.topMargin = dp(10);

        card.addView(check,bp);

        feedback = tv("",15);
        feedback.setPadding(0,dp(12),0,0);
        card.addView(feedback);

        root.addView(card,
                new LinearLayout.LayoutParams(-1,0,1));

        TextView footer =
                tv("Offline • No ads • Progress saved locally",11);
        footer.setTextColor(Color.GRAY);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0,dp(12),0,0);

        root.addView(footer);

        setContentView(root);

        check.setOnClickListener(v -> checkAnswer());
    }

    void showQuestion() {

        Q q = questions.get(current % questions.size());

        question.setText(q.q);
        answer.setText("");
        feedback.setText("");

        int today = solved % 20;

        routine.setText(
                "TODAY  •  " + today + "/20 completed  •  " +
                (20-today) + " left\n" +
                "5 Quant  •  5 Finance  •  5 Logic  •  5 Focus"
        );

        stats.setText(
                "🏆 Score: " + score +
                "     🔥 Streak: " + streak +
                "     ✓ Solved: " + solved
        );

        xpText.setText(
                "⚡ " + xp + " XP     •     LEVEL " + (1 + xp/100)
        );

        check.setText("CHECK");
    }

    void checkAnswer() {

        String user = answer.getText().toString()
                .trim()
                .replace("₹","")
                .replace("%","")
                .toLowerCase();

        if(user.isEmpty()) {
            feedback.setTextColor(Color.RED);
            feedback.setText("Enter your answer.");
            return;
        }

        Q q = questions.get(current % questions.size());

        if(user.equals(q.a.toLowerCase())) {

            score++;
            solved++;
            streak++;
            xp += 10;

            if(streak % 5 == 0) {
                xp += 10;
                feedback.setText("✓ Correct!  +10 XP  🎉 Bonus +10 XP");
            } else {
                feedback.setText("✓ Correct!  +10 XP");
            }

            feedback.setTextColor(Color.rgb(20,130,60));

            check.setText("NEXT");

            check.setOnClickListener(v -> {
                current++;
                showQuestion();
                check.setOnClickListener(x -> checkAnswer());
                save();
            });

            save();

        } else {

            streak = 0;

            feedback.setTextColor(Color.rgb(190,30,30));
            feedback.setText("Try again. Hint: " + q.hint);

            save();
        }

        stats.setText(
                "🏆 Score: " + score +
                "     🔥 Streak: " + streak +
                "     ✓ Solved: " + solved
        );

        xpText.setText(
                "⚡ " + xp + " XP     •     LEVEL " + (1 + xp/100)
        );
    }

    void save() {

        getPreferences(0)
                .edit()
                .putInt("xp",xp)
                .putInt("score",score)
                .putInt("streak",streak)
                .putInt("solved",solved)
                .apply();
    }
                      }
