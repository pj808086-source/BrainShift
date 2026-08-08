package com.brainshift.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    static class Q {
        String text;
        String answer;
        String hint;
        String topic;

        Q(String t, String a, String h, String tp) {
            text = t;
            answer = a;
            hint = h;
            topic = tp;
        }
    }

    final ArrayList<Q> qs = new ArrayList<Q>();

    int i = 0;
    int score = 0;
    int streak = 0;
    int solved = 0;
    int xp = 0;
    int dailySolved = 0;

    TextView question;
    TextView feedback;
    TextView scoreV;
    TextView streakV;
    TextView solvedV;
    TextView xpV;
    TextView levelV;
    TextView routineV;
    TextView topicV;

    EditText answer;
    Button action;

    boolean waitingForNext = false;

    SharedPreferences prefs;

    int dp(float n) {
        return (int) (n * getResources().getDisplayMetrics().density + 0.5f);
    }

    TextView text(String s, float size, int color) {
        TextView v = new TextView(this);
        v.setText(s);
        v.setTextSize(size);
        v.setTextColor(color);
        return v;
    }

    GradientDrawable bg(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    LinearLayout box() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(dp(16), dp(14), dp(16), dp(14));
        l.setBackground(bg(Color.WHITE, 18));
        return l;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        prefs = getSharedPreferences("brainshift", MODE_PRIVATE);

        loadQuestions();
        loadProgress();
        buildUi();
        render();
    }

    void loadProgress() {

        score = prefs.getInt("score", 0);
        streak = prefs.getInt("streak", 0);
        solved = prefs.getInt("solved", 0);
        xp = prefs.getInt("xp", 0);

        String today = new SimpleDateFormat(
                "yyyyMMdd",
                Locale.US
        ).format(new Date());

        String savedDate = prefs.getString("date", "");

        if (!today.equals(savedDate)) {
            dailySolved = 0;

            prefs.edit()
                    .putString("date", today)
                    .putInt("dailySolved", 0)
                    .apply();
        } else {
            dailySolved = prefs.getInt("dailySolved", 0);
        }

        if (dailySolved > 20) {
            dailySolved = 20;
        }
    }

    void saveProgress() {

        prefs.edit()
                .putInt("score", score)
                .putInt("streak", streak)
                .putInt("solved", solved)
                .putInt("xp", xp)
                .putInt("dailySolved", dailySolved)
                .apply();
    }

    void loadQuestions() {

        qs.clear();

        // -------------------------
        // QUANT - 5 QUESTIONS
        // -------------------------

        qs.add(new Q(
                "What is 25% of 80?",
                "20",
                "25% means one-fourth.",
                "Quant"
        ));

        qs.add(new Q(
                "What comes next: 3, 6, 9, 12, ?",
                "15",
                "Add 3 each time.",
                "Quant"
        ));

        qs.add(new Q(
                "What is half of 90?",
                "45",
                "Divide 90 by 2.",
                "Quant"
        ));

        qs.add(new Q(
                "What is 10% of ₹300?",
                "30",
                "10% means one-tenth.",
                "Quant"
        ));

        qs.add(new Q(
                "What is 20% of 50?",
                "10",
                "One-fifth of 50.",
                "Quant"
        ));

        // -------------------------
        // FINANCE - 5 QUESTIONS
        // -------------------------

        qs.add(new Q(
                "You have ₹500 and spend ₹135. How much is left?",
                "365",
                "Subtract ₹135 from ₹500.",
                "Finance"
        ));

        qs.add(new Q(
                "A product costs ₹200. Its price increases by 15%. What is the new price?",
                "230",
                "15% of ₹200 is ₹30.",
                "Finance"
        ));

        qs.add(new Q(
                "You invest ₹1,000 and earn ₹100 interest. What is the total amount?",
                "1100",
                "Add interest to principal.",
                "Finance"
        ));

        qs.add(new Q(
                "A share costs ₹100 and rises to ₹120. What is the profit percentage?",
                "20",
                "Profit is ₹20 on ₹100.",
                "Finance"
        ));

        qs.add(new Q(
                "A product bought for ₹400 is sold for ₹500. What is the profit?",
                "100",
                "Selling price minus cost price.",
                "Finance"
        ));

        // -------------------------
        // LOGIC - 5 QUESTIONS
        // -------------------------

        qs.add(new Q(
                "Which is greater: 47 or 74?",
                "74",
                "Compare the tens digits.",
                "Logic"
        ));

        qs.add(new Q(
                "What comes next: 2, 4, 8, 16, ?",
                "32",
                "Each number doubles.",
                "Logic"
        ));

        qs.add(new Q(
                "A clock shows 6:00. Which number is opposite 6?",
                "12",
                "Look directly across the centre.",
                "Logic"
        ));

        qs.add(new Q(
                "If today is Monday, what day comes after tomorrow?",
                "Wednesday",
                "Tomorrow is Tuesday.",
                "Logic"
        ));

        qs.add(new Q(
                "Which is smaller: 86 or 68?",
                "68",
                "Compare the tens digits.",
                "Logic"
        ));

        // -------------------------
        // FOCUS - 5 QUESTIONS
        // -------------------------

        qs.add(new Q(
                "For focused study, which is better: one task or five tasks at once?",
                "one task",
                "Focused work means concentrating on one task.",
                "Focus"
        ));

        qs.add(new Q(
                "Before starting a study session, should you remove distractions?",
                "yes",
                "Reduce notifications and unnecessary distractions.",
                "Focus"
        ));

        qs.add(new Q(
                "If you study for 25 minutes and take a short break, what technique is this?",
                "pomodoro",
                "Think of the popular timed-study technique.",
                "Focus"
        ));

        qs.add(new Q(
                "When you do not understand a concept, should you skip it forever?",
                "no",
                "Review the concept and try again.",
                "Focus"
        ));

        qs.add(new Q(
                "What is better for long-term learning: active recall or only rereading?",
                "active recall",
                "Testing yourself strengthens retrieval.",
                "Focus"
        ));
    }

    void buildUi() {

        ScrollView scroll = new ScrollView(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(
                dp(18),
                dp(22),
                dp(18),
                dp(24)
        );
        root.setBackgroundColor(Color.rgb(246, 247, 251));

        // -------------------------
        // HEADER
        // -------------------------

        LinearLayout head = new LinearLayout(this);
        head.setOrientation(LinearLayout.VERTICAL);
        head.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView brand = text(
                "BRAIN SHIFT",
                12,
                Color.DKGRAY
        );
        brand.setTypeface(null, Typeface.BOLD);

        TextView title = text(
                "Focus. Think. Grow. 🧠",
                23,
                Color.rgb(17, 24, 39)
        );
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        head.addView(brand);
        head.addView(title);

        xpV = text(
                "⚡ 0 XP",
                13,
                Color.rgb(91, 75, 219)
        );
        xpV.setGravity(Gravity.CENTER);
        xpV.setPadding(dp(10), dp(6), dp(10), dp(6));
        xpV.setBackground(bg(Color.WHITE, 30));

        head.addView(
                xpV,
                new LinearLayout.LayoutParams(
                        dp(100),
                        dp(42)
                )
        );

        root.addView(head);

        // -------------------------
        // LEVEL
        // -------------------------

        LinearLayout level = box();
        level.setOrientation(LinearLayout.HORIZONTAL);

        levelV = text(
                "LEVEL 1",
                14,
                Color.rgb(91, 75, 219)
        );
        levelV.setTypeface(null, Typeface.BOLD);

        TextView levelHint = text(
                "Keep building your streak",
                12,
                Color.GRAY
        );

        level.addView(
                levelV,
                new LinearLayout.LayoutParams(
                        0,
                        dp(40),
                        1
                )
        );

        level.addView(
                levelHint,
                new LinearLayout.LayoutParams(
                        0,
                        dp(40),
                        1
                )
        );

        root.addView(
                level,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        // -------------------------
        // ROUTINE
        // -------------------------

        routineV = text(
                "",
                14,
                Color.rgb(55, 65, 81)
        );

        routineV.setPadding(
                0,
                dp(14),
                0,
                dp(10)
        );

        root.addView(routineV);

        // -------------------------
        // STATS
        // -------------------------

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);

        scoreV = text("", 13, Color.DKGRAY);
        streakV = text("", 13, Color.DKGRAY);
        solvedV = text("", 13, Color.DKGRAY);

        stats.addView(
                scoreV,
                new LinearLayout.LayoutParams(
                        0,
                        dp(48),
                        1
                )
        );

        stats.addView(
                streakV,
                new LinearLayout.LayoutParams(
                        0,
                        dp(48),
                        1
                )
        );

        stats.addView(
                solvedV,
                new LinearLayout.LayoutParams(
                        0,
                        dp(48),
                        1
                )
        );

        root.addView(stats);

        // -------------------------
        // CHALLENGE
        // -------------------------

        LinearLayout challenge = box();

        TextView label = text(
                "YOUR NEXT CHALLENGE",
                11,
                Color.GRAY
        );
        label.setTypeface(null, Typeface.BOLD);

        topicV = text(
                "QUANT",
                11,
                Color.rgb(91, 75, 219)
        );
        topicV.setTypeface(null, Typeface.BOLD);
        topicV.setPadding(0, dp(6), 0, 0);

        question = text(
                "",
                21,
                Color.rgb(17, 24, 39)
        );

        question.setTypeface(null, Typeface.BOLD);

        question.setPadding(
                0,
                dp(16),
                0,
                dp(18)
        );

        challenge.addView(label);
        challenge.addView(topicV);
        challenge.addView(
                question,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        // -------------------------
        // ANSWER
        // -------------------------

        answer = new EditText(this);
        answer.setHint("Your answer");
        answer.setSingleLine(true);
        answer.setTextSize(17);
        answer.setPadding(
                dp(14),
                dp(4),
                dp(14),
                dp(4)
        );

        challenge.addView(
                answer,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(56)
                )
        );

        // -------------------------
        // BUTTON
        // -------------------------

        action = new Button(this);
        action.setText("CHECK");
        action.setTextColor(Color.WHITE);
        action.setTextSize(14);
        action.setTypeface(null, Typeface.BOLD);
        action.setAllCaps(false);
        action.setBackground(
                bg(Color.rgb(91, 75, 219), 14)
        );

        LinearLayout.LayoutParams bp =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(56)
                );

        bp.topMargin = dp(10);

        challenge.addView(action, bp);

        // -------------------------
        // FEEDBACK
        // -------------------------

        feedback = text(
                "",
                15,
                Color.rgb(21, 128, 61)
        );

        feedback.setPadding(
                0,
                dp(12),
                0,
                0
        );

        challenge.addView(feedback);

        root.addView(
                challenge,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        // -------------------------
        // FOOTER
        // -------------------------

        TextView footer = text(
                "Offline-first • Your progress stays on this device",
                11,
                Color.GRAY
        );

        footer.setGravity(Gravity.CENTER);
        footer.setPadding(
                0,
                dp(14),
                0,
                0
        );

        root.addView(footer);

        scroll.addView(root);

        setContentView(scroll);

        // -------------------------
        // CLICK EVENTS
        // -------------------------

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (waitingForNext) {
                    nextQuestion();
                } else {
                    check();
                }
            }
        });

        answer.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(
                            TextView v,
                            int id,
                            android.view.KeyEvent event) {

                        if (id == EditorInfo.IME_ACTION_DONE) {

                            if (waitingForNext) {
                                nextQuestion();
                            } else {
                                check();
                            }

                            return true;
                        }

                        return false;
                    }
                }
        );
    }

    void render() {

        if (qs.size() == 0) {
            return;
        }

        if (i >= qs.size()) {
            i = 0;
        }

        Q q = qs.get(i);

        int today = dailySolved;
        int remaining = Math.max(0, 20 - today);

        if (today >= 20) {

            routineV.setText(
                    "TODAY • 20/20 completed 🎉\n" +
                    "Great work. You can continue practicing."
            );

        } else {

            routineV.setText(
                    "TODAY • " +
                    today +
                    "/20 completed • " +
                    remaining +
                    " left\n" +
                    "5 Quant • 5 Finance • 5 Logic • 5 Focus"
            );
        }

        question.setText(q.text);
        topicV.setText(q.topic.toUpperCase(Locale.US));

        answer.setText("");
        feedback.setText("");

        action.setText("CHECK");

        waitingForNext = false;

        int level = 1 + (xp / 100);

        levelV.setText("LEVEL " + level);

        xpV.setText("⚡ " + xp + " XP");

        scoreV.setText("Score\n" + score);
        streakV.setText("🔥 Streak\n" + streak);
        solvedV.setText("Solved\n" + solved);

        answer.requestFocus();
    }

    String clean(String s) {

        return s
                .trim()
                .replace("₹", "")
                .replace("%", "")
                .replace(",", "")
                .toLowerCase(Locale.US);
    }

    void check() {

        if (waitingForNext) {
            nextQuestion();
            return;
        }

        String a = clean(
                answer.getText()
                        .toString()
        );

        Q q = qs.get(i);

        if (a.length() == 0) {

            feedback.setTextColor(
                    Color.rgb(185, 28, 28)
            );

            feedback.setText(
                    "Enter your answer first."
            );

            return;
        }

        String correct = clean(q.answer);

        if (a.equals(correct)) {

            score++;
            solved++;
            streak++;

            xp += 10;

            if (streak % 5 == 0) {
                xp += 10;

                feedback.setText(
                        "✓ Correct! 🔥 5-streak bonus +10 XP"
                );

            } else {

                feedback.setText(
                        "✓ Correct! +1 Score • +10 XP"
                );
            }

            feedback.setTextColor(
                    Color.rgb(21, 128, 61)
            );

            if (dailySolved < 20) {
                dailySolved++;
            }

            saveProgress();

            action.setText("NEXT");

            waitingForNext = true;

        } else {

            streak = 0;

            feedback.setTextColor(
                    Color.rgb(185, 28, 28)
            );

            feedback.setText(
                    "✗ Not quite.\nHint: " + q.hint
            );

            saveProgress();

            action.setText("NEXT");

            waitingForNext = true;
        }

        updateStats();
    }

    void updateStats() {

        int level = 1 + (xp / 100);

        levelV.setText(
                "LEVEL " + level
        );

        xpV.setText(
                "⚡ " + xp + " XP"
        );

        scoreV.setText(
                "Score\n" + score
        );

        streakV.setText(
                "🔥 Streak\n" + streak
        );
          

        solvedV.setText(
                "Solved\n" + solved
        );

   
