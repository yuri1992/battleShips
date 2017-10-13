package descriptor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="ship" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="shipTypeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="position">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="direction">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="ROW"/>
 *                         &lt;enumeration value="COLUMN"/>
 *                         &lt;enumeration value="RIGHT_DOWN"/>
 *                         &lt;enumeration value="RIGHT_UP"/>
 *                         &lt;enumeration value="UP_RIGHT"/>
 *                         &lt;enumeration value="DOWN_RIGHT"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
    "ship"
})
public class Board {

    @XmlElement(required = true)
    protected List<Ship> ship;

    /**
     * Gets the value of the ship property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ship property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShip().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ship }
     *
     *
     */
    public List<Ship> getShip() {
        if (ship == null) {
            ship = new ArrayList<Ship>();
        }
        return this.ship;
    }


}
