import java.awt.*;

public class Oval extends Rectangle{

    public Oval(Vector sz, Vector p, Vector v, Color c) {
        super(sz, p, v, c);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(c);
        g.fillOval(p.ix, p.iy, sz.ix, sz.iy);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
