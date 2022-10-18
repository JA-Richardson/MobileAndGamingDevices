    package tees.ac.uk.w9383619.mobileandgamedevicesica;

    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Paint;

    public class Joystick
    {
        public Object resetActuator;
        private int outerCirclePosX;
    private int outerCirclePosY;
    private int innerCirclePosX;
    private int innerCirclePosY;
    private int outerCircleRadius;
    private int innerCircleRadius;

    private Paint outerPaint;
    private Paint innerPaint;
    private double centreToTouchDistance;
        private boolean isPressed;

        public Joystick(int centrePosX, int centrePosY, int outerCircleRadius, int innerCircleRadius)
    {
        outerCirclePosX = centrePosX;
        outerCirclePosY = centrePosY;
        innerCirclePosX = centrePosX;
        innerCirclePosY = centrePosY;

        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        outerPaint = new Paint();
        outerPaint.setColor(Color.YELLOW);
        outerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        innerPaint = new Paint();
        innerPaint.setColor(Color.BLUE);
        innerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCirclePosX, outerCirclePosY, outerCircleRadius, outerPaint);
        canvas.drawCircle(innerCirclePosX, innerCirclePosY, innerCircleRadius, innerPaint);
    }

    public void update() {
    }

        public void setIsPressed(boolean isPressed) {
            this.isPressed = isPressed;
        }

        public boolean isPressed(double touchPosX, double touchPosY)
        {
            centreToTouchDistance = Math.sqrt(Math.pow(outerCirclePosX - touchPosX, 2) + Math.pow(outerCirclePosY - touchPosY,2));
            return centreToTouchDistance < outerCircleRadius;
        }

        public boolean getIsPressed() {
            return isPressed;
        }

        public void setActuator(double x, double y) {
        }

        public void resetActuator() {

        }
    }
