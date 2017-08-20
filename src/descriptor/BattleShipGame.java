
package descriptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *       &lt;sequence>
 *         &lt;element name="GameType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="BASIC"/>
 *               &lt;enumeration value="ADVANCE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="boardSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="shipTypes">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="shipType" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="category">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;enumeration value="REGULAR"/>
 *                                   &lt;enumeration value="L_SHAPE"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="mine" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="boards">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="board" maxOccurs="2" minOccurs="2">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ship" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="shipTypeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="position">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                                               &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                       &lt;element name="direction">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;enumeration value="ROW"/>
 *                                             &lt;enumeration value="COLUMN"/>
 *                                             &lt;enumeration value="RIGHT_DOWN"/>
 *                                             &lt;enumeration value="RIGHT_UP"/>
 *                                             &lt;enumeration value="UP_RIGHT"/>
 *                                             &lt;enumeration value="DOWN_RIGHT"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
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
    "gameType",
    "boardSize",
    "shipTypes",
    "mine",
    "boards"
})
@XmlRootElement(name = "BattleShipGame")
public class BattleShipGame {

    @XmlElement(name = "GameType", required = true)
    protected String gameType;
    protected int boardSize;
    @XmlElement(required = true)
    protected ShipTypes shipTypes;
    protected Mine mine;
    @XmlElement(required = true)
    protected Boards boards;

    /**
     * Gets the value of the gameType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGameType() {
        return gameType;
    }

    /**
     * Sets the value of the gameType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGameType(String value) {
        this.gameType = value;
    }

    /**
     * Gets the value of the boardSize property.
     * 
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Sets the value of the boardSize property.
     * 
     */
    public void setBoardSize(int value) {
        this.boardSize = value;
    }

    /**
     * Gets the value of the shipTypes property.
     * 
     * @return
     *     possible object is
     *     {@link ShipTypes }
     *     
     */
    public ShipTypes getShipTypes() {
        return shipTypes;
    }

    /**
     * Sets the value of the shipTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipTypes }
     *     
     */
    public void setShipTypes(ShipTypes value) {
        this.shipTypes = value;
    }

    /**
     * Gets the value of the mine property.
     * 
     * @return
     *     possible object is
     *     {@link Mine }
     *     
     */
    public Mine getMine() {
        return mine;
    }

    /**
     * Sets the value of the mine property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mine }
     *     
     */
    public void setMine(Mine value) {
        this.mine = value;
    }

    /**
     * Gets the value of the boards property.
     * 
     * @return
     *     possible object is
     *     {@link Boards }
     *     
     */
    public Boards getBoards() {
        return boards;
    }

    /**
     * Sets the value of the boards property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boards }
     *     
     */
    public void setBoards(Boards value) {
        this.boards = value;
    }


}
