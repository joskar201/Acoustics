package com.example.joshua.acoustics.GiutarTuner.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joshua.acoustics.GiutarTuner.Note;
import com.example.joshua.acoustics.GiutarTuner.TunerUpdate;
import com.example.joshua.acoustics.GiutarTuner.tarsos.PitchDetectionResult;

import java.text.DecimalFormat;

public class TunerView extends LinearLayout implements TunerUpdate{
    private static final String TAG = TunerUpdate.class.getSimpleName();
    //Should keep this in sync with DirectionPointerView.DEFAULT_WIGGLE_ROOM
    public static final int DEFAULT_WIGGLE_ROOM = 5;
    public static final String IN_TUNE = "in tune";
    public static final int ORANGE = Color.parseColor("#FF9800");
    public static final int GREEN = Color.parseColor("#85D52C");
    public static final int WHITE = Color.parseColor("#FFFFFF");
    public static final int GRAY = Color.parseColor("#B0BEC5");

    private Note note;
    private PitchDetectionResult result;
    private boolean allowAddView;

    private CircleTunerView circleTunerView;
    private DirectionPointerView directionPointerView;
    private TextView fTextView;
    private TextView pTextView;

    private SpannableStringBuilder sb;

    public TunerView(Context context){
        super(context);
        init(context, null);
    }

    public TunerView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public TunerView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TunerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        allowAddView = true;
        setOrientation(VERTICAL);
        sb = new SpannableStringBuilder();
        circleTunerView = new CircleTunerView(context, attrs);
        directionPointerView = new DirectionPointerView(context, attrs);
        fTextView = new TextView(context, attrs);
        pTextView = new TextView(context, attrs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        circleTunerView.setLayoutParams(params);
        fTextView.setLayoutParams(params);
        pTextView.setLayoutParams(params);
        LinearLayout.LayoutParams dParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dParams.gravity = Gravity.CENTER_HORIZONTAL;
        dParams.setMargins(0, dpToPx(16), 0, dpToPx(16));
        directionPointerView.setLayoutParams(dParams);
        fTextView.setTextColor(GRAY);
        pTextView.setTextColor(GREEN);
        fTextView.setTextSize(16);
        pTextView.setTextSize(16);
        addView(circleTunerView);
        addView(directionPointerView);
        addView(fTextView);
        addView(pTextView);
        allowAddView = false;
    }

    private int dpToPx(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    @Override
    public void updateNote(Note newNote, PitchDetectionResult result) {
        this.note = newNote;
        this.result = result;
        circleTunerView.updateNote(newNote, result);
        directionPointerView.updateNote(newNote, result);
        sb.clear();
        sb.clearSpans();
        String aFreq = String.valueOf(new DecimalFormat("######.##").format(note.getActualFrequency()));
        String prob = String.valueOf(new DecimalFormat("######.##").format((result.getProbability() * 100))) + "%";
        if(newNote.getActualFrequency() < newNote.getFrequency()){
            if(newNote.getActualFrequency() + DEFAULT_WIGGLE_ROOM >= newNote.getFrequency()) {
                sb.append(IN_TUNE);
                sb.setSpan(new ForegroundColorSpan(ORANGE), 0, IN_TUNE.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }else{
                sb.append(aFreq);
                sb.setSpan(new ForegroundColorSpan(ORANGE), 0, aFreq.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append(" / " + note.getFrequency());
            }
        }else{
            if(newNote.getActualFrequency() - DEFAULT_WIGGLE_ROOM <= newNote.getFrequency()){
                sb.append(IN_TUNE);
                sb.setSpan(new ForegroundColorSpan(ORANGE), 0, IN_TUNE.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }else{
                sb.append(aFreq);
                sb.setSpan(new ForegroundColorSpan(ORANGE), 0, aFreq.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                sb.append(" / " + note.getFrequency());
            }
        }
        fTextView.setText(sb);
        pTextView.setText(prob);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params){
        if(allowAddView){
            super.addView(child, index, params);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params){
        if(allowAddView){
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index){
        if(allowAddView){
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child){
        if(allowAddView){
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int width, int height){
        if(allowAddView){
            super.addView(child, width, height);
        }
    }

    //even more delegation
    public void addOnNoteSelectedListener(DialView.OnNoteSelectedListener l){
        if(circleTunerView != null){
            circleTunerView.addOnNoteSelectedListener(l);
        }
    }

    //even more delegation
    public boolean removeOnNoteSelectedListener(DialView.OnNoteSelectedListener l){
        if(circleTunerView != null){
            return circleTunerView.removeOnNoteSelectedListener(l);
        }
        return false;
    }

    public Note getCurrentNote(){
        return note;
    }

    public PitchDetectionResult getCurrentResult(){
        return result;
    }

}
