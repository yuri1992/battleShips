package game.engine;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"x", "y"})
@XmlRootElement(name = "position")
public class Point extends java.awt.Point {
    @XmlAttribute(name="x")
    public void setX(int x) {
        this.x = x;
    }

    @XmlAttribute(name="y")
    public void setY(int y) {
        this.y = y;
    }

}
