import java.awt.*;

public class Rectangle {

    Vector p, v, sz;
    Color c;

    public Rectangle( Vector sz, Vector p, Vector v, Color c){
        this.p = p;
        this.v = v;
        this.sz = sz;
        this.c = c;
    }

    public void draw(Graphics2D g){
        g.setColor(c);
        g.fillRect(p.ix, p.iy, sz.ix, sz.iy);
    }

    public void update(float dt){
        p.add(Vector.mult(v, dt));
    }

}
