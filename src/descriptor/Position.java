package descriptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Position {

    @XmlAttribute(name = "x", required = true)
    protected int x;
    @XmlAttribute(name = "y", required = true)
    protected int y;

    /**
     * Gets the value of the x property.
     *
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     *
     */
    public void setX(int value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     *
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     *
     */
    public void setY(int value) {
        this.y = value;
    }

}
