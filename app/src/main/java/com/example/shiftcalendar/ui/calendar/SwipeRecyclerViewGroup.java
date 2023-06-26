package com.example.shiftcalendar.ui.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.time.LocalDate;
import java.util.Calendar;

public class SwipeRecyclerViewGroup extends FrameLayout {

    private GestureDetector gestureDetector;

    private CalendarFragment calendarFragment;

    public SwipeRecyclerViewGroup(Context context) {
        super(context);
        init(context);
    }

    public void addAttributes(CalendarFragment cf){
        this.calendarFragment = cf;
    }

    public SwipeRecyclerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeRecyclerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        gestureDetector = new GestureDetector(context, new SwipeGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return gestureDetector.onTouchEvent(ev);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    // Swipe right
                    // Implement your desired behavior here
                    calendarFragment.decreaseMonth();
                    result = true;
                } else {
                    // Swipe left
                    // Implement your desired behavior here
                    calendarFragment.increaseMonth();
                    result = true;
                }
            }
            return result || super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
