package descriptor;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="shipTypeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="position">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="direction">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ROW"/>
 *               &lt;enumeration value="COLUMN"/>
 *               &lt;enumeration value="RIGHT_DOWN"/>
 *               &lt;enumeration value="RIGHT_UP"/>
 *               &lt;enumeration value="UP_RIGHT"/>
 *               &lt;enumeration value="DOWN_RIGHT"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "shipTypeId",
    "position",
    "direction"
})
public class Ship {

    @XmlElement(required = true)
    protected String shipTypeId;
    @XmlElement(required = true)
    protected Position position;
    @XmlElement(required = true)
    protected String direction;

    /**
     * Gets the value of the shipTypeId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShipTypeId() {
        return shipTypeId;
    }

    /**
     * Sets the value of the shipTypeId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShipTypeId(String value) {
        this.shipTypeId = value;
    }

    /**
     * Gets the value of the position property.
     *
     * @return
     *     possible object is
     *     {@link Position }
     *
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     *
     * @param value
     *     allowed object is
     *     {@link Position }
     *
     */
    public void setPosition(Position value) {
        this.position = value;
    }

    /**
     * Gets the value of the direction property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDirection(String value) {
        this.direction = value;
    }


}
